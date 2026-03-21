package dao;

import entity.User;
import util.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // ==============================
    // LOGIN
    // ==============================

    public User checkLogin(String email, String password) {

        String sql = "SELECT * FROM Users WHERE email = ? AND passwordHash = ?";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new User(
                        rs.getInt("id"),
                        rs.getString("userName"),
                        rs.getString("email"),
                        "",
                        rs.getString("passwordHash"),
                        rs.getString("role").trim(),
                        rs.getInt("isActive"),
                        rs.getTimestamp("createdAt")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


  

    // ==============================
    // REGISTER PATIENT
    // ==============================
    // 1. Kiểm tra Email có tồn tại không
    // 2. Đổi mật khẩu dựa trên Email
    public void updatePasswordByEmail(String email, String newPassword) {
        String query = "UPDATE Users SET password = ? WHERE email = ?";
        try (java.sql.Connection conn = new util.DbUtils().getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newPassword); // Nhớ mã hóa MD5/SHA nếu sếp có dùng nhé
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

     public boolean checkEmailExist(String email) {

        String sql = "SELECT id FROM Users WHERE email = ?";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

public List<User> getAllUsers(){

    List<User> list = new ArrayList<>();

    String sql = "SELECT * FROM Users\n" +
"WHERE isActive != -1\n" +
"ORDER BY id DESC";

    try{

        Connection conn = DbUtils.getConnection();

        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while(rs.next()){

           User u = new User();

u.setId(rs.getInt("id"));
u.setUserName(rs.getString("userName"));
u.setEmail(rs.getString("email"));
u.setRole(rs.getString("role").toUpperCase());
u.setIsActive(rs.getInt("isActive"));

list.add(u);

        }

    }catch(Exception e){
        e.printStackTrace();
    }

    return list;
}
    public boolean registerPatient(String name,
                                   String email,
                                   String password,
                                   String phone,
                                   String address) {

        String insertUser = "INSERT INTO Users(userName,email,passwordHash,role,isActive,createdAt) "
                + "VALUES(?,?,?,?,1,GETDATE())";

        String insertPatient = "INSERT INTO Patients(userId,name,phone,address) VALUES(?,?,?,?)";

        try (Connection conn = new DbUtils().getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement psUser = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);

            psUser.setString(1, name);
            psUser.setString(2, email);
            psUser.setString(3, password);
            psUser.setString(4, "PATIENT");

            psUser.executeUpdate();

            ResultSet rs = psUser.getGeneratedKeys();

            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            int userId = rs.getInt(1);

            PreparedStatement psPatient = conn.prepareStatement(insertPatient);

            psPatient.setInt(1, userId);
            psPatient.setString(2, name);
            psPatient.setString(3, phone);
            psPatient.setString(4, address);

            psPatient.executeUpdate();

            conn.commit();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;
    }



    
    // ==============================
    // GET USER BY ROLE
    // ==============================

    public List<User> getUsersByRole(String role) {

        List<User> list = new ArrayList<>();

        String sql = "SELECT * FROM Users WHERE role=? ORDER BY id DESC";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("userName"),
                        rs.getString("email"),
                        "",
                        "",
                        rs.getString("role").trim(),
                        rs.getInt("isActive"),
                        rs.getTimestamp("createdAt")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==============================
    // UPDATE USER (ADMIN)
    // ==============================

   public boolean updateUserByAdmin(int userId, String userName, String email, String role) {

    Connection conn = null;

    try {

        conn = DbUtils.getConnection();
        conn.setAutoCommit(false);

        // update USERS
        String sqlUser = "UPDATE Users SET userName=?, email=?, role=? WHERE id=?";

        PreparedStatement psUser = conn.prepareStatement(sqlUser);

        psUser.setString(1, userName);
        psUser.setString(2, email);
        psUser.setString(3, role.toUpperCase().trim());
        psUser.setInt(4, userId);

        psUser.executeUpdate();

        String r = role.toUpperCase().trim();

        if (r.equals("DOCTOR")) {

            String sql = "UPDATE Doctors SET name=? WHERE userId=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }

        else if (r.equals("STAFF")) {

            String sql = "UPDATE Staffs SET name=? WHERE userId=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }

        else if (r.equals("PATIENT")) {

            String sql = "UPDATE Patients SET name=? WHERE userId=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }

        conn.commit();

        return true;

    } catch (Exception e) {

        try {
            if (conn != null) conn.rollback();
        } catch (Exception ex) {}

        e.printStackTrace();
    }

    return false;
}

    // ==============================
    // LOCK USER
    // ==============================

   public void toggleUserStatus(int id) throws Exception {

    String sql = "UPDATE Users SET isActive = CASE WHEN isActive = 1 THEN 0 ELSE 1 END WHERE id = ? AND isActive != -1";

    Connection conn = DbUtils.getConnection();
    PreparedStatement ps = conn.prepareStatement(sql);

    ps.setInt(1, id);

    ps.executeUpdate();
}
    // ==============================
    // ADD USER BY ADMIN
    // ==============================

 
    // ==============================
// SEARCH USER BY EMAIL
// ==============================

public List<User> searchUsers(String query, int isActive, String role) {

    List<User> list = new ArrayList<>();

    String sql = "SELECT u.*, COALESCE(d.name, s.name, p.name, u.userName) AS trueName "
            + "FROM Users u "
            + "LEFT JOIN Doctors d ON u.id = d.userId "
            + "LEFT JOIN Staffs s ON u.id = s.userId "
            + "LEFT JOIN Patients p ON u.id = p.userId "
            + "WHERE u.role = ? AND u.isActive != -1";

    if (isActive != -99) {
        sql += " AND u.isActive = ?";
    }

    if (query != null && !query.trim().isEmpty()) {
        sql += " AND (u.email LIKE ? OR COALESCE(d.name,s.name,p.name,u.userName) LIKE ?)";
    }

    try (Connection conn = DbUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, role.toUpperCase().trim());

        int idx = 2;

        if (isActive != -99) {
            ps.setInt(idx++, isActive);
        }

        if (query != null && !query.trim().isEmpty()) {
            String p = "%" + query + "%";
            ps.setString(idx++, p);
            ps.setString(idx++, p);
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            User u = new User();

            u.setId(rs.getInt("id"));
            u.setEmail(rs.getString("email"));
            u.setUserName(rs.getString("trueName"));
            u.setRole(rs.getString("role"));
            u.setIsActive(rs.getInt("isActive"));

            list.add(u);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
public boolean updateUserStatus(int userId, int status) {
    String sql = "UPDATE Users SET isActive = ? WHERE id = ?";
    try (Connection conn = DbUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, status);
        ps.setInt(2, userId);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
public boolean addUser(String name, String email, String pass, String role) {

    Connection conn = null;

    try {

        conn = DbUtils.getConnection();
        conn.setAutoCommit(false);

        String sqlUser = "INSERT INTO Users(userName,email,passwordHash,role,isActive,createdAt) VALUES(?,?,?,?,1,GETDATE())";

        PreparedStatement psUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);

        psUser.setString(1, name);
        psUser.setString(2, email);
        psUser.setString(3, pass);
        psUser.setString(4, role.toUpperCase());

        psUser.executeUpdate();

        ResultSet rs = psUser.getGeneratedKeys();

        int userId = -1;

        if (rs.next()) {
            userId = rs.getInt(1);
        }

        if (userId == -1) {
            conn.rollback();
            return false;
        }

        String r = role.toLowerCase();

        if (r.equals("doctor")) {

            String sqlDoctor =
                    "INSERT INTO Doctors(userId,name,gender,position,phone,licenseNumber) " +
                    "VALUES(?, ?, 1, 'Doctor', '0000000000', 'TEMP')";

            PreparedStatement ps = conn.prepareStatement(sqlDoctor);

            ps.setInt(1, userId);
            ps.setString(2, name);

            ps.executeUpdate();
        }

        else if (r.equals("staff")) {

            String sqlStaff =
                    "INSERT INTO Staffs(userId,name,gender,position,phone) " +
                    "VALUES(?, ?, 1, 'Staff', '0000000000')";

            PreparedStatement ps = conn.prepareStatement(sqlStaff);

            ps.setInt(1, userId);
            ps.setString(2, name);

            ps.executeUpdate();
        }

        else if (r.equals("patient")) {

            String sqlPatient =
                    "INSERT INTO Patients(userId,name) VALUES(?,?)";

            PreparedStatement ps = conn.prepareStatement(sqlPatient);

            ps.setInt(1, userId);
            ps.setString(2, name);

            ps.executeUpdate();
        }

        conn.commit();

        return true;

    } catch (Exception e) {

        try {
            if (conn != null) conn.rollback();
        } catch (Exception ex) {}

        e.printStackTrace();
    }

    return false;
}
// ==============================
// CHECK EMAIL EXIST FOR UPDATE
// ==============================

public boolean checkEmailExistForUpdate(String email, int userId){

    String sql = "SELECT id FROM Users WHERE email = ? AND id <> ?";

    try(Connection conn = new DbUtils().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){

        ps.setString(1, email);
        ps.setInt(2, userId);

        ResultSet rs = ps.executeQuery();

        return rs.next();

    }catch(Exception e){
        e.printStackTrace();
    }

    return true;
}
// ==============================
// UPDATE EMAIL
// ==============================

public boolean updateEmail(int userId, String email){

    String sql = "UPDATE Users SET email=? WHERE id=?";

    try(Connection conn = new DbUtils().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){

        ps.setString(1, email);
        ps.setInt(2, userId);

        return ps.executeUpdate() > 0;

    }catch(Exception e){
        e.printStackTrace();
    }

    return false;
}
// GET DELETED USERS
public List<User> getDeletedUsers() {

    List<User> list = new ArrayList<>();

    String sql = "SELECT * FROM Users WHERE isActive = -1";

    try (Connection conn = DbUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            User u = new User();

            u.setId(rs.getInt("id"));
            u.setUserName(rs.getString("userName"));
            u.setEmail(rs.getString("email"));
            u.setRole(rs.getString("role"));
            u.setIsActive(rs.getInt("isActive"));

            list.add(u);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

public void restoreUser(int userId) throws Exception {

    String sql = "UPDATE Users SET isActive = 1 WHERE id=?";

    try (Connection conn = DbUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, userId);
        ps.executeUpdate();

    }

}
public void deleteUser(int userId) throws Exception {

    String sql = "UPDATE Users SET isActive = -1 WHERE id=?";

    try (Connection conn = DbUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, userId);
        ps.executeUpdate();

    }

}
public User getUserById(int id) throws Exception{

    String sql = "SELECT * FROM Users WHERE id = ?";

    Connection conn = DbUtils.getConnection();
    PreparedStatement ps = conn.prepareStatement(sql);

    ps.setInt(1, id);

    ResultSet rs = ps.executeQuery();

    if(rs.next()){

        User u = new User();

        u.setId(rs.getInt("id"));
        u.setUserName(rs.getString("userName"));
        u.setEmail(rs.getString("email"));
        u.setRole(rs.getString("role"));
        u.setIsActive(rs.getInt("isActive"));

        return u;
    }

    return null;
}

    //=====================================================
    //////////////////////////////////////Hoàng
    //=====================================================
    // UPDATE AVATAR
    public boolean updateAvatar(int userId, String avatarUrl) {
        String sql = "UPDATE Users SET avatarUrl = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, avatarUrl);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // 1. Kiểm tra Email đã tồn tại chưa (Dùng khi đổi Email)
    

    // 3. Kiểm tra Mật khẩu hiện tại (Dùng bảo mật)
    public boolean checkCurrentPassword(int userId, String currentPassword) {
        String sql = "SELECT id FROM Users WHERE id = ? AND passwordHash = ?";
        try (Connection conn = new util.DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, currentPassword);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 4. Cập nhật Mật khẩu mới
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE Users SET passwordHash = ? WHERE id = ?";
        try (Connection conn = new util.DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    //=====================================================
    //=====================================================

}