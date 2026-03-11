/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.MedicalRecordDAO;
import util.ErrorMessages;
import dao.MedicalRecordDTO;
import entity.MedicalRecord;
import java.util.List;


/**
 *
 * @author Yuikiri
 */
public class MedicalRecordService {
    private MedicalRecordDAO medicalRecordDAO;

    public MedicalRecordService() {
        this.medicalRecordDAO = new MedicalRecordDAO();
    }

    public List<MedicalRecordDTO> getActiveList() {
        return medicalRecordDAO.getAllActiveMedicalRecords();
    }

    public List<MedicalRecordDTO> getListForAdmin() {
        return medicalRecordDAO.getAllMedicalRecordsForAdmin();
    }

    public MedicalRecordDTO getRecordByAppointmentId(int appointmentId) {
        return medicalRecordDAO.getByAppointmentId(appointmentId);
    }

    // TẠO HỒ SƠ BỆNH ÁN
    public boolean createNewRecord(int appointmentId, String diagnosis, String notes) {
        // RÀNG BUỘC CỰC KỲ QUAN TRỌNG: Check UNIQUE appointmentId
        // Tránh tình trạng Bác sĩ bấm "Lưu" 2 lần tạo ra 2 bệnh án trùng lặp cho 1 lần khám
        if (medicalRecordDAO.getByAppointmentId(appointmentId) != null) {
            return false; // Lịch hẹn này đã có bệnh án rồi, không được tạo thêm!
        }
        
        return medicalRecordDAO.insertMedicalRecord(appointmentId, diagnosis, notes);
    }

    // CẬP NHẬT
    public boolean updateRecord(int id, String diagnosis, String notes) {
        return medicalRecordDAO.updateMedicalRecord(id, diagnosis, notes);
    }

    // BẬT / TẮT (XÓA MỀM)
    public boolean deactivateRecord(int id) {
        return medicalRecordDAO.toggleMedicalRecordStatus(id, 0); 
    }

    public boolean activateRecord(int id) {
        return medicalRecordDAO.toggleMedicalRecordStatus(id, 1); 
    }
}
