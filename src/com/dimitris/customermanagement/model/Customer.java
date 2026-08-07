package com.dimitris.customermanagement.model;

public class Customer {

    private int customerId;
    private String firstName;
    private String lastName;
    private String afm;
    private String phone;
    private String email;

    public Customer(int customerId,
                    String firstName,
                    String lastName,
                    String afm,
                    String phone,
                    String email) {

        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.afm = afm;
        this.phone = phone;
        this.email = email;
    }

    public Customer(String firstName,
                    String lastName,
                    String afm,
                    String phone,
                    String email) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.afm = afm;
        this.phone = phone;
        this.email = email;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}