package com.javacrud.javacrud.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Sharded;

@Document(collection = "daily_usage")
@Sharded(shardKey = { "userId", "mdn" })
public class DailyUsage {
    
    @Id
    private String id;
    private String mdn;
    private String userId;
    private Date usageDate;
    private Number usedInMb;
    private String nextCycleId;

    @DocumentReference
    private Cycle cycle;

    // Constructor
    public DailyUsage(String mdn, String userId, Date usageDate, Number usedInMb, String nextCycleId, Cycle cycle) {
        this.mdn = mdn;
        this.userId = userId;
        this.usageDate = usageDate;
        this.usedInMb = usedInMb;
        this.nextCycleId = nextCycleId;
        this.cycle = cycle;
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

    public void setNextCycleId(String nextCycleId) {
        this.nextCycleId = nextCycleId;
    }

    public String getNextCycleId() {
        return this.nextCycleId;
    }

    public Date getUsageDate() {
        return this.usageDate;
    }

    public void setUsageDate(Date usageDate) {
        this.usageDate = usageDate;
    }

    public Number getUsedInMb() {
        return this.usedInMb;
    }

    public void setUsedInMb(Number usedInMb) {
        this.usedInMb = usedInMb;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public Cycle getCycle() {
        return this.cycle;
    }
}
