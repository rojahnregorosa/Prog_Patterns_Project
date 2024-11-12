package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private String streetName;
    private String city;
    private String province;
    private String zipCode;
    private int streetNumber;

    public Address(int streetNumber, String streetName, String city, String province, String zipCode) {
        this.streetName = streetName;
        this.city = city;
        this.province = province;
        this.zipCode = zipCode;
        this.streetNumber = streetNumber;
    }
}
