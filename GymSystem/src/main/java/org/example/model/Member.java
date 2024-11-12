package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Member extends User {
    protected static int memberIdCounter = 1;
    protected String memberId;
    protected Membership membershipType;
    protected double balance; //how much they have to pay
    private List<Notification> notifications;

    public Member(String fname, String lname, Address address, String phoneNumber, Membership membershipType, double balance) {
        super(fname, lname, address, phoneNumber);
        this.memberId = String.valueOf(memberIdCounter++);
        this.membershipType = membershipType;
        this.balance = balance;
        this.notifications = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public List<Notification> getNotifications() {
        return notifications;
    }

    /**
     *
     * @param notification
     */
    public void addNotification(Notification notification) {
        notifications.add(notification);
    }
}
