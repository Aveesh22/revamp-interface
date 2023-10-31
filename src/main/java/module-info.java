module com.banking.revampinterface {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.banking.revampinterface to javafx.fxml;
    exports com.banking.revampinterface;
}