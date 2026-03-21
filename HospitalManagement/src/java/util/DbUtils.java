/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Computing Fundamental - HCM Campus
 */
public class DbUtils {
//    Do not change this code
//    private static final String DB_NAME = "DB_Hospital_PRJ_01";
//    private static final String DB_USER_NAME = "sa";
//    private static final String DB_PASSWORD = "1234512345";
//
//    public static Connection getConnection() throws ClassNotFoundException, SQLException {
//        Connection conn = null;
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        String url = "jdbc:sqlserver://localhost:1433;databaseName=" + DB_NAME;
//        conn = DriverManager.getConnection(url, DB_USER_NAME, DB_PASSWORD);
//        return conn;
//    }
//    
//    public static void main(String[] args) {
//        try {
//            System.out.println(getConnection());
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    ////////////////////////////
    // 1. SẾP ĐIỀN 4 THÔNG SỐ CỦA SOMEE VÀO ĐÂY NHÉ
    private static final String DB_SERVER = "DB_Hospital_PRJ_Management_02.mssql.somee.com"; // Điền Server / Data Source của Somee
    private static final String DB_NAME = "DB_Hospital_PRJ_Management_02";        // Điền Database Name
    private static final String DB_USER_NAME = "yuikiri_SQLLogin_3";     // Điền User ID
    private static final String DB_PASSWORD = "7vjfppbzx5";      // Điền Mật khẩu

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        // 2. CHÌA KHÓA QUAN TRỌNG: Nối DB_SERVER vào và thêm đuôi SSL (encrypt=true;trustServerCertificate=true;)
        String url = "jdbc:sqlserver://" + DB_SERVER + ":1433;databaseName=" + DB_NAME 
                   + ";encrypt=true;trustServerCertificate=true;";
                   
        conn = DriverManager.getConnection(url, DB_USER_NAME, DB_PASSWORD);
        return conn;
    }
    
    // Hàm main này sếp dùng để test kết nối luôn (Bấm chuột phải -> Run File)
    public static void main(String[] args) {
        try {
            System.out.println("Đang kết nối tới Server Somee bên Mỹ... Sếp đợi 2-3 giây nhé!");
            Connection testConn = getConnection();
            if (testConn != null) {
                System.out.println("✅ TING TING! KẾT NỐI THÀNH CÔNG: " + testConn);
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("❌ Lỗi: Không tìm thấy Driver JDBC. Sếp add file .jar vào Libraries chưa?");
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("❌ Lỗi SQL: Sếp kiểm tra lại Tên Server, User, Pass hoặc mạng nhé!");
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
