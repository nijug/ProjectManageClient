package com.example.projectmanageclient.controller;
import com.example.projectmanageclient.model.Project;
import com.example.projectmanageclient.service.ProjectService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;


public class ProjectListController {
    @FXML
    private TableView<Project> projectTableView;

    @FXML
    private TableColumn<Project, String> nameColumn;
    @FXML
    private TableColumn<Project, String> descriptionColumn;
    @FXML
    private TableColumn<Project, String> dateStartedColumn;
    @FXML
    private TableColumn<Project, String> dateEndedColumn;
    @FXML
    private TableColumn<Project, String> priorityColumn;




    private ProjectService ps;

    public void initialize() {

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        dateStartedColumn.setCellValueFactory(cellData -> {
            Date dateStarted = cellData.getValue().getDateStarted();
            String formattedDate = dateStarted != null ? dateStarted.toString() : "";
            return new SimpleStringProperty(formattedDate);
        });

        dateEndedColumn.setCellValueFactory(cellData -> {
            Date dateEnded = cellData.getValue().getDateEnded();
            String formattedDate = dateEnded != null ? dateEnded.toString() : "";
            return new SimpleStringProperty(formattedDate);
        });;
        priorityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPriority()));


        ps = ProjectService.getInstance();
        projectTableView.getItems().addAll(ps.readProjects());
    }

    @FXML
    private void onDeleteButtonClick() {
        Project selectedProject = projectTableView.getSelectionModel().getSelectedItem();
        ps.deleteProjects(selectedProject.getId());
        projectTableView.getItems().remove(selectedProject);
    }

    public void onModifyButtonClick() {
        Project selectedProject = projectTableView.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {
            // Prompt for a new project name
            TextInputDialog nameDialog = new TextInputDialog(selectedProject.getName());
            nameDialog.setTitle("Modify Name");
            nameDialog.setHeaderText("Enter new name for the project:");
            Optional<String> newNameResult = nameDialog.showAndWait();

            newNameResult.ifPresent(newName -> {
                // Prompt for a new project description
                TextInputDialog descriptionDialog = new TextInputDialog(selectedProject.getDescription());
                descriptionDialog.setTitle("Modify Description");
                descriptionDialog.setHeaderText("Enter new description for the project:");
                Optional<String> newDescriptionResult = descriptionDialog.showAndWait();

                newDescriptionResult.ifPresent(newDescription -> {
                    // Prompt for a new priority using ChoiceBox
                    ChoiceBox<String> priorityChoiceBox = new ChoiceBox<>();
                    priorityChoiceBox.getItems().addAll("High", "Medium", "Low");
                    priorityChoiceBox.setValue(selectedProject.getPriority());

                    Alert priorityDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    priorityDialog.setTitle("Modify Priority");
                    priorityDialog.setHeaderText("Select new priority for the project:");
                    priorityDialog.getDialogPane().setContent(priorityChoiceBox);

                    Optional<ButtonType> newPriorityResult = priorityDialog.showAndWait();

                    ((Optional<?>) newPriorityResult).ifPresent(buttonType -> {
                        if (buttonType == ButtonType.OK) {
                            // User confirmed the modification
                            selectedProject.setName(newName);
                            selectedProject.setDescription(newDescription);
                            selectedProject.setPriority(priorityChoiceBox.getValue());

                            // Update other properties like dateStarted and dateEnded similarly.

                            // Refresh the TableView if necessary
                            projectTableView.refresh();

                            // You can also display a success message here if needed
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Success");
                            successAlert.setHeaderText("Project Modified");
                            successAlert.setContentText("The project has been successfully modified.");
                            successAlert.showAndWait();
                        }
                    });
                });
            });
        }
    }

}
