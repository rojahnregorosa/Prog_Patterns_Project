package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member extends User {
    protected static int memberIdCounter = 1;
    protected int memberId; //maybe string
    protected Membership membershipType;
    protected double balance; //how much they have to pay

    public Member(String fname, String lname, Address address, String phoneNumber, Membership membershipType, double balance) {
        super(fname, lname, address, phoneNumber);
        this.memberId = memberIdCounter++;
        this.membershipType = membershipType;
        this.balance = balance;
    }


}
