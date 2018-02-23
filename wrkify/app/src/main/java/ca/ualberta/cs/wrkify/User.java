/* MainActivity
 *
 * Version 0.0
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
 * User provides an interface for users
 *
 * @see ConcreteUser
 */

public interface User {
    String getUsername();
    String getEmail();
    String getPhoneNumber();

    void setUsername(String username);
    void setEmail(String email);
    void setPhoneNumber(String email);
}
