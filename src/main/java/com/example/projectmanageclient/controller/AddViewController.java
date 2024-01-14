package com.example.projectmanageclient.controller;

import com.example.projectmanageclient.LanguageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.ProjectService.Project;
import org.ProjectService.ProjectService;

import java.time.LocalDate;
import java.util.ResourceBundle;


public class AddViewController {
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

    @FXML
    private Button submitButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label dateStartedLabel;

    @FXML
    private Label dateEndedLabel;

    @FXML
    private Label priorityLabel;


    private LanguageManager languageManager;
    private ProjectService ps;

    private Project currentProject;

    private ResourceBundle bundle;

    private OnSubmitCallback callback;

    @FXML
    public void initialize() {
        ps = ProjectService.getInstance(LoginController.getAccessToken());
        dateStartedPicker.setEditable(false);
        dateStartedPicker.setEditable(false);

        dateStartedPicker.valueProperty().addListener((obs, oldStartDate, newStartDate) -> validateDates());
        dateEndedPicker.valueProperty().addListener((obs, oldEndDate, newEndDate) -> validateDates());

        languageManager = LanguageManager.getInstance();
        updateTexts();
    }


    private void validateDates() {
        LocalDate startDate = dateStartedPicker.getValue();
        LocalDate endDate = dateEndedPicker.getValue();

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {

            AlertController ac = new AlertController();
            ac.showAlert(bundle.getString("WrongDate"));

            dateEndedPicker.setValue(null);
        }
    }

    private void updateTexts() {
        bundle = languageManager.getResourceBundle();
        nameLabel.setText(bundle.getString("name"));
        descriptionLabel.setText(bundle.getString("description"));
        dateStartedLabel.setText(bundle.getString("dateStarted"));
        dateEndedLabel.setText(bundle.getString("dateEnded"));
        priorityLabel.setText(bundle.getString("priority"));
        submitButton.setText(bundle.getString("submit"));

    }

    @FXML
    private void onSubmitButtonClick(ActionEvent e) {

        if (nameInput.getText().isEmpty() || descriptionInput.getText().isEmpty() || dateStartedPicker.getValue() == null || dateEndedPicker.getValue() == null || priorityChoiceBox.getValue() == null) {
            AlertController ac = new AlertController();
            ac.showAlert(bundle.getString("FillAllFields"));
            return;
        }
        if (currentProject == null) {
            ps.addProjects(new Project(nameInput.getText(), descriptionInput.getText(), java.sql.Date.valueOf(dateStartedPicker.getValue()), java.sql.Date.valueOf(dateEndedPicker.getValue()), priorityChoiceBox.getValue()));
        }
        else {
            currentProject.setName(nameInput.getText());
            currentProject.setDescription(descriptionInput.getText());
            currentProject.setDateStarted(java.sql.Date.valueOf(dateStartedPicker.getValue()));
            currentProject.setDateEnded(java.sql.Date.valueOf(dateEndedPicker.getValue()));
            currentProject.setPriority(priorityChoiceBox.getValue());

            ps.updateProjects(currentProject);

        }
        if (callback != null) {
            callback.onSubmit();
        }
        closeWindow(e);
    }

    public void setProjectData(Project project) {
        this.currentProject = project;
        if (currentProject != null) {
            nameInput.setText(currentProject.getName());
            descriptionInput.setText(currentProject.getDescription());
            dateStartedPicker.setValue(currentProject.getDateStarted().toLocalDate());
            dateEndedPicker.setValue(currentProject.getDateEnded().toLocalDate());
            priorityChoiceBox.setValue(currentProject.getPriority());
        }

    }

    private void closeWindow(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }

    public void setOnSubmitCallback(OnSubmitCallback callback) {
        this.callback = callback;
    }

    public void setDarkMode(boolean isDarkMode) {
        Node root = nameInput.getScene().getRoot();
        if (isDarkMode) {
            root.getStyleClass().add("dark");
        } else {
            root.getStyleClass().remove("dark");
        }
    }
    public interface OnSubmitCallback {
        void onSubmit();
    }
}
