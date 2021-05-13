package models;

import java.time.YearMonth;

public class Revenue {
    public double revenue;
    public YearMonth yearMonth;

    public Revenue(double revenue, int month, int year) {
        this.revenue = revenue;
        this.yearMonth = YearMonth.of(year, month);
    }
}
