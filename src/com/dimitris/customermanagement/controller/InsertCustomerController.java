package com.dimitris.customermanagement.controller;

import com.dimitris.customermanagement.database.CustomerDAO;
import com.dimitris.customermanagement.model.Customer;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InsertCustomerController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField afmField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    private final CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    private void handleAddCustomer() {

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String afm = afmField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (firstName.isEmpty()
                || lastName.isEmpty()
                || afm.isEmpty()
                || phone.isEmpty()
                || email.isEmpty()) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Σφάλμα Εισαγωγής",
                    "Παρακαλώ συμπληρώστε όλα τα πεδία."
            );
            return;
        }

        if (!afm.matches("\\d{9}")) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Σφάλμα Εισαγωγής",
                    "Το ΑΦΜ πρέπει να αποτελείται από 9 αριθμούς."
            );
            return;
        }

        if (!phone.matches("\\d{10}")) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Σφάλμα Εισαγωγής",
                    "Ο αριθμός τηλεφώνου πρέπει να αποτελείται από ακριβώς 10 αριθμούς."
            );
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Σφάλμα Εισαγωγής",
                    "Μη έγκυρη μορφή email."
            );
            return;
        }

        Customer customer =
                new Customer(firstName, lastName, afm, phone, email);

        try {
            customerDAO.insertCustomer(customer);

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Επιτυχής Εισαγωγή",
                    "Ο πελάτης προστέθηκε επιτυχώς."
            );

            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Σφάλμα Βάσης Δεδομένων",
                    "Δεν ήταν δυνατή η αποθήκευση του πελάτη."
            );

            e.printStackTrace();
        }
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        return email.matches(
                "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        );
    }
}