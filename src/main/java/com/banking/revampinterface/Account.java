package com.banking.revampinterface;

import java.text.DecimalFormat;

/**
 * This abstract class defines an Account on
 * the AccountDatabase
 * @author Aveesh Patel, Patryk Dziedzic
 */
public abstract class Account implements Comparable<Account>
{
    protected Profile holder;

    protected double balance;

    /**
     * Determines the monthly interest for the Account
     * @return the monthly interest
     */
    public abstract double monthlyInterest();

    /**
     * Determines the monthly fee for the Account
     * @return the monthly fee
     */
    public abstract double monthlyFee();

    /**
     * Determines the account type
     * @return the account type as a String
     */
    public abstract String accountType();

    /**
     * Determines the String to print for the account
     * @return the output String
     */
    public String printOutput() {
        return accountType() + "::" + holder.toString() + "::" + "Balance " +
                DecimalFormat.getCurrencyInstance().format(balance);
    }

    /**
     * Determines the String to print for the account for PI
     * @return the output String
     */
    public String printOutput_PI() {
        return printOutput() + "::fee " +
                DecimalFormat.getCurrencyInstance().format(monthlyFee()) + "::monthly interest " +
                DecimalFormat.getCurrencyInstance().format(monthlyInterest()/12 * balance);
    }

    /**
     * Get the balance of the account
     * @return the balance of the account
     */
    protected double getBalance()
    {
        return balance;
    }

    /**
     * Sets the balance of the account
     * @param balance the balance to add or subtract from the account
     */
    protected void setBalance(double balance)
    {
        this.balance = balance;
    }

    /**
     * Compares two accounts and returns an integer value depicting the
     * result of the comparison between them. It is first sorted alphabetically
     * by account type, and then by Profile holder.
     * @param acct the account to be compared to
     * @return -1, 0, 1
     */
    @Override
    public int compareTo(Account acct)
    {
        int result = this.accountType().compareTo(acct.accountType());
        if (result == 0) {
            return this.holder.compareTo(acct.holder);
        }
        else return result;
    }

    /**
     * Determines whether two accounts are the same (or equal)
     * @param obj the account to be compared
     * @return true or false depending on whether the two accounts are equal
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Account) {
            Account acct = (Account) obj;
            return this.getClass().equals(acct.getClass()) && holder.equals(acct.holder);
        }
        return false;
    }
}
