package RHMS.reporting;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import RHMS.appointments.Appointment;
import RHMS.appointments.AppointmentManager;
import RHMS.healthdata.Vitals;
import RHMS.healthdata.VitalsDatabase;
import RHMS.interaction.Feedback;
import RHMS.interaction.FeedbackManager;
import RHMS.interaction.Prescription;
import RHMS.usermanagement.User;
import RHMS.usermanagement.UserManager;

public class ReportGenerator {
    public static void generatePatientReport(String patientId) {
        System.out.println("\n=== Patient Report ===");
        System.out.println("Patient ID: " + patientId);
        System.out.println("Name: " + UserManager.getUser(patientId).getName());

        // Appointment History
        System.out.println("\nAppointment History:");
        List<Appointment> appointments = AppointmentManager.getPatientAppointments(patientId);
        for (Appointment appt : appointments) {
            System.out.println("Date: " + appt.getDateTime() + " | Status: " + appt.getStatus());
        }

        // Vital Signs History
        System.out.println("\nVital Signs History:");
        List<Vitals> vitals = VitalsDatabase.getPatientVitals(patientId);
        for (Vitals vital : vitals) {
            System.out.println("Date: " + vital.getDate());
            System.out.println("Heart Rate: " + vital.getHeartRate() + " bpm");
            System.out.println("Blood Pressure: " + vital.getBloodPressure() + " mmHg");
            System.out.println("Temperature: " + vital.getTemperature() + " °C");
            System.out.println("---");
        }
    }

    public static void generateDoctorReport(String doctorId) {
        System.out.println("\n=== Doctor Report ===");
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Name: " + UserManager.getUser(doctorId).getName());

        // Patient List
        System.out.println("\nPatient List:");
        List<Appointment> appointments = AppointmentManager.getDoctorAppointments(doctorId);
        for (Appointment appt : appointments) {
            System.out.println("Patient: " + appt.getPatientName());
            System.out.println("Date: " + appt.getDateTime() + " | Status: " + appt.getStatus());
            System.out.println("---");
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