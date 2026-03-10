/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.PatientDAO;
import dao.PatientDTO;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class PatientService {
    private final PatientDAO patientDAO = new PatientDAO();

    public PatientDTO getPatientProfile(int userId) throws ErrorMessages.AppException {
        try {
            PatientDTO profile = patientDAO.getPatientByUserId(userId);
            
            if (profile == null) {
                // Ném lỗi 404 (Không tìm thấy bệnh nhân)
                throw new ErrorMessages.AppException(ErrorMessages.PATIENT_NOT_FOUND);
            }
            
            return profile;
            
        } catch (ErrorMessages.AppException e) {
            throw e; // Lỗi 404 thì ném đi tiếp
        } catch (Exception e) {
            e.printStackTrace(); // Log cho Dev
            // Lỗi sập DB, ném 500
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR); 
        }
    }
}
