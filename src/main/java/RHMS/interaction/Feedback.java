package RHMS.interaction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;
    private String feedbackId;
    private String patientId;
    private String doctorId;
    private String feedback;
    private String prescription;
    private String dateTime;
    private String appointmentId;

    public Feedback(String patientId, String doctorId, String feedback, String prescription, String appointmentId) {
        this.feedbackId = generateFeedbackId();
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.feedback = feedback;
        this.prescription = prescription;
        this.appointmentId = appointmentId;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private String generateFeedbackId() {
        return "FB" + System.currentTimeMillis();
    }

    // Getters
    public String getFeedbackId() { return feedbackId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getFeedback() { return feedback; }
    public String getPrescription() { return prescription; }
    public String getDateTime() { return dateTime; }
    public String getAppointmentId() { return appointmentId; }

    // Setters
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    @Override
    public String toString() {
        return String.format("Feedback ID: %s\nDate: %s\nFeedback: %s\nPrescription: %s",
            feedbackId, dateTime, feedback, prescription);
    }

    public void displayFeedback() {
        System.out.println("\n--- Doctor Feedback ---");
        System.out.println("Patient ID: " + patientId);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Feedback: " + feedback);
        if (prescription != null) {
            System.out.println("Prescription: " + prescription);
        } else {
            System.out.println("No prescription provided.");
        }
    }
}