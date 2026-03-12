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
<<<<<<< cak
    private static final String DB_NAME = "DB_Hospital_PRJ_01";
    private static final String DB_USER_NAME = "sa";
    private static final String DB_PASSWORD = "12345";
=======
    
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
    
    
    
    
    
    
    private static final String DB_SERVER = "DB_Hospital_PRJ_Management.mssql.somee.com";
    private static final String DB_NAME = "DB_Hospital_PRJ_Management";
    private static final String DB_USER_NAME = "yuikiri_SQLLogin_2";
    private static final String DB_PASSWORD = "tibqur9648";
>>>>>>> main

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        // Cú pháp đặc trị cho Java kết nối SQL Server trên mạng
        // Bắt buộc phải có encrypt=false;trustServerCertificate=true để không bị chặn SSL
        String url = "jdbc:sqlserver://" + DB_SERVER + ";databaseName=" + DB_NAME 
                   + ";encrypt=false;trustServerCertificate=true";
        
        return DriverManager.getConnection(url, DB_USER_NAME, DB_PASSWORD);
    }
    
    public static void main(String[] args) {
        try {
            System.out.println("Đang gọi lên Server: " + DB_SERVER + " ...");
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("🔥 CHÚC MỪNG SẾP! Database đã lên mây thành công!");
                System.out.println("Chi tiết Connection: " + conn);
                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            System.err.println("Lỗi: Kiểm tra lại xem đã Add file sqljdbc4.jar vào thư viện chưa!");
        } catch (SQLException ex) {
            System.err.println("Lỗi SQL: Không thể kết nối. Hãy xem kỹ chi tiết lỗi bên dưới:");
            ex.printStackTrace();
        }
    }
}
