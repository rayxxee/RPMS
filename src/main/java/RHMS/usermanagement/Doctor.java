package RHMS.usermanagement;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import RHMS.appointments.Appointment;
import RHMS.appointments.AppointmentManager;
import RHMS.communication.ChatClient;
import RHMS.healthdata.Vitals;
import RHMS.healthdata.VitalsDatabase;
import RHMS.interaction.Feedback;
import RHMS.interaction.FeedbackManager;
import RHMS.interaction.Prescription;
import RHMS.reporting.ReportGenerator;

public class Doctor extends User {
    private static final long serialVersionUID = 1L;
    private String specialty;

    public Doctor(String id, String name, String email, String password, String role, String phone, String specialty) {
        super(id, name, email, password, role, phone);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void doctorMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== Doctor Menu =====");
            System.out.println("1. View Patient Data");
            System.out.println("2. Provide Feedback");
            System.out.println("3. Manage Appointments");
            System.out.println("4. View Health Trends");
            System.out.println("5. Generate Report");
            System.out.println("6. Start Chat with Patient");
            System.out.println("7. Refresh Data");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1: viewPatientData(); break;
                    case 2: provideFeedback(); break;
                    case 3: manageAppointments(); break;
                    case 4: viewHealthTrends(); break;
                    case 5: generateReport(); break;
                    case 6: startChat(); break;
                    case 7: refreshData(); break;
                    case 0: logout(); return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private void viewPatientData() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        Vitals vital = VitalsDatabase.getVital(patientId);
        if (vital != null) {
            System.out.println("\n=== Patient Vitals ===");
            System.out.println("Heart Rate: " + vital.getHeartRate());
            System.out.println("Blood Pressure: " + vital.getBloodPressure());
            System.out.println("Body Temperature: " + vital.getTemperature());
        }
    }

    private Feedback provideFeedback() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        System.out.print("Enter Feedback Text: ");
        String feedbackText = scanner.nextLine();
        Prescription prescription = new Prescription();
        System.out.print("Add medications? (yes/no): ");
        String addMeds = scanner.nextLine();
        while (addMeds.equalsIgnoreCase("yes")) {
            System.out.print("Medication Name: ");
            String med = scanner.nextLine();
            System.out.print("Dosage: ");
            String dosage = scanner.nextLine();
            System.out.print("Schedule: ");
            String schedule = scanner.nextLine();
            prescription.addMedication(med, dosage, schedule);
            System.out.print("Add another? (yes/no): ");
            addMeds = scanner.nextLine();
        }
        Feedback feedback = new Feedback(patientId, getId(), feedbackText, prescription.toString(), null);
        User patient = UserManager.getUser(patientId);
        if (patient instanceof Patient) {
            ((Patient) patient).setLastFeedback(feedback);
            FeedbackManager.addFeedback(feedback);
        }
        System.out.println("Feedback saved!");
        return feedback;
    }

    private void manageAppointments() {
        List<Appointment> appointments = AppointmentManager.getDoctorAppointments(getId());
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        System.out.println("\nAppointments:");
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appt = appointments.get(i);
            System.out.printf("%d. Patient: %s (ID: %s), Status: %s\n",
                    i + 1, appt.getPatientName(), appt.getPatientId(), appt.getStatus());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter appointment number: ");
        try {
            int apptNo = scanner.nextInt();
            scanner.nextLine();
            if (apptNo < 1 || apptNo > appointments.size()) {
                System.out.println("Invalid appointment number.");
                return;
            }
            Appointment appt = appointments.get(apptNo - 1);
            System.out.println("1. Approve");
            System.out.println("2. Cancel");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                AppointmentManager.approveAppointment(appt.getAppointmentId());
            } else if (choice == 2) {
                AppointmentManager.cancelAppointment(appt.getAppointmentId());
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
            scanner.nextLine();
        }
    }

    private void viewHealthTrends() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        Vitals vital = VitalsDatabase.getVital(patientId);
        if (vital == null) {
            System.out.println("No vitals data available.");
            return;
        }
        System.out.println("\n=== Health Trends (Simulated Line Graph) ===");
        System.out.println("Heart Rate: " + vital.getHeartRate() + " bpm");
        System.out.println("Blood Pressure: " + vital.getBloodPressure());
        System.out.println("Body Temperature: " + vital.getTemperature() + "°C");
    }

    private void generateReport() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        ReportGenerator.generatePatientReport(patientId);
    }

    private void startChat() throws IOException {
        System.out.print("Enter Patient ID to chat with: ");
        Scanner scanner = new Scanner(System.in);
        String patientId = scanner.nextLine();
        ChatClient client = new ChatClient("localhost", 1234, getId(), getName(), patientId);
        client.start();
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
}