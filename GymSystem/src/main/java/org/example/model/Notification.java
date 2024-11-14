package org.example.model;

public interface Notification {
    void sendNotification();
    void setMessage(String message);  // New method to set a custom message
    String getMessage();
}
