package com.example.projectmanageclient.controller;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.stage.Stage;


public class ButtonViewController {

    @FXML
    public Button addProjectButton;

    @FXML

    public Button loginButton;
    @FXML
    public Button showProjectButton;



    @FXML
    private void onAddProjectButtonClick(ActionEvent e) {
        openNewWindow("/com/example/projectmanageclient/AddView.fxml", "Add Project");
    }


    @FXML
    private void onShowProjectButtonClick(ActionEvent e) {
        openNewWindow("/com/example/projectmanageclient/ProjectListView.fxml", "Show Project");
    }

    public void onLoginButtonClick(ActionEvent e) {
        openNewWindow("/com/example/projectmanageclient/LoginView.fxml", "Login");
    }
    private void openNewWindow(String fxmlFileName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}

