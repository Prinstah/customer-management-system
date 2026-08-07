package com.dimitris.customermanagement.controller;

import com.dimitris.customermanagement.database.CustomerDAO;
import com.dimitris.customermanagement.model.Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;

public class CustomerListController {

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> firstNameColumn;

    @FXML
    private TableColumn<Customer, String> lastNameColumn;

    @FXML
    private TableColumn<Customer, String> afmColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    @FXML
    private TableColumn<Customer, String> emailColumn;

    @FXML
    private TextField searchAfmField;

    @FXML
    private Label totalCustomersLabel;

    private final CustomerDAO customerDAO = new CustomerDAO();

    private ObservableList<Customer> customerList;
    private FilteredList<Customer> filteredCustomers;

    @FXML
    private void initialize() {

        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("firstName")
        );

        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastName")
        );

        afmColumn.setCellValueFactory(
                new PropertyValueFactory<>("afm")
        );

        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phone")
        );

        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("email")
        );

        customerTable.setEditable(true);

        firstNameColumn.setCellFactory(
                TextFieldTableCell.forTableColumn(
                        new DefaultStringConverter()
                )
        );

        lastNameColumn.setCellFactory(
                TextFieldTableCell.forTableColumn(
                        new DefaultStringConverter()
                )
        );

        afmColumn.setCellFactory(
                TextFieldTableCell.forTableColumn(
                        new DefaultStringConverter()
                )
        );

        phoneColumn.setCellFactory(
                TextFieldTableCell.forTableColumn(
                        new DefaultStringConverter()
                )
        );

        emailColumn.setCellFactory(
                TextFieldTableCell.forTableColumn(
                        new DefaultStringConverter()
                )
        );

        configureEditHandlers();

        loadCustomers();

        configureSearch();
    }

    private void configureEditHandlers() {

        firstNameColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();
            String oldValue = customer.getFirstName();

            customer.setFirstName(event.getNewValue());

            updateCustomer(
                    customer,
                    () -> customer.setFirstName(oldValue)
            );
        });

        lastNameColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();
            String oldValue = customer.getLastName();

            customer.setLastName(event.getNewValue());

            updateCustomer(
                    customer,
                    () -> customer.setLastName(oldValue)
            );
        });

        afmColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();

            String oldValue = customer.getAfm();
            String newValue = event.getNewValue().trim();

            if (!newValue.matches("\\d{9}")) {

                showErrorAlert(
                        "Μη έγκυρο ΑΦΜ",
                        "Το ΑΦΜ πρέπει να αποτελείται από 9 αριθμούς."
                );

                customer.setAfm(oldValue);
                customerTable.refresh();
                return;
            }

            customer.setAfm(newValue);

            updateCustomer(
                    customer,
                    () -> customer.setAfm(oldValue)
            );
        });

        phoneColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();

            String oldValue = customer.getPhone();
            String newValue = event.getNewValue().trim();

            if (!newValue.matches("\\d{10}")) {

                showErrorAlert(
                        "Μη έγκυρο Τηλέφωνο",
                        "Το τηλέφωνο πρέπει να αποτελείται από 10 αριθμούς."
                );

                customer.setPhone(oldValue);
                customerTable.refresh();
                return;
            }

            customer.setPhone(newValue);

            updateCustomer(
                    customer,
                    () -> customer.setPhone(oldValue)
            );
        });

        emailColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();

            String oldValue = customer.getEmail();
            String newValue = event.getNewValue().trim();

            if (!isValidEmail(newValue)) {

                showErrorAlert(
                        "Μη έγκυρο Email",
                        "Παρακαλώ εισάγετε ένα έγκυρο email."
                );

                customer.setEmail(oldValue);
                customerTable.refresh();
                return;
            }

            customer.setEmail(newValue);

            updateCustomer(
                    customer,
                    () -> customer.setEmail(oldValue)
            );
        });
    }

    private void configureSearch() {

        searchAfmField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    String searchValue = newValue.trim();

                    filteredCustomers.setPredicate(customer -> {

                        if (searchValue.isEmpty()) {
                            return true;
                        }

                        return customer.getAfm().contains(searchValue);
                    });

                    updateCustomerCount();
                }
        );
    }

    @FXML
    private void handleDeleteCustomer() {

        Customer selectedCustomer =
                customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {

            showErrorAlert(
                    "Δεν έχει επιλεγεί πελάτης",
                    "Παρακαλώ επιλέξτε έναν πελάτη από τον πίνακα."
            );

            return;
        }

        Alert confirmation =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Επιβεβαίωση Διαγραφής");
        confirmation.setHeaderText("Διαγραφή πελάτη");

        confirmation.setContentText(
                "Είστε σίγουροι ότι θέλετε να διαγράψετε τον πελάτη "
                        + selectedCustomer.getFirstName()
                        + " "
                        + selectedCustomer.getLastName()
                        + ";"
        );

        Optional<ButtonType> result =
                confirmation.showAndWait();

        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            try {

                customerDAO.deleteCustomer(
                        selectedCustomer.getCustomerId()
                );

                customerList.remove(selectedCustomer);

                updateCustomerCount();

                showInformationAlert(
                        "Επιτυχής Διαγραφή",
                        "Ο πελάτης διαγράφηκε επιτυχώς."
                );

            } catch (SQLException e) {

                showErrorAlert(
                        "Σφάλμα Διαγραφής",
                        "Δεν ήταν δυνατή η διαγραφή του πελάτη."
                );

                e.printStackTrace();
            }
        }
    }

    private void updateCustomer(
            Customer customer,
            Runnable rollback) {

        try {

            customerDAO.updateCustomer(customer);

        } catch (SQLException e) {

            rollback.run();
            customerTable.refresh();

            showErrorAlert(
                    "Σφάλμα Ενημέρωσης",
                    "Δεν ήταν δυνατή η ενημέρωση του πελάτη."
            );

            e.printStackTrace();
        }
    }

    private void loadCustomers() {

        try {

            List<Customer> customers =
                    customerDAO.getAllCustomers();

            customerList =
                    FXCollections.observableArrayList(customers);

            filteredCustomers =
                    new FilteredList<>(customerList, customer -> true);

            customerTable.setItems(filteredCustomers);

            updateCustomerCount();

        } catch (SQLException e) {

            showErrorAlert(
                    "Σφάλμα Βάσης Δεδομένων",
                    "Δεν ήταν δυνατή η φόρτωση των πελατών."
            );

            e.printStackTrace();
        }
    }

    private void updateCustomerCount() {

        totalCustomersLabel.setText(
                "Σύνολο Πελατών: " + filteredCustomers.size()
        );
    }

    private boolean isValidEmail(String email) {

        return email.matches(
                "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        );
    }

    private void showErrorAlert(
            String title,
            String message) {

        Alert alert =
                new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInformationAlert(
            String title,
            String message) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}