/* ConcreteUser
 *
 * Version 0.1
 *
 * Feb 23, 2018
 *
 * Copyright (c) 2018 CMPUT301W18T18, CMPUT301, University of Alberta - All
 * rights Reserved you may use, distribute or modify this code under terms and
 * conditions of Code of Student Behavior at University of Alberta you can find
 * a copy of the license in this project. Otherwise, please contact
 * pelliott@ualberta.ca
 */
package ca.ualberta.cs.wrkify;

/**
 * ConcreteUser is the java implementation of User
 * TODO: add restrictions to setters
 *
 * @see User
 */

public class ConcreteUser implements User {
    private String username;
    private String email;
    private String phoneNumber;

    public ConcreteUser(String username, String email, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
