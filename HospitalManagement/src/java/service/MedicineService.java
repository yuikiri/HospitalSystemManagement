/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.MedicineDAO;
import dao.MedicineDTO;
import java.util.List;
import util.ErrorMessages;
import entity.Medicine;
/**
 *
 * @author Yuikiri
 */
public class MedicineService {
    private MedicineDAO medicineDAO;

    public MedicineService() {
        this.medicineDAO = new MedicineDAO();
    }

    public List<MedicineDTO> getActiveList() {
        return medicineDAO.getAllActiveMedicines();
    }

    public List<MedicineDTO> getListForAdmin() {
        return medicineDAO.getAllMedicinesForAdmin();
    }

    public MedicineDTO getById(int id) {
        return medicineDAO.getMedicineById(id);
    }

    // THÊM THUỐC MỚI
    public boolean createNewMedicine(String name, String unit, double price, int stockQuantity, String description) {
        if (price < 0 || stockQuantity < 0) {
            return false; // Giá và Tồn kho không được âm!
        }
        return medicineDAO.insertMedicine(name, unit, price, stockQuantity, description);
    }

    // SỬA THÔNG TIN THUỐC
    public boolean updateMedicineInfo(int id, String name, String unit, double price, String description) {
        if (price < 0) return false;
        return medicineDAO.updateMedicine(id, name, unit, price, description);
    }

    // NGHIỆP VỤ NHẬP HÀNG (+ Tồn kho)
    public boolean importStock(int id, int quantityToAdded) {
        if (quantityToAdded <= 0) return false;
        return medicineDAO.updateStock(id, quantityToAdded);
    }

    // NGHIỆP VỤ XUẤT THUỐC DỰA TRÊN ĐƠN (- Tồn kho)
    public boolean dispenseMedicine(int id, int quantityToDeduct) {
        MedicineDTO medicine = medicineDAO.getMedicineById(id);
        if (medicine == null || quantityToDeduct <= 0) return false;

        // KHO KHÔNG ĐỦ THUỐC ĐỂ PHÁT
        if (medicine.getStockQuantity() < quantityToDeduct) {
            return false; 
        }

        // Truyền số Âm để trừ tồn kho trong DB
        return medicineDAO.updateStock(id, -quantityToDeduct);
    }

    // ĐÌNH CHỈ / LƯU HÀNH LẠI
    public boolean deactivateMedicine(int id) {
        return medicineDAO.toggleMedicineStatus(id, 0); 
    }

    public boolean activateMedicine(int id) {
        return medicineDAO.toggleMedicineStatus(id, 1); 
    }
    // 1. TÌM KIẾM THUỐC (Dùng cho thanh Search của Admin)
    public List<MedicineDTO> searchMedicines(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return medicineDAO.getAllActiveMedicines();
        }
        return medicineDAO.searchMedicines(keyword);
    }

    // 2. LẤY DANH SÁCH THUỐC TRONG THÙNG RÁC (isActive = 0)
    public List<MedicineDTO> getDeletedMedicines() {
        return medicineDAO.getDeletedMedicines();
    }

    // 3. XÓA MỀM (Chuyển vào thùng rác)
    public boolean softDeleteMedicine(int id) {
        return medicineDAO.toggleMedicineStatus(id, 0);
    }

    // 4. KHÔI PHỤC THUỐC (Từ thùng rác ra danh sách chính)
    public boolean restoreMedicine(int id) {
        return medicineDAO.toggleMedicineStatus(id, 1);
    }
    
    // 5. CẬP NHẬT THÔNG TIN THUỐC (Bản đầy đủ bao gồm cả Stock trực tiếp)
    public boolean updateMedicineFull(int id, String name, String unit, double price, int stock, String desc) {
        if (price < 0 || stock < 0) return false;
        return medicineDAO.updateMedicineFull(id, name, unit, price, stock, desc);
    }
}
