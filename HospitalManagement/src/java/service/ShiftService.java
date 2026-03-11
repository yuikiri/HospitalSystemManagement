/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.ShiftDAO;
import dao.ShiftDTO;
import java.util.List;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class ShiftService {
    private final ShiftDAO shiftDAO = new ShiftDAO();

    // Lấy toàn bộ ca trực (Ví dụ: cho màn hình quản lý lịch trực của Admin)
    public List<ShiftDTO> getAllShifts() throws ErrorMessages.AppException {
        try {
            return shiftDAO.getAllShifts();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // Lấy chi tiết 1 ca trực
    public ShiftDTO getShiftById(int id) throws ErrorMessages.AppException {
        try {
            ShiftDTO shift = shiftDAO.getShiftById(id);
            
            if (shift == null) {
                // Ném lỗi 404
                throw new ErrorMessages.AppException(ErrorMessages.SHIFT_NOT_FOUND);
            }
            
            return shift;
            
        } catch (ErrorMessages.AppException e) {
            throw e; 
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
}
