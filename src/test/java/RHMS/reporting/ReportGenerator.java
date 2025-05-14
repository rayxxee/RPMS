package RHMS.reporting;

import RHMS.appointments.Appointment;
import RHMS.appointments.AppointmentManager;
import RHMS.healthdata.VitalSign;
import RHMS.healthdata.VitalsDatabase;
import RHMS.interaction.Feedback;
import RHMS.interaction.FeedbackManager;
import RHMS.interaction.Prescription;
import RHMS.usermanagement.User;
import RHMS.usermanagement.UserManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReportGenerator {
    public static void generatePatientReport(String patientId) {
        try {
            VitalSign vital = VitalsDatabase.getVital(patientId);
            ArrayList<Feedback> feedbacks = FeedbackManager.getFeedbacksByPatientId(patientId);
            StringBuilder report = new StringBuilder();
            report.append("=== Patient Report ===\n");
            report.append("Patient ID: ").append(patientId).append("\n");
            if (vital != null) {
                report.append("\nVitals:\n");
                report.append("Heart Rate: ").append(vital.getHeartRate()).append(" bpm\n");
                report.append("Oxygen Level: ").append(vital.getOxygenLevel()).append("%\n");
                report.append("Blood Pressure: ").append(vital.getBloodPressure()).append("\n");
                report.append("Body Temperature: ").append(vital.getBodyTemperature()).append("°C\n");
                report.append("Respiratory Rate: ").append(vital.getRespiratoryRate()).append("\n");
                report.append("Pulse Rate: ").append(vital.getPulseRate()).append("\n");
                report.append("Blood Glucose Level: ").append(vital.getBloodGlucoseLevel()).append("\n");
            } else {
                report.append("No vitals data available.\n");
            }
            report.append("\nFeedback History:\n");
            if (feedbacks.isEmpty()) {
                report.append("No feedback available.\n");
            } else {
                for (Feedback feedback : feedbacks) {
                    report.append("Feedback: ").append(feedback.getFeedbackText()).append("\n");
                    Prescription prescription = feedback.getPrescription();
                    if (prescription != null) {
                        for (int i = 0; i < prescription.getMedications().size(); i++) {
                            report.append("Medication: ").append(prescription.getMedications().get(i))
                                    .append(", Dosage: ").append(prescription.getDosages().get(i))
                                    .append(", Schedule: ").append(prescription.getSchedules().get(i)).append("\n");
                        }
                    }
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(patientId + "_report.txt"))) {
                writer.write(report.toString());
                System.out.println("Report generated: " + patientId + "_report.txt");
            }
        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    public static void generateSystemReport() {
        try {
            StringBuilder report = new StringBuilder();
            report.append("=== System Report ===\n");
            report.append("\nRegistered Users:\n");
            for (User user : UserManager.getUsers()) {
                report.append("ID: ").append(user.getId())
                        .append(" | Name: ").append(user.getName())
                        .append(" | Role: ").append(user.getRole()).append("\n");
            }
            report.append("\nAppointments:\n");
            for (Appointment appt : AppointmentManager.getAppointments()) {
                report.append("ID: ").append(appt.getAppointmentId())
                        .append(" | Patient ID: ").append(appt.getPatientId())
                        .append(" | Doctor ID: ").append(appt.getDoctorId())
                        .append(" | Status: ").append(appt.getStatus()).append("\n");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("system_report.txt"))) {
                writer.write(report.toString());
                System.out.println("System report generated: system_report.txt");
            }
        } catch (IOException e) {
            System.out.println("Error generating system report: " + e.getMessage());
        }
    }
}