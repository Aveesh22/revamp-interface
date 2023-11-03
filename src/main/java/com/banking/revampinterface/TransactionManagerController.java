package com.banking.revampinterface;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;

/**
 * This class defines the Controller for the Transaction Manager
 * @author Aveesh Patel, Patryk Dziedzic
 */
public class TransactionManagerController
{
    @FXML
    private TextArea outputText;
    @FXML
    private TextField fname_OC;
    @FXML
    private TextField lname_OC;
    @FXML
    private DatePicker dob_OC;
    @FXML
    private TextField initBal_OC;
    @FXML
    private CheckBox isLoyal;

    @FXML
    private ToggleGroup campus;
    @FXML
    private RadioButton campus_NB;
    @FXML
    private RadioButton campus_Newark;
    @FXML
    private RadioButton campus_Camden;

    @FXML
    private ToggleGroup accounts_OC;
    @FXML
    private RadioButton acct_Checking_OC;
    @FXML
    private RadioButton acct_CollegeChecking_OC;
    @FXML
    private RadioButton acct_Savings_OC;
    @FXML
    private RadioButton acct_MoneyMarket_OC;

    @FXML
    private ToggleGroup accounts_DW;
    @FXML
    private RadioButton acct_Checking_DW;
    @FXML
    private RadioButton acct_CollegeChecking_DW;
    @FXML
    private RadioButton acct_Savings_DW;
    @FXML
    private RadioButton acct_MoneyMarket_DW;


    private static final int MIN_BALANCE_FOR_NO_FEE_IN_MONEY_MARKET = 2000;
    private static final int OF_AGE = 16;
    private static final int OVER_COLLEGE_AGE = 24;
    private static final String dateFormat = "MM/dd/yyyy";
    private AccountDatabase database = new AccountDatabase();


    /**
     * Check if the account is valid
     * @param dob the date of birth of the Profile holder
     * @param balance the balance of the account
     * @return true or false dependent on if the account is valid or not
     */
    private boolean acctIsValid(Date dob, double balance) {
        if(isFuture(dob))
            outputText.setText("DOB invalid: " + dob + " cannot be today or a future day.");
        else if(!dob.isValid())
            outputText.setText("DOB invalid: " + dob + " not a valid calendar date!");
        else if(!isOfAge(dob))
            outputText.setText("DOB invalid: " + dob + " under 16.");
        else if(acct_CollegeChecking_OC.isSelected() && !isOfAge_College(dob))
            outputText.setText("DOB invalid: " + dob + " over 24.");
        else if(balance <= 0)
            outputText.setText("Initial deposit cannot be 0 or negative.");
        else
            return true;
        return false;
    }

    /**
     * Create an Account object with the command line parameters
     * @return the instantiated Account object
     */
    private Account createAccount() {
        Account acct = null;
        Date dob = new Date(dob_OC.getValue().format(DateTimeFormatter.ofPattern(dateFormat)));
        Profile holder = new Profile(fname_OC.getText(), lname_OC.getText(), dob);
        double balance = Double.parseDouble(initBal_OC.getText());

        if (acctIsValid(dob, balance)) {
            if (acct_Checking_OC.isSelected())
                acct = new Checking(holder, balance);
            else if (acct_CollegeChecking_OC.isSelected()) {
                if (campus_NB.isSelected() && !campus_NB.isDisabled())
                    acct = new CollegeChecking(holder, balance, Campus.NEW_BRUNSWICK);
                else if (campus_Newark.isSelected() && !campus_Newark.isDisabled())
                    acct = new CollegeChecking(holder, balance, Campus.NEWARK);
                else if (campus_Camden.isSelected() && !campus_Camden.isDisabled())
                    acct = new CollegeChecking(holder, balance, Campus.CAMDEN);
                else
                    outputText.setText("Invalid campus code.");
            } else if (acct_Savings_OC.isSelected())
                acct = new Savings(holder, balance, isLoyal.isSelected());
            else if (acct_MoneyMarket_OC.isSelected()) {
                if (balance < MIN_BALANCE_FOR_NO_FEE_IN_MONEY_MARKET) {
                    outputText.setText("Minimum of $2000 to open a Money Market account.");
                } else
                    acct = new MoneyMarket(holder, balance);
            } else
                outputText.setText("Invalid account type.");
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
            Account acct = createAccount();
            if (acct == null) throw new NullPointerException();
            if (acct instanceof Checking) {
                if (database.contains((Checking) acct, true))
                    outputText.setText(acct + " is already in the database.");
                else if (database.open(acct))
                    outputText.setText(acct + " opened.");
            }
            else if (database.contains(acct))
                outputText.setText(acct + " is already in the database.");
            else if (database.open(acct))
                outputText.setText(acct + " opened.");
        }
        catch (NullPointerException e) {
            outputText.setText(outputText.getText() + "\n" +
                    "Missing data for opening an account or account is not valid.");
        }
        catch (ArrayIndexOutOfBoundsException e) {
            outputText.setText("ArrayIndexOutOfBoundsException thrown.");
        }
        catch (NumberFormatException e) {
            outputText.setText(outputText.getText() + "\n" + "Not a valid amount.");
        }
    }

    public void initialize()
    {
        /*
        campus_NB.setDisable(true);
        campus_Newark.setDisable(true);
        campus_Camden.setDisable(true);

        campus.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == acct_CollegeChecking_OC) {
                campus_NB.setDisable(false);
                campus_Newark.setDisable(false);
                campus_Camden.setDisable(false);
            } else {
                campus_NB.setDisable(true);
                campus_Newark.setDisable(true);
                campus_Camden.setDisable(true);
            }
        });*/
    }

}