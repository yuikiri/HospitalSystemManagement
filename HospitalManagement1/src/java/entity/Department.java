// entity/departments.java
package entity;

public class Department {
    private int id;
    private String name;
    private String description;
    //constructor

    public Department() {
    }

    public Department(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    @Override
    public String toString() {
        return "Department{" + "id=" + id + ", name=" + name + ", description=" + description + '}';
    }
    

    
}