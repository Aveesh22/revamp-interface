package com.banking.revampinterface;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * This class defines the Controller for the Transaction Manager
 * @author Aveesh Patel, Patryk Dziedzic
 */
public class TransactionManagerController
{
    @FXML
    private Label welcomeText;
    @FXML
    private TextField fname_OpenClose;
    @FXML
    private TextField lname_OpenClose;
    @FXML
    private DatePicker dob_OpenClose;
    @FXML
    private TextField initBal_OpenClose;

    private static final int MIN_BALANCE_FOR_NO_FEE_IN_MONEY_MARKET = 2000;
    private static final int OF_AGE = 16;
    private static final int OVER_COLLEGE_AGE = 24;
    private AccountDatabase database = new AccountDatabase();


    /**
     * Check if the account is valid
     * @param cmd The current input line as a String array of tokens
     * @param dob the date of birth of the Profile holder
     * @param balance the balance of the account
     * @return true or false dependent on if the account is valid or not
     */
    private boolean acctIsValid(String[] cmd, Date dob, double balance) {
        if(isFuture(dob))
            System.out.println("DOB invalid: " + dob + " cannot be today or a future day.");
        else if(!dob.isValid())
            System.out.println("DOB invalid: " + dob + " not a valid calendar date!");
        else if(!isOfAge(dob))
            System.out.println("DOB invalid: " + dob + " under 16.");
        else if(cmd[Command.ACCT.getIndex()].equals("CC") && !isOfAge_College(dob))
            System.out.println("DOB invalid: " + dob + " over 24.");
        else if(balance <= 0)
            System.out.println("Initial deposit cannot be 0 or negative.");
        else
            return true;
        return false;
    }

    /**
     * Create an Account object with the command line parameters
     * @param cmd the command line parameters
     * @param holder the Profile holder for the Account
     * @param balance the balance for the Account
     * @return the instantiated Account object
     */
    private Account createAccount(String[] cmd, Profile holder, double balance)
    {
        Account acct = null;
        switch (cmd[Command.ACCT.getIndex()]) {
            case "C":
                acct = new Checking(holder, balance);
                break;
            case "CC":
                int code = Integer.parseInt(cmd[Command.CODE.getIndex()]);
                for (Campus campus : Campus.values()) {
                    if (campus.getCode() == code) {
                        acct = new CollegeChecking(holder, balance, campus);
                    }
                }
                if (acct == null)
                    System.out.println("Invalid campus code.");
                break;
            case "S":
                boolean isLoyal = Integer.parseInt(cmd[Command.CODE.getIndex()]) == 1;
                acct = new Savings(holder, balance, isLoyal);
                break;
            case "MM":
                if (balance < MIN_BALANCE_FOR_NO_FEE_IN_MONEY_MARKET) {
                    System.out.println("Minimum of $2000 to open a Money Market account.");
                    break;
                }
                acct = new MoneyMarket(holder, balance);
                break;
            default:
                System.out.println("Invalid account type.");
                break;
        }
        return acct;
    }

    /**
     * Check if a Profile holder's date of birth is today or in the future
     * @param dob the date of birth to check
     * @return true if the date of birth is today or a future date.
     */
    private boolean isFuture(Date dob) {
        Date today = Date.today();
        boolean result;
        if (dob.getYear() == today.getYear()) { //year == current year
            if (dob.getMonth() == today.getMonth()) {
                result = dob.getDay() > today.getDay();
            }
            else
                result = dob.getMonth() > today.getMonth();
        }
        else
            result = dob.getYear() > today.getYear(); //year > current year

        if(today.equals(dob))
            return true;

        return result;
    }

    /**
     * Check if a Profile holder is of age
     * @param dob the date of birth to check
     * @return true if the holder is of age
     */
    private boolean isOfAge(Date dob) {
        Date today = Date.today();
        int difference = today.getYear() - dob.getYear();
        if (difference == OF_AGE) {
            if (dob.getMonth() > today.getMonth())
                return false;
            else if (dob.getMonth() < today.getMonth())
                return true;
            else
                return dob.getDay() <= today.getDay();
        }
        else
            return difference >= OF_AGE;
    }

    /**
     * Check if a Profile holder is of college age
     * @param dob the date of birth to check
     * @return true if the holder is of college age
     */
    private boolean isOfAge_College(Date dob) {
        if (!isOfAge(dob)) return false;
        Date today = Date.today();
        int difference = today.getYear() - dob.getYear();
        if (difference == OVER_COLLEGE_AGE) {
            if (dob.getMonth() > today.getMonth())
                return true;
            else if (dob.getMonth() < today.getMonth())
                return false;
            else
                return dob.getDay() > today.getDay();
        }
        else
            return today.getYear() - dob.getYear() < OVER_COLLEGE_AGE;
    }

    /**
     * Opens an account with the desired account type.
     * @param event The current input line as a String array of tokens
     */
    @FXML
    protected void onOpenButtonClick(Event event) {
        try {
            Date dob = new Date(dob_OpenClose.toString());
            Profile holder = new Profile(fname_OpenClose.toString(), lname_OpenClose.toString(), dob);
            double balance = Double.parseDouble(initBal_OpenClose.toString());
            Account acct;

            if (acctIsValid(cmd, dob, balance)) {
                acct = createAccount(cmd, holder, balance);
                if (acct != null) {
                    if (acct instanceof Checking) {
                        if (database.contains((Checking) acct, true))
                            System.out.println(acct + " is already in the database.");
                        else if (database.open(acct))
                            System.out.println(acct + " opened.");
                    }
                    else if (database.contains(acct))
                        System.out.println(acct + " is already in the database.");
                    else if (database.open(acct))
                        System.out.println(acct + " opened.");
                }
            }
        }
        catch (NullPointerException e) {
            System.out.println("Missing data for opening an account.");
        }
        catch (NumberFormatException e) {
            System.out.println("Not a valid amount.");
        }
    }
}