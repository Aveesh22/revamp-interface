package com.banking.revampinterface;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

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
    private CheckBox isLoyalCheck;
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
    private ToggleGroup campus;
    @FXML
    private RadioButton campus_NB;
    @FXML
    private RadioButton campus_Newark;
    @FXML
    private RadioButton campus_Camden;


    @FXML
    private TextField fname_DW;
    @FXML
    private TextField lname_DW;
    @FXML
    private DatePicker dob_DW;
    @FXML
    private TextField amount_DW;
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

    @FXML
    private Button loadAccountsBtn;


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
     * Create an Account object to open and add to the database
     * @return the instantiated Account object
     */
    private Account createAccount_Open() {
        Account acct = null;
        Date dob = new Date(dob_OC.getValue().format(DateTimeFormatter.ofPattern(dateFormat)));
        Profile holder = new Profile(fname_OC.getText(), lname_OC.getText(), dob);
        double balance = Double.parseDouble(initBal_OC.getText());

        try {
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
                    acct = new Savings(holder, balance, isLoyalCheck.isSelected());
                else if (acct_MoneyMarket_OC.isSelected()) {
                    if (balance < MIN_BALANCE_FOR_NO_FEE_IN_MONEY_MARKET) {
                        outputText.setText("Minimum of $2000 to open a Money Market account.");
                    } else
                        acct = new MoneyMarket(holder, balance);
                } else
                    outputText.setText("Invalid account type.");
            }
        } catch (DateTimeParseException e) {
            outputText.setText("DOB invalid: " + dob_OC.getValue().toString() + " not a valid calendar date!");
        }
        return acct;
    }

    /**
     * Create an Account object to open and add to the database given a command
     * @return the instantiated Account object
     */
    private Account createAccount_Open(String[] cmd) {
        Account acct = null;
        Date dob = new Date(cmd[Command.DOB.getIndex()]);
        Profile holder = new Profile(cmd[Command.FNAME.getIndex()], cmd[Command.LNAME.getIndex()], dob);
        double balance = Double.parseDouble(cmd[Command.MONEY.getIndex()]);

        try {
            if (acctIsValid(dob, balance)) {
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
                            outputText.setText("Invalid campus code.");
                        break;
                    case "S":
                        boolean isLoyal = Integer.parseInt(cmd[Command.CODE.getIndex()]) == 1;
                        acct = new Savings(holder, balance, isLoyal);
                        break;
                    case "MM":
                        if (balance < MIN_BALANCE_FOR_NO_FEE_IN_MONEY_MARKET) {
                            outputText.setText("Minimum of $2000 to open a Money Market account.");
                            break;
                        }
                        acct = new MoneyMarket(holder, balance);
                        break;
                    default:
                        outputText.setText("Invalid account type.");
                        break;
                }
            }
        } catch (DateTimeParseException e) {
            outputText.setText("DOB invalid: " + dob_OC.getValue().toString() + " not a valid calendar date!");
        }
        return acct;
    }

    /**
     * Create an Account object with a Profile and default values to search for to close
     * @return the instantiated Account object
     */
    private Account createAccount_Close()
    {
        Account acct = null;
        Date dob = new Date(dob_OC.getValue().format(DateTimeFormatter.ofPattern(dateFormat)));
        Profile holder = new Profile(fname_OC.getText(), lname_OC.getText(), dob);

        try {
            if (acct_Checking_OC.isSelected())
                acct = new Checking(holder);
            else if (acct_CollegeChecking_OC.isSelected())
                acct = new CollegeChecking(holder);
            else if (acct_Savings_OC.isSelected())
                acct = new Savings(holder);
            else if (acct_MoneyMarket_OC.isSelected())
                acct = new MoneyMarket(holder);
            else
                outputText.setText("Invalid account type.");
        } catch (DateTimeParseException e) {
            outputText.setText("DOB invalid: " + dob_OC.getValue().toString() + " not a valid calendar date!");
        }
        return acct;
    }

    /**
     * Create an Account object with the GUI parameters to deposit/withdraw from
     * @return the instantiated Account object
     */
    private Account createAccount_DW()
    {
        Account acct = null;
        Date dob = new Date(dob_DW.getValue().format(DateTimeFormatter.ofPattern(dateFormat)));
        Profile holder = new Profile(fname_DW.getText(), lname_DW.getText(), dob);
        double balance = Double.parseDouble(amount_DW.getText());

        try {
            if (balance <= 0)
                outputText.setText("Amount cannot be 0 or negative.");
            else if (isFuture(dob))
                outputText.setText("DOB invalid: " + dob + " cannot be today or a future day.");
            else if (!dob.isValid())
                outputText.setText("DOB invalid: " + dob + " not a valid calendar date!");
            else {
                if (acct_Checking_DW.isSelected())
                    acct = new Checking(holder, balance);
                else if (acct_CollegeChecking_DW.isSelected())
                    acct = new CollegeChecking(holder, balance);
                else if (acct_Savings_DW.isSelected())
                    acct = new Savings(holder, balance);
                else if (acct_MoneyMarket_DW.isSelected())
                    acct = new MoneyMarket(holder, balance);
                else
                    outputText.setText("Invalid account type.");
            }
        } catch (DateTimeParseException e) {
            outputText.setText("DOB invalid: " + dob_OC.getValue().toString() + " not a valid calendar date!");
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
     * @param event The click event
     */
    @FXML
    protected void onOpenButtonClick(Event event) {
        try {
            Account acct = createAccount_Open();
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
            outputText.setText(outputText.getText() + "\n" + "Account is not valid.");
        }
        catch (ArrayIndexOutOfBoundsException e) {
            outputText.setText("ArrayIndexOutOfBoundsException thrown.");
        }
        catch (NumberFormatException e) {
            outputText.setText(outputText.getText() + "\n" + "Not a valid amount.");
        }
    }

    /**
     * Close an existing account and remove it from the database.
     * @param event The click event
     */
    @FXML
    protected void onCloseButtonClick(Event event) {
        try {
            Account acct = createAccount_Close();
            if (acct == null) throw new NullPointerException();
            if (!database.contains(acct))
                outputText.setText(acct + " is not in the database.");
            else {
                if (database.close(acct))
                    outputText.setText(acct + " has been closed.");
            }
        }
        catch (NullPointerException e) {
            outputText.setText(outputText.getText() + "\n" + "Account is not valid.");
        }
        catch (ArrayIndexOutOfBoundsException e) {
            outputText.setText("ArrayIndexOutOfBoundsException thrown.");
        }
        catch (NumberFormatException e) {
            outputText.setText(outputText.getText() + "\n" + "Not a valid amount.");
        }
    }

    /**
     * Clear the GUI fields
     * @param event The click event
     */
    @FXML
    protected void onClearButtonClick(Event event) {
        fname_OC.setText("");
        lname_OC.setText("");
        dob_OC.setValue(null);
        isLoyalCheck.setSelected(false);
        if (accounts_OC.getSelectedToggle() != null)
            accounts_OC.getSelectedToggle().setSelected(false);
        if (campus.getSelectedToggle() != null)
            campus.getSelectedToggle().setSelected(false);
        isLoyalCheck.setDisable(true);
        campus_NB.setDisable(true);
        campus_Newark.setDisable(true);
        campus_Camden.setDisable(true);
        initBal_OC.setText("");
    }

    /**
     * Deposit money into an existing account.
     * @param event The click event
     */
    @FXML
    protected void onDepositButtonClick(Event event) {
        try {
            Account acct = createAccount_DW();
            if (acct == null) throw new NullPointerException();
            if (!database.contains(acct))
                outputText.setText(acct + " is not in the database.");
            else {
                database.deposit(acct);
                outputText.setText(acct + " Deposit - balance updated.");
            }
        }
        catch (NullPointerException e) {
            outputText.setText(outputText.getText() + "\n" + "Account is not valid.");
        }
        catch (ArrayIndexOutOfBoundsException e) {
            outputText.setText("ArrayIndexOutOfBoundsException thrown.");
        }
        catch (NumberFormatException e) {
            outputText.setText(outputText.getText() + "\n" + "Not a valid amount.");
        }
    }

    /**
     * Withdraw money from an existing account.
     * @param event The click event
     */
    @FXML
    protected void onWithdrawButtonClick(Event event) {
        try {
            Account acct = createAccount_DW();
            if (acct == null) throw new NullPointerException();
            if (!database.contains(acct))
                outputText.setText(acct + " is not in the database.");
            else {
                if (database.withdraw(acct))
                    outputText.setText(acct + " Withdraw - balance updated.");
                else
                    outputText.setText(acct + " Withdraw - insufficient fund.");
            }
        }
        catch (NullPointerException e) {
            outputText.setText(outputText.getText() + "\n" + "Account is not valid.");
        }
        catch (ArrayIndexOutOfBoundsException e) {
            outputText.setText("ArrayIndexOutOfBoundsException thrown.");
        }
        catch (NumberFormatException e) {
            outputText.setText(outputText.getText() + "\n" + "Not a valid amount.");
        }
    }

    /**
     * Print All Accounts
     * @param event The click event
     */
    @FXML
    protected void onPButtonClick(Event event) {
        outputText.setText("*Accounts sorted by account type and profile.\n" +
                database.printSorted() +
                "*end of list.");
    }

    /**
     * Print Interests and Fees
     * @param event The click event
     */
    @FXML
    protected void onPIButtonClick(Event event) {
        outputText.setText("*list of accounts with fee and monthly interest\n" +
                database.printFeesAndInterests() +
                "*end of list.");
    }

    /**
     * Update Accounts with Interests and Fees
     * @param event The click event
     */
    @FXML
    protected void onUBButtonClick(Event event) {
        outputText.setText("*list of accounts with fees and interests applied.\n" +
                database.printUpdatedBalances() +
                "*end of list.");
    }

    /**
     * Load Accounts from File
     * @param event The click event
     */
    @FXML
    protected void onLoadAccountsButtonClick(Event event) {
        FileChooser fileChooser = new FileChooser();
        try {
            File file = fileChooser.showOpenDialog(loadAccountsBtn.getScene().getWindow());
            Scanner scanner = new Scanner(file);
            String currLine;
            while (scanner.hasNextLine()) {
                currLine = scanner.nextLine();
                String[] cmd = currLine.split(",");
                if (cmd.length > 1) {
                    Account acct = createAccount_Open(cmd);
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
            }
            outputText.setText("Accounts loaded.");
        } catch (NullPointerException e) {
            outputText.setText(outputText.getText() + "\n" + "Account is not valid.");
        } catch (ArrayIndexOutOfBoundsException e) {
            outputText.setText("ArrayIndexOutOfBoundsException thrown.");
        } catch (NumberFormatException e) {
            outputText.setText(outputText.getText() + "\n" + "Not a valid amount.");
        } catch (Exception e) {
            outputText.setText("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Initialize the GUI with default values and add a listener for the toggles.
     */
    public void initialize()
    {
        isLoyalCheck.setDisable(true);
        campus_NB.setDisable(true);
        campus_Newark.setDisable(true);
        campus_Camden.setDisable(true);

        accounts_OC.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                if (newToggle.equals(acct_Checking_OC) || newToggle.equals(acct_CollegeChecking_OC)) {
                    isLoyalCheck.setDisable(true);
                    if (newToggle.equals(acct_CollegeChecking_OC)) {
                        campus_NB.setDisable(false);
                        campus_Newark.setDisable(false);
                        campus_Camden.setDisable(false);
                    }
                } else {
                    isLoyalCheck.setDisable(false);
                    campus_NB.setDisable(true);
                    campus_Newark.setDisable(true);
                    campus_Camden.setDisable(true);
                }
            }
        });
    }

}