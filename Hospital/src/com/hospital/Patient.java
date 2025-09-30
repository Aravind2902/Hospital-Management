package com.hospital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scn;

    public Patient(Connection connection, Scanner scn) {
        this.connection = connection;
        this.scn = scn;
    }

    // Method to add a new patient
    public void addPatient() {
        System.out.println("Enter the Patient Details :");

        System.out.print("Enter patient Name: ");
        String name = scn.next();

        System.out.print("Enter patient Age: ");
        int age = scn.nextInt();

        System.out.print("Enter patient Gender: ");
        String gender = scn.next();

        String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("✅ Patient Added Successfully!");
            } else {
                System.out.println("⚠️ Failed to add patient!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to view all patients
    public void viewPatient() {
        String query = "SELECT * FROM patients";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet r = ps.executeQuery()) {

            System.out.println("Patients:\n");
            System.out.printf("%-12s %-15s %-8s %-10s%n", "ID", "Name", "Age", "Gender");
            System.out.println("------------------------------------------");

            while (r.next()) {
                int id = r.getInt("id");
                String name = r.getString("name");
                int age = r.getInt("age");
                String gender = r.getString("gender");

                System.out.printf("%-12d %-15s %-8d %-10s%n", id, name, age, gender);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to check if a patient exists by ID
    public boolean getPatientId(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet r = ps.executeQuery()) {
                return r.next(); // true if record exists
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
