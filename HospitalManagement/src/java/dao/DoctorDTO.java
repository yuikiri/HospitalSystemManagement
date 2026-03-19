package dao;

public class DoctorDTO {
    private int id;
    private int userId;
    private String email;
    private String avatarUrl;
    private int isActive;
    private String name;
    private int gender;
    private String position;
    private String phone;
    private String licenseNumber;

    public DoctorDTO() {}

    // Constructor ĐẦY ĐỦ (Dùng cho Login)
    public DoctorDTO(int id, int userId, String email, String avatarUrl, int isActive, String name, int gender, String position, String phone, String licenseNumber) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.isActive = isActive;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
    }

    // Constructor 7 THAM SỐ (Dùng cho mapDoctor - KHÔNG ĐƯỢC THROW LỖI)
    public DoctorDTO(int id, int userId, String name, int gender, String position, String phone, String licenseNumber) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
    }

    // Getter & Setter (Nhấn Alt + Insert để tạo lại cho đầy đủ nhé)
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getAvatarUrl() { return avatarUrl; }
    public int getIsActive() { return isActive; }
    public String getName() { return name; }
    public int getGender() { return gender; }
    public String getPosition() { return position; }
    public String getPhone() { return phone; }
    public String getLicenseNumber() { return licenseNumber; }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    
}