/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import entity.DoctorShift;
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
public class DoctorShiftDAO {
    public boolean insertDoctorShift(DoctorShift ds) {
        String sql = "INSERT INTO DoctorShifts (doctorId, shiftId, role) VALUES (?, ?, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ds.getDoctorId());
            ps.setInt(2, ds.getShiftId());
            ps.setString(3, ds.getRole());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return false;
    }

    public List<DoctorShiftDTO> getDoctorShiftsByDoctorId(int doctorId) {
        List<DoctorShiftDTO> list = new ArrayList<>();
        String sql = "SELECT ds.doctorId, ds.shiftId, ds.role, d.name AS doctorName, s.startTime, s.endTime, r.roomNumber " +
                     "FROM DoctorShifts ds " +
                     "JOIN Doctors d ON ds.doctorId = d.id " +
                     "JOIN Shifts s ON ds.shiftId = s.id " +
                     "JOIN Rooms r ON s.roomId = r.id " +
                     "WHERE ds.doctorId = ?";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                while (rs.next()) {
                    list.add(new DoctorShiftDTO(
                        rs.getInt("doctorId"),
                        rs.getInt("shiftId"),
                        rs.getString("doctorName"),
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
