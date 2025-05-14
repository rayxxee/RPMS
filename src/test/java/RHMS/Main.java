package RHMS;

import RHMS.appointments.AppointmentManager;
import RHMS.communication.ChatServer;
import RHMS.healthdata.VitalsDatabase;
import RHMS.interaction.FeedbackManager;
import RHMS.usermanagement.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            UserManager.loadUsers();
            VitalsDatabase.loadVitals();
            AppointmentManager.loadAppointments();
            FeedbackManager.loadFeedbacks();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

        new Thread(() -> {
            try {
                ChatServer.main(new String[]{});
            } catch (IOException e) {
                System.out.println("Chat server error: " + e.getMessage());
            }
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

    private static void login(Scanner scanner) throws IOException {
        System.out.print("Enter User ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        User user = UserManager.getUser(id);
        if (user != null && user.login(password)) {
            System.out.println("Login successful! Welcome, " + user.getName());
            if (user instanceof Patient) {
                ((Patient) user).patientMenu();
            } else if (user instanceof Doctor) {
                ((Doctor) user).doctorMenu();
            } else if (user instanceof Administrator) {
                ((Administrator) user).adminMenu();
            }
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void register(Scanner scanner) throws IOException {
        System.out.println("\n--- Register New User ---");
        System.out.print("Enter Role (Patient/Doctor): ");
        String role = scanner.nextLine();
        if (!role.equalsIgnoreCase("Patient") && !role.equalsIgnoreCase("Doctor")) {
            System.out.println("Invalid role. Only Patient or Doctor allowed.");
            return;
        }
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        if (UserManager.getUser(id) != null) {
            System.out.println("User ID already exists.");
            return;
        }
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        User user = role.equalsIgnoreCase("Patient") ?
                new Patient(id, name, email, password, role, phone) :
                new Doctor(id, name, email, password, role, phone);
        UserManager.addUser(user);
        System.out.println("Registration successful!");
    }
}