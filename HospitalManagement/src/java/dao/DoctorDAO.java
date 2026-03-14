package dao;

import entity.Doctor;
import util.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import entity.User;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    // 1. LẤY HỒ SƠ THEO USERID (Dùng ngay sau khi bác sĩ Login thành công)
    public DoctorDTO getDoctorByUserId(int userId) {
        String sql = "SELECT * FROM Doctors WHERE userId = ?";
        try ( Connection conn = new DbUtils().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapDoctor(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 2. CẬP NHẬT HỒ SƠ (Dành cho nút "Cập nhật thông tin")
    public boolean updateDoctorProfile(int id, String name, int gender, String position, String phone, String licenseNumber) {
        String sql = "UPDATE Doctors SET name = ?, gender = ?, position = ?, phone = ?, licenseNumber = ? WHERE id = ?";
        try ( Connection conn = new DbUtils().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, gender);
            ps.setString(3, position);
            ps.setString(4, phone);
            ps.setString(5, licenseNumber);
            ps.setInt(6, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. LẤY DANH SÁCH BÁC SĨ ĐANG HOẠT ĐỘNG (JOIN với Users để check isActive)
    public List<DoctorDTO> getAllActiveDoctors() {
        List<DoctorDTO> list = new ArrayList<>();
        String sql = "SELECT d.* FROM Doctors d JOIN Users u ON d.userId = u.id WHERE u.isActive = 1";
        try ( Connection conn = new DbUtils().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapDoctor(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 4. KIỂM TRA TRÙNG SỐ ĐIỆN THOẠI / GIẤY PHÉP
    public boolean checkUniqueField(String fieldName, String value, int excludeDoctorId) {
        String sql = "SELECT id FROM Doctors WHERE " + fieldName + " = ? AND id != ?";
        try ( Connection conn = new DbUtils().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setInt(2, excludeDoctorId);
            try ( ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return true;
        }
    }

    private DoctorDTO mapDoctor(ResultSet rs) throws SQLException {
        return new DoctorDTO(
                rs.getInt("id"),
                rs.getInt("userId"),
                rs.getString("name"),
                rs.getInt("gender"),
                rs.getString("position"),
                rs.getString("phone"),
                rs.getString("licenseNumber")
        );
    }

    public boolean updateDoctorProfile(int userId, String name, String email, String phone) {
        String sqlUser = "UPDATE Users SET email = ? WHERE userId = ?";
        String sqlDoctor = "UPDATE Doctors SET name = ?, phone = ? WHERE userId = ?";

        // Sử dụng Transaction hoặc cập nhật lần lượt tùy cấu trúc của bạn
        // Ở đây tôi viết logic cơ bản:
        try ( Connection conn = DbUtils.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            try ( PreparedStatement psUser = conn.prepareStatement(sqlUser);  PreparedStatement psDoctor = conn.prepareStatement(sqlDoctor)) {

                // 1. Update bảng Users
                psUser.setString(1, email);
                psUser.setInt(2, userId);
                psUser.executeUpdate();

                // 2. Update bảng Doctors
                psDoctor.setString(1, name);
                psDoctor.setString(2, phone);
                psDoctor.setInt(3, userId);
                psDoctor.executeUpdate();

                conn.commit(); // Thành công
                return true;
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
