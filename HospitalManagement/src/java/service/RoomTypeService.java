/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.RoomTypeDAO;
import dao.RoomTypeDTO;
import java.util.List;
import entity.RoomType;
import util.ErrorMessages;
/**
 *
 * @author Yuikiri
 */
public class RoomTypeService {
    private RoomTypeDAO roomTypeDAO;

    public RoomTypeService() {
        this.roomTypeDAO = new RoomTypeDAO();
    }

    // 1. Lấy danh sách
    public List<RoomTypeDTO> getActiveList() {
        return roomTypeDAO.getAllActiveRoomTypes();
    }

    public List<RoomTypeDTO> getListForAdmin() {
        return roomTypeDAO.getAllRoomTypesForAdmin();
    }

    public RoomTypeDTO getById(int id) {
        return roomTypeDAO.getRoomTypeById(id);
    }

    // 2. Thêm mới Loại phòng
    public boolean createNewRoomType(String name, double price) {
        // Kiểm tra logic: Giá tiền không được âm và tên không được trùng
        if (price < 0) return false;
        
        if (roomTypeDAO.checkNameExist(name)) {
            return false; // Tên đã tồn tại
        }
        return roomTypeDAO.insertRoomType(name, price);
    }

    // 3. Sửa thông tin Loại phòng
    public boolean updateRoomType(int id, String newName, double price) {
        if (price < 0) return false;

        RoomTypeDTO oldRoomType = roomTypeDAO.getRoomTypeById(id);
        if (oldRoomType == null) return false;

        // Nếu đổi tên, kiểm tra xem tên mới có trùng với loại phòng khác không
        if (!oldRoomType.getName().equalsIgnoreCase(newName)) {
            if (roomTypeDAO.checkNameExist(newName)) {
                return false; 
            }
        }
        
        return roomTypeDAO.updateRoomType(id, newName, price);
    }

    // 4. Bật / Tắt trạng thái
    public boolean deactivateRoomType(int id) {
        return roomTypeDAO.toggleRoomTypeStatus(id, 0); // 0 = Ngưng sử dụng
    }

    public boolean activateRoomType(int id) {
        return roomTypeDAO.toggleRoomTypeStatus(id, 1); // 1 = Mở lại
    }
}
