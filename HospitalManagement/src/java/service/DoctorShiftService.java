/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DoctorShiftDAO;
import dao.DoctorShiftDTO;
import java.util.List;
import util.ErrorMessages;
import entity.DoctorShift;
/**
 *
 * @author Yuikiri
 */
public class DoctorShiftService {
    private final DoctorShiftDAO doctorShiftDAO = new DoctorShiftDAO();

    public List<DoctorShiftDTO> getDoctorShifts(int doctorId) throws ErrorMessages.AppException {
        try {
            return doctorShiftDAO.getDoctorShiftsByDoctorId(doctorId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    public boolean addDoctorShift(DoctorShift doctorShift) throws ErrorMessages.AppException {
        try {
            boolean isSuccess = doctorShiftDAO.insertDoctorShift(doctorShift);
            if (!isSuccess) {
                // Sẽ nhảy vào đây nếu vi phạm UQ_Doctor_Shift trong SQL
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
