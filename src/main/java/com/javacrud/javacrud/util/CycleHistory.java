package com.javacrud.javacrud.util;

import java.time.LocalDate;

public class CycleHistory {
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;

    // Constructor
    public CycleHistory(String id, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for startDate
    public LocalDate getStartDate() {
        return startDate;
    }

    // Setter for startDate
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    // Getter for endDate
    public LocalDate getEndDate() {
        return endDate;
    }

    // Setter for endDate
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
