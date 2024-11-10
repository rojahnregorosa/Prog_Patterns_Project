package org.example.model;

import lombok.Getter;

@Getter
public class Address {
    private final String streetName;
    private final String city;
    private final String province;
    private final String zipCode;

    public Address(String city, String province, String streetName, String zipCode) {
        this.city = city;
        this.province = province;
        this.streetName = streetName;

        if (!isZipCodeValid(zipCode)) {
            throw new IllegalArgumentException("Invalid Zip Code!");
        }

        this.zipCode = zipCode;
    }

    private boolean isZipCodeValid(String zipCode) {
        return zipCode != null && zipCode.matches("^[A-Z]\\d[A-Z]\\d[A-Z]\\d$]");
    }
}
