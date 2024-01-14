package com.example.projectmanageclient.controller;

import com.example.projectmanageclient.ProjectManageClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.fxml.FXML;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import com.google.common.hash.Hashing;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;


public class LoginController {
    @FXML
    private WebView webView;

    @Getter
    private static String accessToken;

    @FXML
    public void initialize() {

        WebEngine webEngine = webView.getEngine();

        startOAuth2Flow(webEngine);
        System.out.println("Web engine location: " + webEngine.getLocation());
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Web engine location: " + webEngine.getLocation()+ newValue);
            if (newValue.startsWith("http://127.0.0.1:8080")) {
                handleRedirect(newValue);
            }
        });
    }

    private void startOAuth2Flow(WebEngine webEngine) {
        byte[] sha256hash = Hashing.sha256()
                .hashString("nKuqVYMBExRtTcAq4xd5ZtRjB-4XFZoc_tYnUQYBpAg", StandardCharsets.UTF_8)
                .asBytes();

        String base64UrlSafe = Base64.encodeBase64URLSafeString(sha256hash);
        System.out.println("Base64UrlSafe: " + base64UrlSafe);
        webEngine.load("http://20.215.201.5:8080/oauth2/authorize?response_type=code&client_id=public-client&redirect_uri=http://127.0.0.1:8080&code_challenge=" + base64UrlSafe + "&code_challenge_method=S256&username=admin&password=admin");

    }

    private void handleRedirect(String location) {
        String authCode = location.split("code=")[1];

        new Thread(() -> {
            accessToken = null;
            try {
                accessToken = exchangeAuthCodeForAccessToken(authCode);
                Platform.runLater(() -> {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(ProjectManageClient.class.getResource("ProjectListView.fxml"));
                        Parent root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));

                        stage.setMinWidth(800);
                        stage.setMinHeight(600);

                        stage.sizeToScene();
                        stage.setTitle("Manage Projects");
                        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/projectmanageclient/images/paper_13781926.png"))));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Stage loginStage = (Stage) webView.getScene().getWindow();
                    loginStage.close();
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public String exchangeAuthCodeForAccessToken(String authCode) throws IOException {

        String url = "http://20.215.201.5:8080/oauth2/token";

        String clientId = "public-client";
        String redirectURI= "http://127.0.0.1:8080";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");

        System.out.println("Auth code: " + authCode);

        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String parameters = "grant_type=authorization_code&code=" + authCode + "&redirect_uri=" +redirectURI+ "&code_verifier=nKuqVYMBExRtTcAq4xd5ZtRjB-4XFZoc_tYnUQYBpAg" + "&client_id=" + clientId;
        System.out.println("Full address of the request with parameters: " + url + "?" + parameters);

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(parameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> map = mapper.readValue(response.toString(), Map.class);

            return (String) map.get("access_token");
        } else {

            return "Error: " + responseCode;
        }
    }

}