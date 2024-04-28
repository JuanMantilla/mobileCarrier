package com.javacrud.javacrud.util;

public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;

    public UserResponse(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getter for 'id'
    public String getId() {
        return id;
    }

    // Setter for 'id'
    public void setId(String id) {
        this.id = id;
    }

    // Getter for 'firstName'
    public String getFirstName() {
        return firstName;
    }

    // Setter for 'firstName'
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter for 'lastName'
    public String getLastName() {
        return lastName;
    }

    // Setter for 'lastName'
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter for 'email'
    public String getEmail() {
        return email;
    }

    // Setter for 'email'
    public void setEmail(String email) {
        this.email = email;
    }
}
