package olim.com.hospitalmanagementsystem.dao;

import olim.com.hospitalmanagementsystem.model.Patient;
import java.sql.SQLException;
import java.util.List;

public interface PatientDAO {
    /**
     * Insert a new patient into the database
     * @param patient Patient object to be inserted
     * @return the generated patient ID
     * @throws SQLException if a database access error occurs
     */
    int insertPatient(Patient patient) throws SQLException;
    
    /**
     * Get a patient by ID
     * @param patientId the ID of the patient to retrieve
     * @return Patient object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    Patient getPatientById(int patientId) throws SQLException;
    
    /**
     * Get all patients from the database
     * @return List of Patient objects
     * @throws SQLException if a database access error occurs
     */
    List<Patient> getAllPatients() throws SQLException;
    
    /**
     * Update an existing patient in the database
     * @param patient Patient object with updated information
     * @return true if successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    boolean updatePatient(Patient patient) throws SQLException;
    
    /**
     * Delete a patient from the database
     * @param patientId the ID of the patient to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    boolean deletePatient(int patientId) throws SQLException;
}