/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class MedicineDTO {
    private int id;
    private String name;
    private String unit;
    private double price;
    private int stockQuantity;
    private String description;
    private int isActive;
    //constructor

    public MedicineDTO() {
    }

    public MedicineDTO(int id, String name, String unit, double price, int stockQuantity, String description, int isActive) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public int getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "MedicineDTO{" + "id=" + id + ", name=" + name + ", unit=" + unit + ", price=" + price + ", stockQuantity=" + stockQuantity + ", description=" + description + ", isActive=" + isActive + '}';
    }
    
}
