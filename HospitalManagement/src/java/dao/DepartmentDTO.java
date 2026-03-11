/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class DepartmentDTO {
    private int id;
    private String name;
    private String description;
    private int isActive;
    //construxctor

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
        return "DepartmentDTO{" + "id=" + id + ", name=" + name + ", description=" + description + ", isActive=" + isActive + '}';
    }
    
}
