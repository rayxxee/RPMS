package RHMS.notifications;

import java.io.IOException;

public interface Notifiable {
    void sendAlert(String patientId, String message) throws IOException;
}