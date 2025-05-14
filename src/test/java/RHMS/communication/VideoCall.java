package RHMS.communication;

import java.awt.*;
import java.net.URI;

public class VideoCall {
    public static void startVideoCall(String meetingLink) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(meetingLink));
                System.out.println("Video call started: " + meetingLink);
            } else {
                System.out.println("Desktop browsing not supported. Please open the link manually: " + meetingLink);
            }
        } catch (Exception e) {
            System.out.println("Error starting video call: " + e.getMessage());
        }
    }

    public static String generateMeetingLink() {
        // Simulated Google Meet link (replace with actual API integration in production)
        return "https://meet.google.com/abc-defg-hij";
    }
}