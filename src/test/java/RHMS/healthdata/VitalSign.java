package RHMS.healthdata;

import java.io.Serializable;

public class VitalSign implements Serializable {
    private static final long serialVersionUID = 1L;
    private String pid;
    private int heartRate;
    private int oxygenLevel;
    private String bloodPressure;
    private double bodyTemperature;
    private int respiratoryRate;
    private int pulseRate;
    private double bloodGlucoseLevel;

    public VitalSign(int heartRate, int oxygenLevel, String bloodPressure, double bodyTemperature,
                     int respiratoryRate, int pulseRate, double bloodGlucoseLevel) {
        this.heartRate = heartRate;
        this.oxygenLevel = oxygenLevel;
        this.bloodPressure = bloodPressure;
        this.bodyTemperature = bodyTemperature;
        this.respiratoryRate = respiratoryRate;
        this.pulseRate = pulseRate;
        this.bloodGlucoseLevel = bloodGlucoseLevel;
    }

    public String getPid() { return pid; }
    public int getHeartRate() { return heartRate; }
    public int getOxygenLevel() { return oxygenLevel; }
    public String getBloodPressure() { return bloodPressure; }
    public double getBodyTemperature() { return bodyTemperature; }
    public int getRespiratoryRate() { return respiratoryRate; }
    public int getPulseRate() { return pulseRate; }
    public double getBloodGlucoseLevel() { return bloodGlucoseLevel; }

    public void setPid(String pid) { this.pid = pid; }
}