/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Yuikiri
 */
public class RoomType {
    private int id;
    private String name;
    private double price;
    private String description; // Có thể null trong DB, nhưng ta vẫn lấy lên
    private int isActive; // 1: Active, 0: Banned
    //constructor

    public RoomType() {
    }

    public RoomType(int id, String name, double price, String description, int isActive) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "RoomType{" + "id=" + id + ", name=" + name + ", price=" + price + ", description=" + description + ", isActive=" + isActive + '}';
    }

    
    
}
