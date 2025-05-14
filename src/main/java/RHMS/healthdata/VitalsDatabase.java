package RHMS.healthdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class VitalsDatabase {
    private static final String VITALS_FILE = "vitals.ser";
    private static List<Vitals> vitalsList = new ArrayList<>();

    static {
        try {
            loadVitals();
        } catch (Exception e) {
            System.out.println("No existing vitals data found. Starting with empty database.");
        }
    }

    public static List<Vitals> getPatientVitals(String patientId) {
        List<Vitals> patientVitals = new ArrayList<>();
        for (Vitals v : vitalsList) {
            if (v.getPatientId().equals(patientId)) {
                patientVitals.add(v);
            }
        }
        return patientVitals;
    }

    public static Vitals getVital(String id) {
        for (Vitals vital : vitalsList) {
            if (id.equals(vital.getPatientId())) return vital;
        }
        System.out.println("Vitals not found for Patient ID: " + id);
        return null;
    }

    public static void addVital(Vitals vitals) {
        // Remove any existing vitals for this patient
        vitalsList.removeIf(v -> v.getPatientId().equals(vitals.getPatientId()));
        vitalsList.add(vitals);
        try {
            saveVitals();
        } catch (IOException e) {
            System.err.println("Error saving vitals: " + e.getMessage());
        }
    }

    public static void saveVitals() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(VITALS_FILE))) {
            oos.writeObject(vitalsList);
            System.out.println("Vitals saved successfully to " + VITALS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving vitals to file: " + e.getMessage());
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadVitals() throws IOException, ClassNotFoundException {
        File file = new File(VITALS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                vitalsList = (List<Vitals>) ois.readObject();
                System.out.println("Vitals loaded successfully from " + VITALS_FILE);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading vitals from file: " + e.getMessage());
                throw e;
            }
        } else {
            System.out.println("No existing vitals file found. Starting with empty database.");
            vitalsList = new ArrayList<>();
        }
    }

    public static void refreshData() throws IOException, ClassNotFoundException {
        loadVitals();
        System.out.println("Vitals data refreshed.");
    }
}