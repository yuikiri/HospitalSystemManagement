/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.ShiftDAO;
import dao.ShiftDTO;
import java.sql.Timestamp;
import java.util.List;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class ShiftService {
    private ShiftDAO shiftDAO;

    public ShiftService() {
        this.shiftDAO = new ShiftDAO();
    }

    public List<ShiftDTO> getActiveList() {
        return shiftDAO.getAllActiveShifts();
    }

    public List<ShiftDTO> getListForAdmin() {
        return shiftDAO.getAllShiftsForAdmin();
    }

    // THÊM CA TRỰC MỚI (Kèm kiểm tra Logic khắt khe)
    public boolean createNewShift(int roomId, Timestamp startTime, Timestamp endTime, String note) {
        // Ràng buộc 1: Giờ kết thúc phải SAU giờ bắt đầu
        if (!endTime.after(startTime)) {
            return false; 
        }

        // Ràng buộc 2: Chống đụng lịch (Không thể xếp 2 ca trực trong cùng 1 phòng vào cùng 1 thời điểm)
        if (shiftDAO.checkTimeConflict(roomId, startTime, endTime)) {
            return false; // Bị trùng lịch, từ chối tạo!
        }

        return shiftDAO.insertShift(roomId, startTime, endTime, note);
    }

    // SỬA CA TRỰC
    public boolean updateShiftInfo(int id, int roomId, Timestamp startTime, Timestamp endTime, String status, String note) {
        if (!endTime.after(startTime)) return false;

        // Lưu ý: Nếu Admin đổi thời gian, có thể cần check lại Conflict 
        // (Trong thực tế cần viết hàm checkTimeConflict loại trừ chính ID đang sửa ra, 
        // nhưng tạm thời chúng ta cứ update thông tin cơ bản)
        
        return shiftDAO.updateShift(id, roomId, startTime, endTime, status, note);
    }

    // HỦY / KHÔI PHỤC CA TRỰC
    public boolean deactivateShift(int id) {
        return shiftDAO.toggleShiftStatus(id, 0); // 0 = Hủy ca
    }

    public boolean activateShift(int id) {
        return shiftDAO.toggleShiftStatus(id, 1); // 1 = Khôi phục ca
    }
}
