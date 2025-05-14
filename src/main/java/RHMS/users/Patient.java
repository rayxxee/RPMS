package RHMS.users;

import java.io.Serializable;

public class Patient extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String doctorId;
    private String medicalHistory;
    private String allergies;
    private String currentMedications;

    public Patient(String id, String name, String email, String password, String doctorId) {
        super(id, name, email, password, "Patient");
        this.doctorId = doctorId;
        this.medicalHistory = "";
        this.allergies = "";
        this.currentMedications = "";
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getCurrentMedications() {
        return currentMedications;
    }

    public void setCurrentMedications(String currentMedications) {
        this.currentMedications = currentMedications;
    }
} 