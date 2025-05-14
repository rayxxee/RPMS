package RHMS.appointments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import RHMS.usermanagement.UserManager;

public class AppointmentManager {
    private static List<Appointment> appointments = new ArrayList<>();
    private static final String APPOINTMENTS_FILE = System.getProperty("user.home") + File.separator + "rpm_appointments.ser";

    static {
        try {
            loadAppointments();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    public static void scheduleAppointment(Appointment appointment) {
        appointments.add(appointment);
        try {
            saveAppointments();
        } catch (IOException e) {
            System.out.println("Error saving appointment: " + e.getMessage());
        }
    }

    public static void requestAppointment(String patientId, String doctorId, String doctorName, String dateTime, String reason) {
        String patientName = UserManager.getUser(patientId) != null ? 
            UserManager.getUser(patientId).getName() : "Unknown Patient";
            
        Appointment appointment = new Appointment(
            patientId,
            patientName,
            doctorId,
            doctorName,
            dateTime,
            "Pending",
            reason
        );
        appointments.add(appointment);
        try {
            saveAppointments();
        } catch (IOException e) {
            System.out.println("Error saving appointment request: " + e.getMessage());
        }
    }

    public static List<Appointment> getDoctorAppointments(String doctorId) {
        List<Appointment> doctorAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equals(doctorId)) {
                doctorAppointments.add(appointment);
            }
        }
        return doctorAppointments;
    }

    public static List<Appointment> getPatientAppointments(String patientId) {
        List<Appointment> patientAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId().equals(patientId)) {
                patientAppointments.add(appointment);
            }
        }
        return patientAppointments;
    }

    public static List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    public static Appointment getAppointment(Integer appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId.toString())) {
                return appointment;
            }
        }
        return null;
    }

    public static void approveAppointment(String appointmentId) throws IOException {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                appointment.setStatus("Approved");
                saveAppointments();
                return;
            }
        }
    }

    public static void cancelAppointment(String appointmentId) throws IOException {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                appointment.setStatus("Canceled");
                saveAppointments();
                return;
            }
        }
    }

    public static void completeAppointment(String appointmentId) throws IOException {
        List<Appointment> appointments = getAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                appointment.setStatus("Completed");
                break;
            }
        }
        saveAppointments();
    }

    public static void saveAppointments() throws IOException {
        File file = new File(APPOINTMENTS_FILE);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(appointments);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadAppointments() throws IOException, ClassNotFoundException {
        File file = new File(APPOINTMENTS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                appointments = (List<Appointment>) ois.readObject();
            }
        }
    }

    public static void refreshData() throws IOException, ClassNotFoundException {
        loadAppointments();
    }
}