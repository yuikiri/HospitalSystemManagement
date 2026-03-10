/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DepartmentDAO;
import java.util.List;
import entity.Department;
import util.ErrorMessages;
/**
 *
 * @author Yuikiri
 */
public class DepartmentService {
    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    // Dịch vụ lấy danh sách (Không có khả năng trả về null, nếu rỗng thì trả về list rỗng)
    public List<Department> getAllDepartments() throws ErrorMessages.AppException {
        try {
            return departmentDAO.getAllDepartments();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // Dịch vụ lấy chi tiết (Có kiểm tra lỗi 404)
    public Department getDepartmentById(int id) throws ErrorMessages.AppException {
        try {
            Department dept = departmentDAO.getDepartmentById(id);
            
            if (dept == null) {
                // Ném lỗi 404 nếu không tìm thấy ID Khoa
                throw new ErrorMessages.AppException(ErrorMessages.DEPARTMENT_NOT_FOUND);
            }
            
            return dept;
            
        } catch (ErrorMessages.AppException e) {
            throw e; // Ném tiếp lỗi 404
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
}
