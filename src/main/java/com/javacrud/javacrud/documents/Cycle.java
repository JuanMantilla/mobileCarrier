package com.javacrud.javacrud.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.util.Date;

@Document(collection = "cycle")
@Sharded(shardKey = { "userId", "mdn" })
public class Cycle {
    @Id
    private String id; // Auto-generated by MongoDB

    private String mdn; // The phone number of a customer
    private Date startDate; // The start date of a billing cycle
    private Date endDate; // The end date of a billing cycle
    private String userId; // Foreign key to the id of the user collection

    public Cycle(String mdn, Date startDate, Date endDate, String userId) {
        this.mdn = mdn;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }
    // Getter for id (MongoDB will automatically generate it)
    public String getId() {
        return id;
    }

    // Setter for mdn
    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    // Getter for mdn
    public String getMdn() {
        return mdn;
    }

    // Setter for startDate
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    // Getter for startDate
    public Date getStartDate() {
        return startDate;
    }

    // Setter for endDate
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // Getter for endDate
    public Date getEndDate() {
        return endDate;
    }

    // Setter for userId
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter for userId
    public String getUserId() {
        return userId;
    }
}
