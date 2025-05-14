package RHMS.usermanagement;

import RHMS.reporting.ReportGenerator;

import java.util.Scanner;

public class Administrator extends User {
    private static final long serialVersionUID = 1L;

    public Administrator(String id, String name, String email, String password, String role, String phone) {
        super(id, name, email, password, role, phone);
    }

    public void adminMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== Administrator Menu =====");
            System.out.println("1. Add Patient");
            System.out.println("2. Add Doctor");
            System.out.println("3. Remove User");
            System.out.println("4. View All Users");
            System.out.println("5. Generate System Report");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1: addUser("Patient"); break;
                    case 2: addUser("Doctor"); break;
                    case 3: removeUser(); break;
                    case 4: UserManager.viewAllUsers(); break;
                    case 5: ReportGenerator.generateSystemReport(); break;
                    case 0: logout(); return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private void addUser(String role) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nAdding new " + role + "...");
        try {
            System.out.print("Enter ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            System.out.print("Enter Phone: ");
            String phone = scanner.nextLine();
            if (role.equalsIgnoreCase("Patient")) {
                UserManager.addUser(new Patient(id, name, email, password, role, phone));
            } else if (role.equalsIgnoreCase("Doctor")) {
                UserManager.addUser(new Doctor(id, name, email, password, role, phone));
            }
            System.out.println(role + " added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private void removeUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter User ID to remove: ");
        String id = scanner.nextLine();
        try {
            UserManager.removeUser(id);
            System.out.println("User removed successfully.");
        } catch (Exception e) {
            System.out.println("Error removing user: " + e.getMessage());
        }
    }
}