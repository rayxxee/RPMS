package RHMS.notifications;

import RHMS.communication.ChatClient;

import java.io.IOException;

public class SMSNotification {
    public static void sendSMS(String message) throws IOException {
        ChatClient client = new ChatClient("localhost", 1234, "system", "all");
        client.sendMessage(message);
    }
}