package com.banking.revampinterface;

/**
 * This class defines a College Checking account with specific parameters
 * for monthly fees and inherits other properties and behaviors from
 * the Account and Checking classes
 * @author Aveesh Patel, Patryk Dziedzic
 */
public class CollegeChecking extends Checking
{
    private Campus campus; //campus code
    public static final double NO_MONTHLY_FEE = 0.00;

    /**
     * Parameterized constructor with only a Profile
     * @param holder Profile object for the account holder
     */
    public CollegeChecking(Profile holder) {
        this.holder = holder;
    }

    /**
     * Parameterized constructor with only a Profile and a balance
     * @param holder Profile object for the account holder
     * @param balance balance for the account
     */
    public CollegeChecking(Profile holder, double balance) {
        this.holder = holder;
        this.balance = balance;
    }

    /**
     * Parameterized constructor
     * @param holder Profile object for the account holder
     * @param balance current balance in the account
     */
    public CollegeChecking(Profile holder, double balance, Campus campus) {
        this.holder = holder;
        this.balance = balance;
        this.campus = campus;
    }

    /**
     * Returns the monthly fee for the College Checking account
     * @return a double depicting the monthly fee for the College Checking account
     */
    @Override
    public double monthlyFee()
    {
        return NO_MONTHLY_FEE;
    }

    /**
     * Overrides the accountType() method to return the class name
     * @return the account type as a String
     */
    @Override
    public String accountType() {
        return "College Checking";
    }

    /**
     * Determines the String to print for the account
     * @return the output String
     */
    @Override
    public String printOutput() {
        return super.printOutput() + "::" + campus.name();
    }

    /**
     * Overridden method which returns the textual representation of a College Checking account
     * @return the textual representation of a College Checking account
     */
    @Override
    public String toString()
    {
        return holder.toString() + "(CC)";
    }
}
