/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Timestamp;

/**
 *
 * @author Yuikiri
 */
public class RoomTypeDTO {
    private int id;
    private String name;
    private double price;
    private Timestamp createdAt;
    private int isActive;
    //

    public RoomTypeDTO(int id, String name, double price, Timestamp createdAt, int isActive) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public int getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "RoomTypeDTO{" + "id=" + id + ", name=" + name + ", price=" + price + ", createdAt=" + createdAt + ", isActive=" + isActive + '}';
    }
    
}
