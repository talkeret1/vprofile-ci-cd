package com.visualpathit.account.controller;

import java.util.List;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.model.User;
import com.visualpathit.account.service.UserService;
import com.visualpathit.account.utils.ElasticsearchUtil;

@Controller
public class ElasticSearchController {

        private static final Logger logger = LoggerFactory.getLogger(ElasticSearchController.class);

        @Autowired
        private UserService userService;

        @Autowired
        private Components components;

        @Autowired
        private ElasticsearchUtil elasticsearchUtil;

        // =========================
        // helper
        // =========================

        private String getElasticUrl() {

                String host = components.getElasticsearchHost();
                String port = components.getElasticsearchPort();
                String scheme = components.getElasticsearchScheme();

                if (host == null || host.isEmpty()) {
                        return "N/A";
                }

                if (host.contains("amazonaws.com") || host.contains(".on.aws")) {
                        return scheme + "://" + host;
                }

                return scheme + "://" + host + ":" + port;
        }

        private String getSearchEngineName() {

                String host = components.getElasticsearchHost();

                if (host != null &&
                                (host.contains("amazonaws.com") || host.contains(".on.aws"))) {
                        return "OpenSearch";
                }

                return "Elasticsearch";
        }

        // =========================
        // INDEX USERS
        // =========================

        @RequestMapping(value = "/user/search/sync", method = RequestMethod.GET)
        public String insert(Model model) {

                boolean hasErrors = false;
                int successCount = 0;

                try (RestHighLevelClient client = elasticsearchUtil.getRestHighLevelClient()) {

                        boolean ping = client.ping(RequestOptions.DEFAULT);

                        if (!ping) {
                                model.addAttribute("result", "FAILED: ES/OpenSearch DOWN");
                                model.addAttribute("elasticUrl", getElasticUrl());
                                return "elasticeSearchRes";
                        }

                        List<User> users = userService.getList();

                        for (User user : users) {

                                try {
                                        IndexRequest request = new IndexRequest("users")
                                                        .id(String.valueOf(user.getId()))
                                                        .source(XContentFactory.jsonBuilder()
                                                                        .startObject()
                                                                        .field("name", user.getUsername())
                                                                        .field("DOB", user.getDateOfBirth() != null
                                                                                        ? user.getDateOfBirth()
                                                                                                        .toString()
                                                                                        : "")
                                                                        .field("fatherName", user.getFatherName())
                                                                        .field("motherName", user.getMotherName())
                                                                        .field("gender", user.getGender())
                                                                        .field("nationality", user.getNationality())
                                                                        .field("phoneNumber", user.getPhoneNumber())
                                                                        .endObject());

                                        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

                                        logger.info("Indexed user {} -> {}", user.getId(), response.getResult());
                                        successCount++;

                                } catch (Exception e) {
                                        hasErrors = true;
                                        logger.error("Index failed user {}", user.getId(), e);
                                }
                        }

                        model.addAttribute("result",
                                        successCount == 0
                                                        ? "FAILED: No users indexed"
                                                        : hasErrors
                                                                        ? "PARTIAL SUCCESS: " + successCount
                                                                        : "SUCCESS into " + getSearchEngineName());

                        model.addAttribute("elasticUrl", getElasticUrl());
                        return "elasticeSearchRes";

                } catch (Exception e) {

                        logger.error("ES connection failed", e);

                        model.addAttribute("result", "FAILED: Cannot connect to ES/OpenSearch");
                        model.addAttribute("elasticUrl", getElasticUrl());
                        return "elasticeSearchRes";
                }
        }

        // =========================
        // HEALTH
        // =========================

        @GetMapping("/user/elasticsearch/health")
        public String health(Model model) {

                try (RestHighLevelClient client = elasticsearchUtil.getRestHighLevelClient()) {

                        boolean ok = client.ping(RequestOptions.DEFAULT);

                        model.addAttribute("result",
                                        ok ? getSearchEngineName() + " OK" : "FAILED");

                } catch (Exception e) {
                        model.addAttribute("result", "FAILED: " + e.getMessage());
                }

                model.addAttribute("elasticUrl", getElasticUrl());
                return "elasticeSearchRes";
        }

        // =========================
        // VIEW
        // =========================

        @RequestMapping(value = "/rest/users/view/{id}", method = RequestMethod.GET)
        public String view(@PathVariable String id, Model model) {

                try (RestHighLevelClient client = elasticsearchUtil.getRestHighLevelClient()) {

                        GetResponse response = client.get(new GetRequest("users", id), RequestOptions.DEFAULT);

                        model.addAttribute("res",
                                        response.isExists() ? response.getSourceAsString() : "NOT FOUND");

                } catch (Exception e) {
                        model.addAttribute("res", "ERROR: " + e.getMessage());
                }

                model.addAttribute("elasticUrl", getElasticUrl());
                return "elasticeSearchRes";
        }

        // =========================
        // UPDATE
        // =========================

        @RequestMapping(value = "/rest/users/update/{id}", method = RequestMethod.GET)
        public String update(@PathVariable String id, Model model) {

                try (RestHighLevelClient client = elasticsearchUtil.getRestHighLevelClient()) {

                        UpdateResponse response = client.update(
                                        new UpdateRequest("users", id)
                                                        .doc(XContentFactory.jsonBuilder()
                                                                        .startObject()
                                                                        .field("gender", "male")
                                                                        .endObject()),
                                        RequestOptions.DEFAULT);

                        model.addAttribute("res", response.status().toString());

                } catch (Exception e) {
                        model.addAttribute("res", "UPDATE FAILED: " + e.getMessage());
                }

                model.addAttribute("elasticUrl", getElasticUrl());
                return "elasticeSearchRes";
        }

        // =========================
        // DELETE
        // =========================

        @RequestMapping(value = "/rest/users/delete/{id}", method = RequestMethod.GET)
        public String delete(@PathVariable String id, Model model) {

                try (RestHighLevelClient client = elasticsearchUtil.getRestHighLevelClient()) {

                        DeleteResponse response = client.delete(
                                        new DeleteRequest("users", id),
                                        RequestOptions.DEFAULT);

                        model.addAttribute("res", response.getResult().toString());

                } catch (Exception e) {
                        model.addAttribute("res", "DELETE FAILED: " + e.getMessage());
                }

                model.addAttribute("elasticUrl", getElasticUrl());
                return "elasticeSearchRes";
        }
}