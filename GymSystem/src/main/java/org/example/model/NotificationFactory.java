package org.example.model;

public class NotificationFactory {
    public static Notification createNotification(String type, String message) {
        Notification notification = null;
        if (type.equalsIgnoreCase("email")) {
            notification = new EmailNotification();
        } else if (type.equalsIgnoreCase("sms")) {
            notification = new SMSNotification();
        } else {
            System.out.println("Unknown Notification type" + type);
        }
        if (notification != null) {
            notification.setMessage(message);  // Set the custom message
        }
        return notification;
    }
}
