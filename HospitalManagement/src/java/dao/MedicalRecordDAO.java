/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.MedicalRecord;
/**
 *
 * @author Yuikiri
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import util.DbUtils;
public class MedicalRecordDAO {
    // 1. Lưu bệnh án mới
    public boolean insertMedicalRecord(MedicalRecord mr) {
        // Sử dụng GETDATE() của SQL Server cho createdAt
        String sql = "INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt) VALUES (?, ?, ?, GETDATE())";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, mr.getAppointmentId());
            ps.setString(2, mr.getDiagnosis());
            ps.setString(3, mr.getNotes());
            
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return false;
    }

    // 2. Lấy bệnh án theo ID Lịch hẹn (Vì quan hệ là 1-1 nên dùng cách này rất tiện)
    public MedicalRecordDTO getRecordByAppointmentId(int appointmentId) {
        // JOIN 4 bảng: MedicalRecords -> Appointments -> Patients & Doctors
        String sql = "SELECT m.id AS recordId, m.appointmentId, p.name AS patientName, d.name AS doctorName, " +
                     "m.diagnosis, m.notes, m.createdAt " +
                     "FROM MedicalRecords m " +
                     "JOIN Appointments a ON m.appointmentId = a.id " +
                     "JOIN Patients p ON a.patientId = p.id " +
                     "JOIN Doctors d ON a.doctorId = d.id " +
                     "WHERE m.appointmentId = ?";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, appointmentId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                    return new MedicalRecordDTO(
                        rs.getInt("recordId"),
                        rs.getInt("appointmentId"),
                        rs.getString("patientName"),
                        "Bs. " + rs.getString("doctorName"),
                        rs.getString("diagnosis"),
                        rs.getString("notes"),
                        sdf.format(rs.getTimestamp("createdAt"))
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
