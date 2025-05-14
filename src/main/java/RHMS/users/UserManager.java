package RHMS.users;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static List<User> users = new ArrayList<>();

    public static User authenticateUser(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static List<Patient> getPatients() {
        List<Patient> patients = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Patient) {
                patients.add((Patient) user);
            }
        }
        return patients;
    }

    public static List<Doctor> getDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Doctor) {
                doctors.add((Doctor) user);
            }
        }
        return doctors;
    }

    public static void addUser(User user) {
        users.add(user);
        try {
            saveUsers();
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    public static void removeUser(String userId) {
        users.removeIf(user -> user.getId().equals(userId));
        try {
            saveUsers();
        } catch (IOException e) {
            System.out.println("Error saving users after removal: " + e.getMessage());
        }
    }

    public static User getUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
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
                users = (List<User>) ois.readObject();
            }
        }
    }

    public static void refreshData() throws IOException, ClassNotFoundException {
        loadUsers();
        System.out.println("User data refreshed.");
    }
} 