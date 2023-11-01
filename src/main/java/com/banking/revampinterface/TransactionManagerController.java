package com.banking.revampinterface;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * This class defines the Controller for the Transaction Manager
 * @author Aveesh Patel, Patryk Dziedzic
 */
public class TransactionManagerController
{
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick()
    {
        welcomeText.setText("Welcome to your JavaFX Application!");
    }
}