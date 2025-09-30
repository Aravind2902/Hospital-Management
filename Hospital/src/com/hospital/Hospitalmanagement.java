package com.hospital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Hospitalmanagement {

    private static final String url = "jdbc:mysql://localhost:3306/hospitalm";
    private static final String username = "root";
    private static final String password = "Srikavi@1999";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
        }

        Scanner scn = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            System.out.println("connected db");

            Patient patient = new Patient(connection, scn);
            Doctors doctor = new Doctors(connection);

            while (true) {
                System.out.println("Welcome TO Hospital Mangement");
                System.out.println("1,Add patient");
                System.out.println("2,view patient");
                System.out.println("3,view doctors");
                System.out.println("4,Book Appointment");
                System.out.println("5,Exit");

                System.out.println("Enter your choice:");
                int choice = scn.nextInt();

                switch (choice) {
                    case 1:
                        patient.addPatient();
                        break;

                    case 2:
                        patient.viewPatient();
                        break;

                    case 3:
                        doctor.viewDoctor();
                        break;

                    case 4:
                        bookappointment(patient, doctor, connection, scn);
                        System.out.println();
                        break;

                    case 5:
                        return;

                    default:
                        System.out.println("Invalid Request");
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bookappointment(Patient patient, Doctors doctors, Connection connection, Scanner scn) {
        System.out.println("Enter the patient ID: ");
        int patientId = scn.nextInt();

        System.out.println("Enter the patient Name: ");
        String patientname = scn.next();

        System.out.println("Enter the Doctor Id: ");
        int doctorid = scn.nextInt();

        System.out.println("Enter the Apppointment date(yyyy-mm-dd): ");
        String appointmentdate = scn.next();

        if (patient.getPatientId(patientId) && doctors.getdoctorid(doctorid)) {
            if (checkdoctoravailability(doctorid, appointmentdate, connection)) {

                String appointmentquery =
                        "INSERT INTO appointments(patient_id, Patient_name, doctor_id, appointment_date) VALUES(?, ?, ?, ?)";

                try {
                    PreparedStatement p = connection.prepareStatement(appointmentquery);

                    p.setInt(1, patientId);
                    p.setString(2, patientname);
                    p.setInt(3, doctorid);
                    p.setString(4, appointmentdate);

                    int rowsaffected = p.executeUpdate();
                    if (rowsaffected > 0) {
                        System.out.println("Appointment Booked");
                    } else {
                        System.out.println("Failed to Book Appointment");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean checkdoctoravailability(int doctorid, String appointmentdate, Connection connection) {
        try {
            String query =
                    "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=?";

            PreparedStatement p = connection.prepareStatement(query);
            p.setInt(1, doctorid);
            p.setString(2, appointmentdate);
            ResultSet r = p.executeQuery();

            if (r.next()) {
                int count = r.getInt(1);
                if (count == 0)
                    return true;
                else
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}