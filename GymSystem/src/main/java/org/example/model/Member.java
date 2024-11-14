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
    /**
     * -- GETTER --
     *
     * @return
     */
    @Getter
    private static List<Notification> notifications;

    public Member(String fname, String lname, Address address, String phoneNumber, Membership membershipType, double balance) {
        super(fname, lname, address, phoneNumber);
        this.memberId = String.valueOf(memberIdCounter++);
        this.membershipType = membershipType;
        this.balance = balance;
        notifications = new ArrayList<>();
    }

    /**
     * Adds notification
     *
     * @param notification the new notification
     */
    public static void addNotification(Notification notification) {
        notifications.add(notification);
    }
}
