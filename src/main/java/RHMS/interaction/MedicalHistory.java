package RHMS.interaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicalHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private String patientId;
    private String history;
    private List<Feedback> feedbacks;

    public MedicalHistory(String patientId) {
        this.patientId = patientId;
        this.history = "";
        this.feedbacks = new ArrayList<>();
    }

    public String getMedicalHistory() {
        return history;
    }

    public void setMedicalHistory(String history) {
        this.history = history;
    }

    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
    }

    public void displayMedicalHistory() {
        System.out.println("\n=== Medical History for Patient " + patientId + " ===");
        System.out.println("History: " + history);
        System.out.println("\nFeedback History:");
        for (Feedback feedback : feedbacks) {
            feedback.displayFeedback();
            System.out.println("---");
        }
    }
}