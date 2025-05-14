package RHMS.appointments;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AppointmentManager {
    private static ArrayList<Appointment> appointments = new ArrayList<>();

    public static Appointment getAppointment(int index) {
        if (index >= 0 && index < appointments.size()) {
            return appointments.get(index);
        }
        System.out.println("Invalid appointment index.");
        return null;
    }

    public static void requestAppointment() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter Appointment ID: ");
            String appointmentId = scanner.nextLine();
            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            System.out.print("Enter Time (HH:MM AM/PM): ");
            String time = scanner.nextLine();
            System.out.print("Enter Doctor ID: ");
            String doctorId = scanner.nextLine();
            System.out.print("Enter Patient ID: ");
            String patientId = scanner.nextLine();
            Appointment appt = new Appointment(appointmentId, date, time, doctorId, patientId, "Requested");
            appointments.add(appt);
            saveAppointments();
            System.out.println("Appointment requested successfully!");
        } catch (Exception e) {
            System.out.println("Error saving appointment: " + e.getMessage());
        }
    }

    public static boolean approveAppointment(String appointmentId) throws IOException {
        for (Appointment appt : appointments) {
            if (appt.getAppointmentId().equals(appointmentId) && appt.getStatus().equals("Requested")) {
                appt.setStatus("Approved");
                saveAppointments();
                System.out.println("Appointment approved: " + appointmentId);
                return true;
            }
        }
        System.out.println("Appointment not found or not requestable.");
        return false;
    }

    public static boolean cancelAppointment(String appointmentId) throws IOException {
        for (Appointment appt : appointments) {
            if (appt.getAppointmentId().equals(appointmentId) && !appt.getStatus().equals("Canceled")) {
                appt.setStatus("Canceled");
                saveAppointments();
                System.out.println("Appointment canceled: " + appointmentId);
                return true;
            }
        }
        System.out.println("Appointment not found or already canceled.");
        return false;
    }

    public static ArrayList<Integer> getAppointmentsByDoctorId(String doctorId) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getDoctorId().equals(doctorId)) {
                indices.add(i);
            }
        }
        return indices;
    }

    public static ArrayList<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    public static void saveAppointments() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("appointments.ser"))) {
            oos.writeObject(appointments);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadAppointments() throws IOException, ClassNotFoundException {
        File file = new File("appointments.ser");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                appointments = (ArrayList<Appointment>) ois.readObject();
            }
        }
    }
}