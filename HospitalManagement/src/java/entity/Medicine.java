/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Yuikiri
 */
public class Medicine {
    private int id;
    private String name;
    private double price;
    private int stock;
    private String unit;
    private String description;
    //constructor

    public Medicine() {
    }

    public Medicine(int id, String name, double price, int stock, String unit, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.unit = unit;
        this.description = description;
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

    public int getStock() {
        return stock;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Medicine{" + "id=" + id + ", name=" + name + ", price=" + price + ", stock=" + stock + ", unit=" + unit + ", description=" + description + '}';
    }
    
}
