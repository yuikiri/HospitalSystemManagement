package dao;

public class DepartmentDTO {

    private int id;
    private String name;
    private String description;
    private int isActive;

    public DepartmentDTO() {
    }

    public DepartmentDTO(int id, String name, String description, int isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "DepartmentDTO{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                ", isActive=" + isActive +
                '}';
    }
}