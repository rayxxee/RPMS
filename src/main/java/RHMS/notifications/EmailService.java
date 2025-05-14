package RHMS.notifications;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailService {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "your.email@gmail.com"; // Replace with your email
    private static final String EMAIL_PASSWORD = "your_app_password"; // Replace with your app password

    public static void sendEmergencyEmail(String toEmail, String patientId, String patientName, String reason) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("EMERGENCY ALERT: Patient " + patientId + " Requires Immediate Attention");

            String emailContent = String.format(
                "EMERGENCY ALERT\n\n" +
                "Patient ID: %s\n" +
                "Patient Name: %s\n" +
                "Reason: %s\n\n" +
                "This patient requires immediate medical attention. Please respond as soon as possible.",
                patientId, patientName, reason
            );

            message.setText(emailContent);

            Transport.send(message);
            System.out.println("Emergency email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send emergency email: " + e.getMessage());
        }
    }
} 