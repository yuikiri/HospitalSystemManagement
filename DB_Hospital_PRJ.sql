/* ================================
   RESET DATABASE
================================ */
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'DB_Hospital_PRJ_01')
BEGIN
    ALTER DATABASE DB_Hospital_PRJ_01 SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE DB_Hospital_PRJ_01;
END
GO

CREATE DATABASE DB_Hospital_PRJ_01;
GO

USE DB_Hospital_PRJ_01;
GO

/* ================================
   USERS
================================ */
CREATE TABLE Users (
    id INT IDENTITY PRIMARY KEY,
    userName NVARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
	avatarUrl NVARCHAR(250),
    passwordHash NVARCHAR(255) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('admin','doctor','staff','patient')),
    isActive Int,
    createdAt DATETIME2
);

/* ================================
   DEPARTMENTS
================================ */
CREATE TABLE Departments (
    id INT IDENTITY PRIMARY KEY,
    name NVARCHAR(255) NOT NULL UNIQUE,
    description NVARCHAR(MAX),
	isActive Int
);

/* ================================
   ROOM TYPE
================================ */
CREATE TABLE RoomType (
    id INT IDENTITY PRIMARY KEY,
    name NVARCHAR(255) NOT NULL UNIQUE,
    price decimal(18,2) NOT NULL,
    createdAt DATETIME2,
	isActive Int
);

/* ================================
   DOCTORS
================================ */
CREATE TABLE Doctors (
    id INT IDENTITY PRIMARY KEY,
    userId INT NOT NULL UNIQUE,
    name NVARCHAR(255) NOT NULL,
	gender int,
    position NVARCHAR(100) NOT NULL,
    phone varchar(15),
    licenseNumber VARCHAR(100) NOT NULL,
    CONSTRAINT FK_Doctors_Users FOREIGN KEY (userId) REFERENCES Users(id)
);

/* ================================
   STAFFS
================================ */
CREATE TABLE Staffs (
    id INT IDENTITY PRIMARY KEY,
    userId INT NOT NULL UNIQUE,
    name NVARCHAR(255) NOT NULL,
	gender int,
    position VARCHAR(100) NOT NULL,
    phone varchar(15),
    CONSTRAINT FK_Staffs_Users FOREIGN KEY (userId) REFERENCES Users(id)
);

/* ================================
   PATIENTS
================================ */
CREATE TABLE Patients (
    id INT IDENTITY PRIMARY KEY,
    userId INT UNIQUE NULL,
    name NVARCHAR(255) NOT NULL,
    dob DATE,
    gender int,
    phone varchar(15),
    address NVARCHAR(MAX),
    CONSTRAINT FK_Patients_Users FOREIGN KEY (userId) REFERENCES Users(id)
);

/* ================================
   DOCTOR - DEPARTMENT
================================ */
CREATE TABLE DoctorDepartments (
    doctorId INT NOT NULL,
    departmentId INT NOT NULL,
    CONSTRAINT FK_DD_Doctors FOREIGN KEY (doctorId) REFERENCES Doctors(id),
    CONSTRAINT FK_DD_Departments FOREIGN KEY (departmentId) REFERENCES Departments(id)
);

/* ================================
   STAFF - DEPARTMENT
================================ */
CREATE TABLE StaffDepartments (
    staffId INT NOT NULL,
    departmentId INT NOT NULL,
    CONSTRAINT FK_SD_Staffs FOREIGN KEY (staffId) REFERENCES Staffs(id),
    CONSTRAINT FK_SD_Departments FOREIGN KEY (departmentId) REFERENCES Departments(id)
);

/* ================================
   ROOMS
================================ */
CREATE TABLE Rooms (
    id INT IDENTITY PRIMARY KEY,
    departmentId INT NOT NULL,
    roomType INT NOT NULL,
    roomNumber INT NOT NULL,
    status VARCHAR(30),
	isActive Int,
    CONSTRAINT FK_Rooms_Departments FOREIGN KEY (departmentId) REFERENCES Departments(id),
    CONSTRAINT FK_Rooms_RoomType FOREIGN KEY (roomType) REFERENCES RoomType(id)
);

