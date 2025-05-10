module olim.com.hospitalmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens olim.com.hospitalmanagementsystem to javafx.fxml;
    exports olim.com.hospitalmanagementsystem;
}