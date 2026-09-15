package com.medicare.main;

import com.medicare.models.Inpatient;
import com.medicare.models.Patient;
import com.medicare.models.PatientCategory;
import com.medicare.services.BedManager;
import com.medicare.services.PatientManager;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Main console-based application for MediCare Hospital.
 */
public class HospitalAdmissionSystem {
    private static PatientManager patientManager = new PatientManager();
    private static BedManager bedManager = new BedManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedData();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1: managePatientsMenu(); break;
                case 2: manageBedsMenu(); break;
                case 3: generateReportsMenu(); break;
                case 4:
                    running = false;
                    System.out.println("Exiting MediCare System. Goodbye!");
                    break;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void seedData() {
        patientManager.registerPatient(new Inpatient("P001", "John", "Doe", 45, "Male", "Flu", PatientCategory.INPATIENT));
        patientManager.registerPatient(new Patient("P002", "Jane", "Smith", 30, "Female", "Checkup", PatientCategory.OUTPATIENT));
        patientManager.registerPatient(new Patient("P003", "Bob", "Johnson", 60, "Male", "Fracture", PatientCategory.EMERGENCY));
        System.out.println("System initialized with sample data.");
    }

    private static void printMainMenu() {
        System.out.println("\n=== MediCare Hospital Admission System ===");
        System.out.println("1. Manage Patients");
        System.out.println("2. Manage Beds");
        System.out.println("3. Generate Reports");
        System.out.println("4. Exit");
        System.out.print("Enter choice: ");
    }

