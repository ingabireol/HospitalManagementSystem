package olim.com.hospitalmanagementsystem.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import olim.com.hospitalmanagementsystem.model.Patient;
import olim.com.hospitalmanagementsystem.service.PatientService;
import olim.com.hospitalmanagementsystem.service.impl.PatientServiceImpl;
import olim.com.hospitalmanagementsystem.util.DatabaseUtil;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    // FXML Controls for Patient form
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtPhoneNumber;
    @FXML private TextField txtPatientId;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    
    // FXML Controls for Patient table
    @FXML private TableView<Patient> tblPatients;
    @FXML private TableColumn<Patient, Integer> colPatientId;
    @FXML private TableColumn<Patient, String> colFirstName;
    @FXML private TableColumn<Patient, String> colLastName;
    @FXML private TableColumn<Patient, String> colAddress;
    @FXML private TableColumn<Patient, String> colPhoneNumber;
    
    // Service for patient operations
    private PatientService patientService;
    
    // Observable list to hold patients for table
    private ObservableList<Patient> patientList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the patient service
        patientService = new PatientServiceImpl();
        
        // Configure table columns
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        
        // Load patients when the view is initialized
        loadPatients();
        
        // Set up listener for table row selection
        tblPatients.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> displayPatientDetails(newValue));
        
        // Initially disable update and delete buttons
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }
    
    /**
     * Load patients from database and display in table
     */
    private void loadPatients() {
        try {
            // Get all patients from the database
            List<Patient> patients = patientService.findAllPatients();
            
            // Convert to observable list and set to table
            patientList = FXCollections.observableArrayList(patients);
            tblPatients.setItems(patientList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                    "Error loading patients: " + e.getMessage());
        }
    }
    
    /**
     * Display the selected patient's details in the form
     */
    private void displayPatientDetails(Patient patient) {
        if (patient != null) {
            txtPatientId.setText(String.valueOf(patient.getPatientId()));
            txtFirstName.setText(patient.getFirstName());
            txtLastName.setText(patient.getLastName());
            txtAddress.setText(patient.getAddress());
            txtPhoneNumber.setText(patient.getPhoneNumber());
            
            // Enable update and delete buttons
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnSave.setDisable(true);
        }
    }
    
    /**
     * Handle save button action to add a new patient
     */
    @FXML
    private void handleSaveAction(ActionEvent event) {
        if (validateInput()) {
            try {
                // Create a new patient object from form data
                Patient patient = new Patient(
                        txtFirstName.getText(),
                        txtLastName.getText(),
                        txtAddress.getText(),
                        txtPhoneNumber.getText());
                
                // Save to database
                int patientId = patientService.addPatient(patient);
                
                if (patientId > 0) {
                    // Set the generated ID
                    patient.setPatientId(patientId);
                    
                    // Add to the table
                    patientList.add(patient);
                    
                    // Show success message
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                            "Patient saved successfully with ID: " + patientId);
                    
                    // Clear the form
                    clearForm();
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                        "Error saving patient: " + e.getMessage());
            }
        }
    }
    
    /**
     * Handle update button action to update an existing patient
     */
    @FXML
    private void handleUpdateAction(ActionEvent event) {
        if (validateInput()) {
            try {
                // Get the patient ID from the form
                int patientId = Integer.parseInt(txtPatientId.getText());
                
                // Create a patient object with updated data
                Patient patient = new Patient(
                        patientId,
                        txtFirstName.getText(),
                        txtLastName.getText(),
                        txtAddress.getText(),
                        txtPhoneNumber.getText());
                
                // Update in database
                boolean success = patientService.updatePatient(patient);
                
                if (success) {
                    // Update in the table
                    int index = findPatientIndex(patientId);
                    if (index >= 0) {
                        patientList.set(index, patient);
                    }
                    
                    // Show success message
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                            "Patient updated successfully");
                    
                    // Clear the form
                    clearForm();
                } else {
                    showAlert(Alert.AlertType.WARNING, "Warning", 
                            "Failed to update patient");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                        "Error updating patient: " + e.getMessage());
            }
        }
    }
    
    /**
     * Handle delete button action to delete a patient
     */
    @FXML
    private void handleDeleteAction(ActionEvent event) {
        try {
            // Get the patient ID from the form
            int patientId = Integer.parseInt(txtPatientId.getText());
            
            // Confirm deletion
            Optional<ButtonType> result = showConfirmationDialog(
                    "Delete Confirmation", 
                    "Are you sure you want to delete this patient?");
            
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete from database
                boolean success = patientService.removePatient(patientId);
                
                if (success) {
                    // Remove from the table
                    int index = findPatientIndex(patientId);
                    if (index >= 0) {
                        patientList.remove(index);
                    }
                    
                    // Show success message
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                            "Patient deleted successfully");
                    
                    // Clear the form
                    clearForm();
                } else {
                    showAlert(Alert.AlertType.WARNING, "Warning", 
                            "Failed to delete patient");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                    "Error deleting patient: " + e.getMessage());
        }
    }
    
    /**
     * Handle clear button action to reset the form
     */
    @FXML
    private void handleClearAction(ActionEvent event) {
        clearForm();
    }
    
    /**
     * Clear the form and reset button states
     */
    private void clearForm() {
        txtPatientId.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtAddress.clear();
        txtPhoneNumber.clear();
        
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setDisable(false);
        
        tblPatients.getSelectionModel().clearSelection();
    }
    
    /**
     * Validate form input
     * @return true if input is valid, false otherwise
     */
    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (txtFirstName.getText().trim().isEmpty()) {
            errorMessage.append("First name cannot be empty\n");
        }
        
        if (txtLastName.getText().trim().isEmpty()) {
            errorMessage.append("Last name cannot be empty\n");
        }
        
        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                    errorMessage.toString());
            return false;
        }
        
        return true;
    }
    
    /**
     * Find the index of a patient in the observable list
     * @param patientId the ID to find
     * @return the index of the patient, or -1 if not found
     */
    private int findPatientIndex(int patientId) {
        for (int i = 0; i < patientList.size(); i++) {
            if (patientList.get(i).getPatientId() == patientId) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Show an alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show a confirmation dialog and return the result
     */
    private Optional<ButtonType> showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}