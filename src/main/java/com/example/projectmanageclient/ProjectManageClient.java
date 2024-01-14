package com.example.projectmanageclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Objects;

import javafx.scene.image.Image;
public class ProjectManageClient extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ProjectManageClient.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 370);
        stage.setTitle("Login");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/projectmanageclient/images/paper_13781926.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        try {
            launch();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}