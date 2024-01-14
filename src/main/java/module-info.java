module com.example.projectmanageclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires ProjectService;
    requires javafx.web;
    requires com.google.common;
    requires org.apache.commons.codec;


    opens com.example.projectmanageclient to javafx.fxml;
    exports com.example.projectmanageclient;
    exports com.example.projectmanageclient.controller;
    opens com.example.projectmanageclient.controller to javafx.fxml, com.fasterxml.jackson.databind;
}