/* ================================
   SHIFTS (DUTY)
================================ */
CREATE TABLE Shifts (
    id INT IDENTITY PRIMARY KEY,
    roomId INT NOT NULL,
    startTime DATETIME2 NOT NULL,
    endTime DATETIME2 NOT NULL,
    status VARCHAR(30),
	isActive Int,
    note NVARCHAR(MAX),
    CONSTRAINT FK_Shifts_Rooms FOREIGN KEY (roomId) REFERENCES Rooms(id)
);

/* ================================
   DOCTOR SHIFTS
================================ */
CREATE TABLE DoctorShifts (
    doctorId INT NOT NULL,
    shiftId INT NOT NULL,
    role VARCHAR(20),
    CONSTRAINT FK_DS_Doctors FOREIGN KEY (doctorId) REFERENCES Doctors(id),
    CONSTRAINT FK_DS_Shifts FOREIGN KEY (shiftId) REFERENCES Shifts(id),
    CONSTRAINT UQ_Doctor_Shift UNIQUE (doctorId, shiftId)
);

/* ================================
   STAFF SHIFTS
================================ */
CREATE TABLE StaffShifts (
    staffId INT NOT NULL,
    shiftId INT NOT NULL,
    role NVARCHAR(50),
    CONSTRAINT FK_SS_Staffs FOREIGN KEY (staffId) REFERENCES Staffs(id),
    CONSTRAINT FK_SS_Shifts FOREIGN KEY (shiftId) REFERENCES Shifts(id)
);

/* ================================
   APPOINTMENTS
================================ */
CREATE TABLE Appointments (
    id INT IDENTITY PRIMARY KEY,
    patientId INT NOT NULL,
    doctorId INT NOT NULL,
    roomId INT NOT NULL,
    startTime DATETIME2 NOT NULL,
	endTime DATETIME2,
    status VARCHAR(30),
    createdAt DATETIME2,
	isActive Int,
    CONSTRAINT FK_App_Patients FOREIGN KEY (patientId) REFERENCES Patients(id),
    CONSTRAINT FK_App_Doctors FOREIGN KEY (doctorId) REFERENCES Doctors(id),
    CONSTRAINT FK_App_Rooms FOREIGN KEY (roomId) REFERENCES Rooms(id)
);

/* ================================
   MEDICAL RECORDS
================================ */
CREATE TABLE MedicalRecords (
    id INT IDENTITY PRIMARY KEY,
    appointmentId INT NOT NULL UNIQUE,
    diagnosis NVARCHAR(MAX) NOT NULL,
    notes NVARCHAR(MAX),
    createdAt DATETIME2,
	isActive Int,
    CONSTRAINT FK_MR_Appointments FOREIGN KEY (appointmentId) REFERENCES Appointments(id)
);

/* ================================
   PRESCRIPTIONS
================================ */
CREATE TABLE Prescriptions (
    id INT IDENTITY PRIMARY KEY,
    medicalRecordId INT NOT NULL UNIQUE,
    notes NVARCHAR(MAX),
    createdAt DATETIME2,
    status VARCHAR(30),
	isActive Int,
    CONSTRAINT FK_Pres_MR FOREIGN KEY (medicalRecordId) REFERENCES MedicalRecords(id)
);

/* ================================
   MEDICINES
================================ */
CREATE TABLE Medicines (
    id INT IDENTITY PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    unit CHAR(20) NOT NULL,
    price decimal(18,2) NOT NULL,
	stockquantity int default 0,
    description NVARCHAR(MAX),
	isActive Int
);

