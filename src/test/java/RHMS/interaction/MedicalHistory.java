package RHMS.interaction;

import java.io.Serializable;
import java.util.ArrayList;

public class MedicalHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private String patientId;
    private ArrayList<Feedback> feedbackHistory = new ArrayList<>();

    public MedicalHistory(String patientId) {
        this.patientId = patientId;
    }

    public void addFeedback(Feedback feedback) {
        feedbackHistory.add(feedback);
    }

    public ArrayList<Feedback> getFeedbackHistory() { return feedbackHistory; }

    public void displayMedicalHistory() {
        System.out.println("\n--- Medical History for Patient ID: " + patientId + " ---");
        if (feedbackHistory.isEmpty()) {
            System.out.println("No medical history found.");
        } else {
            for (Feedback feedback : feedbackHistory) {
                feedback.displayFeedback();
            }
        }
    }
}