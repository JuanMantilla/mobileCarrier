package com.javacrud.javacrud.documents;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "daily_usage")
public class DailyUsage {
    
    @Id
    private String id;
    private String mdn;
    private String userId;
    private LocalDate usageDate;
    private Number usedInMb;

    // Constructor
    public DailyUsage(String mdn, String userId, LocalDate usageDate, Number usedInMb) {
        this.mdn = mdn;
        this.userId = userId;
        this.usageDate = usageDate;
        this.usedInMb = usedInMb;
    }

    public String getId() {
        return id;
    }

    public String getMdn() {
        return this.mdn;
    }

    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getUsageDate() {
        return this.usageDate;
    }

    public void setUsageDate(LocalDate usageDate) {
        this.usageDate = usageDate;
    }

    public Number getUsedInMb() {
        return this.usedInMb;
    }

    public void setName(Number usedInMb) {
        this.usedInMb = usedInMb;
    }
}
