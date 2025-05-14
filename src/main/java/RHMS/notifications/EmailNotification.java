package RHMS.notifications;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailNotification {
    public static void sendEmail(String recipientEmail, String message) {
        final String senderEmail = "rayyanaiza74@gmail.com"; // Replace with your email
        final String senderPassword = "pbveuwxbnheuzmqj"; // Replace with your app password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            msg.setSubject("Emergency Alert from RPMS");
            msg.setText(message);
            Transport.send(msg);
            System.out.println("Email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
}