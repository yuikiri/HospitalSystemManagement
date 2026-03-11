/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.StaffShiftDAO;
import dao.StaffShiftDTO;
import java.util.List;
import util.ErrorMessages;
import entity.StaffShift;

/**
 *
 * @author Yuikiri
 */
public class StaffShiftService {
    private final StaffShiftDAO staffShiftDAO = new StaffShiftDAO();

    public List<StaffShiftDTO> getStaffShifts(int staffId) throws ErrorMessages.AppException {
        try {
            return staffShiftDAO.getStaffShiftsByStaffId(staffId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    public boolean addStaffShift(StaffShift staffShift) throws ErrorMessages.AppException {
        try {
            boolean isSuccess = staffShiftDAO.insertStaffShift(staffShift);
            if (!isSuccess) {
                throw new ErrorMessages.AppException(ErrorMessages.SHIFT_ASSIGN_ERROR);
            }
            return true;
        } catch (ErrorMessages.AppException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
}
