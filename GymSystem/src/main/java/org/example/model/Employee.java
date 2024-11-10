package org.example.model;

import lombok.Getter;

@Getter
public class Employee extends User{
    private static int employeeIdCounter = 1;
    private String employeeId;

    public Employee(String name, Address address, String phoneNumber, String employeeId) {
        super(name, address, phoneNumber);
        this.employeeId = String.valueOf(employeeIdCounter++);
    }
}
