/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Timestamp;

/**
 *
 * @author Yuikiri
 */
public class UserDTO {
    private int id;
    private String userName;
    private String email;
    private String avatarUrl;
    private String role;
    private int isActive; // Rất quan trọng để JSP biết đường vẽ nút Khóa/Mở Khóa
    private Timestamp createdAt;

    //constructor

    public UserDTO() {
    }

    public UserDTO(int id, String userName, String email, String avatarUrl, String role, int isActive, Timestamp createdAt) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public UserDTO(int id, String userName, String email, String role) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getRole() {
        return role;
    }

    public int getIsActive() {
        return isActive;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "id=" + id + ", userName=" + userName + ", email=" + email + ", avatarUrl=" + avatarUrl + ", role=" + role + ", isActive=" + isActive + ", createdAt=" + createdAt + '}';
    }

    
    
}
