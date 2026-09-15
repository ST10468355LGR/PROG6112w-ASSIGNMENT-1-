package com.medicare.models;

/**
 * Represents an Inpatient, inheriting from Patient.
 */
public class Inpatient extends Patient {
    private String wardNumber;
    private String bedNumber;

    public Inpatient(String patientId, String firstName, String lastName, int age, String gender, String medicalCondition, PatientCategory category) {
        super(patientId, firstName, lastName, age, gender, medicalCondition, category);
        this.wardNumber = "Ward 1";
        this.bedNumber = null;
    }

    public String getWardNumber() { return wardNumber; }
    public void setWardNumber(String wardNumber) { this.wardNumber = wardNumber; }

    public String getBedNumber() { return bedNumber; }
    public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }

    @Override
    public String displayDetails() {
        String bedInfo = (bedNumber == null) ? "Unallocated" : bedNumber;
        return super.displayDetails() + " | Ward: " + wardNumber + " | Bed: " + bedInfo;
    }
}
