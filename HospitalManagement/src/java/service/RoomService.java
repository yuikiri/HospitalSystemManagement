/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.RoomDAO;
import dao.RoomDTO;
import java.util.List;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class RoomService {
    private RoomDAO roomDAO;

    public RoomService() {
        this.roomDAO = new RoomDAO();
    }

    // 1. Lấy danh sách
    public List<RoomDTO> getActiveList() {
        return roomDAO.getAllActiveRooms();
    }

    public List<RoomDTO> getListForAdmin() {
        return roomDAO.getAllRoomsForAdmin();
    }

    // 2. Thêm mới Phòng
    public boolean createNewRoom(int departmentId, int roomType, int roomNumber) {
        // Kiểm tra logic: Số phòng không được âm
        if (roomNumber <= 0) return false;
        
        // Ràng buộc quan trọng: Không được có 2 số phòng trùng nhau trong 1 khoa
        if (roomDAO.checkRoomNumberExist(departmentId, roomNumber)) {
            return false; 
        }
        return roomDAO.insertRoom(departmentId, roomType, roomNumber);
    }

    // 3. Sửa thông tin Phòng
    public boolean updateRoomInfo(int id, int oldDepartmentId, int newDepartmentId, int oldRoomNumber, int newRoomNumber, int roomType, String status) {
        if (newRoomNumber <= 0) return false;

        // Nếu Admin đổi Khoa hoặc đổi số phòng, phải check trùng lại
        if (oldDepartmentId != newDepartmentId || oldRoomNumber != newRoomNumber) {
            if (roomDAO.checkRoomNumberExist(newDepartmentId, newRoomNumber)) {
                return false; // Số phòng này đã tồn tại ở khoa đó rồi!
            }
        }
        
        return roomDAO.updateRoom(id, newDepartmentId, roomType, newRoomNumber, status);
    }

    // 4. Bật / Tắt trạng thái
    public boolean deactivateRoom(int id) {
        return roomDAO.toggleRoomStatus(id, 0); 
    }

    public boolean activateRoom(int id) {
        return roomDAO.toggleRoomStatus(id, 1); 
    }
}
