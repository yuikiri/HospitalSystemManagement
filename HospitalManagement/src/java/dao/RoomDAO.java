package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class RoomDAO {
    // KỸ THUẬT JOIN 3 BẢNG ĐỂ LẤY TÊN HIỂN THỊ
    //===========================================
    // ĐÃ FIX: Đổi từ JOIN thành LEFT JOIN ở bảng Departments
    // Để các phòng có departmentId = NULL vẫn hiện lên danh sách
    ///////////////Hoàng
    //===========================================
    // Câu lệnh lôi cả giá tiền (rt.price) lên
    private final String SELECT_JOIN_SQL = 
        "SELECT r.*, d.name AS departmentName, rt.name AS roomTypeName, rt.price " +
        "FROM Rooms r " +
        "LEFT JOIN Departments d ON r.departmentId = d.id " +
        "JOIN RoomType rt ON r.roomType = rt.id ";

    // 1. Dành cho trang Đặt lịch Bệnh nhân
    public List<RoomDTO> getAllActiveRoomsAvailable() {
        List<RoomDTO> list = new ArrayList<>();
        
        // 🌟 THÊM ĐIỀU KIỆN: r.status = 'available' VÀO CÂU SQL
        String sql = SELECT_JOIN_SQL + "WHERE r.isActive = 1 AND r.status = 'available' ORDER BY r.roomNumber ASC";
        
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new RoomDTO(
                    rs.getInt("id"), rs.getInt("departmentId"), rs.getString("departmentName"),
                    rs.getInt("roomType"), rs.getString("roomTypeName"),
                    rs.getInt("roomNumber"), rs.getString("status"), rs.getInt("isActive"),
                    rs.getDouble("price") // Lấy giá tiền
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. Dành cho Admin (ĐÃ FIX LỖI CONSTRUCTOR)
    public List<RoomDTO> getAllRoomsForAdmin() {
        List<RoomDTO> list = new ArrayList<>();
        // ĐÃ THÊM: WHERE r.isActive >= 0
        String sql = SELECT_JOIN_SQL + "WHERE r.isActive >= 0 ORDER BY r.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new RoomDTO(
                    rs.getInt("id"), rs.getInt("departmentId"), rs.getString("departmentName"),
                    rs.getInt("roomType"), rs.getString("roomTypeName"),
                    rs.getInt("roomNumber"), rs.getString("status"), rs.getInt("isActive"),
                    rs.getDouble("price") 
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // =========================================================
    // [HÀM MỚI BỔ SUNG] - Lấy thông tin phòng theo số phòng (Dùng để tính tiền)
    // =========================================================
    public RoomDTO getRoomByNumber(int roomNumber) {
        String sql = SELECT_JOIN_SQL + "WHERE r.roomNumber = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new RoomDTO(
                        rs.getInt("id"), rs.getInt("departmentId"), rs.getString("departmentName"),
                        rs.getInt("roomType"), rs.getString("roomTypeName"),
                        rs.getInt("roomNumber"), rs.getString("status"), rs.getInt("isActive"),
                        rs.getDouble("price")
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // =========================================================
    // [HÀM MỚI BỔ SUNG] - Lấy thông tin phòng theo ID
    // =========================================================
    public RoomDTO getRoomById(int id) {
        String sql = SELECT_JOIN_SQL + "WHERE r.id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new RoomDTO(
                        rs.getInt("id"), rs.getInt("departmentId"), rs.getString("departmentName"),
                        rs.getInt("roomType"), rs.getString("roomTypeName"),
                        rs.getInt("roomNumber"), rs.getString("status"), rs.getInt("isActive"),
                        rs.getDouble("price")
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public int getDefaultRoomByDepartment(int departmentId) {
        String sql =
        "SELECT TOP 1 r.id " +
        "FROM Rooms r " +
        "JOIN RoomType rt ON r.roomType = rt.id " +
        "WHERE r.departmentId = ? AND rt.price = 0";

        try(Connection conn = new DbUtils().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, departmentId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return rs.getInt("id");
        }catch(Exception e){e.printStackTrace();}
        return -1;
    }

    // 3. KIỂM TRA TRÙNG SỐ PHÒNG 
    public boolean checkRoomNumberExist(int departmentId, int roomNumber) {
        // Chỉ check trùng khi có phân khoa (departmentId > 0)
        if (departmentId <= 0) return false; 
        
        String sql = "SELECT id FROM Rooms WHERE departmentId = ? AND roomNumber = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            ps.setInt(2, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return true; 
    }

    // 4. THÊM MỚI PHÒNG 
    public boolean insertRoom(int departmentId, int roomType, int roomNumber) {
        String sql = "INSERT INTO Rooms (departmentId, roomType, roomNumber, status, isActive) VALUES (?, ?, ?, 'available', 1)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // FIX LOGIC: Nếu không chọn khoa (departmentId = 0), set giá trị NULL vào DB
            if (departmentId > 0) {
                ps.setInt(1, departmentId);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            
            ps.setInt(2, roomType);
            ps.setInt(3, roomNumber);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 5. CẬP NHẬT THÔNG TIN PHÒNG 
    public boolean updateRoom(int id, int departmentId, int roomType, int roomNumber, String status) {
        String sql = "UPDATE Rooms SET departmentId = ?, roomType = ?, roomNumber = ?, status = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // FIX LOGIC: Tương tự như Insert, xử lý lưu giá trị NULL nếu admin tháo phòng ra khỏi khoa
            if (departmentId > 0) {
                ps.setInt(1, departmentId);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            
            ps.setInt(2, roomType);
            ps.setInt(3, roomNumber);
            ps.setString(4, status);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 6. BẬT/TẮT TRẠNG THÁI 
    public boolean toggleRoomStatus(int id, int isActive) {
        String sql = "UPDATE Rooms SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    
    // 7. TÌM KIẾM VÀ LỌC PHÒNG
   public List<RoomDTO> searchAndFilterRooms(String keyword, int departmentId, String status, int isActive) {
        List<RoomDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT r.*, d.name AS departmentName, rt.name AS roomTypeName, rt.price " +
            "FROM Rooms r " +
            "LEFT JOIN Departments d ON r.departmentId = d.id " +
            "JOIN RoomType rt ON r.roomType = rt.id " + 
            "WHERE r.isActive >= 0 " // ĐÃ THÊM: Luôn luôn ẩn phòng trong thùng rác
        );

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND CAST(r.roomNumber AS VARCHAR) LIKE ? ");
        }
        if (departmentId > 0) {
            sql.append(" AND r.departmentId = ? ");
        } else if (departmentId == -1) { 
            sql.append(" AND r.departmentId IS NULL ");
        }
        
        if (status != null && !status.isEmpty() && !status.equals("all")) {
            sql.append(" AND r.status = ? ");
        }
        // NẾU KHÁC -99 THÌ MỚI LỌC 1 (MỞ) HOẶC 0 (ĐÓNG)
        if (isActive != -99) {
            sql.append(" AND r.isActive = ? ");
        }
        sql.append(" ORDER BY r.id DESC");

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (keyword != null && !keyword.isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword + "%");
            }
            if (departmentId > 0) {
                ps.setInt(paramIndex++, departmentId);
            }
            if (status != null && !status.isEmpty() && !status.equals("all")) {
                ps.setString(paramIndex++, status);
            }
            if (isActive != -99) {
                ps.setInt(paramIndex++, isActive);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new RoomDTO(
                        rs.getInt("id"), rs.getInt("departmentId"), rs.getString("departmentName"),
                        rs.getInt("roomType"), rs.getString("roomTypeName"),
                        rs.getInt("roomNumber"), rs.getString("status"), rs.getInt("isActive"),
                        rs.getDouble("price")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    // --- CHỨC NĂNG THÙNG RÁC ---
    public boolean deleteRoom(int id) {
        String sql = "UPDATE Rooms SET isActive = -1 WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public List<RoomDTO> getDeletedRooms() {
        List<RoomDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "WHERE r.isActive = -1 ORDER BY r.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new RoomDTO(
                    rs.getInt("id"), rs.getInt("departmentId"), rs.getString("departmentName"),
                    rs.getInt("roomType"), rs.getString("roomTypeName"),
                    rs.getInt("roomNumber"), rs.getString("status"), rs.getInt("isActive"),
                    rs.getDouble("price")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean restoreRoom(int id) {
        String sql = "UPDATE Rooms SET isActive = 1 WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
