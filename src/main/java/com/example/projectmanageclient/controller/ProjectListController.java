package com.example.projectmanageclient.controller;
import com.example.projectmanageclient.LanguageManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ProjectService.Project;
import org.ProjectService.ProjectService;
import java.util.ResourceBundle;
import java.util.Locale;


import java.io.IOException;
import java.util.Objects;


public class ProjectListController {


    @FXML
    private TilePane projectContainer;

    @FXML
    private ScrollPane wrapper;

    @FXML
    private Label siteTitle;
    @FXML
    private Button englishButton;
    @FXML
    private Button polishButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addProjectButton;
    @FXML
    private ToggleButton darkModeToggle;


    private ResourceBundle bundle;
    private ProjectService ps;

    private LanguageManager languageManager;

    private Project selectedProject;
    public void initialize() {
        ps = ProjectService.getInstance(LoginController.getAccessToken());
        languageManager = LanguageManager.getInstance();
        englishButton.setOnAction(event -> changeLanguage(new Locale("en")));
        polishButton.setOnAction(event -> changeLanguage(new Locale("pl")));
        updateTexts();
        darkModeToggle.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            Node root = darkModeToggle.getScene().getRoot();
            if (isSelected) {
                root.getStyleClass().add("dark");
            } else {
                root.getStyleClass().remove("dark");
            }
        });


        populateView();
    }

    private void updateTexts() {
        bundle = languageManager.getResourceBundle();
        siteTitle.setText(bundle.getString("title"));
        deleteButton.setText(bundle.getString("delete"));
        addProjectButton.setText(bundle.getString("addProject"));
    }

    private void populateView() {
        Project[] projects = ps.readProjects();

        projectContainer.setHgap(10);
        projectContainer.setVgap(10);
        projectContainer.setPrefColumns(3);
        projectContainer.setPrefColumns(3);
        projectContainer.setTileAlignment(javafx.geometry.Pos.CENTER);
        projectContainer.setPadding(new Insets(30));
        wrapper.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        projectContainer.prefWidthProperty().bind(wrapper.widthProperty());


        for (Project project : projects) {

            HBox card = new HBox(10);
            card.setAlignment(Pos.CENTER);
            card.setPrefWidth(300);
            card.setMaxWidth(600);

            card.getStyleClass().add("card");


            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/projectmanageclient/images/paper_13781926.png"))));

            imageView.setFitWidth(50);
            imageView.setFitHeight(50);



            TextFlow details = new TextFlow();
            details.getStyleClass().add("details");
            details.setLineSpacing(20);


            Label name = new Label(project.getName());
            name.getStyleClass().add("card-title");
            name.setMaxWidth(200);
            name.setWrapText(true);
            details.getChildren().add(name);

            Text descriptionLabel = new Text("\n" + bundle.getString("description") + ": ");
            descriptionLabel.getStyleClass().add("bold-text");
            details.getChildren().add(descriptionLabel);

            Text description = new Text(project.getDescription());
            details.getChildren().add(description);

            Text dateStartedLabel = new Text("\n" + bundle.getString("dateStarted") + ": ");
            dateStartedLabel.getStyleClass().add("bold-text");
            details.getChildren().add(dateStartedLabel);

            Text dateStarted = new Text(project.getDateStarted().toString());
            details.getChildren().add(dateStarted);

            Text dateEndedLabel = new Text("\n" + bundle.getString("dateEnded") + ": ");
            dateEndedLabel.getStyleClass().add("bold-text");
            details.getChildren().add(dateEndedLabel);

            Text dateEnded = new Text(project.getDateEnded().toString());
            details.getChildren().add(dateEnded);

            Text priorityLabel = new Text("\n" + bundle.getString("priority") + ": ");
            priorityLabel.getStyleClass().add("bold-text");
            details.getChildren().add(priorityLabel);

            Text priority = new Text(project.getPriority());
            details.getChildren().add(priority);


            card.getChildren().addAll(imageView, details);

            card.setOnMouseClicked(e -> {

                for (Node node : projectContainer.getChildren()) {
                    if (node instanceof HBox) {
                        node.getStyleClass().remove("card-selected");
                    }
                }
                card.getStyleClass().add("card-selected");

                if (e.getClickCount() == 2) {
                    selectedProject = project;
                    onModifyButtonClick();
                } else {
                    selectedProject = project;
                }
            });
            if (darkModeToggle.isSelected()) {
                card.getStyleClass().add("dark");
            }
            projectContainer.getChildren().add(card);
        }
    }


    @FXML
    private void onDeleteButtonClick() {
        ps.deleteProjects(selectedProject.getId());
        refreshCards();
    }


    public void onModifyButtonClick() {
        if (selectedProject != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projectmanageclient/AddView.fxml"));
                Parent root = loader.load();

                AddViewController addViewController = loader.getController();
                addViewController.setProjectData(selectedProject);
                addViewController.setOnSubmitCallback(this::refreshCards);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Edit Project");
                stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/projectmanageclient/images/paper_13781926.png"))));
                stage.show();
                addViewController.setDarkMode(darkModeToggle.isSelected());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void refreshCards() {

        projectContainer.getChildren().clear();

        populateView();
    }
    @FXML
    private void onAddButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projectmanageclient/AddView.fxml"));
            Parent root = loader.load();

            AddViewController addViewController = loader.getController();
            addViewController.setOnSubmitCallback(this::refreshCards);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();
            stage.setTitle("Add Project");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/projectmanageclient/images/paper_13781926.png"))));
            addViewController.setDarkMode(darkModeToggle.isSelected());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeLanguage(Locale locale) {
        languageManager.changeLocale(locale);
        updateTexts();
        refreshCards();
    }
}


