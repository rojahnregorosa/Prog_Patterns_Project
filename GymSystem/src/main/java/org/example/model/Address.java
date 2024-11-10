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
