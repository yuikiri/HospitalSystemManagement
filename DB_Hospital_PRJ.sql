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
    passwordHash NVARCHAR(255) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('admin','doctor','staff','patient')),
    isActive BIT,
    createdAt DATETIME2
);

/* ================================
   DEPARTMENTS
================================ */
CREATE TABLE Departments (
    id INT IDENTITY PRIMARY KEY,
    name NVARCHAR(255) NOT NULL UNIQUE,
    description NVARCHAR(MAX)
);

/* ================================
   ROOM TYPE
================================ */
CREATE TABLE RoomType (
    id INT IDENTITY PRIMARY KEY,
    name NVARCHAR(255) NOT NULL UNIQUE,
    price decimal(18,2) NOT NULL,
    createdAt DATETIME2
);

/* ================================
   DOCTORS
================================ */
CREATE TABLE Doctors (
    id INT IDENTITY PRIMARY KEY,
    userId INT NOT NULL UNIQUE,
    name NVARCHAR(255) NOT NULL,
    position VARCHAR(100) NOT NULL,
    phone varchar(15),
    licenseNumber VARCHAR(100) NOT NULL,
    status VARCHAR(50),
    CONSTRAINT FK_Doctors_Users FOREIGN KEY (userId) REFERENCES Users(id)
);

/* ================================
   STAFFS
================================ */
CREATE TABLE Staffs (
    id INT IDENTITY PRIMARY KEY,
    userId INT NOT NULL UNIQUE,
    name NVARCHAR(255) NOT NULL,
    position VARCHAR(100) NOT NULL,
    phone varchar(15),
    status VARCHAR(50),
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
    gender CHAR(15),
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
    status varchar(50),
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
    status BIT,
    note NVARCHAR(MAX),
    CONSTRAINT FK_Shifts_Rooms FOREIGN KEY (roomId) REFERENCES Rooms(id)
);

/* ================================
   DOCTOR SHIFTS
================================ */
CREATE TABLE DoctorShifts (
    doctorId INT NOT NULL,
    shiftId INT NOT NULL,
    role VARCHAR(20) CHECK (role IN ('main','support','on_call')),
    CONSTRAINT FK_DS_Doctors FOREIGN KEY (doctorId) REFERENCES Doctors(id),
    CONSTRAINT FK_DS_Shifts FOREIGN KEY (shiftId) REFERENCES Shifts(id),
    CONSTRAINT UQ_Doctor_Shift UNIQUE (doctorId, shiftId)
);

/* ================================
   STAFF SHIFTS
================================ */
CREATE TABLE StaffShifts (
    id INT IDENTITY PRIMARY KEY,
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
    status BIT,
    created_at DATETIME2,
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
    created_at DATETIME2,
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
    status BIT,
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
    description NVARCHAR(MAX)
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
    status BIT,
    paidAt DATETIME2,
    CONSTRAINT FK_Pay_MR FOREIGN KEY (medicalRecordId) REFERENCES MedicalRecords(id)
);





/* ================================================================
   TẦNG 1: DỮ LIỆU DANH MỤC & NỀN TẢNG (Không phụ thuộc khóa ngoại)
================================================================ */

-- 1. USERS (Tài khoản)
-- Chú ý: Cột role bị giới hạn CHECK ('admin','doctor','staff','patient')
INSERT INTO Users (userName, email, passwordHash, role, isActive, createdAt) VALUES 
(N'admin_hethong', 'admin@hospital.com', 'pass1', 'admin', 1, '2026-01-01'),
(N'bs_hoang', 'hoang@hospital.com', 'pass12', 'doctor', 1, '2026-01-02'),
(N'bs_lan', 'lan@hospital.com', 'pass13', 'doctor', 1, '2026-01-02'),
(N'nv_tuan', 'tuan@hospital.com', 'pass14', 'staff', 1, '2026-01-03'),
(N'bn_huong', 'huong@gmail.com', 'pass15', 'patient', 1, '2026-01-04'),
(N'bn_minh', 'minh@gmail.com', 'pass16', 'patient', 1, '2026-01-05');
-- => ID tự tăng sẽ lần lượt là 1 (admin), 2,3 (doctor), 4 (staff), 5,6 (patient)

-- 2. DEPARTMENTS (Khoa khám)
INSERT INTO Departments (name, description) VALUES 
(N'Khoa Tim Mạch', N'Chuyên khám và điều trị các bệnh lý về tim và mạch máu'),
(N'Khoa Nhi', N'Chuyên khám và điều trị bệnh cho trẻ em dưới 15 tuổi'),
(N'Khoa Nội Tổng Hợp', N'Khám sức khỏe tổng quát và các bệnh nội khoa');

-- 3. ROOM TYPE (Loại phòng)
INSERT INTO RoomType (name, price, createdAt) VALUES 
(N'Phòng Khám Thường', 150000, '2026-01-01'),
(N'Phòng Khám VIP', 500000, '2026-01-01'),
(N'Phòng Cấp Cứu', 300000, '2026-01-01');

-- 4. MEDICINES (Thuốc)
-- lý do sửa: bổ sung thêm dữ liệu cho cột stockquantity (số lượng tồn kho) thay vì chỉ để mặc định là 0.
INSERT INTO Medicines (name, unit, price, stockquantity, description) VALUES 
(N'Paracetamol 500mg', N'Viên', 2000, 500, N'Giảm đau, hạ sốt'),
(N'Amoxicillin 250mg', N'Viên', 5000, 1000, N'Kháng sinh điều trị nhiễm khuẩn'),
(N'Vitamin C 1000mg', N'Ống', 15000, 200, N'Tăng cường sức đề kháng'),
(N'Omeprazole 20mg', N'Viên', 8000, 300, N'Điều trị viêm loét dạ dày');

