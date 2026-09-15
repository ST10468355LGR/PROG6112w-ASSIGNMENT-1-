package com.medicare.services;

import com.medicare.models.Inpatient;
import com.medicare.models.Patient;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Manages patient records.
 */
public class PatientManager {
    private final List<Patient> patients;

    public PatientManager() {
        this.patients = new ArrayList<>();
    }

    public void registerPatient(Patient patient) {
        if (searchPatient(patient.getPatientId()) != null) {
            throw new IllegalArgumentException("Patient with ID " + patient.getPatientId() + " already exists.");
        }
        patients.add(patient);
    }

    public Patient searchPatient(String patientId) {
        for (Patient p : patients) {
            if (p.getPatientId().equalsIgnoreCase(patientId)) {
                return p;
            }
        }
        return null;
    }

    public void updatePatient(String patientId, Patient updatedPatient) {
        Patient existing = searchPatient(patientId);
        if (existing == null) {
            throw new IllegalArgumentException("Patient with ID " + patientId + " not found.");
        }
        existing.setFirstName(updatedPatient.getFirstName());
        existing.setLastName(updatedPatient.getLastName());
        existing.setAge(updatedPatient.getAge());
        existing.setGender(updatedPatient.getGender());
        existing.setMedicalCondition(updatedPatient.getMedicalCondition());
        existing.setCategory(updatedPatient.getCategory());
    }

    public Patient deletePatient(String patientId) {
        Patient p = searchPatient(patientId);
        if (p == null) return null;
        
        if (p instanceof Inpatient) {
            Inpatient in = (Inpatient) p;
            if (in.getBedNumber() != null) {
                throw new IllegalStateException("Cannot delete patient " + patientId + ". Bed " + in.getBedNumber() + " is still allocated.");
            }
        }
        patients.remove(p);
        return p;
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    public void sortPatientsBySurname() {
        patients.sort(Comparator.comparing(Patient::getLastName));
    }

    public void sortPatientsById() {
        patients.sort(Comparator.comparing(Patient::getPatientId));
    }

    public int getTotalRegisteredPatients() {
        return patients.size();
    }
}