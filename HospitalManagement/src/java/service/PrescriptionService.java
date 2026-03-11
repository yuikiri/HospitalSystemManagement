/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.PrescriptionDAO;
import dao.PrescriptionDTO;
import dao.PrescriptionItemDAO;
import java.util.List;
import entity.Prescription;
import entity.PrescriptionItem;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class PrescriptionService {
    private PrescriptionDAO prescriptionDAO;

    public PrescriptionService() {
        this.prescriptionDAO = new PrescriptionDAO();
    }

    public List<PrescriptionDTO> getActiveList() {
        return prescriptionDAO.getAllActivePrescriptions();
    }

    public List<PrescriptionDTO> getListForAdmin() {
        return prescriptionDAO.getAllPrescriptionsForAdmin();
    }

    public PrescriptionDTO getByMedicalRecordId(int medicalRecordId) {
        return prescriptionDAO.getByMedicalRecordId(medicalRecordId);
    }

    // TẠO ĐƠN THUỐC MỚI
    public int createNewPrescription(int medicalRecordId, String notes) {
        // RÀNG BUỘC UNIQUE: Một bệnh án chỉ được có MỘT đơn thuốc (Vỏ bọc ngoài)
        if (prescriptionDAO.getByMedicalRecordId(medicalRecordId) != null) {
            return -1; // Đã tồn tại đơn thuốc cho lần khám này!
        }
        
        // Trả về ID của đơn thuốc để Controller tiếp tục gọi hàm thêm chi tiết từng viên thuốc (PrescriptionItems)
        return prescriptionDAO.insertPrescription(medicalRecordId, notes);
    }

    // CẬP NHẬT TRẠNG THÁI (Quy trình cấp phát thuốc)
    public boolean markAsDispensed(int id) {
        // Gọi khi nhân viên Kho Dược đã giao thuốc cho bệnh nhân
        return prescriptionDAO.updatePrescriptionStatus(id, "dispensed");
    }

    // BẬT / TẮT (XÓA MỀM)
    public boolean deactivatePrescription(int id) {
        return prescriptionDAO.togglePrescriptionActive(id, 0); 
    }

    public boolean activatePrescription(int id) {
        return prescriptionDAO.togglePrescriptionActive(id, 1); 
    }
}
