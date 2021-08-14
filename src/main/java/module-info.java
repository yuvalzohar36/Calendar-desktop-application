module NewApp {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires google.http.client.jackson2;
    requires google.api.services.gmail.v1.rev110;
    requires com.google.api.services.calendar;
    requires com.jfoenix;
    requires flow.polymer.template;
    requires flow.server;
    requires java.desktop;
    requires vaadin.time.picker.flow;
    opens com.example.newapp to javafx.graphics;
    exports com.example.newapp;
}
