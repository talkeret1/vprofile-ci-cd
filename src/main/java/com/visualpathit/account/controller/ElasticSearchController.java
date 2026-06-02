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
import org.opensearch.action.index.IndexResponse;
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

        // =========================
        // INDEX USERS
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
                                        System.out.println("Failed user id: " + user.getId());
                                        e.printStackTrace();
                                }
                        }

                        if (successCount == 0) {
                                model.addAttribute("result", "FAILED: No users indexed");
                        } else if (hasErrors) {
                                model.addAttribute("result", "PARTIAL SUCCESS: " + successCount);
                        } else {
                                model.addAttribute("result", "SUCCESS into " + getSearchEngineName());
                        }

                } catch (Exception e) {
                        model.addAttribute("result", "FAILED: connection error - " + e.getMessage());
                }

                model.addAttribute("elasticUrl", getElasticUrl());
                return "elasticeSearchRes";
        }

        // =========================
        // HEALTH
        // =========================

        @GetMapping("/user/elasticsearch/health")
        public String health(Model model) {

                try (RestHighLevelClient client = clientFactory.createClient()) {

                        boolean ok = client.ping(RequestOptions.DEFAULT);
                        model.addAttribute("result", ok ? getSearchEngineName() + " OK" : "FAILED");

                } catch (Exception e) {
                        model.addAttribute("result", "FAILED: " + e.getMessage());
                }

                model.addAttribute("elasticUrl", getElasticUrl());
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
                                model.addAttribute("res", "User not found in " + getSearchEngineName());
                        } else {
                                model.addAttribute("res", response.getSourceAsString());
                        }

                } catch (Exception e) {
                        model.addAttribute("res", "ERROR: " + e.getMessage());
                }

                model.addAttribute("elasticUrl", getElasticUrl());
                return "elasticeSearchRes";
        }

        // =========================
        // UPDATE USER (FIXED)
        // =========================

        @GetMapping("/rest/users/update/{id}")
        public String update(@PathVariable String id, Model model) {

                try (RestHighLevelClient client = clientFactory.createClient()) {

                        Map<String, Object> payload = Map.of("gender", "male");
                        String json = mapper.writeValueAsString(payload);

                        UpdateRequest request = new UpdateRequest("users", id)
                                        .doc(json, XContentType.JSON);

                        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

                        model.addAttribute("res", response.getResult().toString());

                } catch (Exception e) {
                        model.addAttribute("res", "UPDATE FAILED: " + e.getMessage());
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

                        model.addAttribute("res", response.getResult().toString());

                } catch (Exception e) {
                        model.addAttribute("res", "DELETE FAILED: " + e.getMessage());
                }

                return "elasticeSearchRes";
        }
}