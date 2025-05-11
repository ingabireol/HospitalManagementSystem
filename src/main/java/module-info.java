module olim.com.hospitalmanagementsystem {
    // JavaFX dependencies
    requires javafx.controls;
    requires javafx.fxml;

    // JDBC
    requires java.sql;

    // Open packages to FXML
    opens olim.com.hospitalmanagementsystem.view to javafx.fxml;
    opens olim.com.hospitalmanagementsystem.model to javafx.base;

    // Export packages
    exports olim.com.hospitalmanagementsystem;
    exports olim.com.hospitalmanagementsystem.view;
    exports olim.com.hospitalmanagementsystem.model;
}