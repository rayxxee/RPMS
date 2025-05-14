# Remote Patient Monitoring System (RPMS)

A comprehensive JavaFX-based application for managing remote patient monitoring, appointments, and healthcare communications.

## Features

- **User Authentication**
  - Secure login system for doctors and patients
  - Role-based access control
  - Password protection

- **Patient Management**
  - Patient registration and profile management
  - Medical history tracking
  - Vital signs monitoring
  - Appointment scheduling

- **Doctor Features**
  - Patient list management
  - Appointment scheduling and management
  - Patient monitoring dashboard
  - Medical record access

- **Communication**
  - Real-time chat interface between doctors and patients
  - Appointment notifications
  - Feedback system

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.6.0 or higher
- JavaFX 17 or higher

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/rayxxee/RPMS.git
   cd RPMS
   ```

2. Build the project using Maven:
   ```bash
   mvn clean package
   ```

3. The executable JAR file will be created in the `target` directory.

## Running the Application

### Method : Using Maven
```bash
mvn javafx:run
```


## Usage Guide

### For Patients

1. **Login/Registration**
   - Launch the application
   - Click "Register" if you're a new patient
   - Fill in your details and create an account
   - Use your credentials to log in

2. **Managing Appointments**
   - View your upcoming appointments
   - Schedule new appointments
   - Cancel or reschedule existing appointments

3. **Vital Signs**
   - Input your vital signs as required
   - View your historical vital signs data
   - Receive alerts for abnormal readings

4. **Communication**
   - Access the chat interface to communicate with your doctor
   - Send and receive messages
   - View appointment notifications

### For Doctors

1. **Login**
   - Launch the application
   - Log in with your doctor credentials

2. **Patient Management**
   - View your patient list
   - Access patient medical records
   - Monitor patient vital signs

3. **Appointment Management**
   - View and manage appointments
   - Schedule new appointments
   - Send appointment notifications

4. **Communication**
   - Chat with patients
   - Send important notifications
   - Review patient feedback

## Data Storage

The application uses serialized files for data storage:
- `users.ser`: User account information
- `appointments.ser`: Appointment data
- `vitals.ser`: Patient vital signs
- `feedback.ser`: Patient feedback
- `rpm_feedbacks.ser`: Remote patient monitoring feedback

## Troubleshooting

1. **Application won't start**
   - Ensure Java 17 or higher is installed
   - Verify JavaFX is properly configured
   - Check if all dependencies are resolved

2. **Login issues**
   - Verify your credentials
   - Check if your account exists
   - Contact system administrator if problems persist

3. **Data not saving**
   - Ensure write permissions in the application directory
   - Check available disk space
   - Verify file integrity

## Security Notes

- All passwords are securely hashed
- User sessions are properly managed
- Data is stored locally with appropriate access controls
- Regular backups are recommended

## Email Configuration

To set up email notifications in the application:

1. **Gmail Setup (Recommended)**
   - Use a Gmail account for sending notifications
   - Enable 2-Step Verification in your Google Account
   - Generate an App Password:
     1. Go to your Google Account settings
     2. Navigate to Security
     3. Under "2-Step Verification", click on "App passwords"
     4. Select "Mail" and your device
     5. Copy the generated 16-character password

2. **Configure Email Settings**
   - Open `src/main/java/RHMS/notifications/EmailService.java`
   - Replace `your.email@gmail.com` with your Gmail address
   - Replace `your_app_password` with the generated App Password
   - Do the same for `src/main/java/RHMS/notifications/EmailNotification.java`

3. **Important Security Notes**
   - Never commit your email credentials to version control
   - Keep your App Password secure and don't share it
   - If you suspect your App Password is compromised, revoke it immediately and generate a new one

