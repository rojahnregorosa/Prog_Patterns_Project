package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class User {
    protected String name;
    protected Address address;
    protected String phoneNumber;

    public User(String name, Address address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
