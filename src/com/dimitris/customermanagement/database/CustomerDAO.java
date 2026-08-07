package com.dimitris.customermanagement.database;

import com.dimitris.customermanagement.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public void insertCustomer(Customer customer) throws SQLException {

        String sql = "INSERT INTO customers "
                + "(first_name, last_name, tax_id, phone, email) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getAfm());
            statement.setString(4, customer.getPhone());
            statement.setString(5, customer.getEmail());

            statement.executeUpdate();
        }
    }

    public List<Customer> getAllCustomers() throws SQLException {

        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT customer_id, first_name, last_name, tax_id, phone, email "
                + "FROM customers "
                + "ORDER BY customer_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Customer customer = new Customer(
                        resultSet.getInt("customer_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("tax_id"),
                        resultSet.getString("phone"),
                        resultSet.getString("email")
                );

                customers.add(customer);
            }
        }

        return customers;
    }

    public void updateCustomer(Customer customer) throws SQLException {

        String sql = "UPDATE customers "
                + "SET first_name = ?, "
                + "last_name = ?, "
                + "tax_id = ?, "
                + "phone = ?, "
                + "email = ? "
                + "WHERE customer_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getAfm());
            statement.setString(4, customer.getPhone());
            statement.setString(5, customer.getEmail());
            statement.setInt(6, customer.getCustomerId());

            statement.executeUpdate();
        }
    }

    public void deleteCustomer(int customerId) throws SQLException {

        String sql = "DELETE FROM customers "
                + "WHERE customer_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            statement.executeUpdate();
        }
    }
}