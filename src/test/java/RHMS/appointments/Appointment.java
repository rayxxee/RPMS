package RHMS.appointments;

import java.io.Serializable;

public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String appointmentId;
    private String date;
    private String time;
    private String doctorId;
    private String patientId;
    private String status;

    public Appointment(String appointmentId, String date, String time, String doctorId, String patientId, String status) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.status = status;
    }

    public String getAppointmentId() { return appointmentId; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getDoctorId() { return doctorId; }
    public String getPatientId() { return patientId; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public void displayAppointment() {
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Status: " + status);
    }
}