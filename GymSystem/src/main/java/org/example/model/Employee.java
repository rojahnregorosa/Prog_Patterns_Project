package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee extends User{
    private static int employeeIdCounter = 1;
    private String employeeId;

    public Employee(String fName, String lName, Address address, String phoneNumber, String employeeId) {
        super(fName, lName, address, phoneNumber);
        this.employeeId = String.valueOf(employeeIdCounter++);
    }
}
