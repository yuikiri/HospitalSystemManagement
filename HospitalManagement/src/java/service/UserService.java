package service;

import dao.UserDAO;
import entity.User;
import java.util.List;
import util.ErrorMessages;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    // =========================================================
    // 1. LOGIN
    // =========================================================
    public User authenticate(String email, String password) throws ErrorMessages.AppException {

        User user = userDAO.checkLogin(email, password);

        if (user == null) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_CREDENTIALS);
        }

        if (user.getIsActive() == 0) {
            throw new ErrorMessages.AppException(ErrorMessages.ACCOUNT_BANNED);
        }

        return user;
    }

    // =========================================================
    // 2. REGISTER PATIENT
    // =========================================================
    public void registerNewPatient(String name,
                                   String email,
                                   String password,
                                   String phone,
                                   String address)
            throws ErrorMessages.AppException {

        // Check email trùng
        if (userDAO.checkEmailExist(email)) {
            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
        }

        // Insert Users + Patients
        boolean isSuccess = userDAO.registerPatient(name, email, password, phone, address);

        if (!isSuccess) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // =========================================================
    // 3. ADMIN LẤY TẤT CẢ USER
    // =========================================================
    public List<User> getAllUsers(){
    UserDAO dao = new UserDAO();
    return dao.getAllUsers();
}

    // =========================================================
    // 4. ADMIN ADD USER
    // =========================================================
    public void addUserByAdmin(String name,
                               String email,
                               String password,
                               String role)
            throws ErrorMessages.AppException {

        if (name == null || email == null || password == null || role == null) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        if (userDAO.checkEmailExist(email)) {
            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
        }

        boolean isSuccess = userDAO.addUserByAdmin(name, email, password, role);

        if (!isSuccess) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // =========================================================
    // 5. ADMIN UPDATE USER
    // =========================================================
    public void updateUserByAdmin(int userId,
                                  String name,
                                  String email,
                                  String role)
            throws ErrorMessages.AppException {

        if (userDAO.checkEmailExistForUpdate(email, userId)) {
            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
        }

        boolean isSuccess = userDAO.updateUserByAdmin(userId, name, email, role);

        if (!isSuccess) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // =========================================================
    // 6. ADMIN LOCK / UNLOCK USER
    // =========================================================
   public void toggleUserStatus(int id) throws Exception {

    userDAO.toggleUserStatus(id);

}
   public List<User> searchUsers(String email, String isActive) throws Exception {

    return userDAO.searchUsers(email, isActive);

}
   public void deleteUser(int id) throws Exception{
    userDAO.deleteUser(id);
}
   public List<User> getDeletedUsers() throws Exception {

    return userDAO.getDeletedUsers();

}
   public void restoreUser(int id) throws Exception {

    userDAO.restoreUser(id);

}
   public User getUserById(int id) throws Exception{
    return userDAO.getUserById(id);
}
}
