package org.example.controller;

import org.example.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserController {
    protected Map<String, User> users;

    public UserController() {
        users = new HashMap<>();
    }

    /**
     * Register a new user
     * @param user user to be registered
     * @return
     */
    public boolean registerUser(User user) {
        if (user != null && users.containsKey(user.getPhoneNumber())) {
            users.put(user.getPhoneNumber(), user);
            return true;
        }
        return false;
    }

    /**
     * Log in the system
     * @param phoneNumber phone number needed to log in
     * @return user
     */
    public User login(String phoneNumber) {
        User user = users.get(phoneNumber);
        if (user != null) {
            System.out.println("Login successful for user: " + user.getFname() + " " + user.getLname());
            return user;
        }
        System.out.println("Login failed: user not found.");
        return null;
    }

    /**
     * Log out the system
     * @param user user to log out
     */
    public void logout(User user) {
        if (user != null) {
            System.out.println("User logged out: " + user.getFname() + " " + user.getLname());
        }
    }

    /**
     * Updates profile (member and employee)
     * @param phoneNumber
     * @param newFName
     * @param newLName
     * @param newPhoneNumber
     * @return
     */
    public boolean updateProfile(String phoneNumber, String newFName, String newLName, String newPhoneNumber) {
        User user = users.get(phoneNumber);
        if (user != null) {
            user.setFname(newFName);
            user.setLname(newLName);
            user.setPhoneNumber(newPhoneNumber);
            users.remove(phoneNumber);
            users.put(newPhoneNumber, user); // Update key in the map
            return true;
        }
        return false;
    }

    /**
     * Views profile of registered user
     * @param phoneNumber of the user to view
     * @return
     */
    public User viewProfile(String phoneNumber) {
        return users.get(phoneNumber);
    }
}
