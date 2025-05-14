package RHMS;

import java.util.Scanner;
import java.io.IOException;

import RHMS.appointments.AppointmentManager;
import RHMS.communication.ChatServer;
import RHMS.healthdata.VitalsDatabase;
import RHMS.interaction.FeedbackManager;
import RHMS.usermanagement.Administrator;
import RHMS.usermanagement.Doctor;
import RHMS.usermanagement.Patient;
import RHMS.usermanagement.User;
import RHMS.usermanagement.UserManager;

public class Main {
    public static void main(String[] args) {
        try {
            UserManager.loadUsers();
            VitalsDatabase.loadVitals();
            AppointmentManager.loadAppointments();
            FeedbackManager.loadFeedbacks();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
            return; // Exit if we can't load the data
        }

        // Start chat server in a separate thread
        new Thread(() -> {
            ChatServer.main(new String[]{});
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== Remote Patient Monitoring System =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1: login(scanner); break;
                    case 2: register(scanner); break;
                    case 0: System.out.println("Exiting..."); return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Enter User ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        User user = UserManager.getUser(id);
        if (user != null && user.login(password)) {
            System.out.println("Login successful! Welcome, " + user.getName());
            try {
                if (user instanceof Patient) {
                    ((Patient) user).patientMenu();
                } else if (user instanceof Doctor) {
                    ((Doctor) user).doctorMenu();
                } else if (user instanceof Administrator) {
                    ((Administrator) user).adminMenu();
                }
            } catch (IOException e) {
                System.out.println("Error in menu operation: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void register(Scanner scanner) {
        System.out.println("\n--- Register New User ---");
        System.out.print("Enter Role (Patient/Doctor): ");
        String role = scanner.nextLine();
        if (!role.equalsIgnoreCase("Patient") && !role.equalsIgnoreCase("Doctor")) {
            System.out.println("Invalid role. Only Patient or Doctor allowed.");
            return;
        }
        String id = UserManager.generateUserId();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        
        User user;
        if (role.equalsIgnoreCase("Patient")) {
            user = new Patient(id, name, email, password, role, phone);
        } else {
            System.out.print("Enter Specialty: ");
            String specialty = scanner.nextLine();
            user = new Doctor(id, name, email, password, role, phone, specialty);
        }
        UserManager.addUser(user);
        System.out.println("Registration successful! Your ID is: " + id);
    }
}