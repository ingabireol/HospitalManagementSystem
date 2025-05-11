package olim.com.hospitalmanagementsystem.service.impl;

import olim.com.hospitalmanagementsystem.dao.PatientDAO;
import olim.com.hospitalmanagementsystem.dao.impl.PatientDAOImpl;
import olim.com.hospitalmanagementsystem.model.Patient;
import olim.com.hospitalmanagementsystem.service.PatientService;

import java.sql.SQLException;
import java.util.List;

public class PatientServiceImpl implements PatientService {
    
    private PatientDAO patientDAO;
    
    public PatientServiceImpl() {
        this.patientDAO = new PatientDAOImpl();
    }
    
    @Override
    public int addPatient(Patient patient) throws SQLException {
        // Validate patient data
        if (patient.getFirstName() == null || patient.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient first name cannot be empty");
        }
        if (patient.getLastName() == null || patient.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient last name cannot be empty");
        }
        
        return patientDAO.insertPatient(patient);
    }

    @Override
    public Patient findPatientById(int patientId) throws SQLException {
        if (patientId <= 0) {
            throw new IllegalArgumentException("Invalid patient ID: " + patientId);
        }
        return patientDAO.getPatientById(patientId);
    }

    @Override
    public List<Patient> findAllPatients() throws SQLException {
        return patientDAO.getAllPatients();
    }

    @Override
    public boolean updatePatient(Patient patient) throws SQLException {
        // Validate patient data
        if (patient.getPatientId() <= 0) {
            throw new IllegalArgumentException("Invalid patient ID: " + patient.getPatientId());
        }
        if (patient.getFirstName() == null || patient.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient first name cannot be empty");
        }
        if (patient.getLastName() == null || patient.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient last name cannot be empty");
        }
        
        return patientDAO.updatePatient(patient);
    }

    @Override
    public boolean removePatient(int patientId) throws SQLException {
        if (patientId <= 0) {
            throw new IllegalArgumentException("Invalid patient ID: " + patientId);
        }
        return patientDAO.deletePatient(patientId);
    }
}