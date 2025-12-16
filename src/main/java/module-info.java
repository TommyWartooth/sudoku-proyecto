module com.example {
     requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    opens com.example.controllers to javafx.fxml;

    exports com.example;
}
