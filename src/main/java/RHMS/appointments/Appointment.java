package RHMS.appointments;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String appointmentId;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String dateTime;
    private String status;
    private String reason;

    public Appointment(String patientId, String patientName, String doctorId, String doctorName, 
                      String dateTime, String status, String reason) {
        this.appointmentId = java.util.UUID.randomUUID().toString();
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.dateTime = dateTime;
        this.status = status;
        this.reason = reason;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public StringProperty patientNameProperty() {
        return new SimpleStringProperty(patientName);
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public StringProperty doctorNameProperty() {
        return new SimpleStringProperty(doctorName);
    }

    public String getDateTime() {
        return dateTime;
    }

    public StringProperty dateTimeProperty() {
        return new SimpleStringProperty(dateTime);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StringProperty statusProperty() {
        return new SimpleStringProperty(status);
    }

    public String getReason() {
        return reason;
    }

    public void displayAppointment() {
        System.out.println("Patient ID: " + patientId);
        System.out.println("Patient Name: " + patientName);
        System.out.println("Date and Time: " + dateTime);
        System.out.println("Status: " + status);
    }
}