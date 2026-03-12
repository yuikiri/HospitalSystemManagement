package dao;

import util.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    // 1. LẤY HỒ SƠ THEO USERID
    public StaffDTO getStaffByUserId(int userId) {

        String sql = "SELECT s.*, u.email, u.avatarUrl, u.isActive, u.createdAt " +
                     "FROM Staffs s " +
                     "JOIN Users u ON s.userId = u.id " +
                     "WHERE s.userId = ?";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapStaff(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // 2. CẬP NHẬT HỒ SƠ NHÂN VIÊN
    public boolean updateStaffProfile(int id, String name, int gender, String position, String phone) {

        String sql = "UPDATE Staffs SET name = ?, gender = ?, position = ?, phone = ? WHERE id = ?";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, gender);
            ps.setString(3, position);
            ps.setString(4, phone);
            ps.setInt(5, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // 3. CHECK TRÙNG SỐ ĐIỆN THOẠI
    public boolean checkPhoneExist(String phone, int excludeStaffId) {

        String sql = "SELECT id FROM Staffs WHERE phone = ? AND id != ?";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            ps.setInt(2, excludeStaffId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            return true;
        }
    }


    public boolean updateProfile(int userId, String email, String name,
                                 String phone, String position, int gender) {

        boolean check = false;

        try {

            Connection conn = DbUtils.getConnection();

            String sql1 = "UPDATE Users SET email=? WHERE id=?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);

            ps1.setString(1, email);
            ps1.setInt(2, userId);

            ps1.executeUpdate();

            String sql2 = "UPDATE Staffs SET name=?, phone=?, position=?, gender=? WHERE userId=?";

            PreparedStatement ps2 = conn.prepareStatement(sql2);

            ps2.setString(1, name);
            ps2.setString(2, phone);
            ps2.setString(3, position);
            ps2.setInt(4, gender);
            ps2.setInt(5, userId);

            check = ps2.executeUpdate() > 0;

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return check;
    }


    // 4. LẤY TOÀN BỘ NHÂN VIÊN ĐANG HOẠT ĐỘNG (ADMIN)
    public List<StaffDTO> getAllActiveStaffs() {

        List<StaffDTO> list = new ArrayList<>();

        String sql = "SELECT s.*, u.email, u.avatarUrl, u.isActive, u.createdAt " +
                     "FROM Staffs s " +
                     "JOIN Users u ON s.userId = u.id " +
                     "WHERE u.isActive = 1";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapStaff(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // MAP RESULTSET → DTO
    private StaffDTO mapStaff(ResultSet rs) throws SQLException {

        return new StaffDTO(
                rs.getInt("id"),
                rs.getInt("userId"),
                rs.getString("email"),
                rs.getString("avatarUrl"),
                rs.getInt("isActive"),
                rs.getString("name"),
                rs.getInt("gender"),
                rs.getString("position"),
                rs.getString("phone"),
                rs.getString("createdAt")
        );
    }
}