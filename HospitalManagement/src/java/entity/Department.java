// entity/departments.java
package entity;

public class Department {
    private int id;
    private String name;
    private String description;
    private int isActive;
    //constructor

    public Department() {
    }

    public Department(int id, String name, String description, int isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "Department{" + "id=" + id + ", name=" + name + ", description=" + description + ", isActive=" + isActive + '}';
    }

    
}