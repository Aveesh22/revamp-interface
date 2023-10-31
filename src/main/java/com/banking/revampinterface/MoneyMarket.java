package com.banking.revampinterface;

/**
 * This class defines a money market account with specific monthly
 * interest and fees and inherits other properties and behaviors
 * from the Savings class
 */
public class MoneyMarket extends Savings
{
    public static final int MIN_BALANCE_FOR_NO_FEE = 2000;
    public static final int MAX_WITHDRAWAL_FEE = 10;
    public static final int NUM_MAX_WITHDRAWALS = 3;
    public static final double IS_LOYAL_MONTHLY_INTEREST = 0.0475;
    public static final double NOT_LOYAL_MONTHLY_INTEREST = 0.045;
    private int withdrawal; //number of withdrawals

    /**
     * Parameterized constructor with only a Profile
     * @param holder Profile object for the account holder
     */
    public MoneyMarket(Profile holder) {
        this.holder = holder;
    }

    /**
     * Parameterized Constructor which initializes a MoneyMarket account
     * @param holder the Profile holder for the MoneyMarket account
     * @param balance the initial balance for the MoneyMarket account
     */
    public MoneyMarket(Profile holder, double balance)
    {
        this.holder = holder;
        this.balance = balance;
        isLoyal = true;
    }

    /**
     * Determines the monthly fee for the MoneyMarket account
     * @return the monthly fee of the MoneyMarket account
     */
    @Override
    public double monthlyFee()
    {
        int fee = 0;
        if(getBalance() < MIN_BALANCE_FOR_NO_FEE)
            fee = MONTHLY_FEE;
        else
            fee = NO_MONTHLY_FEE;

        if(withdrawal > NUM_MAX_WITHDRAWALS)
            fee += MAX_WITHDRAWAL_FEE;

        return fee;
    }

    /**
     * Determines the monthly interest for the MoneyMarket account
     * @return the monthly interest for the MoneyMarket account
     */
    @Override
    public double monthlyInterest()
    {
        recheckLoyalStatus();
        if(isLoyal)
            return IS_LOYAL_MONTHLY_INTEREST;
        else
            return NOT_LOYAL_MONTHLY_INTEREST;
    }

    /**
     * Overrides the accountType() method to return the class name
     * @return the account type as a String
     */
    @Override
    public String accountType() {
        return "Money Market::" + super.accountType();
    }

    /**
     * Determines the String to print for the account
     * @return the output String
     */
    @Override
    public String printOutput() {
        return super.printOutput() + "::withdrawal: " + withdrawal;
    }

    /**
     * Rechecks the loyalty status of the Profile holder in
     * the MoneyMarket account
     */
    public void recheckLoyalStatus()
    {
        if(getBalance() >= MIN_BALANCE_FOR_NO_FEE)
            isLoyal = true;
        else
            isLoyal = false;
    }

    /**
     * Gets the number of withdrawals from the Money Market account
     * @return the number of withdrawals
     */
    public int getWithdrawal() {
        return withdrawal;
    }

    /**
     * Sets the number of withdrawals for the Money Market account
     * @param withdrawal the number of withdrawals
     */
    public void setWithdrawal(int withdrawal) {
        this.withdrawal = withdrawal;
    }

    /**
     * Overridden method which returns the textual representation of a Money Market account
     * @return the textual representation of a Money Market account
     */
    @Override
    public String toString()
    {
        return holder.toString() + "(MM)";
    }
}
