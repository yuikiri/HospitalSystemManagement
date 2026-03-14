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
u.setRole(rs.getString("role"));
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

        String sql = "UPDATE Users SET userName=?, email=?, role=? WHERE id=?";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userName);
            ps.setString(2, email);
            ps.setString(3, role);
            ps.setInt(4, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
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

    public boolean addUserByAdmin(String userName, String email, String password, String role) {

        String sql = "INSERT INTO Users(userName,email,passwordHash,role,isActive,createdAt) VALUES(?,?,?,?,1,GETDATE())";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userName);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // ==============================
// SEARCH USER BY EMAIL
// ==============================

public List<User> searchUsers(String email, String isActive){

    List<User> list = new ArrayList<>();

    String sql = "SELECT * FROM Users WHERE 1=1";

    if(email != null && !email.isEmpty()){
        sql += " AND email LIKE ?";
    }

    if(isActive != null && !isActive.isEmpty()){
        sql += " AND isActive = ?";
    }

    try{

        Connection conn = DbUtils.getConnection();

        PreparedStatement ps = conn.prepareStatement(sql);

        int index = 1;

        if(email != null && !email.isEmpty()){
            ps.setString(index++, "%" + email + "%");
        }

        if(isActive != null && !isActive.isEmpty()){
            ps.setInt(index++, Integer.parseInt(isActive));
        }

        ResultSet rs = ps.executeQuery();

        while(rs.next()){

            User u = new User();

            u.setId(rs.getInt("id"));
            u.setUserName(rs.getString("userName"));
            u.setEmail(rs.getString("email"));
            u.setRole(rs.getString("role"));
            u.setIsActive(rs.getInt("isActive"));

            list.add(u);
        }

    }catch(Exception e){
        e.printStackTrace();
    }

    return list;
}

// ==============================
// FILTER USER STATUS
// ==============================

public List<User> filterUserStatus(String role, int status){

    List<User> list = new ArrayList<>();

    String sql = "SELECT * FROM Users WHERE role=? AND isActive=?";

    try(Connection conn = new DbUtils().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){

        ps.setString(1, role);
        ps.setInt(2, status);

        ResultSet rs = ps.executeQuery();

        while(rs.next()){

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

    }catch(Exception e){
        e.printStackTrace();
    }

    return list;
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
public List<User> getDeletedUsers() throws Exception {

    List<User> list = new ArrayList<>();

    String sql = "SELECT * FROM Users WHERE isActive = -1";

    Connection conn = DbUtils.getConnection();
    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    while(rs.next()){

        User u = new User();

        u.setId(rs.getInt("id"));
        u.setUserName(rs.getString("userName"));
        u.setEmail(rs.getString("email"));
        u.setRole(rs.getString("role"));
        u.setIsActive(rs.getInt("isActive"));

        list.add(u);
    }

    return list;
}
public void restoreUser(int id) throws Exception {

    String sql = "UPDATE Users SET isActive = 1 WHERE id = ?";

    Connection conn = DbUtils.getConnection();
    PreparedStatement ps = conn.prepareStatement(sql);

    ps.setInt(1, id);

    ps.executeUpdate();
}
public void deleteUser(int id) throws Exception {

    String sql = "UPDATE Users SET isActive = -1 WHERE id = ?";

    Connection conn = DbUtils.getConnection();
    PreparedStatement ps = conn.prepareStatement(sql);

    ps.setInt(1, id);

    ps.executeUpdate();
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
    //=====================================================
    //=====================================================

}