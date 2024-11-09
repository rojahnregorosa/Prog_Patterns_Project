package org.example.model;

import lombok.Getter;

@Getter
public class Member extends User {
    protected static int memberIdCounter = 0;
    protected int memberId; //maybe string
    protected Membership membershipType;
    protected double balance;

    public Member(String name, Address address, String phoneNumber) {
        super(name, address, phoneNumber);
    }

    public Member(String name, Address address, String phoneNumber, Membership membershipType, double balance) {
        super(name, address, phoneNumber);
        this.memberId = memberIdCounter++;
        this.membershipType = membershipType;
        this.balance = balance;
    }


}
