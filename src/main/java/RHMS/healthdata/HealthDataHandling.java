package RHMS.healthdata;

import RHMS.emergency.EmergencyAlert;

import java.io.IOException;
import java.util.Scanner;

public class HealthDataHandling {
    public static void inputVitals(String pid) throws IOException {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter Heart Rate: ");
            double heartRate = scanner.nextDouble();
            System.out.print("Enter Blood Pressure (as a number, e.g., 120): ");
            double bloodPressure = scanner.nextDouble();
            System.out.print("Enter Body Temperature (°C): ");
            double bodyTemperature = scanner.nextDouble();
            String date = "2025-01-01"; // Or use LocalDate.now().toString()
            Vitals vital = new Vitals(pid, date, heartRate, bloodPressure, bodyTemperature);
            EmergencyAlert.checkVitals(vital);
            VitalsDatabase.addVital(vital);
            System.out.println("Vital signs added successfully for Patient ID: " + pid);
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
            scanner.nextLine();
        }
    }
}