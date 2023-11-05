module com.example.projectmanageclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;


    opens com.example.projectmanageclient to javafx.fxml;
    exports com.example.projectmanageclient;
    exports com.example.projectmanageclient.controller;
    exports com.example.projectmanageclient.model;
    opens com.example.projectmanageclient.controller to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.example.projectmanageclient.service to com.fasterxml.jackson.databind;
}