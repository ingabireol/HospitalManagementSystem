package olim.com.hospitalmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import olim.com.hospitalmanagementsystem.util.DatabaseUtil;

import java.io.IOException;
import java.sql.Connection;

public class HospitalMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HospitalMain.class.getResource("patient-view.fxml"));
//        FXMLLoader fxmlLoader = FXMLLoader.load(getClass().getResource("/patient-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hospital Management System!");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() {
        // Close the database connection when the application exits
        DatabaseUtil.closeConnection();
    }

    public static void main(String[] args) {
        // Test database connection before launching UI
        try {
            Connection conn = DatabaseUtil.getConnection();
            System.out.println("Database connection successful!");
        } catch (Exception e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }
        launch(args);
    }

}