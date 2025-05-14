package RHMS.notifications;

import RHMS.usermanagement.Doctor;
import RHMS.usermanagement.User;
import RHMS.usermanagement.UserManager;

import java.io.IOException;

public class NotificationService{

    public static void sendAlert(String patientId, String message) throws IOException {
        User patient = UserManager.getUser(patientId);
        Doctor doctor = UserManager.getRandomDoctor();
        if (patient != null && doctor != null) {
            String emailMessage = patient.getName() + " (ID: " + patientId + ") has critical vitals:\n" + message;
            EmailNotification.sendEmail(doctor.getEmail(), emailMessage);
            SMSNotification.sendSMS(emailMessage);
        }
    }
}