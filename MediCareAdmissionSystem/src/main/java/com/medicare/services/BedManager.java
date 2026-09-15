package com.medicare.services;

import com.medicare.models.Inpatient;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the bed allocation system using a 4x5 2D array for layout.
 */
public class BedManager {
    private final String[][] wardLayout;
    private final int ROWS = 4;
    private final int COLS = 5;
    private final List<String> occupiedBeds;

    public BedManager() {
        wardLayout = new String[ROWS][COLS];
        occupiedBeds = new ArrayList<>();
        initializeBeds();
    }

    private void initializeBeds() {
        int bedCounter = 1;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                wardLayout[i][j] = String.format("B%02d", bedCounter++);
            }
        }
    }

    public String allocateBed(Inpatient patient) {
        if (patient.getBedNumber() != null) {
            throw new IllegalStateException("Patient already has bed " + patient.getBedNumber() + " allocated.");
        }

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                String bedId = wardLayout[i][j];
                if (!occupiedBeds.contains(bedId)) {
                    occupiedBeds.add(bedId);
                    patient.setBedNumber(bedId);
                    return bedId;
                }
            }
        }
        throw new IllegalStateException("No beds available in the ward.");
    }

    public void releaseBed(Inpatient patient) {
        if (patient.getBedNumber() == null) {
            throw new IllegalStateException("Patient does not have a bed allocated.");
        }

        String bedToRelease = patient.getBedNumber();
        if (occupiedBeds.contains(bedToRelease)) {
            occupiedBeds.remove(bedToRelease);
            patient.setBedNumber(null);
        } else {
            throw new IllegalStateException("Data inconsistency: Bed " + bedToRelease + " was not marked as occupied.");
        }
    }

    public String displayWardLayout() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Ward Layout (4x5) ---\n");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                String bedId = wardLayout[i][j];
                if (occupiedBeds.contains(bedId)) {
                    sb.append(String.format("[%s*] ", bedId));
                } else {
                    sb.append(String.format("[%s ] ", bedId));
                }
            }
            sb.append("\n");
        }
        sb.append("Note: [Bxx*] = Occupied, [Bxx ] = Available\n");
        return sb.toString();
    }

    public List<String> getAvailableBeds() {
        List<String> available = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                String bedId = wardLayout[i][j];
                if (!occupiedBeds.contains(bedId)) {
                    available.add(bedId);
                }
            }
        }
        return available;
    }

    public List<String> getOccupiedBeds() {
        return new ArrayList<>(occupiedBeds);
    }

    public int getTotalBeds() {
        return ROWS * COLS;
    }

    public int getOccupiedBedCount() {
        return occupiedBeds.size();
    }
}
