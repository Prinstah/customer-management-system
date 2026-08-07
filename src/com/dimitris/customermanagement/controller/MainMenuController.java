package com.dimitris.customermanagement.controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private void handleInsertCustomer() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../view/InsertCustomer.fxml")
        );

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Εισαγωγή Πελάτη");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    @FXML
    private void handleShowCustomers() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../view/CustomerList.fxml")
        );

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Εμφάνιση Πελατών");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }
}