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
    private final RoomDAO roomDAO = new RoomDAO();

    // Lấy danh sách toàn bộ phòng (Dùng cho trang Quản lý của Admin)
    public List<RoomDTO> getAllRooms() throws ErrorMessages.AppException {
        try {
            return roomDAO.getAllRooms();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // Lấy chi tiết phòng (Dùng khi Bệnh nhân bấm xem phòng trước khi đặt lịch)
    public RoomDTO getRoomById(int roomId) throws ErrorMessages.AppException {
        try {
            RoomDTO room = roomDAO.getRoomById(roomId);
            
            if (room == null) {
                // Ném lỗi 404
                throw new ErrorMessages.AppException(ErrorMessages.ROOM_NOT_FOUND);
            }
            
            return room;
            
        } catch (ErrorMessages.AppException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
}
