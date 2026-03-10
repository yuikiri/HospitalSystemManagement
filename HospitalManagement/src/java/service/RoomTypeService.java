/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.RoomTypeDAO;
import java.util.List;
import entity.RoomType;
import util.ErrorMessages;
/**
 *
 * @author Yuikiri
 */
public class RoomTypeService {
    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAO();

    // Lấy toàn bộ danh sách
    public List<RoomType> getAllRoomTypes() throws ErrorMessages.AppException {
        try {
            return roomTypeDAO.getAllRoomTypes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // Lấy chi tiết 1 Loại phòng (Kiểm tra 404)
    public RoomType getRoomTypeById(int id) throws ErrorMessages.AppException {
        try {
            RoomType type = roomTypeDAO.getRoomTypeById(id);
            
            if (type == null) {
                // Ném lỗi 404 nếu Admin nhập sai ID hoặc ID không tồn tại
                throw new ErrorMessages.AppException(ErrorMessages.ROOM_TYPE_NOT_FOUND);
            }
            
            return type;
            
        } catch (ErrorMessages.AppException e) {
            throw e; // Lỗi 404 thì ném đi tiếp
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
}
