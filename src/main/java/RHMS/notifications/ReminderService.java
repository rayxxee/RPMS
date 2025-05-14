package RHMS.notifications;

import RHMS.appointments.Appointment;
import RHMS.appointments.AppointmentManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReminderService {
    public static void sendAppointmentReminders() {
        List<Appointment> appointments = AppointmentManager.getDoctorAppointments("all");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Appointment appt : appointments) {
            String dateTimeStr = appt.getDateTime();
            if (dateTimeStr != null) {
                try {
                    LocalDateTime appointmentTime = LocalDateTime.parse(dateTimeStr, formatter);
                    if (appointmentTime.isAfter(now) && appointmentTime.isBefore(now.plusHours(24))) {
                        System.out.println("REMINDER: Appointment scheduled for " + 
                            appointmentTime.format(formatter) + " with " + appt.getPatientName());
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing appointment time: " + e.getMessage());
                }
            }
        }
    }

    public static void sendMedicationReminder(String patientId, String medication, String schedule) {
        System.out.println("REMINDER: Time to take " + medication + " as per schedule: " + schedule);
    }
}