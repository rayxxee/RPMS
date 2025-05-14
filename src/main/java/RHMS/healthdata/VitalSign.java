package RHMS.healthdata;

public interface VitalSign {
    String getPatientId();
    String getDate();
    double getHeartRate();
    double getBloodPressure();
    double getTemperature();
} 