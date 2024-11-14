package org.example.model;

public class NotificationService {
    /**
     * Send new notification
     *
     * @param notification the notification to send
     */
    public void sendNotification(Notification notification) {
        notification.sendNotification();
    }
}