/* ================================
   PRESCRIPTION ITEMS
================================ */
CREATE TABLE PrescriptionItems (
    id INT IDENTITY PRIMARY KEY,
    prescriptionId INT NOT NULL,
    medicineId INT NOT NULL,
    quantity INT NOT NULL,
    dosage nvarchar(100),
	frequency nvarchar(100),
	duration nvarchar(100),
    CONSTRAINT FK_PI_Pres FOREIGN KEY (prescriptionId) REFERENCES Prescriptions(id),
    CONSTRAINT FK_PI_Med FOREIGN KEY (medicineId) REFERENCES Medicines(id)
);

/* ================================
   PAYMENTS
================================ */
CREATE TABLE Payments (
    id INT IDENTITY PRIMARY KEY,
    medicalRecordId INT NOT NULL UNIQUE,
    totalAmount decimal(18,2) NOT NULL,
    paymentMethod VARCHAR(20) CHECK (paymentMethod IN ('cash','card','banking')),
    status VARCHAR(30),
    paidAt DATETIME2,
	isActive Int,
    CONSTRAINT FK_Pay_MR FOREIGN KEY (medicalRecordId) REFERENCES MedicalRecords(id)
);





/* ================================
   SAMPLE DATA
================================ */

/* ==========================================================
   1. DỮ LIỆU USERS (Tạo 16 User: 1 Admin, 5 Doctor, 5 Staff, 5 Patient)
========================================================== */
INSERT INTO Users (userName, email, avatarUrl, passwordHash, role, isActive, createdAt) VALUES
('Admin Manager', 'admin@medicare.com', 'admin.png', '123456', 'admin', 1, GETDATE()),
('Dr. John Smith', 'john@medicare.com', 'dr_john.png', '123456', 'doctor', 1, GETDATE()),
('Dr. Sarah Lee', 'sarah@medicare.com', 'dr_sarah.png', '123456', 'doctor', 1, GETDATE()),
('Dr. Michael', 'michael@medicare.com', 'dr_michael.png', '123456', 'doctor', 1, GETDATE()),
('Dr. Emily', 'emily@medicare.com', 'dr_emily.png', '123456', 'doctor', 1, GETDATE()),
('Dr. David', 'david@medicare.com', 'dr_david.png', '123456', 'doctor', 1, GETDATE()),
('Alice Nguyen', 'alice@medicare.com', 'staff_alice.png', '123456', 'staff', 1, GETDATE()),
('Bob Johnson', 'bob@medicare.com', 'staff_bob.png', '123456', 'staff', 1, GETDATE()),
('Carol White', 'carol@medicare.com', 'staff_carol.png', '123456', 'staff', 1, GETDATE()),
('Denis Clark', 'denis@medicare.com', 'staff_denis.png', '123456', 'staff', 1, GETDATE()),
('Eva Martin', 'eva@medicare.com', 'staff_eva.png', '123456', 'staff', 1, GETDATE()),
('Nguyen Van A', 'nva@gmail.com', 'patient_a.png', '123456', 'patient', 1, GETDATE()),
('Tran Thi B', 'ttb@gmail.com', 'patient_b.png', '123456', 'patient', 1, GETDATE()),
('Le Van C', 'lvc@gmail.com', 'patient_c.png', '123456', 'patient', 1, GETDATE()),
('Pham Thi D', 'ptd@gmail.com', 'patient_d.png', '123456', 'patient', 1, GETDATE()),
('Hoang Van E', 'hve@gmail.com', 'patient_e.png', '123456', 'patient', 1, GETDATE());

/* ==========================================================
   2. DỮ LIỆU DEPARTMENTS
========================================================== */
INSERT INTO Departments (name, description, isActive) VALUES
(N'Khoa Tim Mạch', N'Điều trị bệnh lý tim mạch', 1),
(N'Khoa Nhi', N'Chăm sóc sức khỏe trẻ em', 1),
(N'Khoa Nội Tổng Hợp', N'Khám nội khoa chung', 1),
(N'Khoa Ngoại Thần Kinh', N'Phẫu thuật não, cột sống', 1),
(N'Khoa Sản', N'Chăm sóc thai kỳ', 1);

