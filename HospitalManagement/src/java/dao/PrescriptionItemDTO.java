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
public class PrescriptionItemDTO {
    private int id;
    private int prescriptionId;
    
    private int medicineId;
    private String medicineName; // MỚI: Tên thuốc
    private String medicineUnit; // MỚI: Đơn vị tính (Viên, Gói)
    private double medicinePrice; // MỚI: Giá gốc của 1 viên (Để sau tính tiền)
    
    private int quantity;
    private String dosage;
    private String frequency;
    private String duration;
    //constructor

    public PrescriptionItemDTO() {
    }

    public PrescriptionItemDTO(int id, int prescriptionId, int medicineId, String medicineName, String medicineUnit, double medicinePrice, int quantity, String dosage, String frequency, String duration) {
        this.id = id;
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.medicineUnit = medicineUnit;
        this.medicinePrice = medicinePrice;
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

    public String getMedicineName() {
        return medicineName;
    }

    public String getMedicineUnit() {
        return medicineUnit;
    }

    public double getMedicinePrice() {
        return medicinePrice;
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
        return "PrescriptionItemDTO{" + "id=" + id + ", prescriptionId=" + prescriptionId + ", medicineId=" + medicineId + ", medicineName=" + medicineName + ", medicineUnit=" + medicineUnit + ", medicinePrice=" + medicinePrice + ", quantity=" + quantity + ", dosage=" + dosage + ", frequency=" + frequency + ", duration=" + duration + '}';
    }

}
