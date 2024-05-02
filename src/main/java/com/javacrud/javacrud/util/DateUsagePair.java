package com.javacrud.javacrud.util;

import java.util.Date;

public class DateUsagePair {
    private Date date;
    private Number usedInMb;

    // Constructor
    public DateUsagePair(Date date, Number usedInMb) {
        this.date = date;
        this.usedInMb = usedInMb;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Number getUsedInMb() {
        return usedInMb;
    }

    public void setUsedInMb(Number usedInMb) {
        this.usedInMb = usedInMb;
    }
}
