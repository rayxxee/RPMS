package RHMS.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChatServer {
    private static Map<String, Set<PrintWriter>> chatRooms = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Chat Server started on port 1234...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String userId;
        private String targetId;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                userId = in.readLine();
                targetId = in.readLine();
                String roomId = getRoomId(userId, targetId);
                synchronized (chatRooms) {
                    chatRooms.computeIfAbsent(roomId, k -> new HashSet<>()).add(out);
                }
                System.out.println("User " + userId + " joined room with " + targetId);
                String message;
                while ((message = in.readLine()) != null) {
                    broadcast(roomId, userId + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + userId);
            } finally {
                try {
                    socket.close();
                    synchronized (chatRooms) {
                        String roomId = getRoomId(userId, targetId);
                        Set<PrintWriter> writers = chatRooms.get(roomId);
                        if (writers != null) {
                            writers.remove(out);
                            if (writers.isEmpty()) {
                                chatRooms.remove(roomId);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }

        private void broadcast(String roomId, String message) {
            synchronized (chatRooms) {
                Set<PrintWriter> writers = chatRooms.get(roomId);
                if (writers != null) {
                    for (PrintWriter writer : writers) {
                        writer.println(message);
                    }
                }
            }
        }

        private String getRoomId(String userId, String targetId) {
            return userId.compareTo(targetId) < 0 ? userId + "-" + targetId : targetId + "-" + userId;
        }
    }
}