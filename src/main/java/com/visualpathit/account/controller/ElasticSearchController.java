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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.model.User;
import com.visualpathit.account.service.UserService;
import com.visualpathit.account.utils.ElasticsearchUtil;

@Controller
public class ElasticSearchController {

        @Autowired
        private UserService userService;

        @Autowired
        private Components components;

        // =========================
        // helper: build elastic url
        // (docker vs aws)
        // =========================

        private String getElasticUrl() {

                String host = components.getElasticsearchHost();
                String port = components.getElasticsearchPort();
                String scheme = components.getElasticsearchScheme();

                if (host == null || host.isEmpty()) {
                        return "N/A";
                }

                // AWS OpenSearch
                if (host.contains("amazonaws.com")
                                || host.contains(".on.aws")) {

                        return scheme + "://" + host;
                }

                // Docker/local Elasticsearch
                return scheme + "://" + host + ":" + port;
        }

        private String getSearchEngineName() {

                String host = components.getElasticsearchHost();

                if (host != null &&
                                (host.contains("amazonaws.com")
                                                || host.contains(".on.aws"))) {

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

                try (RestHighLevelClient client = ElasticsearchUtil.getRestHighLevelClient()) {

                        // health check
                        boolean ping = client.ping(RequestOptions.DEFAULT);

                        if (!ping) {

                                model.addAttribute(
                                                "result",
                                                "FAILED: Elasticsearch/OpenSearch is DOWN");

                                model.addAttribute(
                                                "elasticUrl",
                                                getElasticUrl());

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
                                                                        .field(
                                                                                        "DOB",
                                                                                        user.getDateOfBirth() != null
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

                                        System.out.println(
                                                        "Indexed user: "
                                                                        + user.getId()
                                                                        + " result: "
                                                                        + response.getResult());

                                        successCount++;

                                } catch (Exception e) {

                                        hasErrors = true;

                                        System.err.println(
                                                        "FAILED indexing user: "
                                                                        + user.getId());

                                        e.printStackTrace();
                                }
                        }

                        // final result
                        if (successCount == 0) {

                                model.addAttribute(
                                                "result",
                                                "FAILED: No users were indexed");

                        } else if (hasErrors) {

                                model.addAttribute(
                                                "result",
                                                "PARTIAL SUCCESS: "
                                                                + successCount
                                                                + " users indexed");

                        } else {

                                model.addAttribute(
                                                "result",
                                                "Users indexed successfully into "
                                                                + getSearchEngineName());
                        }

                        model.addAttribute(
                                        "elasticUrl",
                                        getElasticUrl());

                        return "elasticeSearchRes";

                } catch (Exception e) {

                        e.printStackTrace();

                        model.addAttribute(
                                        "result",
                                        "FAILED: Cannot connect to Elasticsearch/OpenSearch");

                        model.addAttribute(
                                        "elasticUrl",
                                        getElasticUrl());

                        return "elasticeSearchRes";
                }
        }

        // =========================
        // HEALTH CHECK
        // =========================

        @GetMapping("/user/elasticsearch/health")
        public String health(Model model) {

                try (RestHighLevelClient client = ElasticsearchUtil.getRestHighLevelClient()) {

                        boolean ok = client.ping(RequestOptions.DEFAULT);

                        model.addAttribute(
                                        "result",
                                        ok
                                                        ? getSearchEngineName() + " OK"
                                                        : "FAILED");

                } catch (Exception e) {

                        e.printStackTrace();

                        model.addAttribute(
                                        "result",
                                        "FAILED: " + e.getMessage());
                }

                model.addAttribute(
                                "elasticUrl",
                                getElasticUrl());

                return "elasticeSearchRes";
        }

        // =========================
        // VIEW USER
        // =========================

        @RequestMapping(value = "/rest/users/view/{id}", method = RequestMethod.GET)
        public String view(@PathVariable String id, Model model) {

                try (RestHighLevelClient client = ElasticsearchUtil.getRestHighLevelClient()) {

                        GetRequest request = new GetRequest("users", id);

                        GetResponse response = client.get(request, RequestOptions.DEFAULT);

                        if (!response.isExists()) {

                                model.addAttribute(
                                                "res",
                                                "User not found in "
                                                                + getSearchEngineName());

                                model.addAttribute(
                                                "elasticUrl",
                                                getElasticUrl());

                                return "elasticeSearchRes";
                        }

                        model.addAttribute(
                                        "res",
                                        response.getSourceAsString());

                        model.addAttribute(
                                        "elasticUrl",
                                        getElasticUrl());

                } catch (Exception e) {

                        e.printStackTrace();

                        model.addAttribute(
                                        "res",
                                        "ERROR: " + e.getMessage());

                        model.addAttribute(
                                        "elasticUrl",
                                        getElasticUrl());
                }

                return "elasticeSearchRes";
        }

        // =========================
        // UPDATE USER
        // =========================

        @RequestMapping(value = "/rest/users/update/{id}", method = RequestMethod.GET)
        public String update(@PathVariable String id, Model model) {

                try (RestHighLevelClient client = ElasticsearchUtil.getRestHighLevelClient()) {

                        UpdateRequest request = new UpdateRequest("users", id)
                                        .doc(XContentFactory.jsonBuilder()
                                                        .startObject()
                                                        .field("gender", "male")
                                                        .endObject());

                        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

                        model.addAttribute(
                                        "res",
                                        response.status().toString());

                } catch (Exception e) {

                        e.printStackTrace();

                        model.addAttribute(
                                        "res",
                                        "UPDATE FAILED: " + e.getMessage());
                }

                model.addAttribute(
                                "elasticUrl",
                                getElasticUrl());

                return "elasticeSearchRes";
        }

        // =========================
        // DELETE USER
        // =========================

        @RequestMapping(value = "/rest/users/delete/{id}", method = RequestMethod.GET)
        public String delete(@PathVariable String id, Model model) {

                try (RestHighLevelClient client = ElasticsearchUtil.getRestHighLevelClient()) {

                        DeleteRequest request = new DeleteRequest("users", id);

                        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

                        model.addAttribute(
                                        "res",
                                        response.getResult().toString());

                } catch (Exception e) {

                        e.printStackTrace();

                        model.addAttribute(
                                        "res",
                                        "DELETE FAILED: " + e.getMessage());
                }

                model.addAttribute(
                                "elasticUrl",
                                getElasticUrl());

                return "elasticeSearchRes";
        }
}