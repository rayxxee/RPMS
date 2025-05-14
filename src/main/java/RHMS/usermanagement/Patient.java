package RHMS.usermanagement;

import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import RHMS.appointments.AppointmentManager;
import RHMS.communication.ChatClient;
import RHMS.communication.VideoCall;
import RHMS.emergency.PanicButton;
import RHMS.healthdata.HealthDataHandling;
import RHMS.healthdata.VitalsDatabase;
import RHMS.interaction.Feedback;
import RHMS.interaction.FeedbackManager;
import RHMS.interaction.MedicalHistory;
import RHMS.notifications.ReminderService;
import RHMS.reporting.ReportGenerator;

public class Patient extends User {
    private static final long serialVersionUID = 1L;
    private Feedback lastFeedback;
    private MedicalHistory medicalHistory;
    private String doctorId;
    private String allergies;
    private String currentMedications;
    private String medications;
    private String schedules;

    public Patient(String id, String name, String email, String password, String role, String phone) {
        super(id, name, email, password, role, phone);
        this.medicalHistory = new MedicalHistory(id);
        this.doctorId = null;
        this.allergies = "";
        this.currentMedications = "";
        this.medications = "";
        this.schedules = "";
    }

    public void setLastFeedback(Feedback feedback) {
        this.lastFeedback = feedback;
        medicalHistory.addFeedback(feedback);
    }

    public void patientMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== Patient Menu =====");
            System.out.println("1. Input Vital Signs");
            System.out.println("2. Request Appointment");
            System.out.println("3. View Medical History");
            System.out.println("4. View Last Feedback");
            System.out.println("5. Trigger Panic Button");
            System.out.println("6. Start Video Call");
            System.out.println("7. Generate Report");
            System.out.println("8. Start Chat with Doctor");
            System.out.println("9. Refresh Data");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1: HealthDataHandling.inputVitals(getId()); break;
                    case 2: requestAppointment(); break;
                    case 3: medicalHistory.displayMedicalHistory(); break;
                    case 4: viewLastFeedback(); break;
                    case 5: PanicButton.triggerAlert(getId()); break;
                    case 6: startVideoCall(); break;
                    case 7: ReportGenerator.generatePatientReport(getId()); break;
                    case 8: startChat(); break;
                    case 9: refreshData(); break;
                    case 0: logout(); return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private void requestAppointment() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter Doctor ID: ");
            String doctorId = scanner.nextLine();
            System.out.print("Enter Doctor Name: ");
            String doctorName = scanner.nextLine();
            System.out.print("Enter Date and Time (YYYY-MM-DD HH:mm): ");
            String dateTime = scanner.nextLine();
            System.out.print("Enter Reason for Appointment: ");
            String reason = scanner.nextLine();

            AppointmentManager.requestAppointment(getId(), doctorId, doctorName, dateTime, reason);
            System.out.println("Appointment request submitted successfully!");
        } catch (Exception e) {
            System.out.println("Error requesting appointment: " + e.getMessage());
        }
    }

    private void viewLastFeedback() {
        if (lastFeedback == null) {
            System.out.println("No feedback available.");
        } else {
            lastFeedback.displayFeedback();
            if (lastFeedback.getPrescription() != null) {
                String prescription = lastFeedback.getPrescription();
                String[] meds = prescription.split(";");
                for (String med : meds) {
                    String[] parts = med.split(",");
                    if (parts.length >= 2) {
                        ReminderService.sendMedicationReminder(
                            getId(),
                            parts[0].trim(),
                            parts[1].trim()
                        );
                    }
                }
            }
        }
    }

    private void startVideoCall() {
        String meetingLink = VideoCall.generateMeetingLink();
        VideoCall.startVideoCall(meetingLink);
    }

    private void startChat() {
        try {
            System.out.print("Enter Doctor ID to chat with: ");
            Scanner scanner = new Scanner(System.in);
            String doctorId = scanner.nextLine();
            ChatClient client = new ChatClient("localhost", 1234, getId(), getName(), doctorId);
            client.start();
        } catch (Exception e) {
            System.out.println("Error starting chat: " + e.getMessage());
        }
    }

    private void refreshData() {
        try {
            AppointmentManager.refreshData();
            VitalsDatabase.refreshData();
            FeedbackManager.refreshData();
            System.out.println("Data refreshed.");
        } catch (Exception e) {
            System.out.println("Error refreshing data: " + e.getMessage());
        }
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getMedicalHistoryText() {
        return medicalHistory.getMedicalHistory();
    }

    public void setMedicalHistoryText(String medicalHistory) {
        this.medicalHistory.setMedicalHistory(medicalHistory);
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

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getSchedules() {
        return schedules;
    }

    public void setSchedules(String schedules) {
        this.schedules = schedules;
    }

    @Override
    public void displayUserInfo() {
        super.displayUserInfo();
        System.out.println("Doctor ID: " + (doctorId != null ? doctorId : "Not assigned"));
        System.out.println("Medical History: " + getMedicalHistoryText());
        System.out.println("Allergies: " + getAllergies());
        System.out.println("Current Medications: " + getCurrentMedications());
    }
}