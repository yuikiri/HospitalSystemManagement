package dao;

import java.sql.Timestamp;

public class UserDTO {

    private int id;
    private String userName;
    private String email;
    private String avatarUrl;
    private String role;
    private int isActive;
    private Timestamp createdAt;

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
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.role = role;
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
        return "UserDTO{" +
                "id=" + id +
                ", userName=" + userName +
                ", email=" + email +
                ", avatarUrl=" + avatarUrl +
                ", role=" + role +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}