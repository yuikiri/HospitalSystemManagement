/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DoctorDAO;
import dao.DoctorDTO;
import entity.Doctor;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class DoctorService {
    private final DoctorDAO doctorDAO = new DoctorDAO();

    public DoctorDTO getDoctorProfile(int userId) throws ErrorMessages.AppException {
        try {
            // SỬA Ở ĐÂY: Khai báo đúng kiểu DoctorDTO để hứng kết quả từ DAO
            DoctorDTO profile = doctorDAO.getDoctorByUserId(userId);
            
            if (profile == null) {
                // Ném lỗi 404 từ class hệ thống lỗi của bạn
                throw new ErrorMessages.AppException(ErrorMessages.DOCTOR_NOT_FOUND);
            }
            
            // Trả về thẳng profile (vì nó đã là DTO rồi)
            return profile;
            
        } catch (ErrorMessages.AppException e) {
            throw e; // Lỗi nghiệp vụ (404) thì ném đi tiếp
        } catch (Exception e) {
            e.printStackTrace(); // Log cho Dev
            // Lỗi sập Database (NullPointer, SQLException...)
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR); 
        }
    }
}