/* ==========================================================
   3. DỮ LIỆU ROOM TYPE
========================================================== */
INSERT INTO RoomType (name, price, createdAt, isActive) VALUES
(N'Khám Thường', 150000.00, GETDATE(), 1),
(N'Khám VIP', 500000.00, GETDATE(), 1),
(N'Cấp Cứu', 300000.00, GETDATE(), 1),
(N'Phẫu Thuật', 2000000.00, GETDATE(), 1),
(N'Lưu Bệnh 1 Giường', 1200000.00, GETDATE(), 1);

/* ==========================================================
   4. DỮ LIỆU DOCTORS (Khớp userId từ 2->6)
========================================================== */
INSERT INTO Doctors (userId, name, gender, position, phone, licenseNumber) VALUES
(2, N'John Smith', 1, N'Trưởng Khoa', '090111222', 'MED-101'),
(3, N'Sarah Lee', 0, N'Bác sĩ Chuyên Khoa', '090111333', 'MED-102'),
(4, N'Michael', 1, N'Bác sĩ Nội Khoa', '090111444', 'MED-103'),
(5, N'Emily', 0, N'Bác sĩ Phẫu Thuật', '090111555', 'MED-104'),
(6, N'David', 1, N'Bác sĩ Sản Khoa', '090111666', 'MED-105');

/* ==========================================================
   5. DỮ LIỆU STAFFS (Khớp userId từ 7->11)
========================================================== */
INSERT INTO Staffs (userId, name, gender, position, phone) VALUES
(7, N'Alice Nguyen', 0, N'Lễ Tân', '091222111'),
(8, N'Bob Johnson', 1, N'Điều Dưỡng', '091222333'),
(9, N'Carol White', 0, N'Kế Toán', '091222444'),
(10, N'Denis Clark', 1, N'Thủ Kho', '091222555'),
(11, N'Eva Martin', 0, N'Kỹ Thuật Viên', '091222666');

/* ==========================================================
   6. DỮ LIỆU PATIENTS (Khớp userId từ 12->16)
========================================================== */
INSERT INTO Patients (userId, name, dob, gender, phone, address) VALUES
(12, N'Nguyễn Văn A', '1990-05-15', 1, '098111222', N'Quận 1, TP.HCM'),
(13, N'Trần Thị B', '1985-10-22', 0, '098111333', N'Quận 3, TP.HCM'),
(14, N'Lê Văn C', '2010-02-28', 1, '098111444', N'Quận 5, TP.HCM'),
(15, N'Phạm Thị D', '1975-08-08', 0, '098111555', N'Quận 7, TP.HCM'),
(16, N'Hoàng Văn E', '1995-12-01', 1, '098111666', N'Quận Bình Thạnh, TP.HCM');

/* ==========================================================
   7. DỮ LIỆU DOCTOR DEPARTMENTS & STAFF DEPARTMENTS
========================================================== */
INSERT INTO DoctorDepartments (doctorId, departmentId) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5);

INSERT INTO StaffDepartments (staffId, departmentId) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5);

/* ==========================================================
   8. DỮ LIỆU ROOMS
========================================================== */
INSERT INTO Rooms (departmentId, roomType, roomNumber, status, isActive) VALUES
(1, 2, 101, 'available', 1),
(2, 1, 102, 'available', 1),
(3, 1, 103, 'maintenance', 1),
(4, 4, 201, 'available', 1),
(5, 5, 301, 'occupied', 1);

/* ==========================================================
   9. DỮ LIỆU SHIFTS & DOCTOR/STAFF SHIFTS
========================================================== */
INSERT INTO Shifts (roomId, startTime, endTime, status, isActive, note) VALUES
(1, '2026-03-12 07:00:00', '2026-03-12 11:00:00', 'scheduled', 1, N'Ca Sáng'),
(2, '2026-03-12 13:00:00', '2026-03-12 17:00:00', 'scheduled', 1, N'Ca Chiều'),
(4, '2026-03-13 07:00:00', '2026-03-13 11:00:00', 'scheduled', 1, N'Ca Sáng'),
(5, '2026-03-13 17:00:00', '2026-03-14 07:00:00', 'scheduled', 1, N'Ca Đêm'),
(1, '2026-03-14 07:00:00', '2026-03-14 11:00:00', 'scheduled', 1, N'Ca Sáng');

