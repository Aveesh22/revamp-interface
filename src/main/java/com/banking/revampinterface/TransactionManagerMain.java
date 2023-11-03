package com.banking.revampinterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class launches the Transaction Manager GUI
 * @author Aveesh Patel, Patryk Dziedzic
 */
public class TransactionManagerMain extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TransactionManagerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 768, 576);
        stage.setTitle("Project 3 - Revamp Interface");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}