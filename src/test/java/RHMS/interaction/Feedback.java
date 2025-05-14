package RHMS.interaction;

import java.io.Serializable;

public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;
    private String patientId;
    private String doctorId;
    private String feedbackText;
    private Prescription prescription;

    public Feedback(String patientId, String doctorId, String feedbackText, Prescription prescription) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.feedbackText = feedbackText;
        this.prescription = prescription;
    }

    public String getPatientId() { return patientId; }
    public String getFeedbackText() { return feedbackText; }
    public Prescription getPrescription() { return prescription; }

    public void displayFeedback() {
        System.out.println("\n--- Doctor Feedback ---");
        System.out.println("Patient ID: " + patientId);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Feedback: " + feedbackText);
        if (prescription != null) {
            prescription.displayPrescription();
        } else {
            System.out.println("No prescription provided.");
        }
    }
}