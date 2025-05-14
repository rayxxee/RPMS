package RHMS.communication;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private String host;
    private int port;
    private String userId;
    private String userName;
    private String targetUserId;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread receiveThread;
    private Consumer<String> onMessageReceived;
    private volatile boolean running;

    public ChatClient(String host, int port, String userId, String userName, String targetUserId) {
        this.host = host;
        this.port = port;
        this.userId = userId;
        this.userName = userName;
        this.targetUserId = targetUserId;
    }

    public void setOnMessageReceived(Consumer<String> callback) {
        this.onMessageReceived = callback;
    }

    public void start() {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Send initial connection message
            sendConnectionMessage();
            
            // Start receiving messages
            running = true;
            receiveThread = new Thread(this::receiveMessages);
            receiveThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendConnectionMessage() {
        String connectionMessage = String.format("CONNECT:%s:%s:%s", userId, userName, targetUserId);
        out.println(connectionMessage);
    }

    public void sendMessage(String message) {
        if (out != null) {
            String formattedMessage = String.format("MESSAGE:%s:%s:%s", userId, userName, message);
            out.println(formattedMessage);
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while (running && (message = in.readLine()) != null) {
                if (message.startsWith("MESSAGE:")) {
                    String[] parts = message.split(":", 4);
                    if (parts.length == 4) {
                        String senderId = parts[1];
                        String senderName = parts[2];
                        String content = parts[3];
                        
                        // Only process messages from the target user
                        if (senderId.equals(targetUserId)) {
                            String formattedMessage = String.format("%s: %s", senderName, content);
                            if (onMessageReceived != null) {
                                onMessageReceived.accept(formattedMessage);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        running = false;
        try {
            if (out != null) {
                out.println("DISCONNECT:" + userId);
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}