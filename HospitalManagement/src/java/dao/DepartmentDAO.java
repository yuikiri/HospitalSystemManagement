package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

public class DepartmentDAO {

    // 1. LẤY TẤT CẢ KHOA ĐANG HOẠT ĐỘNG (CHO PATIENT/BOOKING)
    public List<DepartmentDTO> getAllActiveDepartments() {
        List<DepartmentDTO> list = new ArrayList<>();
        String sql = "SELECT id, name, description, isActive FROM Departments "
                   + "WHERE isActive = 1 ORDER BY name ASC"; // Sắp xếp ASC cho dễ nhìn

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DepartmentDTO(rs.getInt("id"), rs.getString("name"), 
                                         rs.getString("description"), rs.getInt("isActive")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. LẤY DEPARTMENT CHO ADMIN (CHỈ LẤY ACTIVE + INACTIVE, LOẠI BỎ ĐÃ XÓA -1)
    public List<DepartmentDTO> getAllDepartmentsForAdmin() {
        List<DepartmentDTO> list = new ArrayList<>();
        String sql = "SELECT id, name, description, isActive FROM Departments "
                   + "WHERE isActive != -1 ORDER BY id DESC"; // Lọc bỏ -1 ở đây

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DepartmentDTO(rs.getInt("id"), rs.getString("name"), 
                                         rs.getString("description"), rs.getInt("isActive")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public DepartmentDTO getDepartmentById(int id) {
        String sql = "SELECT id, name, description, isActive FROM Departments WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DepartmentDTO(rs.getInt("id"), rs.getString("name"), 
                                           rs.getString("description"), rs.getInt("isActive"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 4. CHECK TRÙNG TÊN (LOẠI BỎ NHỮNG KHOA ĐÃ XÓA TRONG THÙNG RÁC)
    public boolean checkNameExist(String name) {
        String sql = "SELECT id FROM Departments WHERE name = ? AND isActive != -1"; 
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean insertDepartment(String name, String description) {
        String sql = "INSERT INTO Departments (name, description, isActive) VALUES (?, ?, 1)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean updateDepartment(int id, String name, String description) {
        String sql = "UPDATE Departments SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean toggleDepartmentStatus(int id, int status) {
        String sql = "UPDATE Departments SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 9. SEARCH (CŨNG PHẢI LỌC BỎ -1)
    public List<DepartmentDTO> searchDepartment(String keyword) {
        List<DepartmentDTO> list = new ArrayList<>();
        String sql = "SELECT id, name, description, isActive FROM Departments "
                   + "WHERE name LIKE ? AND isActive != -1 ORDER BY name ASC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DepartmentDTO(rs.getInt("id"), rs.getString("name"), 
                                             rs.getString("description"), rs.getInt("isActive")));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 10. LẤY TRONG THÙNG RÁC
    public List<DepartmentDTO> getDeletedDepartments() {
        List<DepartmentDTO> list = new ArrayList<>();
        String sql = "SELECT id, name, description, isActive FROM Departments WHERE isActive = -1 ORDER BY id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DepartmentDTO(rs.getInt("id"), rs.getString("name"), 
                                         rs.getString("description"), rs.getInt("isActive")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 11. KHÔI PHỤC (Set về 0 - Tạm ngưng cho an toàn)
    public boolean restoreDepartment(int id) {
        String sql = "UPDATE Departments SET isActive = 0 WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 12. XÓA (Soft delete)
   // Sửa lại hàm deleteDepartment (Số 12 trong file của bạn)
public boolean deleteDepartment(int id) {
    Connection conn = null;
    PreparedStatement psRoom = null;
    PreparedStatement psDept = null;
    try {
        conn = new DbUtils().getConnection();
        conn.setAutoCommit(false); // Bắt đầu Transaction để đảm bảo an toàn

        // Bước A: Cập nhật tất cả phòng thuộc khoa này về NULL
        String sqlRooms = "UPDATE Rooms SET departmentId = NULL WHERE departmentId = ?";
        psRoom = conn.prepareStatement(sqlRooms);
        psRoom.setInt(1, id);
        psRoom.executeUpdate();

        // Bước B: Soft delete khoa (Set isActive = -1)
        String sqlDept = "UPDATE Departments SET isActive = -1 WHERE id = ?";
        psDept = conn.prepareStatement(sqlDept);
        psDept.setInt(1, id);
        
        int rowAffected = psDept.executeUpdate();
        
        conn.commit(); // Lưu thay đổi
        return rowAffected > 0;
    } catch (Exception e) {
        if (conn != null) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
        }
        e.printStackTrace();
    } finally {
        // Đóng resource thủ công vì dùng Transaction
        try {
            if (psRoom != null) psRoom.close();
            if (psDept != null) psDept.close();
            if (conn != null) conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
    return false;
}
    // ==========================================================
    // 10. LẤY KHOA LÂM SÀNG (Bắt đầu bằng chữ 'Khoa')
    // ==========================================================
    public List<DepartmentDTO> getClinicalDepartments() {
        List<DepartmentDTO> list = new ArrayList<>();
        // Lọc isActive != -1 để tránh lấy khoa đã xóa
        String sql = "SELECT id, name, description, isActive FROM Departments "
                   + "WHERE isActive = 1 AND name LIKE N'Khoa%' ORDER BY name ASC";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DepartmentDTO(rs.getInt("id"), rs.getString("name"), 
                                         rs.getString("description"), rs.getInt("isActive")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ==========================================================
    // 11. LẤY PHÒNG BAN HÀNH CHÍNH (Không bắt đầu bằng chữ 'Khoa')
    // ==========================================================
    public List<DepartmentDTO> getStaffDepartments() {
        List<DepartmentDTO> list = new ArrayList<>();
        // Lọc isActive != -1 để tránh lấy phòng ban đã xóa
        String sql = "SELECT id, name, description, isActive FROM Departments "
                   + "WHERE isActive = 1 AND name NOT LIKE N'Khoa%' ORDER BY name ASC";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DepartmentDTO(rs.getInt("id"), rs.getString("name"), 
                                         rs.getString("description"), rs.getInt("isActive")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    public List<DepartmentDTO> searchDepartmentComplex(String keyword, int status, String type) {
    List<DepartmentDTO> list = new ArrayList<>();
    // Gốc câu lệnh: không lấy hàng trong thùng rác (isActive != -1)
    StringBuilder sql = new StringBuilder("SELECT * FROM Departments WHERE isActive != -1 ");

    // 1. Lọc theo từ khóa (nếu có)
    if (keyword != null && !keyword.isEmpty()) {
        sql.append(" AND name LIKE ? ");
    }
    // 2. Lọc theo trạng thái (nếu status != -99)
    if (status != -99) {
        sql.append(" AND isActive = ? ");
    }
    // 3. Lọc theo loại (Khoa/Phòng)
    if ("khoa".equals(type)) {
        sql.append(" AND name LIKE N'Khoa%' ");
    } else if ("phong".equals(type)) {
        sql.append(" AND name NOT LIKE N'Khoa%' ");
    }

    sql.append(" ORDER BY id DESC");

    try (Connection conn = new DbUtils().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {
        
        int paramIndex = 1;
        if (keyword != null && !keyword.isEmpty()) {
            ps.setString(paramIndex++, "%" + keyword + "%");
        }
        if (status != -99) {
            ps.setInt(paramIndex++, status);
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DepartmentDTO(rs.getInt("id"), rs.getString("name"), 
                                         rs.getString("description"), rs.getInt("isActive")));
            }
        }
    } catch (Exception e) { e.printStackTrace(); }
    return list;
}
    // Thêm chữ static vào dòng này
// Sửa đoạn cuối file của bạn thành như sau:
public List<DepartmentDTO> getDepartmentWithFilter(String keyword, int status, String type) {
    // Gọi trực tiếp, không dùng tên lớp DepartmentDAO phía trước
    return searchDepartmentComplex(keyword, status, type);
}
}