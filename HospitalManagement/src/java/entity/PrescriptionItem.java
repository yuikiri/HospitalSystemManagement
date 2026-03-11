/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Yuikiri
 */
public class PrescriptionItem {
    private int id;
    private int prescriptionId; // Trỏ về Đơn thuốc tổng
    private int medicineId;     // Trỏ về kho thuốc
    private int quantity;
    private String dosage;      // VD: "1 viên"
    private String frequency;   // VD: "Sáng, Tối"
    private String duration;    // VD: "5 ngày"
    //constructor

    public PrescriptionItem() {
    }

    public PrescriptionItem(int id, int prescriptionId, int medicineId, int quantity, String dosage, String frequency, String duration) {
        this.id = id;
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public int getMedicineId() {
        return medicineId;
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
        return "PrescriptionItem{" + "id=" + id + ", prescriptionId=" + prescriptionId + ", medicineId=" + medicineId + ", quantity=" + quantity + ", dosage=" + dosage + ", frequency=" + frequency + ", duration=" + duration + '}';
    }

    
    
}
