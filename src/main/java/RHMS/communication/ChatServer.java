package RHMS.communication;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private int port;
    private ServerSocket serverSocket;
    private Map<String, ClientHandler> clients;
    private ExecutorService threadPool;
    private volatile boolean running;

    public ChatServer(int port) {
        this.port = port;
        this.clients = new ConcurrentHashMap<>();
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Chat server started on port " + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            threadPool.shutdown();
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String userId;
        private String userName;
        private String targetUserId;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("CONNECT:")) {
                        handleConnection(message);
                    } else if (message.startsWith("MESSAGE:")) {
                        handleMessage(message);
                    } else if (message.startsWith("DISCONNECT:")) {
                        handleDisconnect(message);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                cleanup();
            }
        }

        private void handleConnection(String message) {
            String[] parts = message.split(":", 4);
            if (parts.length == 4) {
                userId = parts[1];
                userName = parts[2];
                targetUserId = parts[3];
                clients.put(userId, this);
                System.out.println(userName + " (ID: " + userId + ") connected");
            }
        }

        private void handleMessage(String message) {
            String[] parts = message.split(":", 4);
            if (parts.length == 4) {
                String senderId = parts[1];
                String senderName = parts[2];
                String content = parts[3];

                // Forward message to target user
                ClientHandler targetClient = clients.get(targetUserId);
                if (targetClient != null) {
                    targetClient.out.println(message);
                }
            }
        }

        private void handleDisconnect(String message) {
            String[] parts = message.split(":");
            if (parts.length == 2) {
                String disconnectedUserId = parts[1];
                clients.remove(disconnectedUserId);
                System.out.println("User " + disconnectedUserId + " disconnected");
            }
        }

        private void cleanup() {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
                if (userId != null) {
                    clients.remove(userId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer(1234);
        server.start();
    }
}