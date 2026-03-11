/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.PrescriptionItemDAO;
import dao.PrescriptionItemDTO;
import java.util.List;

/**
 *
 * @author Yuikiri
 */
public class PrescriptionItemService {
    private PrescriptionItemDAO itemDAO;

    public PrescriptionItemService() {
        this.itemDAO = new PrescriptionItemDAO();
    }

    // Lấy chi tiết đơn thuốc để in ra giấy hoặc hiển thị
    public List<PrescriptionItemDTO> getDetailsOfPrescription(int prescriptionId) {
        return itemDAO.getItemsByPrescriptionId(prescriptionId);
    }

    // Kê thêm 1 món thuốc
    public boolean addMedicineToPrescription(int prescriptionId, int medicineId, int quantity, String dosage, String frequency, String duration) {
        if (quantity <= 0) return false;
        
        // Thường Bác sĩ sẽ gõ: "1 viên", "Sáng, Tối", "5 ngày"
        return itemDAO.insertItem(prescriptionId, medicineId, quantity, dosage, frequency, duration);
    }

    // Gỡ thuốc nếu bác sĩ đổi ý (Chỉ cho phép gỡ khi đơn thuốc chưa bị phát)
    public boolean removeMedicine(int itemId) {
        return itemDAO.deleteItem(itemId);
    }
}
