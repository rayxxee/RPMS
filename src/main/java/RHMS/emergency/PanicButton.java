package RHMS.emergency;

import java.io.IOException;

import RHMS.healthdata.Vitals;
import RHMS.healthdata.VitalsDatabase;
import RHMS.notifications.NotificationService;

public class PanicButton {
    public static void triggerAlert(String patientId) throws IOException {
        Vitals vital = VitalsDatabase.getVital(patientId);
        if (vital != null) {
            System.out.println("🔴 Panic Button activated by Patient ID: " + patientId);
            NotificationService.sendAlert(patientId, "Panic button triggered by Patient ID: " + patientId);
        } else {
            System.out.println("No vitals found for Patient ID: " + patientId);
        }
    }
}