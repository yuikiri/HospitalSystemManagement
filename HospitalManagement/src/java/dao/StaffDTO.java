package dao;

public class StaffDTO {

    private int id;
    private int userId;

    // từ bảng Users
    private String email;
    private String avatarUrl;
    private int isActive;
    private String createdAt;

    // từ bảng Staffs
    private String name;
    private int gender;
    private String position;
    private String phone;

    public StaffDTO() {
    }

    public StaffDTO(int id, int userId, String email, String avatarUrl, int isActive,
                    String name, int gender, String position, String phone, String createdAt) {

        this.id = id;
        this.userId = userId;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.isActive = isActive;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public int getIsActive() {
        return isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public String getPosition() {
        return position;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "StaffDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", email=" + email +
                ", avatarUrl=" + avatarUrl +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", name=" + name +
                ", gender=" + gender +
                ", position=" + position +
                ", phone=" + phone +
                '}';
    }
}