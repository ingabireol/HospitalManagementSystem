package olim.com.hospitalmanagementsystem.service;

import olim.com.hospitalmanagementsystem.model.Patient;
import java.sql.SQLException;
import java.util.List;

public interface PatientService {
    /**
     * Add a new patient
     * @param patient Patient to be added
     * @return the generated patient ID
     * @throws SQLException if a database access error occurs
     */
    int addPatient(Patient patient) throws SQLException;
    
    /**
     * Find a patient by ID
     * @param patientId the ID of the patient to find
     * @return Patient if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    Patient findPatientById(int patientId) throws SQLException;
    
    /**
     * Get all patients
     * @return List of all patients
     * @throws SQLException if a database access error occurs
     */
    List<Patient> findAllPatients() throws SQLException;
    
    /**
     * Update an existing patient
     * @param patient Patient with updated information
     * @return true if successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    boolean updatePatient(Patient patient) throws SQLException;
    
    /**
     * Remove a patient by ID
     * @param patientId the ID of the patient to remove
     * @return true if successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    boolean removePatient(int patientId) throws SQLException;
}