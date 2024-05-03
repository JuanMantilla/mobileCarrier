package com.javacrud.javacrud.util;

import java.util.Date;

public class DailyUsageDTO {
    private String mdn;
    private String userId;
    private Date usageDate;
    private Number usedInMb;
    private String nextCycleId;

    // Constructor
    public DailyUsageDTO(String mdn, String userId, Date usageDate, Number usedInMb, String nextCycleId) {
        this.mdn = mdn;
        this.userId = userId;
        this.usageDate = usageDate;
        this.usedInMb = usedInMb;
        this.nextCycleId = nextCycleId;
    }

    // Getter and setter for mdn
    public String getMdn() {
        return mdn;
    }

    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    // Getter and setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and setter for usageDate
    public Date getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(Date usageDate) {
        this.usageDate = usageDate;
    }

    // Getter and setter for usedInMb
    public Number getUsedInMb() {
        return usedInMb;
    }

    public void setUsedInMb(Number usedInMb) {
        this.usedInMb = usedInMb;
    }

    // Getter and setter for nextCycleId
    public String getNextCycleId() {
        return nextCycleId;
    }

    public void setNextCycleId(String nextCycleId) {
        this.nextCycleId = nextCycleId;
    }
}
