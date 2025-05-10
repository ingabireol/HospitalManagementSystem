package olim.com.hospitalmanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HospitalMSController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}