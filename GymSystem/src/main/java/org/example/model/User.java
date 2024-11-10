package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class User {
    protected String fname;
    protected String lname;
    protected Address address;
    protected String phoneNumber;

    public User(String fname, String lname, Address address, String phoneNumber) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
