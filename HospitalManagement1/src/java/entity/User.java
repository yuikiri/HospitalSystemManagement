/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.sql.Timestamp;

/**
 *
 * @author Yuikiri
 */
public class User {
    private int id;
    private String userName;
    private String email;
    private String passwordHash;
    private String role;
    private int isActive; // 1: Active, 0: Banned
    private Timestamp createdAt;
    //constructor

    public User() {
    }

    public User(int id, String userName, String email, String passwordHash, String role, int isActive, Timestamp createdAt) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

  
    //

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
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
        return "User{" + "id=" + id + ", userName=" + userName + ", email=" + email + ", passwordHash=" + passwordHash + ", role=" + role + ", isActive=" + isActive + ", createdAt=" + createdAt + '}';
    }
    
    
}