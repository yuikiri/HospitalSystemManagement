/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.MedicalRecordDAO;
import util.ErrorMessages;
import dao.MedicalRecordDTO;
import entity.MedicalRecord;


/**
 *
 * @author Yuikiri
 */
public class MedicalRecordService {
    private final MedicalRecordDAO recordDAO = new MedicalRecordDAO();

    // Lấy chi tiết bệnh án (Ví dụ: khi Bác sĩ/Bệnh nhân bấm vào 1 ca khám đã hoàn thành)
    public MedicalRecordDTO getRecordByAppointment(int appointmentId) throws ErrorMessages.AppException {
        try {
            MedicalRecordDTO record = recordDAO.getRecordByAppointmentId(appointmentId);
            if (record == null) {
                throw new ErrorMessages.AppException(ErrorMessages.MEDICAL_RECORD_NOT_FOUND);
            }
            return record;
        } catch (ErrorMessages.AppException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // Bác sĩ lập bệnh án mới sau khi khám xong
    public boolean createRecord(MedicalRecord mr) throws ErrorMessages.AppException {
        try {
            boolean isSuccess = recordDAO.insertMedicalRecord(mr);
            if (!isSuccess) {
                // Sẽ nhảy vào đây nếu vi phạm UNIQUE (appointmentId) trong SQL
                throw new ErrorMessages.AppException(ErrorMessages.MEDICAL_RECORD_EXISTED);
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
