/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.UserDAO;
import dao.UserDTO;
import entity.User;
import util.ErrorMessages;
/**
 *
 * @author Yuikiri
 */
public class UserService {
    private final UserDAO userDAO = new UserDAO();

    // Ném ra AppException thay vì Exception chung chung
    public UserDTO authenticate(String email, String password) throws ErrorMessages.AppException {
        User user = userDAO.checkLogin(email, password);

        if (user == null) {
            // Ném lỗi 401
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_CREDENTIALS);
        }

        if (user.getIsActive() == 0) {
            // Ném lỗi 403 (Forbidden / Banned)
            throw new ErrorMessages.AppException(ErrorMessages.ACCOUNT_BANNED);
        }

        return new UserDTO(user.getId(), user.getUserName(), user.getEmail(), user.getRole());
    }
}
