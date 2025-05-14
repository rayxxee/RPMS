package RHMS.interaction;

import java.io.Serializable;
import java.util.ArrayList;

public class Prescription implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<String> medications = new ArrayList<>();
    private ArrayList<String> dosages = new ArrayList<>();
    private ArrayList<String> schedules = new ArrayList<>();

    public void addMedication(String medication, String dosage, String schedule) {
        medications.add(medication);
        dosages.add(dosage);
        schedules.add(schedule);
    }

    public ArrayList<String> getMedications() { return medications; }
    public ArrayList<String> getDosages() { return dosages; }
    public ArrayList<String> getSchedules() { return schedules; }

    public void displayPrescription() {
        System.out.println("\n--- Prescription Details ---");
        for (int i = 0; i < medications.size(); i++) {
            System.out.println("Medication: " + medications.get(i) + ", Dosage: " + dosages.get(i) + ", Schedule: " + schedules.get(i));
        }
    }
}