/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.StaffShift;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;
/**
 *
 * @author Yuikiri
 */
public class StaffShiftDAO {
    public boolean insertStaffShift(StaffShift ss) {
        String sql = "INSERT INTO StaffShifts (staffId, shiftId, role) VALUES (?, ?, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ss.getStaffId());
            ps.setInt(2, ss.getShiftId());
            ps.setString(3, ss.getRole());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return false;
    }

    public List<StaffShiftDTO> getStaffShiftsByStaffId(int staffId) {
        List<StaffShiftDTO> list = new ArrayList<>();
        String sql = "SELECT ss.staffId, ss.shiftId, ss.role, st.name AS staffName, s.startTime, s.endTime, r.roomNumber " +
                     "FROM StaffShifts ss " +
                     "JOIN Staffs st ON ss.staffId = st.id " +
                     "JOIN Shifts s ON ss.shiftId = s.id " +
                     "JOIN Rooms r ON s.roomId = r.id " +
                     "WHERE ss.staffId = ?";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                while (rs.next()) {
                    list.add(new StaffShiftDTO(
                        rs.getInt("staffId"),
                        rs.getInt("shiftId"),
                        rs.getString("staffName"),
                        rs.getString("role"),
                        sdf.format(rs.getTimestamp("startTime")),
                        sdf.format(rs.getTimestamp("endTime")),
                        rs.getInt("roomNumber")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
