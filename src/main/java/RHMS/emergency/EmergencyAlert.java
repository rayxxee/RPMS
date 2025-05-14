package RHMS.emergency;

import RHMS.healthdata.Vitals;
import RHMS.notifications.EmailService;
import RHMS.appointments.Appointment;
import RHMS.appointments.AppointmentManager;
import RHMS.usermanagement.Doctor;
import RHMS.usermanagement.UserManager;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class EmergencyAlert {
    private static final Logger LOGGER = Logger.getLogger(EmergencyAlert.class.getName());
    
    // Vital sign thresholds
    private static final double MAX_HEART_RATE = 120.0;
    private static final double MIN_HEART_RATE = 40.0;
    private static final double MAX_BLOOD_PRESSURE = 180.0;
    private static final double MIN_BLOOD_PRESSURE = 60.0;
    private static final double MAX_TEMPERATURE = 39.0;
    private static final double MIN_TEMPERATURE = 35.0;

    public static void checkVitals(Vitals vitals) {
        if (vitals == null) {
            LOGGER.warning("Received null vitals data");
            return;
        }

        boolean isCritical = false;
        StringBuilder criticalSigns = new StringBuilder();

        // Check heart rate
        if (vitals.getHeartRate() > MAX_HEART_RATE) {
            isCritical = true;
            criticalSigns.append("High heart rate (").append(vitals.getHeartRate()).append(" bpm), ");
        } else if (vitals.getHeartRate() < MIN_HEART_RATE) {
            isCritical = true;
            criticalSigns.append("Low heart rate (").append(vitals.getHeartRate()).append(" bpm), ");
        }

        // Check blood pressure
        if (vitals.getBloodPressure() > MAX_BLOOD_PRESSURE) {
            isCritical = true;
            criticalSigns.append("High blood pressure (").append(vitals.getBloodPressure()).append(" mmHg), ");
        } else if (vitals.getBloodPressure() < MIN_BLOOD_PRESSURE) {
            isCritical = true;
            criticalSigns.append("Low blood pressure (").append(vitals.getBloodPressure()).append(" mmHg), ");
        }

        // Check temperature
        if (vitals.getTemperature() > MAX_TEMPERATURE) {
            isCritical = true;
            criticalSigns.append("High temperature (").append(vitals.getTemperature()).append(" °C), ");
        } else if (vitals.getTemperature() < MIN_TEMPERATURE) {
            isCritical = true;
            criticalSigns.append("Low temperature (").append(vitals.getTemperature()).append(" °C), ");
        }

        if (isCritical) {
            LOGGER.warning("Critical vitals detected for patient " + vitals.getPatientId() + ": " + criticalSigns.toString());
            triggerEmergency(vitals.getPatientId(), criticalSigns.toString());
        }
    }

    public static void triggerEmergency(String patientId) {
        triggerEmergency(patientId, "Manual emergency trigger");
    }

    private static void triggerEmergency(String patientId, String reason) {
        try {
            // Get the patient's last appointment
            List<Appointment> appointments = AppointmentManager.getPatientAppointments(patientId);
            String doctorId = null;
            String doctorEmail = null;
            
            // Find the most recent completed appointment
            for (int i = appointments.size() - 1; i >= 0; i--) {
                Appointment appt = appointments.get(i);
                if ("Completed".equals(appt.getStatus())) {
                    doctorId = appt.getDoctorId();
                    Doctor doctor = (Doctor) UserManager.getUser(doctorId);
                    if (doctor != null) {
                        doctorEmail = doctor.getEmail();
                        break;
                    }
                }
            }

            // If no previous appointment found, get a random doctor
            if (doctorEmail == null) {
                List<Doctor> doctors = UserManager.getDoctors();
                if (!doctors.isEmpty()) {
                    Doctor doctor = doctors.get(0); // Get first available doctor
                    doctorEmail = doctor.getEmail();
                    doctorId = doctor.getId();
                }
            }

            if (doctorEmail != null) {
                // Send emergency email to the doctor
                EmailService.sendEmergencyEmail(doctorEmail, patientId, 
                    UserManager.getUser(patientId).getName(), reason);
                
                LOGGER.info("Emergency alert sent to doctor " + doctorId + " for patient " + patientId);
            } else {
                LOGGER.severe("No doctor available for emergency alert for patient " + patientId);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling emergency alert for patient " + patientId, e);
        }
    }
}