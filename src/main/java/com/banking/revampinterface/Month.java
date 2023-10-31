package com.banking.revampinterface;

/**
 * Enum class to define all the months of the year with their
 * order in the calendar and the number of total days they have.
 * @author Patryk Dziedzic, Aveesh Patel
 */
public enum Month {
    JANUARY (1, 31),
    FEBRUARY_NONLEAP (2, 28),
    FEBRUARY_LEAP (2, 29),
    MARCH (3, 31),
    APRIL (4, 30),
    MAY (5, 31),
    JUNE (6, 30),
    JULY (7, 31),
    AUGUST (8, 31),
    SEPTEMBER (9, 30),
    OCTOBER (10, 31),
    NOVEMBER (11, 30),
    DECEMBER (12, 31);


    private final int monthNumber;
    private final int totalDays;

    /**
     * Constructor to create the Month object
     * with the given month number and total days.
     * @param monthNumber the number of the month in the year
     * @param totalDays the total days in this month
     */
    Month(int monthNumber, int totalDays) {
        this.monthNumber = monthNumber;
        this.totalDays = totalDays;
    }

    /**
     * Get the month number of this month
     * @return the month number of this month in the year
     */
    public int getMonthNumber() {
        return monthNumber;
    }

    /**
     * Get the total days in this month
     * @return the total days in this month
     */
    public int getTotalDays() {
        return totalDays;
    }
}
