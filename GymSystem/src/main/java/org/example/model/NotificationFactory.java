package org.example.model;

import org.example.utils.LanguageManager;

public class NotificationFactory {

    /**
     * Returns new notification
     *
     * @param type    type of notification
     * @param message the content of the notification
     * @return new notification
     */
    public static Notification createNotification(String type, String message) {
        Notification notification = null;
        if (type.equalsIgnoreCase("email")) {
            notification = new EmailNotification();
        } else if (type.equalsIgnoreCase("sms")) {
            notification = new SMSNotification();
        } else {
            System.out.print(LanguageManager.getInstance().getMessage("unknown_notification") + type);
        }
        if (notification != null) {
            notification.setMessage(message);  // Set the custom message
        }
        return notification;
    }
}
