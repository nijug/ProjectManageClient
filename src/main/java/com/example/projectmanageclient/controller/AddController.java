package com.example.projectmanageclient.controller;

import com.example.projectmanageclient.model.Project;
import com.example.projectmanageclient.service.ProjectService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;


public class AddController {
    @FXML
    private TextField nameInput;

    @FXML
    private TextField descriptionInput;

    @FXML
    private DatePicker dateStartedPicker;

    @FXML
    private DatePicker dateEndedPicker;

    @FXML
    private ChoiceBox<String> priorityChoiceBox;
    private ProjectService ps;

    @FXML
    public void initialize() {
        ps = ProjectService.getInstance();
    }

    @FXML
    private void onSubmitButtonClick(ActionEvent e) {
        ps.addProjects(new Project(nameInput.getText(),descriptionInput.getText(), java.sql.Date.valueOf(dateStartedPicker.getValue()),java.sql.Date.valueOf(dateEndedPicker.getValue()), priorityChoiceBox.getValue()));
    }
}
