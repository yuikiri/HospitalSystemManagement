/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.MedicineDAO;
import java.util.List;
import util.ErrorMessages;
import entity.Medicine;
/**
 *
 * @author Yuikiri
 */
public class MedicineService {
    private final MedicineDAO medicineDAO = new MedicineDAO();

    // Lấy toàn bộ danh sách thuốc
    public List<Medicine> getAllMedicines() throws ErrorMessages.AppException {
        try {
            return medicineDAO.getAllMedicines();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // Lấy chi tiết 1 loại thuốc (Kiểm tra 404)
    public Medicine getMedicineById(int id) throws ErrorMessages.AppException {
        try {
            Medicine medicine = medicineDAO.getMedicineById(id);
            
            if (medicine == null) {
                // Ném lỗi 404 nếu ID thuốc không hợp lệ
                throw new ErrorMessages.AppException(ErrorMessages.MEDICINE_NOT_FOUND);
            }
            
            return medicine;
            
        } catch (ErrorMessages.AppException e) {
            throw e; // Ném tiếp lỗi 404
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
}
