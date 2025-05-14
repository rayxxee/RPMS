package RHMS.interaction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackManager {
    private static final String FEEDBACK_FILE = "rpm_feedbacks.ser";
    private static List<Feedback> feedbacks = new ArrayList<>();

    public static void addFeedback(Feedback feedback) throws IOException {
        feedbacks.add(feedback);
        saveFeedbacks();
    }

    public static List<Feedback> getPatientFeedbacks(String patientId) {
        return feedbacks.stream()
            .filter(f -> f.getPatientId().equals(patientId))
            .collect(Collectors.toList());
    }

    public static List<Feedback> getDoctorFeedbacks(String doctorId) {
        return feedbacks.stream()
            .filter(f -> f.getDoctorId().equals(doctorId))
            .collect(Collectors.toList());
    }

    public static void saveFeedbacks() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FEEDBACK_FILE))) {
            oos.writeObject(feedbacks);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadFeedbacks() throws IOException, ClassNotFoundException {
        File file = new File(FEEDBACK_FILE);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                feedbacks = (List<Feedback>) ois.readObject();
            }
        }
    }

    public static void refreshData() throws IOException, ClassNotFoundException {
        loadFeedbacks();
    }
}