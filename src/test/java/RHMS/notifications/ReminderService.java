package RHMS.notifications;

import RHMS.appointments.Appointment;
import RHMS.appointments.AppointmentManager;
import RHMS.usermanagement.User;
import RHMS.usermanagement.UserManager;

import java.io.IOException;
import java.util.ArrayList;

public class ReminderService {
    public static void sendAppointmentReminders() throws IOException {
        ArrayList<Appointment> appointments = AppointmentManager.getAppointments();
        for (Appointment appt : appointments) {
            if (appt.getStatus().equals("Approved")) {
                User patient = UserManager.getUser(appt.getPatientId());
                if (patient != null) {
                    String message = "Reminder: Your appointment on " + appt.getDate() + " at " + appt.getTime() + " is confirmed.";
                    EmailNotification.sendEmail(patient.getEmail(), message);
                    SMSNotification.sendSMS(message);
                }
            }
        }
    }

    public static void sendMedicationReminder(String patientId, String medication, String schedule) throws IOException {
        User patient = UserManager.getUser(patientId);
        if (patient != null) {
            String message = "Reminder: Take your medication '" + medication + "' as per schedule: " + schedule;
            EmailNotification.sendEmail(patient.getEmail(), message);
            SMSNotification.sendSMS(message);
        }
    }
}