package entity;

import java.sql.Timestamp;

public class User {

    private int id;
    private String userName;
    private String email;
    private String avatarUrl;
    private String passwordHash;
    private String role;
    private int isActive;
    private Timestamp createdAt;

    // Constructor
    public User() {
    }

    public User(int id, String userName, String email, String avatarUrl,
            String passwordHash, String role, int isActive, Timestamp createdAt) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Getter
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

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName=" + userName +
                ", email=" + email +
                ", avatarUrl=" + avatarUrl +
                ", passwordHash=" + passwordHash +
                ", role=" + role +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}