INSERT INTO DoctorShifts (doctorId, shiftId, role) VALUES
(1, 1, 'Main'), (2, 2, 'Main'), (4, 3, 'Surgeon'), (5, 4, 'On-call'), (1, 5, 'Main');

INSERT INTO StaffShifts (staffId, shiftId, role) VALUES
(1, 1, 'Support'), (2, 2, 'Nurse'), (5, 3, 'Technician'), (2, 4, 'Nurse'), (1, 5, 'Support');

/* ==========================================================
   10. DỮ LIỆU APPOINTMENTS
========================================================== */
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) VALUES
(1, 1, 1, '2026-03-12 08:00:00', '2026-03-12 08:30:00', 'confirmed', GETDATE(), 1),
(2, 2, 2, '2026-03-12 09:00:00', '2026-03-12 09:30:00', 'pending', GETDATE(), 1),
(3, 3, 3, '2026-03-12 10:00:00', '2026-03-12 10:30:00', 'cancelled', GETDATE(), 1),
(4, 4, 4, '2026-03-13 14:00:00', '2026-03-13 15:30:00', 'confirmed', GETDATE(), 1),
(5, 5, 5, '2026-03-14 08:00:00', '2026-03-14 08:30:00', 'confirmed', GETDATE(), 1);

/* ==========================================================
   11. DỮ LIỆU MEDICAL RECORDS & PRESCRIPTIONS
========================================================== */
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) VALUES
(1, N'Rối loạn nhịp tim', N'Cần nghỉ ngơi', GETDATE(), 1),
(4, N'Chấn thương vùng đầu', N'Theo dõi 24h', GETDATE(), 1),
(5, N'Thai kỳ khỏe mạnh', N'Bổ sung sắt', GETDATE(), 1);

INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) VALUES
(1, N'Uống sau ăn', GETDATE(), 'active', 1),
(2, N'Uống khi đau', GETDATE(), 'active', 1),
(3, N'Uống hàng ngày', GETDATE(), 'active', 1);

/* ==========================================================
   12. DỮ LIỆU MEDICINES & PRESCRIPTION ITEMS
========================================================== */
INSERT INTO Medicines (name, unit, price, stockquantity, description, isActive) VALUES
(N'Paracetamol 500mg', 'Viên', 2000.00, 1000, N'Hạ sốt', 1),
(N'Amoxicillin 250mg', 'Viên', 5000.00, 500, N'Kháng sinh', 1),
(N'Omeprazole 20mg', 'Viên', 8000.00, 300, N'Dạ dày', 1),
(N'Vitamin C', 'Viên', 4000.00, 200, N'Tăng đề kháng', 1),
(N'Oresol', 'Gói', 3000.00, 800, N'Bù nước', 1);

INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration) VALUES
(1, 3, 10, N'1 viên', N'Sáng, tối', N'5 ngày'),
(1, 4, 10, N'1 viên', N'Sáng', N'10 ngày'),
(2, 1, 20, N'1 viên', N'Khi đau', N'7 ngày'),
(2, 2, 15, N'1 viên', N'Sáng, trưa, tối', N'5 ngày'),
(3, 4, 30, N'1 viên', N'Sáng', N'30 ngày');

/* ==========================================================
   13. DỮ LIỆU PAYMENTS
========================================================== */
INSERT INTO Payments (medicalRecordId, totalAmount, paymentMethod, status, paidAt, isActive) VALUES
(1, 620000.00, 'banking', 'paid', '2026-03-12 09:00:00', 1),
(2, 2115000.00, 'cash', 'paid', '2026-03-13 16:00:00', 1),
(3, 120000.00, 'card', 'unpaid', NULL, 1);