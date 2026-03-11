/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.PrescriptionDAO;
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
    private final PrescriptionDAO presDAO = new PrescriptionDAO();
    private final PrescriptionItemDAO itemDAO = new PrescriptionItemDAO();

    // 1. NGHIỆP VỤ: Kê đơn thuốc mới (Lưu cả Cha lẫn Con)
    public boolean createFullPrescription(Prescription header, List<PrescriptionItem> items) throws ErrorMessages.AppException {
        try {
            // Bước 1: Lưu Bảng Cha trước, hứng lấy ID
            int newPrescriptionId = presDAO.insertPrescription(header);
            
            if (newPrescriptionId == -1) {
                // Nếu trùng MedicalRecordId, SQL sẽ văng lỗi UNIQUE
                throw new ErrorMessages.AppException(ErrorMessages.PRESCRIPTION_EXISTED);
            }

            // Bước 2: Duyệt qua danh sách thuốc, gán ID cha cho nó rồi lưu vào DB
            for (PrescriptionItem item : items) {
                item.setPrescriptionId(newPrescriptionId);
                itemDAO.insertItem(item);
            }
            
            return true;

        } catch (ErrorMessages.AppException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
    
    // (Lưu ý: Bạn có thể viết thêm hàm getPrescriptionDetail(...) ở đây
    // Bằng cách gọi DAO lấy thông tin bệnh nhân + itemDAO.getItemsByPrescriptionId)
}
