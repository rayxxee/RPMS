package RHMS.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String specialization;
    private List<String> patientIds;

    public Doctor(String id, String name, String email, String password, String specialization) {
        super(id, name, email, password, "Doctor");
        this.specialization = specialization;
        this.patientIds = new ArrayList<>();
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<String> getPatientIds() {
        return new ArrayList<>(patientIds);
    }

    public void addPatient(String patientId) {
        if (!patientIds.contains(patientId)) {
            patientIds.add(patientId);
        }
    }

    public void removePatient(String patientId) {
        patientIds.remove(patientId);
    }
} 