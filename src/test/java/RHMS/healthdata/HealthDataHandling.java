package RHMS.healthdata;

import RHMS.emergency.EmergencyAlert;

import java.io.IOException;
import java.util.Scanner;

public class HealthDataHandling {
    public static void inputVitals(String pid) throws IOException {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter Heart Rate: ");
            int heartRate = scanner.nextInt();
            System.out.print("Enter Oxygen Level: ");
            int oxygenLevel = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Blood Pressure (e.g., 120/80): ");
            String bloodPressure = scanner.nextLine();
            System.out.print("Enter Body Temperature (°C): ");
            double bodyTemperature = scanner.nextDouble();
            System.out.print("Enter Respiratory Rate: ");
            int respiratoryRate = scanner.nextInt();
            System.out.print("Enter Pulse Rate: ");
            int pulseRate = scanner.nextInt();
            System.out.print("Enter Blood Glucose Level: ");
            double bloodGlucoseLevel = scanner.nextDouble();
            VitalSign vital = new VitalSign(heartRate, oxygenLevel, bloodPressure, bodyTemperature,
                    respiratoryRate, pulseRate, bloodGlucoseLevel);
            vital.setPid(pid);
            EmergencyAlert.checkVitals(vital);
            VitalsDatabase.addVital(vital);
            System.out.println("Vital signs added successfully for Patient ID: " + pid);
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
            scanner.nextLine();
        }
    }
}