    private static int getUserChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }

    private static void managePatientsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Patient Management ---");
            System.out.println("1. Register New Patient");
            System.out.println("2. Search Patient by ID");
            System.out.println("3. Update Patient Details");
            System.out.println("4. Delete Patient");
            System.out.println("5. Display All Patients");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            int choice = getUserChoice();
            switch (choice) {
                case 1: registerNewPatient(); break;
                case 2: searchPatient(); break;
                case 3: updatePatient(); break;
                case 4: deletePatient(); break;
                case 5: displayAllPatients(); break;
                case 6: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void registerNewPatient() {
        try {
            System.out.print("Enter Patient ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter First Name: ");
            String fname = scanner.nextLine();
            System.out.print("Enter Last Name: ");
            String lname = scanner.nextLine();
            System.out.print("Enter Age: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Gender: ");
            String gender = scanner.nextLine();
            System.out.print("Enter Medical Condition: ");
            String condition = scanner.nextLine();
            
            System.out.println("Select Category: 1. Inpatient, 2. Outpatient, 3. Emergency");
            System.out.print("Enter choice: ");
            int catChoice = scanner.nextInt();
            scanner.nextLine();
            
            PatientCategory category;
            switch (catChoice) {
                case 1: category = PatientCategory.INPATIENT; break;
                case 2: category = PatientCategory.OUTPATIENT; break;
                case 3: category = PatientCategory.EMERGENCY; break;
                default: 
                    System.out.println("Invalid category. Defaulting to Outpatient.");
                    category = PatientCategory.OUTPATIENT;
            }

            Patient newPatient;
            if (category == PatientCategory.INPATIENT) {
                newPatient = new Inpatient(id, fname, lname, age, gender, condition, category);
            } else {
                newPatient = new Patient(id, fname, lname, age, gender, condition, category);
            }

            patientManager.registerPatient(newPatient);
            System.out.println("Patient registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input format. Please try again.");
            scanner.nextLine();
        }
    }

    private static void searchPatient() {
        System.out.print("Enter Patient ID to search: ");
        String id = scanner.nextLine();
        Patient p = patientManager.searchPatient(id);
        if (p != null) {
            System.out.println("\nPatient Found:");
            System.out.println(p.displayDetails());
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static void updatePatient() {
        System.out.print("Enter Patient ID to update: ");
        String id = scanner.nextLine();
        Patient existing = patientManager.searchPatient(id);
        if (existing == null) {
            System.out.println("Patient not found.");
            return;
        }
        
        try {
            System.out.print("Enter New First Name (" + existing.getFirstName() + "): ");
            String fname = scanner.nextLine();
            System.out.print("Enter New Last Name (" + existing.getLastName() + "): ");
            String lname = scanner.nextLine();
            System.out.print("Enter New Age (" + existing.getAge() + "): ");
            int age = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter New Gender (" + existing.getGender() + "): ");
            String gender = scanner.nextLine();
            System.out.print("Enter New Condition (" + existing.getMedicalCondition() + "): ");
            String condition = scanner.nextLine();

            Patient updated = new Patient(id, fname, lname, age, gender, condition, existing.getCategory());
            patientManager.updatePatient(id, updated);
            System.out.println("Patient updated successfully.");
        } catch (Exception e) {
            System.out.println("Error updating patient: " + e.getMessage());
        }
    }

    private static void deletePatient() {
        System.out.print("Enter Patient ID to delete: ");
        String id = scanner.nextLine();
        try {
            Patient deleted = patientManager.deletePatient(id);
            if (deleted != null) {
                System.out.println("Patient " + id + " deleted successfully.");
            } else {
                System.out.println("Patient not found.");
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayAllPatients() {
        List<Patient> patients = patientManager.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
            return;
        }
        System.out.println("\n--- All Registered Patients ---");
        for (Patient p : patients) {
            System.out.println(p.displayDetails());
        }
    }

    private static void manageBedsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Bed Management ---");
            System.out.println("1. Allocate Bed to Inpatient");
            System.out.println("2. Release Bed");
            System.out.println("3. Display Ward Layout");
            System.out.println("4. Display Available Beds");
            System.out.println("5. Display Occupied Beds");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            int choice = getUserChoice();
            switch (choice) {
                case 1: allocateBed(); break;
                case 2: releaseBed(); break;
                case 3: System.out.println(bedManager.displayWardLayout()); break;
                case 4: System.out.println("Available Beds: " + bedManager.getAvailableBeds()); break;
                case 5: System.out.println("Occupied Beds: " + bedManager.getOccupiedBeds()); break;
                case 6: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void allocateBed() {
        System.out.print("Enter Inpatient ID to allocate bed: ");
        String id = scanner.nextLine();
        Patient p = patientManager.searchPatient(id);
        
        if (p == null) {
            System.out.println("Patient not found.");
            return;
        }
        if (!(p instanceof Inpatient)) {
            System.out.println("Error: Only Inpatients can be allocated a bed.");
            return;
        }
        
        try {
            String bed = bedManager.allocateBed((Inpatient) p);
            System.out.println("Bed " + bed + " allocated to " + p.getFirstName() + " " + p.getLastName() + ".");
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void releaseBed() {
        System.out.print("Enter Inpatient ID to release bed: ");
        String id = scanner.nextLine();
        Patient p = patientManager.searchPatient(id);
        
        if (p == null) {
            System.out.println("Patient not found.");
            return;
        }
        if (!(p instanceof Inpatient)) {
            System.out.println("Error: Patient is not an Inpatient.");
            return;
        }
        
        try {
            bedManager.releaseBed((Inpatient) p);
            System.out.println("Bed released for patient " + p.getFirstName() + " " + p.getLastName() + ".");
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void generateReportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Reports ---");
            System.out.println("1. Display All Registered Patients");
            System.out.println("2. Display All Available Beds");
            System.out.println("3. Display All Occupied Beds");
            System.out.println("4. Display Total Registered Patients");
            System.out.println("5. Display Total Occupied Beds");
            System.out.println("6. Display Ward Occupancy Percentage");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            int choice = getUserChoice();
            switch (choice) {
                case 1: displayAllPatients(); break;
                case 2: System.out.println("Available Beds: " + bedManager.getAvailableBeds()); break;
                case 3: System.out.println("Occupied Beds: " + bedManager.getOccupiedBeds()); break;
                case 4: System.out.println("Total Registered Patients: " + patientManager.getTotalRegisteredPatients()); break;
                case 5: System.out.println("Total Occupied Beds: " + bedManager.getOccupiedBedCount()); break;
                case 6: 
                    int total = bedManager.getTotalBeds();
                    int occupied = bedManager.getOccupiedBedCount();
                    double percentage = ((double) occupied / total) * 100;
                    System.out.printf("Ward Occupancy: %.2f%% (%d/%d beds occupied)\n", percentage, occupied, total);
                    break;
                case 7: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }
}