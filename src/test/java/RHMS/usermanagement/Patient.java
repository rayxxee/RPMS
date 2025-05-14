package RHMS.usermanagement;

import RHMS.appointments.AppointmentManager;
import RHMS.communication.ChatClient;
import RHMS.emergency.PanicButton;
import RHMS.healthdata.HealthDataHandling;
import RHMS.healthdata.VitalSign;
import RHMS.healthdata.VitalsDatabase;
import RHMS.interaction.Feedback;
import RHMS.interaction.MedicalHistory;
import RHMS.reporting.ReportGenerator;

import java.io.IOException;
import java.util.Scanner;

public class Patient extends User {
    private static final long serialVersionUID = 1L;
    private Feedback lastFeedback;
    private MedicalHistory medicalHistory;

    public Patient(String id, String name, String email, String password, String role, String phone) {
        super(id, name, email, password, role, phone);
        this.medicalHistory = new MedicalHistory(id);
    }

    public void patientMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== Patient Menu =====");
            System.out.println("1. Upload Vitals");
            System.out.println("2. View Doctor Feedback");
            System.out.println("3. Schedule Appointment");
            System.out.println("4. View Health Trends");
            System.out.println("5. Generate Report");
            System.out.println("6. Start Chat with Doctor");
            System.out.println("7. Trigger Panic Button");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1: handleVitalUploads(); break;
                    case 2: viewDoctorFeedback(); break;
                    case 3: scheduleAppointment(); break;
                    case 4: viewHealthTrends(); break;
                    case 5: generateReport(); break;
                    case 6: startChat(); break;
                    case 7: PanicButton.triggerAlert(getId()); break;
                    case 0: logout(); return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private void handleVitalUploads() throws IOException {
        System.out.println("\n--- Vital Data Management ---");
        System.out.println("1. Add Vitals");
        System.out.println("2. Update Vitals");
        System.out.println("0. Back");
        Scanner scanner = new Scanner(System.in);
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: HealthDataHandling.inputVitals(getId()); break;
                case 2: updateVitals(); break;
                case 0: return;
                default: System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private void updateVitals() throws IOException {
        VitalSign existingVital = VitalsDatabase.getVital(getId());
        if (existingVital == null) {
            System.out.println("No vitals found for Patient ID: " + getId());
            return;
        }
        HealthDataHandling.inputVitals(getId());
    }

    private void viewDoctorFeedback() {
        if (lastFeedback != null) {
            lastFeedback.displayFeedback();
        } else {
            System.out.println("No feedback available.");
        }
    }

    private void scheduleAppointment() {
        AppointmentManager.requestAppointment();
    }

    private void viewHealthTrends() {
        VitalSign vital = VitalsDatabase.getVital(getId());
        if (vital == null) {
            System.out.println("No vitals data available.");
            return;
        }
        System.out.println("\n=== Health Trends (Simulated Line Graph) ===");
        System.out.println("Heart Rate: " + vital.getHeartRate() + " bpm");
        System.out.println("Oxygen Level: " + vital.getOxygenLevel() + "%");
        System.out.println("Body Temperature: " + vital.getBodyTemperature() + "°C");
    }

    private void generateReport() {
        ReportGenerator.generatePatientReport(getId());
    }

    private void startChat() throws IOException {
        System.out.print("Enter Doctor ID to chat with: ");
        Scanner scanner = new Scanner(System.in);
        String doctorId = scanner.nextLine();
        ChatClient client = new ChatClient("localhost", 1234, getId(), doctorId);
        client.start();
    }

    public void setLastFeedback(Feedback feedback) {
        this.lastFeedback = feedback;
        medicalHistory.addFeedback(feedback);
    }
}