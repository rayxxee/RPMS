package RHMS.usermanagement;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class UserManager {
    private static ArrayList<User> users = new ArrayList<>();

    public static void addUser(User user) throws IOException {
        users.add(user);
        saveUsers();
    }

    public static User getUser(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) return user;
        }
        System.out.println("User not found for ID: " + id);
        return null;
    }

    public static void removeUser(String id) throws IOException {
        users.removeIf(user -> user.getId().equals(id));
        saveUsers();
    }

    public static void viewAllUsers() {
        System.out.println("\n--- Registered Users ---");
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (User user : users) {
                System.out.println("ID: " + user.getId() + " | Name: " + user.getName() + " | Role: " + user.getRole());
            }
        }
    }

    public static Doctor getRandomDoctor() {
        ArrayList<Doctor> doctors = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Doctor) doctors.add((Doctor) user);
        }
        if (doctors.isEmpty()) {
            System.out.println("No doctors available.");
            return null;
        }
        return doctors.get(new Random().nextInt(doctors.size()));
    }

    public static ArrayList<User> getUsers() {
        return new ArrayList<>(users);
    }

    public static void saveUsers() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
            oos.writeObject(users);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadUsers() throws IOException, ClassNotFoundException {
        File file = new File("users.ser");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (ArrayList<User>) ois.readObject();
            }
        }
    }
}