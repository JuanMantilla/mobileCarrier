package com.javacrud.javacrud.util;

import java.time.LocalDate;

public class DateUsagePair {
    private LocalDate date;
    private Number usedInMb;

    // Constructor
    public DateUsagePair(LocalDate date, Number usedInMb) {
        this.date = date;
        this.usedInMb = usedInMb;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Number getUsedInMb() {
        return usedInMb;
    }

    public void setUsedInMb(Number usedInMb) {
        this.usedInMb = usedInMb;
    }
}
