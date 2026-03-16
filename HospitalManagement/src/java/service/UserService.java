package service;

import dao.UserDAO;
import entity.User;
import java.util.List;
import util.ErrorMessages;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    // ==============================
    // LOGIN
    // ==============================
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

    // ==============================
    // REGISTER PATIENT (USER REGISTER)
    // ==============================
    public void registerNewPatient(String name, String email, String password, String phone, String address)
            throws ErrorMessages.AppException {

        if (userDAO.checkEmailExist(email)) {
            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
        }

        boolean success = userDAO.registerPatient(name, email, password, phone, address);

        if (!success) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // ==============================
    // ADMIN GET ALL USERS
    // ==============================
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // ==============================
    // ADMIN ADD USER
    // ==============================
    public void addUserByAdmin(String name, String email, String password, String role)
            throws ErrorMessages.AppException {

        if (name == null || name.trim().isEmpty()) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        if (email == null || email.trim().isEmpty()) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        if (password == null || password.trim().isEmpty()) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        if (role == null || role.trim().isEmpty()) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        // check email
        if (userDAO.checkEmailExist(email)) {
            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
        }

        boolean success = userDAO.addUser(name, email, password, role);

        if (!success) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // ==============================
    // ADMIN UPDATE USER
    // ==============================
    public void updateUserByAdmin(int userId, String name, String email, String role)
            throws ErrorMessages.AppException {

        if (userDAO.checkEmailExistForUpdate(email, userId)) {
            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
        }

        boolean success = userDAO.updateUserByAdmin(userId, name, email, role);

        if (!success) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // ==============================
    // LOCK / UNLOCK USER
    // ==============================
    public boolean updateUserStatus(int userId, int newStatus) {
        return userDAO.updateUserStatus(userId, newStatus);
    }

    // ==============================
    // SEARCH USER
    // ==============================
    public List<User> searchUsers(String query, int isActive, String role) {

        if (role == null || role.trim().isEmpty()) {
            role = "DOCTOR";
        }

        return userDAO.searchUsers(query, isActive, role.trim());
    }

    // ==============================
    // DELETE USER (SOFT DELETE)
    // ==============================
   public void deleteUser(int id) throws Exception {
    userDAO.deleteUser(id);
}

    // ==============================
    // GET DELETED USERS
    // ==============================
    public List<User> getDeletedUsers() throws Exception {
        return userDAO.getDeletedUsers();
    }

    // ==============================
    // RESTORE USER
    // ==============================
    public void restoreUser(int id) throws Exception {
        userDAO.restoreUser(id);
    }

    // ==============================
    // GET USER BY ID
    // ==============================
    public User getUserById(int id) throws Exception {
        return userDAO.getUserById(id);
    }

    // ==============================
    // UPDATE AVATAR
    // ==============================
    public void updateAvatar(int userId, String avatarUrl) throws Exception {

        if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
            throw new Exception("Avatar không hợp lệ!");
        }

        boolean success = userDAO.updateAvatar(userId, avatarUrl);

        if (!success) {
            throw new Exception("Không thể cập nhật avatar!");
        }
    }

}