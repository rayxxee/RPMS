package RHMS.interaction;

import java.io.*;
import java.util.ArrayList;

public class FeedbackManager {
    private static ArrayList<Feedback> feedbacks = new ArrayList<>();

    public static void addFeedback(Feedback feedback) throws IOException {
        feedbacks.add(feedback);
        saveFeedbacks();
    }

    public static ArrayList<Feedback> getFeedbacksByPatientId(String patientId) {
        ArrayList<Feedback> patientFeedbacks = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            if (feedback.getPatientId().equals(patientId)) {
                patientFeedbacks.add(feedback);
            }
        }
        return patientFeedbacks;
    }

    public static void saveFeedbacks() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("feedback.ser"))) {
            oos.writeObject(feedbacks);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadFeedbacks() throws IOException, ClassNotFoundException {
        File file = new File("feedback.ser");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                feedbacks = (ArrayList<Feedback>) ois.readObject();
            }
        }
    }
}