package RHMS.healthdata;

import java.io.*;
import java.util.ArrayList;

public class VitalsDatabase {
    private static ArrayList<VitalSign> vitals = new ArrayList<>();

    public static VitalSign getVital(String id) {
        for (VitalSign vital : vitals) {
            if (id.equals(vital.getPid())) return vital;
        }
        System.out.println("Vitals not found for Patient ID: " + id);
        return null;
    }

    public static void addVital(VitalSign vital) throws IOException {
        vitals.removeIf(v -> v.getPid().equals(vital.getPid()));
        vitals.add(vital);
        saveVitals();
    }

    public static void saveVitals() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("vitals.ser"))) {
            oos.writeObject(vitals);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadVitals() throws IOException, ClassNotFoundException {
        File file = new File("vitals.ser");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                vitals = (ArrayList<VitalSign>) ois.readObject();
            }
        }
    }
}