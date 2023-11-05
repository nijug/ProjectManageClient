package com.example.projectmanageclient.service;


import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.example.projectmanageclient.model.Project;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;


public class ProjectService {

    private String URL;
    private String create_endpoint;
    private String read_all_endpoint;

    private static ProjectService instance;
    private ProjectService() {
        AppData appData;
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get("src/main/resources/com/example/projectmanageclient/appdata.json"));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            appData = objectMapper.readValue(jsonData, AppData.class);
            URL = appData.getURL();
            create_endpoint = appData.getCreate_endpoint();
            read_all_endpoint = appData.getRead_all_endpoint();
            System.out.println(URL + " " + create_endpoint + " " + read_all_endpoint);
        } catch (Exception e) {
            System.out.println("Error while reading JSON file");
            e.printStackTrace();
        }

    }

    public static ProjectService getInstance() {
        if (instance == null) {
            instance = new ProjectService();
        }
        return instance;
    }

    public void addProjects(Project project) {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String projectJson;
        try {
            projectJson = objectMapper.writeValueAsString(project);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + create_endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(projectJson))
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode == 201) {
                System.out.println("Project created successfully.");
            } else {
                System.out.println("Failed to create the project. HTTP Status Code: " + statusCode);
                System.out.println(response.body());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Project[] readProjects() {

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + read_all_endpoint))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode == 200) {
                String responseBody = response.body();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Project projects[] = objectMapper.readValue(responseBody, Project[].class);
                return projects;
            } else {
                System.err.println("Error: HTTP status code " + statusCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteProjects(Integer projectId) {
        HttpClient httpClient = HttpClient.newHttpClient();

        // You may need to adjust the URI to include the project ID or other necessary information
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + projectId))
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode == 204) {
                System.out.println("Project deleted successfully.");
            } else {
                System.out.println("Failed to delete the project. HTTP Status Code: " + statusCode);
                System.out.println(response.body());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateProjects(Project updatedProject) {

        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String projectJson;

        try {
            projectJson = objectMapper.writeValueAsString(updatedProject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + updatedProject.getId()))
                .method("PUT", HttpRequest.BodyPublishers.ofString(projectJson))
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode == 200) {
                System.out.println("Project updated successfully.");
            } else {
                System.out.println("Failed to update the project. HTTP Status Code: " + statusCode);
                System.out.println(response.body());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Getter
    public static class AppData {
        @JsonProperty("URL")
        private String URL;
        @JsonProperty("create_endpoint")
        private String create_endpoint;
        @JsonProperty("read_all_endpoint")
        private String read_all_endpoint;

        public AppData() {
            // Default constructor
        }

    }
}