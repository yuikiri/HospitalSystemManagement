/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.StaffDAO;
import dao.StaffDTO;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class StaffService {
    private final StaffDAO staffDAO = new StaffDAO();

    public StaffDTO getStaffProfile(int userId) throws ErrorMessages.AppException {
        try {
            StaffDTO profile = staffDAO.getStaffByUserId(userId);
            
            if (profile == null) {
                // Ném lỗi 404 (Không tìm thấy nhân viên)
                throw new ErrorMessages.AppException(ErrorMessages.STAFF_NOT_FOUND);
            }
            
            return profile;
            
        } catch (ErrorMessages.AppException e) {
            throw e; // Ném tiếp lỗi nghiệp vụ (404) để Controller bắt
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi cho Backend Developer
            // Ném lỗi 500 nếu sập Database
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR); 
        }
    }
}
