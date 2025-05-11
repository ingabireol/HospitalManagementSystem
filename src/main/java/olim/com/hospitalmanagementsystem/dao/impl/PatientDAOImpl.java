package olim.com.hospitalmanagementsystem.dao.impl;

import olim.com.hospitalmanagementsystem.dao.PatientDAO;
import olim.com.hospitalmanagementsystem.model.Patient;
import olim.com.hospitalmanagementsystem.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    // SQL statements
    private static final String INSERT_PATIENT = 
            "INSERT INTO Patient (First_Name, Last_Name, Address, Phone_Number) VALUES (?, ?, ?, ?) RETURNING Patient_ID";
    private static final String GET_PATIENT_BY_ID = 
            "SELECT * FROM Patient WHERE Patient_ID = ?";
    private static final String GET_ALL_PATIENTS = 
            "SELECT * FROM Patient ORDER BY Patient_ID";
    private static final String UPDATE_PATIENT = 
            "UPDATE Patient SET First_Name = ?, Last_Name = ?, Address = ?, Phone_Number = ? WHERE Patient_ID = ?";
    private static final String DELETE_PATIENT = 
            "DELETE FROM Patient WHERE Patient_ID = ?";

    @Override
    public int insertPatient(Patient patient) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int patientId = -1;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(INSERT_PATIENT);
            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setString(3, patient.getAddress());
            stmt.setString(4, patient.getPhoneNumber());
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                patientId = rs.getInt(1);
                patient.setPatientId(patientId); // Update the patient object with the generated ID
            }
            
            DatabaseUtil.commitTransaction();
            return patientId;
        } catch (SQLException e) {
            DatabaseUtil.rollbackTransaction();
            throw e;
        } finally {
            close(rs, stmt);
        }
    }

    @Override
    public Patient getPatientById(int patientId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Patient patient = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(GET_PATIENT_BY_ID);
            stmt.setInt(1, patientId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                patient = mapResultSetToPatient(rs);
            }
            
            return patient;
        } finally {
            close(rs, stmt);
        }
    }

    @Override
    public List<Patient> getAllPatients() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Patient> patients = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(GET_ALL_PATIENTS);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            
            return patients;
        } finally {
            close(rs, stmt);
        }
    }

    @Override
    public boolean updatePatient(Patient patient) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(UPDATE_PATIENT);
            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setString(3, patient.getAddress());
            stmt.setString(4, patient.getPhoneNumber());
            stmt.setInt(5, patient.getPatientId());
            
            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0;
            
            if (success) {
                DatabaseUtil.commitTransaction();
            } else {
                DatabaseUtil.rollbackTransaction();
            }
            
            return success;
        } catch (SQLException e) {
            DatabaseUtil.rollbackTransaction();
            throw e;
        } finally {
            close(null, stmt);
        }
    }

    @Override
    public boolean deletePatient(int patientId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(DELETE_PATIENT);
            stmt.setInt(1, patientId);
            
            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0;
            
            if (success) {
                DatabaseUtil.commitTransaction();
            } else {
                DatabaseUtil.rollbackTransaction();
            }
            
            return success;
        } catch (SQLException e) {
            DatabaseUtil.rollbackTransaction();
            throw e;
        } finally {
            close(null, stmt);
        }
    }

    // Helper method to map ResultSet to Patient object
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        return new Patient(
            rs.getInt("Patient_ID"),
            rs.getString("First_Name"),
            rs.getString("Last_Name"),
            rs.getString("Address"),
            rs.getString("Phone_Number")
        );
    }

    // Helper method to close resources
    private void close(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}