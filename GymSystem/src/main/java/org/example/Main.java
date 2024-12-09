package org.example;

import org.example.controller.GymSystem;
import org.example.database.DatabaseSetup;

public class Main {
    public static void main(String[] args) {
        DatabaseSetup.createTables();
        GymSystem gymSystem = new GymSystem();
        gymSystem.start();
    }
}