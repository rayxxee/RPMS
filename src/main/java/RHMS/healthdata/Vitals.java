package RHMS.healthdata;

import java.io.Serializable;

public class Vitals implements VitalSign, Serializable {
    private static final long serialVersionUID = 1L;
    private String patientId;
    private String date;
    private double heartRate;
    private double bloodPressure;
    private double temperature;

    public Vitals(String patientId, String date, double heartRate, double bloodPressure, double temperature) {
        this.patientId = patientId;
        this.date = date;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDate() {
        return date;
    }

    public double getHeartRate() {
        return heartRate;
    }

    public double getBloodPressure() {
        return bloodPressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public void displayVitals() {
        System.out.println("Patient ID: " + patientId);
        System.out.println("Date: " + date);
        System.out.println("Heart Rate: " + heartRate + " bpm");
        System.out.println("Blood Pressure: " + bloodPressure + " mmHg");
        System.out.println("Temperature: " + temperature + " °C");
    }
} 