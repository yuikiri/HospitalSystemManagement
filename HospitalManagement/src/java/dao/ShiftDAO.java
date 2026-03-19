package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

/**
 * Data Access Object for Shifts
 * @author Yuikiri
 */
public class ShiftDAO {

    // CÂU LỆNH JOIN CHUẨN ĐỂ LẤY THÔNG TIN PHÒNG VÀ KHOA
    private final String SELECT_JOIN_SQL = 
        "SELECT s.*, r.roomNumber, d.name AS departmentName " +
        "FROM Shifts s " +
        "JOIN Rooms r ON s.roomId = r.id " +
        "JOIN Departments d ON r.departmentId = d.id ";

    // 1. LẤY CA TRỰC ĐANG HOẠT ĐỘNG
    public List<ShiftDTO> getAllActiveShifts() {
        List<ShiftDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "WHERE s.isActive = 1 ORDER BY s.startTime ASC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ShiftDTO(
                    rs.getInt("id"), rs.getInt("roomId"), rs.getInt("roomNumber"),
                    rs.getString("departmentName"), rs.getTimestamp("startTime"),
                    rs.getTimestamp("endTime"), rs.getString("status"),
                    rs.getInt("isActive"), rs.getString("note")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. LẤY TOÀN BỘ CA TRỰC CHO ADMIN
    public List<ShiftDTO> getAllShiftsForAdmin() {
        List<ShiftDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "ORDER BY s.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ShiftDTO(
                    rs.getInt("id"), rs.getInt("roomId"), rs.getInt("roomNumber"),
                    rs.getString("departmentName"), rs.getTimestamp("startTime"),
                    rs.getTimestamp("endTime"), rs.getString("status"),
                    rs.getInt("isActive"), rs.getString("note")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 3. KIỂM TRA ĐỤNG LỊCH PHÒNG
    public boolean checkTimeConflict(int roomId, Timestamp start, Timestamp end) {
        String sql = "SELECT id FROM Shifts WHERE roomId = ? AND isActive = 1 " +
                     "AND ((startTime < ? AND endTime > ?) " +
                     "OR (startTime >= ? AND startTime < ?))";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setTimestamp(2, end);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, start);
            ps.setTimestamp(5, end);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false; 
    }

    // 4. LẤY LỊCH TRỰC TRONG TUẦN THEO EMAIL
    public List<ShiftDTO> getWeeklyShiftsByEmail(String email, String personType, Timestamp startOfWeek, Timestamp endOfWeek) {
        List<ShiftDTO> list = new ArrayList<>();
        String sql = "";
        
        if ("doctor".equals(personType)) {
            sql = "SELECT s.id, s.roomId, r.roomNumber, d.name AS departmentName, s.startTime, s.endTime, s.status, s.isActive, ds.role AS note " +
                  "FROM Shifts s " +
                  "JOIN Rooms r ON s.roomId = r.id " +
                  "JOIN Departments d ON r.departmentId = d.id " +
                  "JOIN DoctorShifts ds ON s.id = ds.shiftId " +
                  "JOIN Users u ON ds.doctorId = u.id " +
                  "WHERE u.email = ? AND s.startTime >= ? AND s.startTime < ? AND s.isActive = 1";
        } else {
            sql = "SELECT s.id, s.roomId, r.roomNumber, d.name AS departmentName, s.startTime, s.endTime, s.status, s.isActive, ss.role AS note " +
                  "FROM Shifts s " +
                  "JOIN Rooms r ON s.roomId = r.id " +
                  "JOIN Departments d ON r.departmentId = d.id " +
                  "JOIN StaffShifts ss ON s.id = ss.shiftId " +
                  "JOIN Users u ON ss.staffId = u.id " +
                  "WHERE u.email = ? AND s.startTime >= ? AND s.startTime < ? AND s.isActive = 1";
        }

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setTimestamp(2, startOfWeek);
            ps.setTimestamp(3, endOfWeek);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ShiftDTO(
                        rs.getInt("id"), rs.getInt("roomId"), rs.getInt("roomNumber"),
                        rs.getString("departmentName"), rs.getTimestamp("startTime"),
                        rs.getTimestamp("endTime"), rs.getString("status"),
                        rs.getInt("isActive"), rs.getString("note")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 5. LẤY DANH SÁCH PHÒNG TRỐNG (AJAX)
    public List<RoomDTO> getAvailableRooms(Timestamp start, Timestamp end) {
        List<RoomDTO> list = new ArrayList<>();
        String sql = "SELECT r.id, r.roomNumber, d.name as departmentName " +
                     "FROM Rooms r " +
                     "JOIN Departments d ON r.departmentId = d.id " +
                     "WHERE r.isActive = 1 AND r.id NOT IN (" +
                     "    SELECT roomId FROM Shifts " +
                     "    WHERE startTime = ? AND isActive = 1" +
                     ")";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, start);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RoomDTO r = new RoomDTO();
                    r.setId(rs.getInt("id"));
                    r.setRoomNumber(rs.getInt("roomNumber"));
                    r.setDepartmentName(rs.getString("departmentName"));
                    list.add(r);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 6. TÌM SHIFT ID ĐÃ TỒN TẠI (Sửa lại logic JOIN cho Staff)
    public int getExistingShiftId(String email, String personType, Timestamp startTime) {
        String sql = "";
        if ("doctor".equals(personType)) {
            sql = "SELECT s.id FROM Shifts s " +
                  "JOIN DoctorShifts ds ON s.id = ds.shiftId " +
                  "JOIN Users u ON ds.doctorId = u.id " +
                  "WHERE u.email = ? AND s.startTime = ? AND s.isActive = 1";
        } else {
            // FIX LỖI Ở ĐÂY: Phải JOIN đúng bảng StaffShifts và cột staffId
            sql = "SELECT s.id FROM Shifts s " +
                  "JOIN StaffShifts ss ON s.id = ss.shiftId " +
                  "JOIN Users u ON ss.staffId = u.id " +
                  "WHERE u.email = ? AND s.startTime = ? AND s.isActive = 1";
        }
        
        try (Connection conn = new util.DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setTimestamp(2, startTime);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // 7. CẬP NHẬT PHÒNG CHO SHIFT CÓ SẴN
    public boolean updateShiftRoom(int shiftId, int roomId, String note) {
        String sql = "UPDATE Shifts SET roomId = ?, note = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setString(2, note);
            ps.setInt(3, shiftId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 8. TẠO MỚI SHIFT TRẢ VỀ ID
    public int insertShiftReturnId(int roomId, Timestamp startTime, Timestamp endTime, String note) {
        String sql = "INSERT INTO Shifts (roomId, startTime, endTime, status, isActive, note) VALUES (?, ?, ?, 'scheduled', 1, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, roomId);
            ps.setTimestamp(2, startTime);
            ps.setTimestamp(3, endTime);
            ps.setString(4, note);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // 9. PHÂN CÔNG (ASSIGN) CHO NGƯỜI DÙNG
    public boolean assignShiftToPerson(int shiftId, String email, String personType, String role) {
        String queryUserId = "SELECT id FROM Users WHERE email = ?";
        int userId = -1;
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement psUser = conn.prepareStatement(queryUserId)) {
            psUser.setString(1, email);
            try (ResultSet rs = psUser.executeQuery()) {
                if (rs.next()) userId = rs.getInt("id");
            }
            if (userId == -1) return false;
            
            // Xóa phân công cũ nếu có (để update nhiệm vụ)
            String deleteOld = "doctor".equals(personType) ? "DELETE FROM DoctorShifts WHERE shiftId = ?" : "DELETE FROM StaffShifts WHERE shiftId = ?";
            try (PreparedStatement psDel = conn.prepareStatement(deleteOld)) {
                psDel.setInt(1, shiftId);
                psDel.executeUpdate();
            }

            String insertSql = "doctor".equals(personType) 
                    ? "INSERT INTO DoctorShifts (doctorId, shiftId, role) VALUES (?, ?, ?)"
                    : "INSERT INTO StaffShifts (staffId, shiftId, role) VALUES (?, ?, ?)";
            try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                psInsert.setInt(1, userId);
                psInsert.setInt(2, shiftId);
                psInsert.setString(3, role);
                return psInsert.executeUpdate() > 0;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    // BẬT / TẮT TRẠNG THÁI CA TRỰC (Hủy hoặc Khôi phục)
    public boolean toggleShiftStatus(int id, int isActive) {
        String sql = "UPDATE Shifts SET isActive = ? WHERE id = ?";
        try (Connection conn = new util.DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return false;
    }
}