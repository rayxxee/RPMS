package RHMS.notifications;

import RHMS.communication.ChatClient;
import RHMS.usermanagement.User;
import RHMS.usermanagement.UserManager;

import java.io.IOException;

public class SMSNotification {
    public static void sendSMS(String message, String targetUserId) throws IOException {
        User targetUser = UserManager.getUser(targetUserId);
        if (targetUser == null) {
            System.out.println("Cannot send SMS: Target user ID " + targetUserId + " not found.");
            return;
        }
        ChatClient client = new ChatClient("localhost", 1234, "system", "System", targetUserId);
        client.sendMessage("SMS Notification: " + message);
        // Note: The client is not closed immediately to allow message delivery
    }
}