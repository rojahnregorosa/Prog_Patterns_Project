package org.example.model;

public class EmailNotification implements Notification {
    private String message;

    @Override
    public void sendNotification() {
        System.out.println("Email Notification: " + message);
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}