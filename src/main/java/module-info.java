module olim.com.hospitalmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens olim.com.hospitalmanagementsystem to javafx.fxml;
    exports olim.com.hospitalmanagementsystem;
}