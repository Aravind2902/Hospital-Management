package com.hospital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Doctors {
    private Connection connection;

    public Doctors(Connection connection) {
        this.connection = connection;
    }

    // View all doctors
    public void viewDoctor() {
        String query = "SELECT * FROM doctors";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet r = ps.executeQuery();

            System.out.println("doctor");
            System.out.println("____****_____");
            System.out.println("doctor id | Name | dept");
            System.out.println("*");

            while (r.next()) {
                int id = r.getInt("id");
                String name = r.getString("name");
                String department = r.getString("dept");

                System.out.printf("| %-10s | %-7s | %-14s |\n", id, name, department);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get doctor by ID
    public boolean getdoctorid(int id) {

        try {
        	String query = "SELECT * FROM doctors WHERE id=?";
        	
            PreparedStatement p = connection.prepareStatement(query);
            p.setInt(1, id);

            ResultSet r = p.executeQuery();
            if (r.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}