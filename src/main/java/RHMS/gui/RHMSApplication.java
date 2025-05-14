package RHMS.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import RHMS.usermanagement.UserManager;
import RHMS.usermanagement.Patient;
import RHMS.usermanagement.Doctor;
import RHMS.usermanagement.User;
import RHMS.healthdata.Vitals;
import RHMS.healthdata.VitalsDatabase;
import RHMS.emergency.EmergencyAlert;
import RHMS.appointments.Appointment;
import RHMS.appointments.AppointmentManager;
import RHMS.communication.ChatClient;
import RHMS.communication.ChatServer;
import RHMS.communication.VideoCall;
import RHMS.interaction.Feedback;
import RHMS.interaction.FeedbackManager;
import RHMS.notifications.ReminderService;
import RHMS.reporting.ReportGenerator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.concurrent.Worker;
import javafx.scene.control.ProgressIndicator;
import java.io.File;

public class RHMSApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Remote Health Monitoring System");
        showLoginScreen(primaryStage);
    }

    private void showLoginScreen(Stage primaryStage) {
        VBox loginBox = new VBox(10);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20));
        loginBox.setStyle("-fx-background-color: #F6F6F2;");

        Label titleLabel = new Label("Remote Health Monitoring System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #6FB3B8;");

        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID");
        userIdField.setMaxWidth(300);
        userIdField.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        passwordField.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        loginButton.setMaxWidth(300);

        Button registerButton = new Button("Register New User");
        registerButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        registerButton.setMaxWidth(300);

        loginButton.setOnAction(e -> {
            String userId = userIdField.getText();
            String password = passwordField.getText();
            
            try {
                User user = UserManager.authenticateUser(userId, password);
                if (user != null) {
                    showMainWindow(primaryStage, user);
                } else {
                    showAlert("Login Failed", "Invalid credentials!", Alert.AlertType.ERROR);
                }
            } catch (Exception ex) {
                showAlert("Error", "Login failed: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        registerButton.setOnAction(e -> showRegistrationScreen(primaryStage));

        loginBox.getChildren().addAll(titleLabel, userIdField, passwordField, loginButton, registerButton);
        Scene scene = new Scene(loginBox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showRegistrationScreen(Stage primaryStage) {
        VBox registerBox = new VBox(10);
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPadding(new Insets(20));
        registerBox.setStyle("-fx-background-color: #F6F6F2;");

        Label titleLabel = new Label("Register New User");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #6FB3B8;");

        TextField userIdField = new TextField();
        userIdField.setPromptText("Choose User ID");
        userIdField.setMaxWidth(300);
        userIdField.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Patient", "Doctor");
        roleComboBox.setPromptText("Select Role");
        roleComboBox.setMaxWidth(300);
        roleComboBox.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        nameField.setMaxWidth(300);
        nameField.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);
        emailField.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        passwordField.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        phoneField.setMaxWidth(300);
        phoneField.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");

        // Add specialty field that only shows when Doctor is selected
        TextField specialtyField = new TextField();
        specialtyField.setPromptText("Specialty");
        specialtyField.setMaxWidth(300);
        specialtyField.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");
        specialtyField.setVisible(false);

        // Show/hide specialty field based on role selection
        roleComboBox.setOnAction(e -> {
            specialtyField.setVisible(roleComboBox.getValue() != null && roleComboBox.getValue().equals("Doctor"));
        });

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        registerButton.setMaxWidth(300);

        Button backButton = new Button("Back to Login");
        backButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        backButton.setMaxWidth(300);

        registerButton.setOnAction(e -> {
            String userId = userIdField.getText();
            String role = roleComboBox.getValue();
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String phone = phoneField.getText();
            String specialty = specialtyField.getText();

            if (userId.isEmpty() || role == null || name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                showAlert("Error", "Please fill all fields!", Alert.AlertType.ERROR);
                return;
            }

            if (role.equals("Doctor") && specialty.isEmpty()) {
                showAlert("Error", "Please enter doctor's specialty!", Alert.AlertType.ERROR);
                return;
            }

            try {
                if (UserManager.getUser(userId) != null) {
                    showAlert("Error", "User ID already exists! Please choose a different one.", Alert.AlertType.ERROR);
                    return;
                }

                User newUser;
                if (role.equals("Patient")) {
                    newUser = new Patient(userId, name, email, password, role, phone);
                } else {
                    newUser = new Doctor(userId, name, email, password, role, phone, specialty);
                }
                UserManager.addUser(newUser);
                showAlert("Success", "Registration successful!", Alert.AlertType.INFORMATION);
                showLoginScreen(primaryStage);
            } catch (Exception ex) {
                showAlert("Error", "Registration failed: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        backButton.setOnAction(e -> showLoginScreen(primaryStage));

        registerBox.getChildren().addAll(
            titleLabel, userIdField, roleComboBox, nameField, emailField,
            passwordField, phoneField, specialtyField, registerButton, backButton
        );

        Scene scene = new Scene(registerBox, 400, 550);
        primaryStage.setScene(scene);
    }

    private void showMainWindow(Stage stage, User user) {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #F6F6F2;");

        // Top section with welcome message and logout
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");

        Label welcomeLabel = new Label("Welcome, " + user.getName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #6FB3B8;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        logoutButton.setOnAction(e -> showLoginScreen(stage));

        Button globalRefreshButton = new Button("Refresh All Data");
        globalRefreshButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        globalRefreshButton.setOnAction(e -> {
            try {
                UserManager.refreshData();
                AppointmentManager.refreshData();
                VitalsDatabase.refreshData();
                FeedbackManager.refreshData();
                showAlert("Success", "All data refreshed successfully!", Alert.AlertType.INFORMATION);
                // Refresh the current view
                if (user instanceof Doctor) {
                    showDoctorDashboard(mainLayout, (Doctor) user);
                } else {
                    showPatientDashboard(mainLayout, (Patient) user);
                }
            } catch (Exception ex) {
                showAlert("Error", "Failed to refresh data: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        topBar.getChildren().addAll(welcomeLabel, logoutButton, globalRefreshButton);
        mainLayout.setTop(topBar);

        // Center content based on user type
        if (user instanceof Doctor) {
            showDoctorDashboard(mainLayout, (Doctor) user);
        } else {
            showPatientDashboard(mainLayout, (Patient) user);
        }

        Scene scene = new Scene(mainLayout, 1000, 800);
        stage.setScene(scene);
    }

    private void showDoctorDashboard(BorderPane mainLayout, Doctor doctor) {
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");

        // Patients Tab
        Tab patientsTab = new Tab("Patients");
        VBox patientsContent = new VBox(10);
        patientsContent.setPadding(new Insets(20));

        // Get patients who have requested appointments from this doctor
        List<Appointment> doctorAppointments = AppointmentManager.getDoctorAppointments(doctor.getId());
        List<String> patientIds = doctorAppointments.stream()
            .map(Appointment::getPatientId)
            .distinct()
            .collect(Collectors.toList());
        
        List<Patient> patients = UserManager.getPatients().stream()
            .filter(p -> patientIds.contains(p.getId()))
            .collect(Collectors.toList());

        TableView<Patient> patientTable = new TableView<>();
        TableColumn<Patient, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Patient, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Patient, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        patientTable.getColumns().addAll(idColumn, nameColumn, phoneColumn);
        patientTable.setItems(FXCollections.observableArrayList(patients));

        // Vitals display area
        VBox vitalsBox = new VBox(10);
        vitalsBox.setPadding(new Insets(20));
        vitalsBox.setStyle("-fx-background-color: #F6F6F2; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");
        
        Label vitalsTitle = new Label("Patient Vitals");
        vitalsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #6FB3B8;");
        
        GridPane vitalsGrid = new GridPane();
        vitalsGrid.setHgap(10);
        vitalsGrid.setVgap(10);
        vitalsGrid.setPadding(new Insets(10));
        
        Label heartRateLabel = new Label("Heart Rate:");
        Label heartRateValue = new Label();
        Label bloodPressureLabel = new Label("Blood Pressure:");
        Label bloodPressureValue = new Label();
        Label temperatureLabel = new Label("Temperature:");
        Label temperatureValue = new Label();
        Label dateLabel = new Label("Last Updated:");
        Label dateValue = new Label();
        
        vitalsGrid.add(heartRateLabel, 0, 0);
        vitalsGrid.add(heartRateValue, 1, 0);
        vitalsGrid.add(bloodPressureLabel, 0, 1);
        vitalsGrid.add(bloodPressureValue, 1, 1);
        vitalsGrid.add(temperatureLabel, 0, 2);
        vitalsGrid.add(temperatureValue, 1, 2);
        vitalsGrid.add(dateLabel, 0, 3);
        vitalsGrid.add(dateValue, 1, 3);
        
        vitalsBox.getChildren().addAll(vitalsTitle, vitalsGrid);
        
        // Update vitals when a patient is selected
        patientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Vitals vitals = VitalsDatabase.getVital(newSelection.getId());
                if (vitals != null) {
                    heartRateValue.setText(String.format("%.1f bpm", vitals.getHeartRate()));
                    bloodPressureValue.setText(String.format("%.1f mmHg", vitals.getBloodPressure()));
                    temperatureValue.setText(String.format("%.1f °C", vitals.getTemperature()));
                    dateValue.setText(vitals.getDate());
                } else {
                    heartRateValue.setText("No data");
                    bloodPressureValue.setText("No data");
                    temperatureValue.setText("No data");
                    dateValue.setText("No data");
                }
            }
        });

        Button refreshButton = new Button("Refresh Patients");
        refreshButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        refreshButton.setOnAction(e -> {
            try {
                UserManager.refreshData();
                VitalsDatabase.refreshData();
                List<Appointment> updatedAppointments = AppointmentManager.getDoctorAppointments(doctor.getId());
                List<String> updatedPatientIds = updatedAppointments.stream()
                    .map(Appointment::getPatientId)
                    .distinct()
                    .collect(Collectors.toList());
                
                List<Patient> updatedPatients = UserManager.getPatients().stream()
                    .filter(p -> updatedPatientIds.contains(p.getId()))
                    .collect(Collectors.toList());
                
                patientTable.setItems(FXCollections.observableArrayList(updatedPatients));
            } catch (Exception ex) {
                showAlert("Error", "Failed to refresh data: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        patientsContent.getChildren().addAll(patientTable, vitalsBox, refreshButton);
        patientsTab.setContent(patientsContent);

        // Appointments Tab
        Tab appointmentsTab = new Tab("Appointments");
        VBox appointmentsContent = new VBox(10);
        appointmentsContent.setPadding(new Insets(20));

        TableView<Appointment> appointmentTable = new TableView<>();
        TableColumn<Appointment, String> patientNameColumn = new TableColumn<>("Patient");
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        TableColumn<Appointment, String> dateColumn = new TableColumn<>("Date & Time");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        TableColumn<Appointment, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Appointment, String> reasonColumn = new TableColumn<>("Reason");
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

        appointmentTable.getColumns().addAll(patientNameColumn, dateColumn, statusColumn, reasonColumn);
        appointmentTable.setItems(FXCollections.observableArrayList(AppointmentManager.getDoctorAppointments(doctor.getId())));

        HBox appointmentButtons = new HBox(10);
        Button refreshAppointmentsButton = new Button("Refresh Appointments");
        refreshAppointmentsButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        refreshAppointmentsButton.setOnAction(e -> {
            try {
                AppointmentManager.refreshData();
                appointmentTable.setItems(FXCollections.observableArrayList(AppointmentManager.getDoctorAppointments(doctor.getId())));
            } catch (Exception ex) {
                showAlert("Error", "Failed to refresh appointments: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button approveButton = new Button("Approve Selected");
        approveButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        approveButton.setOnAction(e -> {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment != null && "Pending".equals(selectedAppointment.getStatus())) {
                try {
                    AppointmentManager.approveAppointment(selectedAppointment.getAppointmentId());
                    refreshAppointmentsButton.fire();
                    showAlert("Success", "Appointment approved successfully!", Alert.AlertType.INFORMATION);
                } catch (Exception ex) {
                    showAlert("Error", "Failed to approve appointment: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Please select a pending appointment to approve", Alert.AlertType.ERROR);
            }
        });

        Button cancelButton = new Button("Cancel Selected");
        cancelButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        cancelButton.setOnAction(e -> {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment != null && !"Canceled".equals(selectedAppointment.getStatus())) {
                try {
                    AppointmentManager.cancelAppointment(selectedAppointment.getAppointmentId());
                    refreshAppointmentsButton.fire();
                    showAlert("Success", "Appointment canceled successfully!", Alert.AlertType.INFORMATION);
                } catch (Exception ex) {
                    showAlert("Error", "Failed to cancel appointment: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Please select a valid appointment to cancel", Alert.AlertType.ERROR);
            }
        });

        Button completeButton = new Button("Mark as Complete");
        completeButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        completeButton.setOnAction(e -> {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment != null && "Approved".equals(selectedAppointment.getStatus())) {
                try {
                    AppointmentManager.completeAppointment(selectedAppointment.getAppointmentId());
                    refreshAppointmentsButton.fire();
                    showAlert("Success", "Appointment marked as complete!", Alert.AlertType.INFORMATION);
                } catch (Exception ex) {
                    showAlert("Error", "Failed to mark appointment as complete: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Please select an approved appointment to mark as complete", Alert.AlertType.ERROR);
            }
        });

        Button addFeedbackButton = new Button("Add Feedback");
        addFeedbackButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        addFeedbackButton.setOnAction(e -> {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment != null && "Completed".equals(selectedAppointment.getStatus())) {
                showFeedbackDialog(doctor, selectedAppointment);
            } else {
                showAlert("Error", "Please select a completed appointment to add feedback", Alert.AlertType.ERROR);
            }
        });

        appointmentButtons.getChildren().addAll(refreshAppointmentsButton, approveButton, cancelButton, completeButton, addFeedbackButton);
        appointmentsContent.getChildren().addAll(appointmentTable, appointmentButtons);
        appointmentsTab.setContent(appointmentsContent);

        // Communication Tab
        Tab communicationTab = new Tab("Communication");
        VBox communicationContent = new VBox(10);
        communicationContent.setPadding(new Insets(20));

        Button startVideoCallButton = new Button("Start Video Call");
        startVideoCallButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        startVideoCallButton.setOnAction(e -> {
            String meetingLink = VideoCall.generateMeetingLink();
            showVideoCallWindow(meetingLink);
        });

        Button chatButton = new Button("Open Chat");
        chatButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        chatButton.setOnAction(e -> {
            ChatInterface chatInterface = new ChatInterface(doctor);
            chatInterface.show();
        });

        communicationContent.getChildren().addAll(startVideoCallButton, chatButton);
        communicationTab.setContent(communicationContent);

        tabPane.getTabs().addAll(patientsTab, appointmentsTab, communicationTab);
        mainLayout.setCenter(tabPane);
    }

    private void showPatientDashboard(BorderPane mainLayout, Patient patient) {
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: white; -fx-border-color: #6FB3B8; -fx-border-radius: 5;");

        // Vital Signs Tab
        Tab vitalsTab = new Tab("Vital Signs");
        VBox vitalsContent = new VBox(10);
        vitalsContent.setPadding(new Insets(20));

        GridPane vitalsInput = new GridPane();
        vitalsInput.setHgap(10);
        vitalsInput.setVgap(10);
        vitalsInput.setAlignment(Pos.CENTER);

        TextField heartRateField = new TextField();
        heartRateField.setPromptText("Heart Rate (bpm)");
        TextField bloodPressureField = new TextField();
        bloodPressureField.setPromptText("Blood Pressure (mmHg)");
        TextField temperatureField = new TextField();
        temperatureField.setPromptText("Temperature (°C)");

        Button submitVitalsButton = new Button("Submit Vitals");
        submitVitalsButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        submitVitalsButton.setOnAction(e -> {
            try {
                double heartRate = Double.parseDouble(heartRateField.getText());
                double bloodPressure = Double.parseDouble(bloodPressureField.getText());
                double temperature = Double.parseDouble(temperatureField.getText());

                Vitals vitals = new Vitals(patient.getId(), LocalDate.now().toString(), heartRate, bloodPressure, temperature);
                VitalsDatabase.addVital(vitals);
                EmergencyAlert.checkVitals(vitals);

                showAlert("Success", "Vitals recorded successfully!", Alert.AlertType.INFORMATION);
                heartRateField.clear();
                bloodPressureField.clear();
                temperatureField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter valid numbers!", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                showAlert("Error", "Failed to record vitals: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        vitalsInput.add(new Label("Heart Rate:"), 0, 0);
        vitalsInput.add(heartRateField, 1, 0);
        vitalsInput.add(new Label("Blood Pressure:"), 0, 1);
        vitalsInput.add(bloodPressureField, 1, 1);
        vitalsInput.add(new Label("Temperature:"), 0, 2);
        vitalsInput.add(temperatureField, 1, 2);
        vitalsInput.add(submitVitalsButton, 1, 3);

        TableView<Vitals> vitalsTable = new TableView<>();
        TableColumn<Vitals, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Vitals, Double> heartRateColumn = new TableColumn<>("Heart Rate");
        heartRateColumn.setCellValueFactory(new PropertyValueFactory<>("heartRate"));
        TableColumn<Vitals, Double> bloodPressureColumn = new TableColumn<>("Blood Pressure");
        bloodPressureColumn.setCellValueFactory(new PropertyValueFactory<>("bloodPressure"));
        TableColumn<Vitals, Double> temperatureColumn = new TableColumn<>("Temperature");
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));

        vitalsTable.getColumns().addAll(dateColumn, heartRateColumn, bloodPressureColumn, temperatureColumn);
        vitalsTable.setItems(FXCollections.observableArrayList(VitalsDatabase.getPatientVitals(patient.getId())));

        Button refreshVitalsButton = new Button("Refresh Vitals");
        refreshVitalsButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        refreshVitalsButton.setOnAction(e -> {
            try {
                VitalsDatabase.refreshData();
                vitalsTable.setItems(FXCollections.observableArrayList(VitalsDatabase.getPatientVitals(patient.getId())));
            } catch (Exception ex) {
                showAlert("Error", "Failed to refresh data: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        vitalsContent.getChildren().addAll(vitalsInput, vitalsTable, refreshVitalsButton);
        vitalsTab.setContent(vitalsContent);

        // Appointments Tab
        Tab appointmentsTab = new Tab("Appointments");
        VBox appointmentsContent = new VBox(10);
        appointmentsContent.setPadding(new Insets(20));

        TableView<Appointment> appointmentTable = new TableView<>();
        TableColumn<Appointment, String> doctorNameColumn = new TableColumn<>("Doctor");
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        TableColumn<Appointment, String> appointmentDateColumn = new TableColumn<>("Date & Time");
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        TableColumn<Appointment, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Appointment, String> reasonColumn = new TableColumn<>("Reason");
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

        appointmentTable.getColumns().addAll(doctorNameColumn, appointmentDateColumn, statusColumn, reasonColumn);
        appointmentTable.setItems(FXCollections.observableArrayList(AppointmentManager.getPatientAppointments(patient.getId())));

        HBox appointmentButtons = new HBox(10);
        Button refreshAppointmentsButton = new Button("Refresh Appointments");
        refreshAppointmentsButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        refreshAppointmentsButton.setOnAction(e -> {
            try {
                AppointmentManager.refreshData();
                appointmentTable.setItems(FXCollections.observableArrayList(AppointmentManager.getPatientAppointments(patient.getId())));
            } catch (Exception ex) {
                showAlert("Error", "Failed to refresh appointments: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button requestAppointmentButton = new Button("Request Appointment");
        requestAppointmentButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        requestAppointmentButton.setOnAction(e -> showAppointmentRequestDialog(patient));

        appointmentButtons.getChildren().addAll(refreshAppointmentsButton, requestAppointmentButton);
        appointmentsContent.getChildren().addAll(appointmentTable, appointmentButtons);
        appointmentsTab.setContent(appointmentsContent);

        // Communication Tab
        Tab communicationTab = new Tab("Communication");
        VBox communicationContent = new VBox(10);
        communicationContent.setPadding(new Insets(20));

        Button startVideoCallButton = new Button("Start Video Call");
        startVideoCallButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        startVideoCallButton.setOnAction(e -> {
            String meetingLink = VideoCall.generateMeetingLink();
            showVideoCallWindow(meetingLink);
        });

        Button chatButton = new Button("Open Chat");
        chatButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        chatButton.setOnAction(e -> {
            ChatInterface chatInterface = new ChatInterface(patient);
            chatInterface.show();
        });

        communicationContent.getChildren().addAll(startVideoCallButton, chatButton);
        communicationTab.setContent(communicationContent);

        // Feedback Tab
        Tab feedbackTab = new Tab("Feedback & Prescriptions");
        VBox feedbackContent = new VBox(10);
        feedbackContent.setPadding(new Insets(20));

        TableView<Feedback> feedbackTable = new TableView<>();
        TableColumn<Feedback, String> feedbackDateColumn = new TableColumn<>("Date");
        feedbackDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        TableColumn<Feedback, String> doctorColumn = new TableColumn<>("Doctor");
        doctorColumn.setCellValueFactory(cellData -> {
            String doctorId = cellData.getValue().getDoctorId();
            User doctor = UserManager.getUser(doctorId);
            return new SimpleStringProperty(doctor != null ? doctor.getName() : doctorId);
        });
        TableColumn<Feedback, String> feedbackColumn = new TableColumn<>("Feedback");
        feedbackColumn.setCellValueFactory(new PropertyValueFactory<>("feedback"));
        TableColumn<Feedback, String> prescriptionColumn = new TableColumn<>("Prescription");
        prescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("prescription"));

        feedbackTable.getColumns().addAll(feedbackDateColumn, doctorColumn, feedbackColumn, prescriptionColumn);
        feedbackTable.setItems(FXCollections.observableArrayList(FeedbackManager.getPatientFeedbacks(patient.getId())));

        Button refreshFeedbackButton = new Button("Refresh Feedback");
        refreshFeedbackButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        refreshFeedbackButton.setOnAction(e -> {
            try {
                FeedbackManager.refreshData();
                feedbackTable.setItems(FXCollections.observableArrayList(FeedbackManager.getPatientFeedbacks(patient.getId())));
            } catch (Exception ex) {
                showAlert("Error", "Failed to refresh feedback: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        feedbackContent.getChildren().addAll(feedbackTable, refreshFeedbackButton);
        feedbackTab.setContent(feedbackContent);

        // Emergency Tab
        Tab emergencyTab = new Tab("Emergency");
        VBox emergencyContent = new VBox(10);
        emergencyContent.setPadding(new Insets(20));
        emergencyContent.setAlignment(Pos.CENTER);

        Button panicButton = new Button("EMERGENCY PANIC BUTTON");
        panicButton.setStyle("-fx-background-color: #6FB3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-font-size: 14px;");
        panicButton.setOnAction(e -> {
            try {
                EmergencyAlert.triggerEmergency(patient.getId());
                showAlert("Emergency Alert", "Emergency services have been notified!", Alert.AlertType.WARNING);
            } catch (Exception ex) {
                showAlert("Error", "Failed to trigger emergency: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        emergencyContent.getChildren().add(panicButton);
        emergencyTab.setContent(emergencyContent);

        tabPane.getTabs().addAll(vitalsTab, appointmentsTab, communicationTab, feedbackTab, emergencyTab);
        mainLayout.setCenter(tabPane);
    }

    private void showAppointmentDialog(Doctor doctor) {
        Dialog<Appointment> dialog = new Dialog<>();
        dialog.setTitle("Schedule Appointment");
        dialog.setHeaderText("Schedule a new appointment");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #F6F6F2;");

        ComboBox<Patient> patientComboBox = new ComboBox<>(FXCollections.observableArrayList(UserManager.getPatients()));
        patientComboBox.setPromptText("Select Patient");
        DatePicker datePicker = new DatePicker();
        ComboBox<String> timeComboBox = new ComboBox<>(FXCollections.observableArrayList(
            "09:00", "10:00", "11:00", "12:00", "14:00", "15:00", "16:00", "17:00"
        ));
        timeComboBox.setPromptText("Select Time");

        grid.add(new Label("Patient:"), 0, 0);
        grid.add(patientComboBox, 1, 0);
        grid.add(new Label("Date:"), 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(new Label("Time:"), 0, 2);
        grid.add(timeComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        ButtonType scheduleButtonType = new ButtonType("Schedule", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(scheduleButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setStyle("-fx-background-color: #F6F6F2;");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == scheduleButtonType) {
                Patient selectedPatient = patientComboBox.getValue();
                LocalDate date = datePicker.getValue();
                String time = timeComboBox.getValue();
                if (selectedPatient != null && date != null && time != null) {
                    return new Appointment(selectedPatient.getId(), selectedPatient.getName(), 
                        doctor.getId(), doctor.getName(),
                        LocalDateTime.of(date, java.time.LocalTime.parse(time, java.time.format.DateTimeFormatter.ofPattern("HH:mm"))).toString(), "Scheduled", "Regular checkup");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(appointment -> {
            try {
                AppointmentManager.scheduleAppointment(appointment);
                showAlert("Success", "Appointment scheduled successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                showAlert("Error", "Failed to schedule appointment: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void showAppointmentRequestDialog(Patient patient) {
        Dialog<Appointment> dialog = new Dialog<>();
        dialog.setTitle("Request Appointment");
        dialog.setHeaderText("Request a new appointment");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #F6F6F2;");

        // Create doctor combo box with custom cell factory to show name, ID and specialty
        ComboBox<Doctor> doctorComboBox = new ComboBox<>(FXCollections.observableArrayList(UserManager.getDoctors()));
        doctorComboBox.setPromptText("Select Doctor");
        doctorComboBox.setCellFactory(lv -> new ListCell<Doctor>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);
                if (empty || doctor == null) {
                    setText(null);
                } else {
                    setText(String.format("%s (ID: %s) - %s", doctor.getName(), doctor.getId(), doctor.getSpecialty()));
                }
            }
        });
        doctorComboBox.setButtonCell(new ListCell<Doctor>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);
                if (empty || doctor == null) {
                    setText(null);
                } else {
                    setText(String.format("%s (ID: %s) - %s", doctor.getName(), doctor.getId(), doctor.getSpecialty()));
                }
            }
        });

        DatePicker datePicker = new DatePicker();
        ComboBox<String> timeComboBox = new ComboBox<>(FXCollections.observableArrayList(
            "09:00", "10:00", "11:00", "12:00", "14:00", "15:00", "16:00", "17:00"
        ));
        timeComboBox.setPromptText("Select Time");
        TextArea reasonField = new TextArea();
        reasonField.setPromptText("Reason for appointment");

        grid.add(new Label("Doctor:"), 0, 0);
        grid.add(doctorComboBox, 1, 0);
        grid.add(new Label("Date:"), 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(new Label("Time:"), 0, 2);
        grid.add(timeComboBox, 1, 2);
        grid.add(new Label("Reason:"), 0, 3);
        grid.add(reasonField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType requestButtonType = new ButtonType("Request", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(requestButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setStyle("-fx-background-color: #F6F6F2;");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == requestButtonType) {
                Doctor selectedDoctor = doctorComboBox.getValue();
                LocalDate date = datePicker.getValue();
                String time = timeComboBox.getValue();
                String reason = reasonField.getText();
                if (selectedDoctor != null && date != null && time != null && !reason.isEmpty()) {
                    LocalDateTime dateTime = LocalDateTime.of(date, 
                        java.time.LocalTime.parse(time, java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                    return new Appointment(patient.getId(), patient.getName(), 
                        selectedDoctor.getId(), selectedDoctor.getName(),
                        dateTime.toString(), "Pending", reason);
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(appointment -> {
            try {
                AppointmentManager.requestAppointment(
                    appointment.getPatientId(),
                    appointment.getDoctorId(),
                    appointment.getDoctorName(),
                    appointment.getDateTime(),
                    appointment.getReason()
                );
                showAlert("Success", "Appointment request submitted successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                showAlert("Error", "Failed to submit appointment request: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void showVideoCallWindow(String meetingLink) {
        Stage videoStage = new Stage();
        videoStage.setTitle("Video Call");
        videoStage.setWidth(800);
        videoStage.setHeight(600);

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Handle JavaScript alerts
        webEngine.setOnAlert(event -> {
            showAlert("Video Call", event.getData(), Alert.AlertType.INFORMATION);
        });

        // Handle JavaScript errors
        webEngine.setOnError(event -> {
            showAlert("Error", "Failed to load video call: " + event.getMessage(), Alert.AlertType.ERROR);
        });

        // Configure WebView for modern browser features
        webEngine.setJavaScriptEnabled(true);
        webEngine.setUserAgent("Mozilla/5.0");

        // Load the meeting link
        webEngine.load(meetingLink);

        // Handle window close
        videoStage.setOnCloseRequest(e -> {
            webEngine.executeScript(
                "const tracks = document.querySelectorAll('video, audio');" +
                "tracks.forEach(track => {" +
                "    if (track.srcObject) {" +
                "        track.srcObject.getTracks().forEach(t => t.stop());" +
                "    }" +
                "});"
            );
        });

        Scene scene = new Scene(webView);
        videoStage.setScene(scene);
        videoStage.show();
    }

    private void startChat(String userId, String targetId) {
        try {
            ChatInterface chatInterface = new ChatInterface(UserManager.getUser(userId));
            chatInterface.show();
        } catch (Exception ex) {
            showAlert("Error", "Failed to start chat: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showFeedbackDialog(Doctor doctor, Appointment appointment) {
        Dialog<Feedback> dialog = new Dialog<>();
        dialog.setTitle("Add Feedback");
        dialog.setHeaderText("Add feedback for appointment");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #F6F6F2;");

        TextArea feedbackArea = new TextArea();
        feedbackArea.setPromptText("Enter feedback");
        feedbackArea.setPrefRowCount(5);
        feedbackArea.setWrapText(true);

        TextArea prescriptionArea = new TextArea();
        prescriptionArea.setPromptText("Enter prescription");
        prescriptionArea.setPrefRowCount(5);
        prescriptionArea.setWrapText(true);

        grid.add(new Label("Feedback:"), 0, 0);
        grid.add(feedbackArea, 1, 0);
        grid.add(new Label("Prescription:"), 0, 1);
        grid.add(prescriptionArea, 1, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setStyle("-fx-background-color: #F6F6F2;");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                String feedback = feedbackArea.getText();
                String prescription = prescriptionArea.getText();
                if (!feedback.isEmpty()) {
                    return new Feedback(appointment.getPatientId(), doctor.getId(), feedback, prescription, appointment.getAppointmentId());
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(feedback -> {
            try {
                FeedbackManager.addFeedback(feedback);
                showAlert("Success", "Feedback added successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                showAlert("Error", "Failed to add feedback: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    public static void main(String[] args) {
        // Start the chat server in a separate thread
        new Thread(() -> {
            try {
                ChatServer server = new ChatServer(1234);
                server.start();
            } catch (Exception e) {
                System.err.println("Failed to start chat server: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // Load feedbacks
        try {
            FeedbackManager.loadFeedbacks();
        } catch (Exception e) {
            System.err.println("Failed to load feedbacks: " + e.getMessage());
            e.printStackTrace();
        }
        
        launch(args);
    }
} 