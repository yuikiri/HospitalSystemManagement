/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DepartmentDAO;
import dao.DepartmentDTO;
import java.util.List;
import entity.Department;
import util.ErrorMessages;
/**
 *
 * @author Yuikiri
 */
public class DepartmentService {
    private DepartmentDAO departmentDAO;

    public DepartmentService() {
        this.departmentDAO = new DepartmentDAO();
    }

    // 1. Lấy danh sách
    public List<DepartmentDTO> getActiveList() {
        return departmentDAO.getAllActiveDepartments();
    }

    public List<DepartmentDTO> getListForAdmin() {
        return departmentDAO.getAllDepartmentsForAdmin();
    }
    
    public DepartmentDTO getById(int id) {
        return departmentDAO.getDepartmentById(id);
    }

    // 2. Thêm mới Khoa (Có check trùng tên)
    public boolean createNewDepartment(String name, String description) {
        if (departmentDAO.checkNameExist(name)) {
            return false; // Tên đã tồn tại, từ chối thêm
        }
        return departmentDAO.insertDepartment(name, description);
    }

    // 3. Sửa thông tin Khoa
    public boolean updateDepartment(int id, String newName, String description) {
        // Lấy thông tin khoa cũ
        DepartmentDTO oldDept = departmentDAO.getDepartmentById(id);
        if (oldDept == null) return false;

        // Nếu Admin đổi tên Khoa, phải kiểm tra xem tên mới đó có bị trùng với khoa khác không
        if (!oldDept.getName().equalsIgnoreCase(newName)) {
            if (departmentDAO.checkNameExist(newName)) {
                return false; // Tên mới bị trùng, từ chối sửa
            }
        }
        
        return departmentDAO.updateDepartment(id, newName, description);
    }

    // 4. Bật / Tắt trạng thái
    public boolean deactivateDepartment(int id) {
        return departmentDAO.toggleDepartmentStatus(id, 0); // 0 = Ẩn đi / Đóng cửa
    }

    public boolean activateDepartment(int id) {
        return departmentDAO.toggleDepartmentStatus(id, 1); // 1 = Mở cửa lại
    }
    
    // Gọi xuống DAO để lấy danh sách Khoa Lâm sàng
    
    
    // Lấy Khoa Y tế (Cho form Đặt Lịch, cho Bác sĩ)
    public List<DepartmentDTO> getClinicalDepartments() {
        return departmentDAO.getClinicalDepartments();
    }

    // Lấy Phòng ban Hành chính (Cho form Phân công Nhân viên)
    public List<DepartmentDTO> getStaffDepartments() {
        return departmentDAO.getStaffDepartments();
    }
    // SEARCH DEPARTMENT
public List<DepartmentDTO> searchDepartment(String keyword) {
    return departmentDAO.searchDepartment(keyword);
}
// ==========================================================
    // 5. XÓA KHOA (SOFT DELETE - Set isActive = -1)
    // ==========================================================
    public boolean deleteDepartment(int id) {
    DepartmentDTO dept = departmentDAO.getDepartmentById(id);
    if (dept == null) return false;
    
    // Logic: Khi xóa ở đây, DAO sẽ tự động quét bảng Rooms để set NULL departmentId
    return departmentDAO.deleteDepartment(id);
}
    
    public List<DepartmentDTO> getDeletedList() {
    return departmentDAO.getDeletedDepartments();
}

public boolean restoreDepartment(int id) {
    return departmentDAO.restoreDepartment(id);
}
// Hàm lọc tổng hợp: Từ khóa + Trạng thái + Loại (Khoa/Phòng)
public List<DepartmentDTO> getDepartmentWithFilter(String keyword, int status, String type) {
    return departmentDAO.searchDepartmentComplex(keyword, status, type);
}
}
