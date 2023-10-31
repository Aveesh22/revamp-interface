package com.banking.revampinterface;

import java.util.Calendar;

/**
 * This class defines a Date
 * @author Patryk Dziedzic, Aveesh Patel
 */
public class Date implements Comparable<Date>
{
    private int year;
    private int month;
    private int day;

    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUARTERCENTENNIAL = 400;
    public static final int MONTH_OFFSET = 1;
    public static final int MAX_MONTH = 12; //Calendar class has 0-index months
    public static final int MONTH_INDEX = 0;
    public static final int DAY_INDEX = 1;
    public static final int YEAR_INDEX = 2;



    /**
     * A parameterized constructor that takes a string in the form of "mm/dd/yyyy"
     * @param date string date as "mm/dd/yyyy"
     */
    public Date(String date)
    {
        String[] tokens = date.split("/");
        year = Integer.parseInt(tokens[YEAR_INDEX]);
        month = Integer.parseInt(tokens[MONTH_INDEX]);
        day = Integer.parseInt(tokens[DAY_INDEX]);
    }

    /**
     * A default constructor that creates a date with today's date.
     */
    public Date()
    {
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + MONTH_OFFSET;
        day = cal.get(Calendar.DATE);
    }

    /**
     * A copy constructor that clones a date object.
     * @param date the Date to be cloned
     */
    public Date(Date date)
    {
        year = date.year;
        month = date.month;
        day = date.day;
    }

    /**
     * Gets the year of the given Date
     * @return an integer representing the specific year of the Date
     */
    public int getYear()
    {
        return year;
    }

    /**
     * Gets the month of the given Date
     * @return an integer representing the specific month of the Date
     */
    public int getMonth()
    {
        return month;
    }

    /**
     * Gets the day of the given Date
     * @return an integer representing the specific day of the Date
     */
    public int getDay() {
        return day;
    }

    /**
     * Compares two dates
     * @param date the date to be compared to
     * @return -1, 0, 1
     */
    @Override
    public int compareTo(Date date) {
        //year
        if (this.year > date.year)
            return 1;
        else if (this.year < date.year)
            return -1;
        else {
            //month
            if (this.month > date.month)
                return 1;
            else if (this.month < date.month)
                return -1;
            else {
                //day
                return Integer.compare(this.day, date.day);
            }
        }
    }

    /**
     * Return the textual representation of a date.
     * @return date as string
     */
    @Override
    public String toString()
    {
        return month + "/" + day + "/" + year;
    }

    /**
     * Checks if this Date object is a valid calendar date.
     * - year must be in range
     * - month must be in range
     * - day must be in range for the corresponding month
     * - February must be at most 29 if it is a leap year, 28 otherwise
     * @return true if the Date object is valid, false otherwise
     */
    public boolean isValid()
    {
        return validYear() && validMonth() && validDay();
    }

    /**
     * Checks if this Date object's year is valid.
     * @return true if the year is after 1900
     */
    private boolean validYear()
    {
        return year > 1900;
    }

    /**
     * Checks if this Date object's month is valid.
     * @return true if the month is between 1 and MAX_MONTH
     */
    private boolean validMonth() {
        return month >= 1 && month <= MAX_MONTH;
    }

    /**
     * Checks if this Date object's day is valid.
     * @return true if the day is valid for the corresponding month
     */
    private boolean validDay() {
        for (Month month: Month.values()) {
            if (this.month == month.getMonthNumber()) {
                if (this.month == Month.FEBRUARY_LEAP.getMonthNumber()) {
                    if (isLeap()) {
                        return day >= 1 && day <= Month.FEBRUARY_LEAP.getTotalDays();
                    }
                    else return day >= 1 && day <= Month.FEBRUARY_NONLEAP.getTotalDays();
                }
                return day >= 1 && day <= month.getTotalDays();
            }
        }
        return false;
    }

    /**
     * Checks if this date object's year is a leap year.
     * @return true if it is a leap year, false otherwise
     */
    private boolean isLeap() {
        if (year % QUADRENNIAL == 0) { //evenly divisible by 4
            if (year % CENTENNIAL == 0) {
                if (year % QUARTERCENTENNIAL == 0) {
                    return true;
                }
                else
                    return false;
            }
            else
                return true;
        }
        else
            return false;
    }

    /**
     * Determine whether two dates are the same date
     * @param date the date to be compared to
     * @return true or false depending on whether the dates are the same
     */
    public boolean equals(Date date)
    {
        return year == date.getYear() &&
                month == date.getMonth() &&
                day == date.getDay();
    }

    /**
     * Get today's date.
     * @return today's date
     */
    public static Date today()
    {
        return new Date();
    }

}