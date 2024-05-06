package com.javacrud.javacrud.documents;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Sharded;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "daily_usage")
@Sharded(shardKey = { "userId", "mdn" })
public class DailyUsage {
    
    @Id
    private String id;
    private String mdn;
    private String userId;
    private Number usedInMb;
    private String nextCycleId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date usageDate;

    @DocumentReference
    private Cycle cycle;

    // Constructor for DailyUsage
    public DailyUsage(String mdn, String userId, Date usageDate, Number usedInMb, String nextCycleId, Cycle cycle) {
        this.mdn = mdn;
        this.userId = userId;
        this.usageDate = usageDate;
        this.usedInMb = usedInMb;
        this.nextCycleId = nextCycleId;
        this.cycle = cycle;
    }

    // Getter for id (MongoDB will automatically generate it)
    public String getId() {
        return id;
    }

    // Setter for id
    public String getMdn() {
        return this.mdn;
    }

    // Getter for mdn
    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    // Setter for userId
    public String getUserId() {
        return this.userId;
    }

    // Getter for userId
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Setter for nextCycleId
    public void setNextCycleId(String nextCycleId) {
        this.nextCycleId = nextCycleId;
    }

    // Getter for nextCycleId
    public String getNextCycleId() {
        return this.nextCycleId;
    }

    // Setter for usageDate
    public Date getUsageDate() {
        return this.usageDate;
    }

    // Getter for usageDate
    public void setUsageDate(Date usageDate) {
        this.usageDate = usageDate;
    }

    // Setter for usedInMb
    public Number getUsedInMb() {
        return this.usedInMb;
    }

    // Getter for usedInMb
    public void setUsedInMb(Number usedInMb) {
        this.usedInMb = usedInMb;
    }

    // Setter for cycle
    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    // Getter for cycle
    public Cycle getCycle() {
        return this.cycle;
    }
}
