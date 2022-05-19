package ch.epfl.sweng.hostme.wallet;

import java.util.Calendar;

public enum RemindersDate {

    DAY1(Calendar.DAY_OF_MONTH, -1, "in 1 day"),
    DAY3(Calendar.DAY_OF_MONTH, -3, "in 3 days"),
    WEEK1(Calendar.WEEK_OF_MONTH, -1, "in 1 week"),
    WEEK2(Calendar.WEEK_OF_MONTH, -2, "in 2 weeks"),
    MONTH1(Calendar.MONTH, -1, "in 1 month"),
    MONTH2(Calendar.MONTH, -2, "in 2 months"),
    MONTH3(Calendar.MONTH, -3, "in 3 months");


    public int timeUnit;
    public int number;
    public String message;
    RemindersDate(int timeUnit, int number, String message) {
        this.timeUnit = timeUnit;
        this.number = number;
        this.message = message;
    }
}
