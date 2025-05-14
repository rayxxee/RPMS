package RHMS.usermanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UserManager {
    private static List<User> users = new ArrayList<>();
    private static final String USERS_FILE = System.getProperty("user.home") + File.separator + "rpm_users.ser";

    static {
        try {
            System.out.println("Initializing UserManager - Loading users from file...");
            loadUsers();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error during initial user load: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addUser(User user) {
        users.add(user);
        try {
            saveUsers();
            System.out.println("User added and saved successfully: " + user.getId());
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    public static User getUser(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
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

    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public static String generateUserId() {
        return UUID.randomUUID().toString();
    }

    public static void saveUsers() throws IOException {
        File file = new File(USERS_FILE);
        System.out.println("Saving users to: " + file.getAbsolutePath());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(users);
            System.out.println("Successfully saved " + users.size() + " users");
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadUsers() throws IOException, ClassNotFoundException {
        File file = new File(USERS_FILE);
        System.out.println("Loading users from: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());
        System.out.println("File size: " + (file.exists() ? file.length() : "N/A"));
        
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object loadedObject = ois.readObject();
                System.out.println("Loaded object type: " + (loadedObject != null ? loadedObject.getClass().getName() : "null"));
                
                if (loadedObject instanceof List) {
                    users = (List<User>) loadedObject;
                    System.out.println("Successfully loaded " + users.size() + " users");
                    for (User user : users) {
                        System.out.println("Loaded user - ID: " + user.getId() + ", Name: " + user.getName() + ", Role: " + user.getRole());
                    }
                } else {
                    System.out.println("Error: Loaded object is not a List");
                    users = new ArrayList<>();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading users: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        } else {
            System.out.println("No users file found. Starting with empty user list.");
            users = new ArrayList<>();
        }
    }

    public static void refreshData() {
        try {
            loadUsers();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error refreshing user data: " + e.getMessage());
        }
    }

    public static List<Patient> getDoctorPatients(String doctorId) {
        List<Patient> patients = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Patient) {
                Patient patient = (Patient) user;
                if (patient.getDoctorId() != null && patient.getDoctorId().equals(doctorId)) {
                    patients.add(patient);
                }
            }
        }
        return patients;
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

    public static User authenticateUser(String userId, String password) {
        System.out.println("Attempting to authenticate user: " + userId);
        System.out.println("Current number of users in memory: " + users.size());
        for (User user : users) {
            System.out.println("Checking user - ID: " + user.getId() + ", Name: " + user.getName());
            if (user.getId().equals(userId) && user.getPassword().equals(password)) {
                System.out.println("Authentication successful for user: " + userId);
                return user;
            }
        }
        System.out.println("Authentication failed for user: " + userId);
        return null;
    }
}