/* ================================================================
   TẦNG 2: DỮ LIỆU THỰC THỂ (Phụ thuộc Tầng 1)
================================================================ */

-- 5. DOCTORS (Bác sĩ - Tham chiếu Users 2, 3)
INSERT INTO Doctors (userId, name, position, phone, licenseNumber, status) VALUES 
(2, N'BS. Trần Trọng Hoàng', 'Trưởng Khoa', '0901112222', 'CCHN-11111', 'Active'),
(3, N'BS. Lê Thị Lan', 'Bác Sĩ Chính', '0912223333', 'CCHN-22222', 'Active');

-- 6. STAFFS (Nhân viên / Y tá - Tham chiếu Users 4)
INSERT INTO Staffs (userId, name, position, phone, status) VALUES 
(4, N'Nguyễn Văn Tuấn', 'Điều Dưỡng', '0933445566', 'Active');

-- 7. PATIENTS (Bệnh nhân - Tham chiếu Users 5, 6)
INSERT INTO Patients (userId, name, dob, gender, phone, address) VALUES 
(5, N'Trần Thị Hương', '1995-08-20', N'Nữ', '0987654321', N'123 Lê Lợi, TP.HCM'),
(6, N'Phạm Văn Minh', '1988-12-10', N'Nam', '0909888777', N'456 Nguyễn Huệ, TP.HCM');

-- 8. ROOMS (Phòng khám)
INSERT INTO Rooms (departmentId, roomType, roomNumber, status) VALUES 
(1, 2, 101, 1), -- Khoa Tim Mạch, Phòng VIP, Phòng số 101
(2, 1, 102, 1), -- Khoa Nhi, Phòng Thường, Phòng số 102
(3, 1, 103, 1); -- Khoa Nội, Phòng Thường, Phòng số 103

/* ================================================================
   TẦNG 3: DỮ LIỆU PHÂN CÔNG & LỊCH TRỰC (Phụ thuộc Tầng 1, 2)
================================================================ */

-- 9. DOCTOR DEPARTMENTS & STAFF DEPARTMENTS
INSERT INTO DoctorDepartments (doctorId, departmentId) VALUES (1, 1), (2, 2);
INSERT INTO StaffDepartments (staffId, departmentId) VALUES (1, 1);

-- 10. SHIFTS (Ca trực)
INSERT INTO Shifts (roomId, startTime, endTime, status, note) VALUES 
(1, '2026-03-01 07:00:00', '2026-03-01 11:30:00', 1, N'Ca sáng phòng 101'),
(2, '2026-03-01 13:00:00', '2026-03-01 17:00:00', 1, N'Ca chiều phòng 102');

-- 11. DOCTOR SHIFTS & STAFF SHIFTS
-- Chú ý: role của DoctorShifts bị giới hạn CHECK ('main','support','on_call')
INSERT INTO DoctorShifts (doctorId, shiftId, role) VALUES 
(1, 1, 'main'), 
(2, 2, 'main');
INSERT INTO StaffShifts (staffId, shiftId, role) VALUES 
(1, 1, N'Phụ tá đo huyết áp');

/* ================================================================
   TẦNG 4: DỮ LIỆU NGHIỆP VỤ / GIAO DỊCH KHÁM BỆNH
================================================================ */

-- 12. APPOINTMENTS (Cuộc hẹn)
-- lý do sửa: bảng mới dùng startTime và endTime thay cho cột appointmentTime.
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, created_at) VALUES 
(1, 1, 1, '2026-03-01 08:30:00', '2026-03-01 09:00:00', 1, '2026-02-25'),
(2, 2, 2, '2026-03-01 14:00:00', '2026-03-01 14:30:00', 1, '2026-02-25');

-- 13. MEDICAL RECORDS (Hồ sơ bệnh án)
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, created_at) VALUES 
(1, N'Rối loạn nhịp tim nhẹ', N'Cần theo dõi và uống thuốc đều đặn', '2026-03-01'),
(2, N'Viêm phế quản cấp', N'Giữ ấm cổ, hạn chế nước đá', '2026-03-01');

/* ================================================================
   TẦNG 5: HẬU KỲ (Kê đơn thuốc & Thanh toán)
================================================================ */

-- 14. PRESCRIPTIONS (Đơn thuốc)
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status) VALUES 
(1, N'Đơn thuốc tim mạch', '2026-03-01', 1),
(2, N'Đơn thuốc kháng sinh', '2026-03-01', 1);

-- 15. PRESCRIPTION ITEMS (Chi tiết đơn thuốc)
-- lý do sửa: cột usage đã được tách nhỏ thành dosage (liều lượng), frequency (tần suất), và duration (thời gian dùng).
INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration) VALUES 
(1, 1, 10, N'1 viên', N'khi đau đầu', N'khi cần'),
(2, 2, 20, N'1 viên', N'2 lần/ngày sau ăn', N'10 ngày'),
(2, 3, 10, N'1 ống', N'1 lần/ngày buổi sáng', N'10 ngày');

-- 16. PAYMENTS (Thanh toán)
-- Chú ý: paymentMethod bị giới hạn CHECK ('cash','card','banking')
INSERT INTO Payments (medicalRecordId, totalAmount, paymentMethod, status, paidAt) VALUES 
(1, 520000, 'banking', 1, '2026-03-01'), -- 500k khám VIP + 20k thuốc
(2, 265000, 'cash', 1, '2026-03-01');    -- 150k khám + 115k thuốc

SELECT * FROM Users
