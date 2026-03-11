/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class PrescriptionItemDTO {
    private final int itemId;
    private final String medicineName; // JOIN từ Medicines
    private final String unit;         // JOIN từ Medicines (Viên, Vỉ, Lọ...)
    private final int quantity;
    private final String dosage;
    private final String frequency;
    private final String duration;
    //constructor

    public PrescriptionItemDTO(int itemId, String medicineName, String unit, int quantity, String dosage, String frequency, String duration) {
        this.itemId = itemId;
        this.medicineName = medicineName;
        this.unit = unit;
        this.quantity = quantity;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
    }

    public int getItemId() {
        return itemId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getUnit() {
        return unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "PrescriptionItemDTO{" + "itemId=" + itemId + ", medicineName=" + medicineName + ", unit=" + unit + ", quantity=" + quantity + ", dosage=" + dosage + ", frequency=" + frequency + ", duration=" + duration + '}';
    }
    
}
