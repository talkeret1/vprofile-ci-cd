package com.visualpathit.account.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.opensearch.action.delete.DeleteRequest;
import org.opensearch.action.delete.DeleteResponse;
import org.opensearch.action.get.GetRequest;
import org.opensearch.action.get.GetResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.update.UpdateRequest;
import org.opensearch.action.update.UpdateResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.model.User;
import com.visualpathit.account.service.UserService;
import com.visualpathit.account.utils.ElasticsearchClientFactory;

@Controller
public class ElasticSearchController {

        private final UserService userService;
        private final Components components;
        private final ElasticsearchClientFactory clientFactory;
        private final ObjectMapper mapper = new ObjectMapper();

        public ElasticSearchController(
                        UserService userService,
                        Components components,
                        ElasticsearchClientFactory clientFactory) {
                this.userService = userService;
                this.components = components;
                this.clientFactory = clientFactory;
        }

        // =========================
        // DASHBOARD ATTRIBUTES
        // =========================

        private void addDashboardAttributes(Model model, String result) {

                model.addAttribute("searchEngineName", getSearchEngineName());
                model.addAttribute("searchEngineUrl", getElasticUrl());
                model.addAttribute("environment", getEnvironment());
                model.addAttribute("result", result);

                if (result != null && result.startsWith("SUCCESS")) {
                        model.addAttribute("statusColor", "green");
                        model.addAttribute("statusBadge", "OK");
                } else if (result != null && (result.startsWith("FAILED") || result.startsWith("ERROR"))) {
                        model.addAttribute("statusColor", "red");
                        model.addAttribute("statusBadge", "FAIL");
                } else {
                        model.addAttribute("statusColor", "gray");
                        model.addAttribute("statusBadge", "UNKNOWN");
                }
        }

        // =========================
        // HELPERS
        // =========================

        private String getElasticUrl() {
                String host = components.getElasticsearchHost();
                String port = components.getElasticsearchPort();
                String scheme = components.getElasticsearchScheme();

                if (host == null || host.isBlank())
                        return "N/A";

                if (host.contains("amazonaws.com") || host.contains(".on.aws")) {
                        return scheme + "://" + host;
                }

                return scheme + "://" + host + ":" + port;
        }

        private String getSearchEngineName() {
                String host = components.getElasticsearchHost();

                if (host != null && (host.contains("amazonaws.com") || host.contains(".on.aws"))) {
                        return "OpenSearch";
                }
                return "Elasticsearch";
        }

        private String getEnvironment() {
                String host = components.getElasticsearchHost();

                if (host != null && (host.contains("amazonaws.com") || host.contains(".on.aws"))) {
                        return "☁️ AWS Production";
                }

                return "🐳 Docker Local";
        }

        // =========================
        // SYNC USERS
        // =========================

        @GetMapping("/user/search/sync")
        public String insert(Model model) {

                int successCount = 0;
                boolean hasErrors = false;

                try (RestHighLevelClient client = clientFactory.createClient()) {

                        List<User> users = userService.getList();

                        for (User user : users) {
                                try {
                                        Map<String, Object> payload = new HashMap<>();
                                        payload.put("id", user.getId());
                                        payload.put("name", user.getUsername());
                                        payload.put("DOB",
                                                        user.getDateOfBirth() != null ? user.getDateOfBirth().toString()
                                                                        : "");
                                        payload.put("fatherName", user.getFatherName());
                                        payload.put("motherName", user.getMotherName());
                                        payload.put("gender", user.getGender());
                                        payload.put("nationality", user.getNationality());
                                        payload.put("phoneNumber", user.getPhoneNumber());

                                        IndexRequest request = new IndexRequest("users")
                                                        .id(String.valueOf(user.getId()))
                                                        .source(payload, XContentType.JSON);

                                        client.index(request, RequestOptions.DEFAULT);
                                        successCount++;

                                } catch (Exception e) {
                                        hasErrors = true;
                                }
                        }

                        if (successCount == 0) {
                                addDashboardAttributes(model, "FAILED: No users indexed");
                        } else if (hasErrors) {
                                addDashboardAttributes(model, "PARTIAL SUCCESS: " + successCount);
                        } else {
                                addDashboardAttributes(model, "SUCCESS into " + getSearchEngineName());
                        }

                } catch (Exception e) {
                        addDashboardAttributes(model, "FAILED: connection error");
                }

                return "elasticeSearchRes";
        }

        // =========================
        // VIEW USER
        // =========================

        @GetMapping("/rest/users/view/{id}")
        public String view(@PathVariable String id, Model model) {

                try (RestHighLevelClient client = clientFactory.createClient()) {

                        GetRequest request = new GetRequest("users", id);
                        GetResponse response = client.get(request, RequestOptions.DEFAULT);

                        if (!response.isExists()) {
                                addDashboardAttributes(model, "FAILED: User not found");
                        } else {
                                addDashboardAttributes(model, "SUCCESS: User found");
                        }

                } catch (Exception e) {
                        addDashboardAttributes(model, "ERROR: " + e.getMessage());
                }

                return "elasticeSearchRes";
        }

        // =========================
        // UPDATE USER
        // =========================

        @GetMapping("/rest/users/update/{id}")
        public String update(@PathVariable String id, Model model) {

                try (RestHighLevelClient client = clientFactory.createClient()) {

                        Map<String, Object> payload = Map.of("gender", "male");
                        String json = mapper.writeValueAsString(payload);

                        UpdateRequest request = new UpdateRequest("users", id)
                                        .doc(json, XContentType.JSON);

                        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

                        addDashboardAttributes(model, "SUCCESS: " + response.getResult());

                } catch (Exception e) {
                        addDashboardAttributes(model, "FAILED: update error");
                }

                return "elasticeSearchRes";
        }

        // =========================
        // DELETE USER
        // =========================

        @GetMapping("/rest/users/delete/{id}")
        public String delete(@PathVariable String id, Model model) {

                try (RestHighLevelClient client = clientFactory.createClient()) {

                        DeleteRequest request = new DeleteRequest("users", id);
                        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

                        addDashboardAttributes(model, "SUCCESS: " + response.getResult());

                } catch (Exception e) {
                        addDashboardAttributes(model, "FAILED: delete error");
                }

                return "elasticeSearchRes";
        }
}