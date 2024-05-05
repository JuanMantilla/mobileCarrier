package com.javacrud.javacrud.DTOs;

import java.util.Date;

public class CycleHistoryDTO {
    private String id;
    private Date startDate;
    private Date endDate;

    // Constructor
    public CycleHistoryDTO(String id, Date startDate, Date endDate) {
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
    public Date getStartDate() {
        return startDate;
    }

    // Setter for startDate
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    // Getter for endDate
    public Date getEndDate() {
        return endDate;
    }

    // Setter for endDate
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
