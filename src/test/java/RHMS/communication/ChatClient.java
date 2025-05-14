package RHMS.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader userInput;
    private String userId;
    private String targetId;

    public ChatClient(String host, int port, String userId, String targetId) throws IOException {
        this.socket = new Socket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.userInput = new BufferedReader(new InputStreamReader(System.in));
        this.userId = userId;
        this.targetId = targetId;
        out.println(userId);
        out.println(targetId);
        System.out.println("Connected to chat server as " + userId);
        new Thread(new ReceiveMessages(socket)).start();
    }

    public void sendMessage(String message) {
        if (out != null && message != null) {
            out.println(message);
        }
    }

    public void start() throws IOException {
        System.out.println("Type your messages (type 'exit' to quit):");
        String message;
        while ((message = userInput.readLine()) != null) {
            if (message.equalsIgnoreCase("exit")) break;
            sendMessage(message);
        }
        socket.close();
    }

    static class ReceiveMessages implements Runnable {
        private Socket socket;

        public ReceiveMessages(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server.");
            }
        }
    }
}