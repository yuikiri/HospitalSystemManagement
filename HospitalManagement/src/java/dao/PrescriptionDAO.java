/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.Prescription;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import util.DbUtils;
/**
 *
 * @author Yuikiri
 */
public class PrescriptionDAO {
    // KỸ THUẬT QUAN TRỌNG: Insert và trả về ID vừa sinh ra
    public int insertPrescription(Prescription p) {
        String sql = "INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status) VALUES (?, ?, GETDATE(), ?)";
        
        // Cờ Statement.RETURN_GENERATED_KEYS giúp ta hứng lại ID tự tăng của SQL
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, p.getMedicalRecordId());
            ps.setString(2, p.getNotes());
            ps.setString(3, p.getStatus());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Trả về ID mới
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1; // Thất bại
    }
}
