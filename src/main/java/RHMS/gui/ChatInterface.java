package RHMS.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import RHMS.usermanagement.User;
import RHMS.usermanagement.Doctor;
import RHMS.usermanagement.Patient;
import RHMS.usermanagement.UserManager;
import RHMS.appointments.Appointment;
import RHMS.appointments.AppointmentManager;
import RHMS.communication.ChatClient;

import java.util.List;
import java.util.stream.Collectors;

public class ChatInterface {
    private Stage stage;
    private User currentUser;
    private ComboBox<String> userSelector;
    private TextArea chatArea;
    private TextField messageField;
    private ChatClient chatClient;
    private String selectedUserId;
    private Button sendButton;

    public ChatInterface(User user) {
        this.currentUser = user;
        this.stage = new Stage();
        setupUI();
    }

    private void setupUI() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // User selector (Doctor/Patient dropdown)
        Label selectLabel = new Label("Select user to chat with:");
        userSelector = new ComboBox<>();
        
        // Chat area
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setPrefRowCount(20);
        chatArea.setWrapText(true);
        chatArea.setStyle("-fx-font-size: 14px;");

        // Message input area
        HBox inputArea = new HBox(10);
        messageField = new TextField();
        messageField.setPromptText("Type your message...");
        messageField.setPrefWidth(300);
        messageField.setOnAction(e -> sendMessage()); // Allow sending message with Enter key
        
        sendButton = new Button("Send");
        sendButton.setDisable(true); // Initially disabled until a user is selected
        sendButton.setOnAction(e -> sendMessage());
        
        inputArea.getChildren().addAll(messageField, sendButton);

        root.getChildren().addAll(selectLabel, userSelector, chatArea, inputArea);

        Scene scene = new Scene(root, 400, 500);
        stage.setTitle("Chat - " + currentUser.getName());
        stage.setScene(scene);
        
        // Handle window close
        stage.setOnCloseRequest(e -> {
            if (chatClient != null) {
                chatClient.disconnect();
            }
        });

        // Update user list after all UI components are initialized
        updateUserList();
        userSelector.setOnAction(e -> handleUserSelection());
    }

    private void updateUserList() {
        ObservableList<String> userList = FXCollections.observableArrayList();
        
        if (currentUser instanceof Doctor) {
            // For doctors, show patients with approved or completed appointments
            List<Appointment> appointments = AppointmentManager.getDoctorAppointments(currentUser.getId());
            System.out.println("Doctor " + currentUser.getId() + " has " + appointments.size() + " appointments");
            
            List<String> patientIds = appointments.stream()
                .filter(appt -> {
                    boolean isApproved = "Approved".equals(appt.getStatus()) || "Completed".equals(appt.getStatus());
                    System.out.println("Appointment with patient " + appt.getPatientId() + " has status: " + appt.getStatus() + ", included: " + isApproved);
                    return isApproved;
                })
                .map(Appointment::getPatientId)
                .distinct()
                .collect(Collectors.toList());
            
            System.out.println("Found " + patientIds.size() + " unique patients with approved/completed appointments");
            
            for (String patientId : patientIds) {
                User patient = UserManager.getUser(patientId);
                if (patient != null) {
                    String displayText = patient.getName() + " (" + patientId + ")";
                    System.out.println("Adding patient to list: " + displayText);
                    userList.add(displayText);
                }
            }
        } else if (currentUser instanceof Patient) {
            // For patients, show doctors with approved or completed appointments
            List<Appointment> appointments = AppointmentManager.getPatientAppointments(currentUser.getId());
            System.out.println("Patient " + currentUser.getId() + " has " + appointments.size() + " appointments");
            
            List<String> doctorIds = appointments.stream()
                .filter(appt -> {
                    boolean isApproved = "Approved".equals(appt.getStatus()) || "Completed".equals(appt.getStatus());
                    System.out.println("Appointment with doctor " + appt.getDoctorId() + " has status: " + appt.getStatus() + ", included: " + isApproved);
                    return isApproved;
                })
                .map(Appointment::getDoctorId)
                .distinct()
                .collect(Collectors.toList());
            
            System.out.println("Found " + doctorIds.size() + " unique doctors with approved/completed appointments");
            
            for (String doctorId : doctorIds) {
                User doctor = UserManager.getUser(doctorId);
                if (doctor != null) {
                    String displayText = doctor.getName() + " (" + doctorId + ")";
                    System.out.println("Adding doctor to list: " + displayText);
                    userList.add(displayText);
                }
            }
        }
        
        if (userList.isEmpty()) {
            System.out.println("No users found to chat with");
            userList.add("No available users to chat with");
            userSelector.setDisable(true);
            sendButton.setDisable(true);
        } else {
            System.out.println("Found " + userList.size() + " users to chat with");
            userSelector.setDisable(false);
        }
        
        userSelector.setItems(userList);
    }

    private void handleUserSelection() {
        String selected = userSelector.getValue();
        System.out.println("Selected user: " + selected);
        
        if (selected != null && !selected.equals("No available users to chat with")) {
            try {
                // Extract user ID from the selection (format: "Name (ID)")
                selectedUserId = selected.substring(selected.lastIndexOf("(") + 1, selected.lastIndexOf(")"));
                System.out.println("Extracted user ID: " + selectedUserId);
                
                // Disconnect existing chat client if any
                if (chatClient != null) {
                    chatClient.disconnect();
                }
                
                // Create new chat client
                chatClient = new ChatClient("localhost", 1234, currentUser.getId(), currentUser.getName(), selectedUserId);
                chatClient.setOnMessageReceived(this::displayMessage);
                chatClient.start();
                
                // Enable send button and clear chat area
                sendButton.setDisable(false);
                chatArea.clear();
                chatArea.appendText("Connected to chat with " + selected + "\n");
            } catch (Exception e) {
                System.err.println("Error in handleUserSelection: " + e.getMessage());
                e.printStackTrace();
                showError("Failed to connect to chat: " + e.getMessage());
                sendButton.setDisable(true);
            }
        }
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && chatClient != null) {
            try {
                chatClient.sendMessage(message);
                displayMessage("You: " + message);
                messageField.clear();
            } catch (Exception e) {
                showError("Failed to send message: " + e.getMessage());
            }
        }
    }

    private void displayMessage(String message) {
        Platform.runLater(() -> {
            chatArea.appendText(message + "\n");
            chatArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chat Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public void show() {
        stage.show();
    }

    public void close() {
        if (chatClient != null) {
            chatClient.disconnect();
        }
        stage.close();
    }
} 