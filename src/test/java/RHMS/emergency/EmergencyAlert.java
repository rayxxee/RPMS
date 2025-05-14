package RHMS.emergency;

import RHMS.healthdata.VitalSign;
import RHMS.notifications.NotificationService;

import java.io.IOException;

public class EmergencyAlert {
    public static void checkVitals(VitalSign vital) throws IOException {
        boolean emergency = false;
        StringBuilder alertMessage = new StringBuilder("Emergency Alert for Patient ID: " + vital.getPid() + "\n");
        if (vital.getHeartRate() < 60 || vital.getHeartRate() > 100) {
            alertMessage.append("Abnormal Heart Rate: ").append(vital.getHeartRate()).append("\n");
            emergency = true;
        }
        if (vital.getOxygenLevel() < 90) {
            alertMessage.append("Low Oxygen Level: ").append(vital.getOxygenLevel()).append("\n");
            emergency = true;
        }
        if (vital.getBodyTemperature() < 36.1 || vital.getBodyTemperature() > 37.8) {
            alertMessage.append("Abnormal Body Temperature: ").append(vital.getBodyTemperature()).append("\n");
            emergency = true;
        }
        if (vital.getRespiratoryRate() < 12 || vital.getRespiratoryRate() > 20) {
            alertMessage.append("Abnormal Respiratory Rate: ").append(vital.getRespiratoryRate()).append("\n");
            emergency = true;
        }
        if (vital.getPulseRate() < 60 || vital.getPulseRate() > 100) {
            alertMessage.append("Abnormal Pulse Rate: ").append(vital.getPulseRate()).append("\n");
            emergency = true;
        }
        if (vital.getBloodGlucoseLevel() < 70 || vital.getBloodGlucoseLevel() > 140) {
            alertMessage.append("Abnormal Blood Glucose Level: ").append(vital.getBloodGlucoseLevel()).append("\n");
            emergency = true;
        }
        if (emergency) {
            NotificationService.sendAlert(vital.getPid(), alertMessage.toString());
        } else {
            System.out.println("Vitals are within safe range.");
        }
    }
}