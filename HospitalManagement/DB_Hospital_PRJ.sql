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
    departmentId INT NULL,
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
    doctorId INT NULL,
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

-- 1. USERS (1 Admin, 20 Doctor, 20 Staff, 20 Patient => Tổng 61 Users)
INSERT INTO Users (userName, email, avatarUrl, passwordHash, role, isActive, createdAt) VALUES
('Admin Manager', 'admin@medicare.com', 'admin.png', '123456', 'admin', 1, GETDATE());

-- Insert 20 Doctors (ID từ 2 đến 21)
DECLARE @i INT = 1;
WHILE @i <= 20
BEGIN
    INSERT INTO Users (userName, email, avatarUrl, passwordHash, role, isActive, createdAt) 
    VALUES ('Doctor ' + CAST(@i AS VARCHAR), 'doctor' + CAST(@i AS VARCHAR) + '@medicare.com', 'doc.png', '123456', 'doctor', 1, GETDATE());
    SET @i = @i + 1;
END;

-- Insert 20 Staffs (ID từ 22 đến 41)
SET @i = 1;
WHILE @i <= 20
BEGIN
    INSERT INTO Users (userName, email, avatarUrl, passwordHash, role, isActive, createdAt) 
    VALUES ('Staff ' + CAST(@i AS VARCHAR), 'staff' + CAST(@i AS VARCHAR) + '@medicare.com', 'staff.png', '123456', 'staff', 1, GETDATE());
    SET @i = @i + 1;
END;

-- Insert 20 Patients (ID từ 42 đến 61)
SET @i = 1;
WHILE @i <= 20
BEGIN
    INSERT INTO Users (userName, email, avatarUrl, passwordHash, role, isActive, createdAt) 
    VALUES ('Patient ' + CAST(@i AS VARCHAR), 'patient' + CAST(@i AS VARCHAR) + '@gmail.com', 'pat.png', '123456', 'patient', 1, GETDATE());
    SET @i = @i + 1;
END;




-- 7. DOCTOR DEPARTMENTS (Phân công 20 Bác sĩ vào 10 Khoa Y Tế Lâm Sàng - ID từ 1 đến 10)
INSERT INTO DoctorDepartments (doctorId, departmentId) VALUES
(1, 1), (2, 1), (3, 2), (4, 2), (5, 3), 
(6, 3), (7, 4), (8, 4), (9, 5), (10, 5),
(11, 6), (12, 6), (13, 7), (14, 7), (15, 8),
(16, 8), (17, 9), (18, 9), (19, 10), (20, 10);

-- 8. STAFF DEPARTMENTS (Phân công 20 Nhân viên vào 10 Phòng Ban Hành Chính - ID từ 11 đến 20)
INSERT INTO StaffDepartments (staffId, departmentId) VALUES
(1, 11), (2, 11),  -- Lễ Tân
(3, 12), (4, 12),  -- Kế Toán Thu Ngân
(5, 13), (6, 13),  -- Bảo Vệ
(7, 14), (8, 14),  -- Vệ Sinh
(9, 15), (10, 15), -- IT
(11, 16), (12, 16),-- Phòng/Kho Dược
(13, 17), (14, 17),-- Nhân Sự Hành Chính
(15, 18), (16, 18),-- CSKH
(17, 19), (18, 19),-- Căng Tin
(19, 20), (20, 20);-- Lái xe

-- 3. ROOM TYPES (20 Loại/Mức giá phòng)
INSERT INTO RoomType (name, price, createdAt, isActive) VALUES
(N'Phòng Khám Thường Nội Khoa', 150000.00, GETDATE(), 1),
(N'Phòng Khám Thường Ngoại Khoa', 180000.00, GETDATE(), 1),
(N'Phòng Khám Chuyên Gia', 500000.00, GETDATE(), 1),
(N'Phòng Khám VIP Trọn Gói', 1000000.00, GETDATE(), 1),
(N'Phòng Giường Cấp Cứu', 300000.00, GETDATE(), 1),
(N'Phòng Phẫu Thuật Nhỏ', 2000000.00, GETDATE(), 1),
(N'Phòng Phẫu Thuật Lớn', 5000000.00, GETDATE(), 1),
(N'Phòng Hồi Sức Tích Cực (ICU)', 1500000.00, GETDATE(), 1),
(N'Phòng Lưu Bệnh Tiêu Chuẩn (6 Giường)', 250000.00, GETDATE(), 1),
(N'Phòng Lưu Bệnh (4 Giường)', 350000.00, GETDATE(), 1),
(N'Phòng Lưu Bệnh (2 Giường)', 600000.00, GETDATE(), 1),
(N'Phòng Lưu Bệnh VIP (1 Giường)', 1200000.00, GETDATE(), 1),
(N'Phòng Siêu Âm', 200000.00, GETDATE(), 1),
(N'Phòng X-Quang', 250000.00, GETDATE(), 1),
(N'Phòng Chụp MRI', 2500000.00, GETDATE(), 1),
(N'Phòng Chụp CT', 1500000.00, GETDATE(), 1),
(N'Phòng Nội Soi Dạ Dày', 800000.00, GETDATE(), 1),
(N'Phòng Xét Nghiệm Máu', 100000.00, GETDATE(), 1),
(N'Phòng Tiêm Chủng', 50000.00, GETDATE(), 1),
(N'Phòng Tập Vật Lý Trị Liệu', 300000.00, GETDATE(), 1),

(N'Phòng Khám Mặc Định (Miễn phí)', 0.00, GETDATE(), 1);

-- 4. DOCTORS (20 Bác sĩ - UserID từ 2 đến 21)
INSERT INTO Doctors (userId, name, gender, position, phone, licenseNumber) VALUES
(2, N'Nguyễn Bá Tĩnh', 1, N'Trưởng Khoa', '0901000001', 'MED-201'),
(3, N'Lê Thị Hương', 0, N'Phó Khoa', '0901000002', 'MED-202'),
(4, N'Trần Trọng Trí', 1, N'Bác sĩ Chuyên Khoa I', '0901000003', 'MED-203'),
(5, N'Phạm Mỹ Linh', 0, N'Bác sĩ Chuyên Khoa II', '0901000004', 'MED-204'),
(6, N'Vũ Ngọc Đăng', 1, N'Bác sĩ Nội Trú', '0901000005', 'MED-205'),
(7, N'Hoàng Thanh Trà', 0, N'Bác sĩ Siêu Âm', '0901000006', 'MED-206'),
(8, N'Đặng Lê Tuấn', 1, N'Bác sĩ Ngoại Khoa', '0901000007', 'MED-207'),
(9, N'Bùi Ánh Nguyệt', 0, N'Trưởng Khoa', '0901000008', 'MED-208'),
(10, N'Đỗ Minh Quân', 1, N'Phó Khoa', '0901000009', 'MED-209'),
(11, N'Ngô Thị Thúy', 0, N'Bác sĩ Chuyên Khoa I', '0901000010', 'MED-210'),
(12, N'Hồ Tấn Đạt', 1, N'Bác sĩ Cấp Cứu', '0901000011', 'MED-211'),
(13, N'Phan Hoàng Yến', 0, N'Bác sĩ Sản Khoa', '0901000012', 'MED-212'),
(14, N'Lý Thiên Bảo', 1, N'Bác sĩ Nhi Khoa', '0901000013', 'MED-213'),
(15, N'Châu Mộng Thúy', 0, N'Bác sĩ Da Liễu', '0901000014', 'MED-214'),
(16, N'Cao Quang Thái', 1, N'Trưởng Khoa', '0901000015', 'MED-215'),
(17, N'Trương Mỹ Chi', 0, N'Bác sĩ Tai Mũi Họng', '0901000016', 'MED-216'),
(18, N'Đinh Hữu Khoa', 1, N'Bác sĩ Gây Mê', '0901000017', 'MED-217'),
(19, N'Mai Bảo Lan', 0, N'Bác sĩ Nhãn Khoa', '0901000018', 'MED-218'),
(20, N'Lương Gia Huy', 1, N'Bác sĩ Chấn Thương', '0901000019', 'MED-219'),
(21, N'Võ Phương Anh', 0, N'Bác sĩ Ung Bướu', '0901000020', 'MED-220');

-- 5. STAFFS (20 Nhân viên - UserID từ 22 đến 41 - Chức vụ hành chính rõ ràng)
INSERT INTO Staffs (userId, name, gender, position, phone) VALUES
(22, N'Lê Minh Tuấn', 1, N'Trưởng Ca Lễ Tân', '0912000001'),
(23, N'Nguyễn Hồng Hoa', 0, N'Nhân Viên Lễ Tân', '0912000002'),
(24, N'Trần Ngọc Mai', 0, N'Kế Toán Trưởng', '0912000003'),
(25, N'Đỗ Quốc Bình', 1, N'Nhân Viên Thu Ngân', '0912000004'),
(26, N'Phạm Hữu Tín', 1, N'Trưởng Đội Bảo Vệ', '0912000005'),
(27, N'Vũ Đình Lực', 1, N'Bảo Vệ', '0912000006'),
(28, N'Lý Thị Lệ', 0, N'Tổ Trưởng Tổ Vệ Sinh', '0912000007'),
(29, N'Hoàng Văn Cu', 1, N'Nhân Viên Vệ Sinh', '0912000008'),
(30, N'Ngô Gia Khánh', 1, N'Kỹ Sư Hệ Thống IT', '0912000009'),
(31, N'Bùi Thanh Trúc', 0, N'Nhân Viên IT Support', '0912000010'),
(32, N'Đặng Kim Dung', 0, N'Dược Sĩ Kho', '0912000011'),
(33, N'Hồ Thanh Tâm', 0, N'Trưởng Phòng Nhân Sự', '0912000012'),
(34, N'Phan Tấn Phát', 1, N'Chuyên Viên Hành Chính', '0912000013'),
(35, N'Châu Bảo Hà', 0, N'Trưởng Phòng CSKH', '0912000014'),
(36, N'Cao Cẩm Ly', 0, N'Nhân Viên CSKH', '0912000015'),
(37, N'Trương Công Tuấn', 1, N'Quản Lý Căng Tin', '0912000016'),
(38, N'Đinh Bích Ngọc', 0, N'Đầu Bếp', '0912000017'),
(39, N'Mai Đăng Khôi', 1, N'Tổ Trưởng Đội Xe', '0912000018'),
(40, N'Lương Vĩnh Thiện', 1, N'Tài Xế Cấp Cứu', '0912000019'),
(41, N'Võ Thái Học', 1, N'Bảo Vệ Ca Đêm', '0912000020');

-- 6. PATIENTS (20 Bệnh nhân - UserID từ 42 đến 61)
INSERT INTO Patients (userId, name, dob, gender, phone, address) VALUES
(42, N'Khách Hàng 1', '1990-01-01', 1, '0983000001', N'Quận 1, TP.HCM'),
(43, N'Khách Hàng 2', '1985-02-15', 0, '0983000002', N'Quận 2, TP.HCM'),
(44, N'Khách Hàng 3', '2010-03-20', 1, '0983000003', N'Quận 3, TP.HCM'),
(45, N'Khách Hàng 4', '1975-04-10', 0, '0983000004', N'Quận 4, TP.HCM'),
(46, N'Khách Hàng 5', '1995-05-25', 1, '0983000005', N'Quận 5, TP.HCM'),
(47, N'Khách Hàng 6', '1988-06-30', 0, '0983000006', N'Quận 6, TP.HCM'),
(48, N'Khách Hàng 7', '1992-07-12', 1, '0983000007', N'Quận 7, TP.HCM'),
(49, N'Khách Hàng 8', '1980-08-08', 0, '0983000008', N'Quận 8, TP.HCM'),
(50, N'Khách Hàng 9', '2005-09-19', 1, '0983000009', N'Quận 9, TP.HCM'),
(51, N'Khách Hàng 10', '1965-10-05', 0, '0983000010', N'Quận 10, TP.HCM'),
(52, N'Khách Hàng 11', '1999-11-11', 1, '0983000011', N'Quận 11, TP.HCM'),
(53, N'Khách Hàng 12', '1978-12-22', 0, '0983000012', N'Quận 12, TP.HCM'),
(54, N'Khách Hàng 13', '1983-01-14', 1, '0983000013', N'Gò Vấp, TP.HCM'),
(55, N'Khách Hàng 14', '1996-02-18', 0, '0983000014', N'Bình Thạnh, TP.HCM'),
(56, N'Khách Hàng 15', '2015-03-03', 1, '0983000015', N'Tân Bình, TP.HCM'),
(57, N'Khách Hàng 16', '1970-04-28', 0, '0983000016', N'Tân Phú, TP.HCM'),
(58, N'Khách Hàng 17', '1989-05-05', 1, '0983000017', N'Phú Nhuận, TP.HCM'),
(59, N'Khách Hàng 18', '2001-06-16', 0, '0983000018', N'Bình Tân, TP.HCM'),
(60, N'Khách Hàng 19', '1960-07-27', 1, '0983000019', N'Thủ Đức, TP.HCM'),
(61, N'Khách Hàng 20', '1994-08-09', 0, '0983000020', N'Bình Chánh, TP.HCM');

-- 2. DEPARTMENTS (10 Khoa Y Tế Lâm Sàng + 10 Phòng Ban Hành Chính)
-- Lưu ý: Xóa dữ liệu cũ (nếu cần) trước khi chạy lệnh này:
-- DELETE FROM StaffDepartments; DELETE FROM DoctorDepartments; DELETE FROM Departments;

INSERT INTO Departments (name, description, isActive) VALUES
-- Nhóm Y Tế (Dành cho Bác sĩ) - ID từ 1 đến 10 (Bắt buộc có chữ "Khoa" viết hoa)
(N'Khoa Tim Mạch', N'Chuyên khám và điều trị bệnh tim mạch', 1),
(N'Khoa Nhi', N'Chăm sóc sức khỏe trẻ sơ sinh và trẻ nhỏ', 1),
(N'Khoa Nội Tổng Hợp', N'Khám các bệnh lý nội khoa', 1),
(N'Khoa Ngoại Thần Kinh', N'Phẫu thuật chấn thương sọ não, cột sống', 1),
(N'Khoa Sản', N'Chăm sóc thai phụ và sinh nở', 1),
(N'Khoa Cấp Cứu', N'Tiếp nhận bệnh nhân khẩn cấp 24/7', 1),
(N'Khoa Tai Mũi Họng', N'Khám và nội soi Tai Mũi Họng', 1),
(N'Khoa Răng Hàm Mặt', N'Chăm sóc sức khỏe răng miệng', 1),
(N'Khoa Mắt', N'Đo thị lực và phẫu thuật mắt', 1),
(N'Khoa Da Liễu', N'Điều trị các bệnh lý về da', 1),

-- Nhóm Hành Chính/Hỗ Trợ (Dành cho Staff) - ID từ 11 đến 20 (Tuyệt đối không có chữ "Khoa")
(N'Quầy Lễ Tân', N'Đón tiếp và hướng dẫn bệnh nhân', 1),
(N'Phòng Kế Toán Thu Ngân', N'Xử lý thanh toán và viện phí', 1),
(N'Tổ Bảo Vệ', N'Đảm bảo an ninh bệnh viện', 1),
(N'Tổ Vệ Sinh', N'Giữ gìn môi trường bệnh viện sạch sẽ', 1),
(N'Phòng IT', N'Quản trị hệ thống phần mềm bệnh viện', 1),
(N'Phòng Dược', N'Quản lý xuất nhập thuốc', 1), -- (Sửa từ "Kho Dược" -> "Phòng Dược" hoặc để "Kho Dược" tùy ý sếp, miễn là không có chữ Khoa)
(N'Phòng Hành Chính Nhân Sự', N'Quản lý nhân sự bệnh viện', 1),
(N'Phòng Chăm Sóc Khách Hàng', N'Giải đáp thắc mắc, hỗ trợ VIP', 1),
(N'Nhà Ăn Căng Tin', N'Phục vụ ăn uống cho CBNV và Bệnh nhân', 1),
(N'Tổ Lái Xe Cấp Cứu', N'Vận chuyển bệnh nhân khẩn cấp', 1);


-- 7. DOCTOR DEPARTMENTS (Phân công 20 Bác sĩ vào 10 Khoa Y Tế Lâm Sàng - ID từ 1 đến 10)
INSERT INTO DoctorDepartments (doctorId, departmentId) VALUES
(1, 1), (2, 1), (3, 2), (4, 2), (5, 3), 
(6, 3), (7, 4), (8, 4), (9, 5), (10, 5),
(11, 6), (12, 6), (13, 7), (14, 7), (15, 8),
(16, 8), (17, 9), (18, 9), (19, 10), (20, 10);

-- 8. STAFF DEPARTMENTS (Phân công 20 Nhân viên vào 10 Phòng Ban Hành Chính - ID từ 11 đến 20)
INSERT INTO StaffDepartments (staffId, departmentId) VALUES
(1, 11), (2, 11),  -- Lễ Tân
(3, 12), (4, 12),  -- Kế Toán Thu Ngân
(5, 13), (6, 13),  -- Bảo Vệ
(7, 14), (8, 14),  -- Vệ Sinh
(9, 15), (10, 15), -- IT
(11, 16), (12, 16),-- Phòng/Kho Dược
(13, 17), (14, 17),-- Nhân Sự Hành Chính
(15, 18), (16, 18),-- CSKH
(17, 19), (18, 19),-- Căng Tin
(19, 20), (20, 20);-- Lái xe

-- 9. ROOMS (Tạo 20 Phòng khám/Điều trị thuộc các Khoa Lâm Sàng)
INSERT INTO Rooms (departmentId, roomType, roomNumber, status, isActive) VALUES
(1, 1, 101, 'available', 1), (1, 3, 102, 'available', 1), -- Khoa Tim Mạch
(2, 1, 103, 'available', 1), (2, 9, 104, 'occupied', 1),  -- Khoa Nhi
(3, 1, 105, 'available', 1), (3, 4, 106, 'maintenance', 1),-- Khoa Nội
(4, 7, 201, 'available', 1), (4, 11, 202, 'available', 1), -- Khoa Ngoại Thần Kinh
(5, 13, 203, 'available', 1), (5, 12, 204, 'occupied', 1), -- Khoa Sản
(6, 5, 301, 'available', 1), (6, 8, 302, 'available', 1),  -- Khoa Cấp Cứu
(7, 2, 303, 'available', 1), (7, 6, 304, 'available', 1),  -- Khoa Chấn Thương
(8, 1, 401, 'available', 1), (8, 17, 402, 'available', 1), -- Khoa Tai Mũi Họng
(9, 1, 403, 'available', 1), (9, 3, 404, 'available', 1),  -- Khoa RHM
(10, 1, 501, 'available', 1), (10, 6, 502, 'available', 1),-- Khoa Mắt

(1, 21, 901, 'available', 1), -- Cho Khoa Tim Mạch
(2, 21, 902, 'available', 1), -- Cho Khoa Nhi
(3, 21, 903, 'available', 1), -- Cho Khoa Nội
(4, 21, 904, 'available', 1), -- Cho Khoa Ngoại Thần Kinh
(5, 21, 905, 'available', 1), -- Cho Khoa Sản
(6, 21, 906, 'available', 1), -- Cho Khoa Cấp Cứu
(7, 21, 907, 'available', 1), -- Cho Khoa Chấn Thương
(8, 21, 908, 'available', 1), -- Cho Khoa Tai Mũi Họng
(9, 21, 909, 'available', 1), -- Cho Khoa RHM
(10, 21, 910, 'available', 1); -- Cho Khoa Mắt
-- 10. SHIFTS (Tạo 20 Ca trực tại các phòng)
INSERT INTO Shifts (roomId, startTime, endTime, status, isActive, note) VALUES
(1, '2026-03-12 07:00:00', '2026-03-12 11:00:00', 'scheduled', 1, N'Ca Sáng T.Mạch'),
(2, '2026-03-12 13:00:00', '2026-03-12 17:00:00', 'scheduled', 1, N'Ca Chiều T.Mạch'),
(3, '2026-03-12 07:00:00', '2026-03-12 11:00:00', 'scheduled', 1, N'Ca Sáng Nhi'),
(5, '2026-03-12 07:00:00', '2026-03-12 11:00:00', 'scheduled', 1, N'Ca Sáng Nội Khoa'),
(7, '2026-03-13 07:00:00', '2026-03-13 11:00:00', 'scheduled', 1, N'Ca Mổ Ngoại Thần Kinh'),
(9, '2026-03-13 13:00:00', '2026-03-13 17:00:00', 'scheduled', 1, N'Ca Siêu Âm Sản'),
(11, '2026-03-14 00:00:00', '2026-03-14 07:00:00', 'scheduled', 1, N'Trực Đêm Cấp Cứu'),
(13, '2026-03-14 07:00:00', '2026-03-14 11:00:00', 'scheduled', 1, N'Ca Sáng Chấn Thương'),
(15, '2026-03-15 07:00:00', '2026-03-15 11:00:00', 'scheduled', 1, N'Ca Sáng Tai Mũi Họng'),
(17, '2026-03-15 13:00:00', '2026-03-15 17:00:00', 'scheduled', 1, N'Ca Chiều RHM'),
(19, '2026-03-16 07:00:00', '2026-03-16 11:00:00', 'scheduled', 1, N'Ca Sáng Mắt'),
(1, '2026-03-16 13:00:00', '2026-03-16 17:00:00', 'scheduled', 1, N'Ca Chiều T.Mạch'),
(3, '2026-03-17 07:00:00', '2026-03-17 11:00:00', 'scheduled', 1, N'Ca Sáng Nhi'),
(5, '2026-03-17 13:00:00', '2026-03-17 17:00:00', 'scheduled', 1, N'Ca Chiều Nội Khoa'),
(7, '2026-03-18 07:00:00', '2026-03-18 11:00:00', 'scheduled', 1, N'Ca Sáng Ngoại Thần Kinh'),
(9, '2026-03-18 13:00:00', '2026-03-18 17:00:00', 'scheduled', 1, N'Ca Chiều Sản'),
(11, '2026-03-19 07:00:00', '2026-03-19 11:00:00', 'scheduled', 1, N'Ca Sáng Cấp Cứu'),
(13, '2026-03-19 13:00:00', '2026-03-19 17:00:00', 'scheduled', 1, N'Ca Chiều Chấn Thương'),
(15, '2026-03-20 07:00:00', '2026-03-20 11:00:00', 'scheduled', 1, N'Ca Sáng Tai Mũi Họng'),
(17, '2026-03-20 13:00:00', '2026-03-20 17:00:00', 'scheduled', 1, N'Ca Chiều RHM');

-- 11. DOCTOR SHIFTS & STAFF SHIFTS (20 Phân công cho Bác sĩ và 20 cho Nhân viên Hỗ trợ - Vệ sinh/Bảo vệ khu vực)
INSERT INTO DoctorShifts (doctorId, shiftId, role) VALUES
(1, 1, 'Trực Chính'), (2, 2, 'Trực Chính'), (3, 3, 'Trực Chính'), (5, 4, 'Trực Chính'), (7, 5, 'Phẫu thuật viên'),
(9, 6, 'Bác sĩ Siêu Âm'), (11, 7, 'Trực Đêm'), (13, 8, 'Trực Chính'), (15, 9, 'Trực Chính'), (17, 10, 'Trực Chính'),
(19, 11, 'Trực Chính'), (1, 12, 'Trực Chính'), (4, 13, 'Trực Chính'), (6, 14, 'Trực Chính'), (8, 15, 'Trực Chính'),
(10, 16, 'Trực Chính'), (12, 17, 'Trực Chính'), (14, 18, 'Trực Chính'), (16, 19, 'Trực Chính'), (18, 20, 'Trực Chính');

-- Staffs: Nhân viên Vệ sinh, Bảo vệ, IT cũng có ca trực tại các phòng/khu vực này
INSERT INTO StaffShifts (staffId, shiftId, role) VALUES
(7, 1, N'Vệ sinh phòng khám'), (5, 2, N'Bảo vệ hành lang'), (8, 3, N'Vệ sinh khu Nhi'), (6, 4, N'Bảo vệ khu Nội'), (9, 5, N'Hỗ trợ IT phòng mổ'),
(7, 6, N'Vệ sinh phòng siêu âm'), (6, 7, N'Bảo vệ ca đêm Cấp Cứu'), (8, 8, N'Vệ sinh khu CT'), (5, 9, N'Bảo vệ khu TMH'), (7, 10, N'Vệ sinh khu RHM'),
(8, 11, N'Vệ sinh khu Mắt'), (5, 12, N'Bảo vệ hành lang'), (7, 13, N'Vệ sinh khu Nhi'), (6, 14, N'Bảo vệ khu Nội'), (8, 15, N'Vệ sinh phòng mổ'),
(7, 16, N'Vệ sinh phòng sinh'), (5, 17, N'Bảo vệ Cấp Cứu'), (8, 18, N'Vệ sinh khu CT'), (6, 19, N'Bảo vệ khu TMH'), (7, 20, N'Vệ sinh khu RHM');

-- 12. APPOINTMENTS (20 Lịch hẹn Khám)
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) VALUES
(1, 1, 1, '2026-03-12 08:00:00', '2026-03-12 08:30:00', 'completed', GETDATE(), 1),
(2, 3, 3, '2026-03-12 09:00:00', '2026-03-12 09:30:00', 'completed', GETDATE(), 1),
(3, 5, 5, '2026-03-12 10:00:00', '2026-03-12 10:30:00', 'completed', GETDATE(), 1),
(4, 7, 7, '2026-03-13 08:00:00', '2026-03-13 11:00:00', 'completed', GETDATE(), 1),
(5, 9, 9, '2026-03-13 14:00:00', '2026-03-13 14:30:00', 'completed', GETDATE(), 1),
(6, 11, 11, '2026-03-14 02:00:00', '2026-03-14 03:00:00', 'completed', GETDATE(), 1),
(7, 13, 13, '2026-03-14 08:00:00', '2026-03-14 08:30:00', 'completed', GETDATE(), 1),
(8, 15, 15, '2026-03-15 09:00:00', '2026-03-15 09:30:00', 'completed', GETDATE(), 1),
(9, 17, 17, '2026-03-15 14:00:00', '2026-03-15 14:30:00', 'completed', GETDATE(), 1),
(10, 19, 19, '2026-03-16 08:00:00', '2026-03-16 08:30:00', 'completed', GETDATE(), 1),
(11, 1, 1, '2026-03-16 14:00:00', '2026-03-16 14:30:00', 'completed', GETDATE(), 1),
(12, 3, 3, '2026-03-17 09:00:00', '2026-03-17 09:30:00', 'completed', GETDATE(), 1),
(13, 5, 5, '2026-03-17 15:00:00', '2026-03-17 15:30:00', 'completed', GETDATE(), 1),
(14, 7, 7, '2026-03-18 08:00:00', '2026-03-18 09:00:00', 'pending', GETDATE(), 1),
(15, 9, 9, '2026-03-18 14:00:00', '2026-03-18 14:30:00', 'cancelled', GETDATE(), 1),
(16, 11, 11, '2026-03-19 08:00:00', '2026-03-19 08:30:00', 'pending', GETDATE(), 1),
(17, 13, 13, '2026-03-19 15:00:00', '2026-03-19 15:30:00', 'pending', GETDATE(), 1),
(18, 15, 15, '2026-03-20 09:00:00', '2026-03-20 09:30:00', 'pending', GETDATE(), 1),
(19, 17, 17, '2026-03-20 14:00:00', '2026-03-20 14:30:00', 'pending', GETDATE(), 1),
(20, 19, 19, '2026-03-21 08:00:00', '2026-03-21 08:30:00', 'pending', GETDATE(), 1);

-- 13. MEDICAL RECORDS (Tạo 20 Hồ sơ bệnh án tương ứng với 20 Appointment)
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) VALUES
(1, N'Rối loạn nhịp tim nhẹ', N'Cần theo dõi thêm 1 tuần', GETDATE(), 1),
(2, N'Viêm họng cấp ở trẻ', N'Ăn uống đồ ấm', GETDATE(), 1),
(3, N'Đau dạ dày cấp', N'Kiêng chua cay', GETDATE(), 1),
(4, N'Thoát vị đĩa đệm', N'Chỉ định mổ nội soi', GETDATE(), 1),
(5, N'Khám thai định kỳ tuần 12', N'Thai phát triển bình thường', GETDATE(), 1),
(6, N'Ngộ độc thực phẩm', N'Bù nước, theo dõi sát', GETDATE(), 1),
(7, N'Bong gân mắt cá chân', N'Hạn chế vận động', GETDATE(), 1),
(8, N'Viêm xoang mãn tính', N'Rửa mũi nước muối sinh lý', GETDATE(), 1),
(9, N'Sâu răng hàm dưới', N'Đã trám răng', GETDATE(), 1),
(10, N'Cận thị tăng độ', N'Cắt kính mới 2.5 độ', GETDATE(), 1),
(11, N'Huyết áp cao', N'Điều chỉnh chế độ ăn lạt', GETDATE(), 1),
(12, N'Sốt siêu vi', N'Hạ sốt, lau mát', GETDATE(), 1),
(13, N'Viêm đại tràng', N'Tái khám sau 1 tháng', GETDATE(), 1),
(14, N'Chờ khám chuyên sâu', N'', GETDATE(), 1),
(15, N'Hủy lịch khám', N'', GETDATE(), 1),
(16, N'Chờ xét nghiệm máu', N'', GETDATE(), 1),
(17, N'Chờ chụp X-Quang', N'', GETDATE(), 1),
(18, N'Chờ nội soi tai', N'', GETDATE(), 1),
(19, N'Chờ nhổ răng khôn', N'', GETDATE(), 1),
(20, N'Chờ đo nhãn áp', N'', GETDATE(), 1);

-- 14. PRESCRIPTIONS (20 Đơn thuốc gắn với 20 Bệnh án)
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) VALUES
(1, N'Uống sau ăn no', GETDATE(), 'active', 1),
(2, N'Pha loãng với nước ấm', GETDATE(), 'active', 1),
(3, N'Uống trước ăn 30 phút', GETDATE(), 'active', 1),
(4, N'Uống khi đau nhiều', GETDATE(), 'active', 1),
(5, N'Bổ sung thêm canxi', GETDATE(), 'active', 1),
(6, N'Uống nhiều lần trong ngày', GETDATE(), 'active', 1),
(7, N'Bôi ngoài da, không uống', GETDATE(), 'active', 1),
(8, N'Xịt mũi ngày 2 lần', GETDATE(), 'active', 1),
(9, N'Ngậm tan trong miệng', GETDATE(), 'active', 1),
(10, N'Nhỏ mắt dịu nhẹ', GETDATE(), 'active', 1),
(11, N'Uống mỗi sáng', GETDATE(), 'active', 1),
(12, N'Cách 4-6 tiếng uống 1 lần', GETDATE(), 'active', 1),
(13, N'Uống sau bữa tối', GETDATE(), 'active', 1),
(14, N'Đơn thuốc chờ cấp', GETDATE(), 'pending', 1),
(15, N'Đã hủy', GETDATE(), 'cancelled', 0),
(16, N'Chờ kết quả xét nghiệm', GETDATE(), 'pending', 1),
(17, N'Chờ kết quả chụp chiếu', GETDATE(), 'pending', 1),
(18, N'Chờ nội soi', GETDATE(), 'pending', 1),
(19, N'Kháng sinh dự phòng', GETDATE(), 'pending', 1),
(20, N'Thuốc nhỏ mắt chờ cấp', GETDATE(), 'pending', 1);

-- 15. MEDICINES (20 Loại Thuốc / Vật tư y tế)
INSERT INTO Medicines (name, unit, price, stockquantity, description, isActive) VALUES
(N'Paracetamol 500mg', N'Viên', 2000.00, 5000, N'Thuốc hạ sốt, giảm đau', 1),
(N'Amoxicillin 250mg', N'Viên', 5000.00, 3000, N'Kháng sinh phổ rộng', 1),
(N'Omeprazole 20mg', N'Viên', 8000.00, 2000, N'Thuốc trị trào ngược dạ dày', 1),
(N'Vitamin C 1000mg', N'Viên sủi', 4000.00, 4000, N'Tăng sức đề kháng', 1),
(N'Oresol', N'Gói', 3000.00, 8000, N'Bù nước và điện giải', 1),
(N'Ibuprofen 400mg', N'Viên', 3500.00, 2500, N'Kháng viêm, giảm đau', 1),
(N'Aspirin 81mg', N'Viên', 1500.00, 4000, N'Ngừa huyết khối', 1),
(N'Salbutamol 2mg', N'Viên', 4500.00, 1500, N'Giãn phế quản', 1),
(N'Canxi Corbiere', N'Ống', 7000.00, 3000, N'Bổ sung canxi nước', 1),
(N'Sắt Folate', N'Viên', 6000.00, 2000, N'Bổ máu cho bà bầu', 1),
(N'Salonpas', N'Miếng dán', 25000.00, 1000, N'Giảm đau cơ khớp ngoài da', 1),
(N'Nước muối sinh lý 0.9%', N'Chai', 10000.00, 5000, N'Súc miệng, rửa vết thương', 1),
(N'Xịt mũi Otrivin', N'Chai', 45000.00, 800, N'Trị nghẹt mũi', 1),
(N'Listerine 250ml', N'Chai', 60000.00, 600, N'Nước súc miệng diệt khuẩn', 1),
(N'V-Rohto', N'Chai', 50000.00, 1000, N'Thuốc nhỏ mắt', 1),
(N'Amlodipine 5mg', N'Viên', 3000.00, 3000, N'Thuốc hạ huyết áp', 1),
(N'Metformin 500mg', N'Viên', 2500.00, 4000, N'Thuốc trị tiểu đường', 1),
(N'Men vi sinh Enterogermina', N'Ống', 15000.00, 2000, N'Hỗ trợ tiêu hóa', 1),
(N'Cồn I-ốt 10%', N'Chai lọ', 12000.00, 1500, N'Sát trùng vết thương ngoài da', 1),
(N'Băng gạc y tế', N'Cuộn', 5000.00, 10000, N'Băng bó vết thương', 1);

-- 16. PRESCRIPTION ITEMS (Chi tiết đơn thuốc)
INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration) VALUES
(1, 16, 30, N'1 viên', N'Sáng', N'1 tháng'),     -- Thuốc HA
(2, 1, 10, N'1 viên', N'Sáng, Tối', N'5 ngày'),   -- Hạ sốt cho Nhi
(2, 4, 10, N'1 viên', N'Trưa', N'10 ngày'),       -- Vitamin C
(3, 3, 20, N'1 viên', N'Trước ăn Sáng, Tối', N'10 ngày'), -- Dạ dày
(4, 6, 15, N'1 viên', N'Sáng, Trưa, Tối', N'5 ngày'),     -- Giảm đau xương khớp
(4, 11, 5, N'1 miếng', N'Khi đau', N'5 ngày'),            -- Cao dán
(5, 9, 30, N'1 ống', N'Sáng', N'1 tháng'),        -- Canxi bầu
(5, 10, 30, N'1 viên', N'Trưa', N'1 tháng'),      -- Sắt bầu
(6, 5, 10, N'1 gói', N'Pha 200ml nước', N'5 ngày'),       -- Oresol
(6, 18, 10, N'1 ống', N'Sáng, Tối', N'5 ngày'),           -- Men vi sinh
(7, 11, 10, N'1 miếng', N'Sáng', N'10 ngày'),     -- Cao dán bong gân
(8, 12, 5, N'1 chai', N'Rửa mũi', N'1 tháng'),    -- Nước muối
(8, 13, 2, N'1 chai', N'Xịt 2 bên mũi', N'15 ngày'),      -- Xịt mũi
(9, 1, 10, N'1 viên', N'Khi đau răng', N'5 ngày'),        -- Giảm đau răng
(9, 14, 1, N'1 chai', N'Súc miệng', N'1 tháng'),          -- Nước súc miệng
(10, 15, 2, N'1 chai', N'Nhỏ 2 mắt', N'1 tháng'),         -- Nhỏ mắt
(11, 16, 30, N'1 viên', N'Sáng', N'1 tháng'),     -- Thuốc HA
(12, 1, 10, N'1 viên', N'Khi sốt > 38.5 độ', N'5 ngày'),  -- Hạ sốt siêu vi
(12, 4, 10, N'1 viên', N'Sáng', N'10 ngày'),              -- C sủi
(13, 18, 20, N'1 ống', N'Sáng, Tối', N'10 ngày');         -- Men vi sinh đại tràng

-- 17. PAYMENTS (20 Giao dịch thanh toán)
INSERT INTO Payments (medicalRecordId, totalAmount, paymentMethod, status, paidAt, isActive) VALUES
(1, 210000.00, 'banking', 'paid', '2026-03-12 09:00:00', 1),
(2, 210000.00, 'cash', 'paid', '2026-03-12 10:00:00', 1),
(3, 310000.00, 'card', 'paid', '2026-03-12 11:00:00', 1),
(4, 5200000.00, 'banking', 'paid', '2026-03-13 12:00:00', 1),
(5, 540000.00, 'cash', 'paid', '2026-03-13 15:00:00', 1),
(6, 480000.00, 'banking', 'paid', '2026-03-14 04:00:00', 1),
(7, 430000.00, 'cash', 'paid', '2026-03-14 09:00:00', 1),
(8, 290000.00, 'card', 'paid', '2026-03-15 10:00:00', 1),
(9, 230000.00, 'cash', 'paid', '2026-03-15 15:00:00', 1),
(10, 250000.00, 'banking', 'paid', '2026-03-16 09:00:00', 1),
(11, 240000.00, 'cash', 'paid', '2026-03-16 15:00:00', 1),
(12, 210000.00, 'card', 'paid', '2026-03-17 10:00:00', 1),
(13, 450000.00, 'banking', 'paid', '2026-03-17 16:00:00', 1),
(14, 500000.00, 'cash', 'unpaid', NULL, 1),
(15, 0.00, 'cash', 'cancelled', NULL, 1),
(16, 150000.00, 'card', 'unpaid', NULL, 1),
(17, 250000.00, 'banking', 'unpaid', NULL, 1),
(18, 300000.00, 'cash', 'unpaid', NULL, 1),
(19, 1000000.00, 'card', 'unpaid', NULL, 1),
(20, 200000.00, 'banking', 'unpaid', NULL, 1);



DECLARE @FreeRoomType INT;
SELECT TOP 1 @FreeRoomType = id FROM RoomType WHERE price = 0;

-- Nếu chưa có loại phòng 0đ, tự động tạo mới
IF @FreeRoomType IS NULL
BEGIN
    INSERT INTO RoomType (name, price, createdAt, isActive) 
    VALUES (N'Khám Mặc Định / Hành Chính', 0.00, GETDATE(), 1);
    SET @FreeRoomType = SCOPE_IDENTITY();
END

-- 2. TẠO PHÒNG 0đ CHO TẤT CẢ 10 KHOA BÁC SĨ (Đánh số từ 1000)
INSERT INTO Rooms (departmentId, roomType, roomNumber, status, isActive) VALUES
(1, @FreeRoomType, 1001, 'available', 1),
(2, @FreeRoomType, 1002, 'available', 1),
(3, @FreeRoomType, 1003, 'available', 1),
(4, @FreeRoomType, 1004, 'available', 1),
(5, @FreeRoomType, 1005, 'available', 1),
(6, @FreeRoomType, 1006, 'available', 1),
(7, @FreeRoomType, 1007, 'available', 1),
(8, @FreeRoomType, 1008, 'available', 1),
(9, @FreeRoomType, 1009, 'available', 1),
(10, @FreeRoomType, 1010, 'available', 1);

-- 3. TẠO PHÒNG 0đ CHO TẤT CẢ 10 PHÒNG BAN NHÂN VIÊN (Đánh số từ 1100)
INSERT INTO Rooms (departmentId, roomType, roomNumber, status, isActive) VALUES
(11, @FreeRoomType, 1101, 'available', 1),
(12, @FreeRoomType, 1102, 'available', 1),
(13, @FreeRoomType, 1103, 'available', 1),
(14, @FreeRoomType, 1104, 'available', 1),
(15, @FreeRoomType, 1105, 'available', 1),
(16, @FreeRoomType, 1106, 'available', 1),
(17, @FreeRoomType, 1107, 'available', 1),
(18, @FreeRoomType, 1108, 'available', 1),
(19, @FreeRoomType, 1109, 'available', 1),
(20, @FreeRoomType, 1110, 'available', 1);
GO


PRINT N'--- BẮT ĐẦU TẠO DỮ LIỆU CÁC LỊCH HẸN (PENDING, ACCEPTED, COMPLETED, CANCELLED) ---'

-- KHAI BÁO BIẾN 1 LẦN DUY NHẤT Ở ĐÂY ĐỂ TRÁNH LỖI TRÙNG LẶP
DECLARE @NewAppId INT;
DECLARE @NewMRId INT;
DECLARE @NewPresId INT;

-- =================================================================
-- PHẦN 1: TRẠNG THÁI "CHỜ KHÁM" (PENDING)
-- =================================================================

-- 1. Bệnh nhân 2 đặt lịch Khoa Tim Mạch (Phòng 1001)
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (2, NULL, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 1001), '2026-03-25 08:00:00', '2026-03-25 10:00:00', 'pending', GETDATE(), 1);
SET @NewAppId = SCOPE_IDENTITY(); 
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Chờ khám', N'', GETDATE(), 1);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'', GETDATE(), 'pending', 1);

-- 2. Bệnh nhân 5 đặt lịch Khoa Nhi (Phòng 1002)
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (5, NULL, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 1002), '2026-03-25 13:00:00', '2026-03-25 15:00:00', 'pending', GETDATE(), 1);
SET @NewAppId = SCOPE_IDENTITY();
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Chờ khám', N'', GETDATE(), 1);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'', GETDATE(), 'pending', 1);

-- 3. Bệnh nhân 10 đặt lịch Khoa Ngoại Thần Kinh (Phòng 1004)
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (10, NULL, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 1004), '2026-03-26 08:00:00', '2026-03-26 10:00:00', 'pending', GETDATE(), 1);
SET @NewAppId = SCOPE_IDENTITY();
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Chờ khám', N'', GETDATE(), 1);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'', GETDATE(), 'pending', 1);

-- 4. Bệnh nhân 15 đặt lịch Khoa Sản (Phòng 1005)
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (15, NULL, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 1005), '2026-03-27 10:00:00', '2026-03-27 12:00:00', 'pending', GETDATE(), 1);
SET @NewAppId = SCOPE_IDENTITY();
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Chờ khám', N'', GETDATE(), 1);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'', GETDATE(), 'pending', 1);

-- 5. Bệnh nhân 20 đặt lịch Khoa Răng Hàm Mặt (Phòng 1009)
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (20, NULL, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 1009), '2026-03-28 15:00:00', '2026-03-28 17:00:00', 'pending', GETDATE(), 1);
SET @NewAppId = SCOPE_IDENTITY();
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Chờ khám', N'', GETDATE(), 1);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'', GETDATE(), 'pending', 1);


-- =================================================================
-- PHẦN 2: TRẠNG THÁI "ĐÃ TIẾP NHẬN" (ACCEPTED/PREPARING)
-- =================================================================

-- 1.1. Bệnh nhân 3 -> Khoa Tim Mạch (Bác sĩ 2 tiếp nhận, Phòng 101)
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (3, 2, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 101), DATEADD(hour, 1, GETDATE()), DATEADD(hour, 3, GETDATE()), 'accepted', GETDATE(), 1);
SET @NewAppId = SCOPE_IDENTITY();
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Đang chờ khám', N'Điều dưỡng đã đo HA: 140/90, Nhịp tim: 85 l/p', GETDATE(), 1);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'', GETDATE(), 'pending', 1);

-- 1.2. Bệnh nhân 4 -> Khoa Nhi (Bác sĩ 4 tiếp nhận, Phòng 103)
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (4, 4, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 103), DATEADD(hour, 2, GETDATE()), DATEADD(hour, 4, GETDATE()), 'accepted', GETDATE(), 1);
SET @NewAppId = SCOPE_IDENTITY();
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Đang chờ khám', N'Bé sốt 38.5 độ, quấy khóc', GETDATE(), 1);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'', GETDATE(), 'pending', 1);

-- 1.3. Bệnh nhân 6 -> Khoa Sản (Bác sĩ 14 tiếp nhận, Phòng 203)
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (6, 14, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 203), DATEADD(hour, 3, GETDATE()), DATEADD(hour, 5, GETDATE()), 'accepted', GETDATE(), 1);
SET @NewAppId = SCOPE_IDENTITY();
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Đang chờ khám', N'Thai phụ khám định kỳ tuần 24', GETDATE(), 1);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'', GETDATE(), 'pending', 1);


-- =================================================================
-- PHẦN 3: TRẠNG THÁI "ĐÃ HOÀN THÀNH" (COMPLETED)
-- =================================================================

-- 2.1. Bệnh nhân 7 -> Tai Mũi Họng (Bác sĩ 18 khám xong, Phòng 401)
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (7, 18, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 401), DATEADD(day, -1, GETDATE()), DATEADD(day, -1, DATEADD(hour, 1, GETDATE())), 'completed', GETDATE(), 1);
SET @NewAppId = SCOPE_IDENTITY();
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Viêm amidan hốc mủ', N'Cần súc miệng nước muối thường xuyên, giữ ấm cổ', GETDATE(), 1);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'Uống thuốc sau khi ăn no', GETDATE(), 'active', 1);
SET @NewPresId = SCOPE_IDENTITY();
INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration) VALUES
(@NewPresId, 2, 14, N'1 viên', N'Sáng, Tối', N'7 ngày'),
(@NewPresId, 14, 1, N'15ml', N'Sau khi đánh răng', N'1 tháng');
INSERT INTO Payments (medicalRecordId, totalAmount, paymentMethod, status, paidAt, isActive) 
VALUES (@NewMRId, 350000.00, 'banking', 'paid', GETDATE(), 1);

-- 2.2. Bệnh nhân 8 -> Khoa Da Liễu (Bác sĩ 16 khám xong, Phòng 501)
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (8, 16, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 501), DATEADD(day, -2, GETDATE()), DATEADD(day, -2, DATEADD(hour, 1, GETDATE())), 'completed', GETDATE(), 1);
SET @NewAppId = SCOPE_IDENTITY();
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Viêm da cơ địa', N'Kiêng hải sản, thịt bò. Dưỡng ẩm thường xuyên', GETDATE(), 1);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'Bôi lớp mỏng ngoài da', GETDATE(), 'active', 1);
SET @NewPresId = SCOPE_IDENTITY();
INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration) VALUES
(@NewPresId, 3, 10, N'1 viên', N'Tối trước khi ngủ', N'10 ngày');
INSERT INTO Payments (medicalRecordId, totalAmount, paymentMethod, status, paidAt, isActive) 
VALUES (@NewMRId, 180000.00, 'cash', 'unpaid', NULL, 1);


-- =================================================================
-- PHẦN 4: TRẠNG THÁI "ĐÃ HỦY" (CANCELLED)
-- =================================================================

-- 3.1. Hủy do quá hạn
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (10, NULL, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 1001), DATEADD(day, -10, GETDATE()), DATEADD(day, -10, DATEADD(hour, 2, GETDATE())), 'cancelled', DATEADD(day, -12, GETDATE()), 0);
SET @NewAppId = SCOPE_IDENTITY();
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Chờ khám', N'Hệ thống tự động hủy do quá hạn 7 ngày', GETDATE(), 0);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'', GETDATE(), 'cancelled', 0);

-- 3.2. Hủy khi ĐÃ CÓ bác sĩ tiếp nhận
INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive) 
VALUES (11, 2, (SELECT TOP 1 id FROM Rooms WHERE roomNumber = 101), DATEADD(day, 1, GETDATE()), DATEADD(day, 1, DATEADD(hour, 1, GETDATE())), 'cancelled', GETDATE(), 0);
SET @NewAppId = SCOPE_IDENTITY();
INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) 
VALUES (@NewAppId, N'Đang chờ khám', N'Bệnh nhân báo bận, xin hủy lịch', GETDATE(), 0);
SET @NewMRId = SCOPE_IDENTITY();
INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) 
VALUES (@NewMRId, N'', GETDATE(), 'cancelled', 0);

PRINT N'--- ĐÃ TẠO THÀNH CÔNG DỮ LIỆU ĐỒNG BỘ CHO CÁC TRẠNG THÁI ---'
GO


PRINT N'--- BẮT ĐẦU TẠO COMBO 5 LỊCH SỬ KHÁM CHO TỪNG BỆNH NHÂN (1 ĐẾN 20) ---';

DECLARE @PatientId INT = 1;
DECLARE @DocId INT;
DECLARE @RoomId INT;
DECLARE @AppId INT, @MRId INT, @PresId INT;

WHILE @PatientId <= 20
BEGIN
    -- ===================================================================
    -- 1. TRẠNG THÁI: PENDING (Chờ xác nhận - Tab 1)
    -- Lấy ngẫu nhiên phòng 0đ (roomNumber 1001-1010)
    -- ===================================================================
    SELECT TOP 1 @RoomId = id FROM Rooms WHERE roomNumber = 1000 + ((@PatientId % 10) + 1);
    
    INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive)
    VALUES (@PatientId, NULL, @RoomId, DATEADD(day, 2, GETDATE()), DATEADD(hour, 2, DATEADD(day, 2, GETDATE())), 'pending', GETDATE(), 1);
    SET @AppId = SCOPE_IDENTITY();

    INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive)
    VALUES (@AppId, N'Chờ khám', N'', GETDATE(), 1);
    SET @MRId = SCOPE_IDENTITY();

    INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive)
    VALUES (@MRId, N'', GETDATE(), 'pending', 1);

    -- ===================================================================
    -- 2. TRẠNG THÁI: ACCEPTED (Đã tiếp nhận - Tab 1)
    -- Có Bác sĩ, có Phòng khám thật
    -- ===================================================================
    SET @DocId = (@PatientId % 20) + 1; -- Random Bác sĩ từ 1-20
    -- Tìm phòng tương ứng với khoa của Bác sĩ này
    SELECT TOP 1 @RoomId = r.id FROM Rooms r JOIN DoctorDepartments dd ON r.departmentId = dd.departmentId WHERE dd.doctorId = @DocId;

    INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive)
    VALUES (@PatientId, @DocId, @RoomId, DATEADD(day, 1, GETDATE()), DATEADD(hour, 1, DATEADD(day, 1, GETDATE())), 'accepted', GETDATE(), 1);
    SET @AppId = SCOPE_IDENTITY();

    INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive)
    VALUES (@AppId, N'Đang chờ khám', N'Bệnh nhân đã đến quầy, điều dưỡng đang đo huyết áp.', GETDATE(), 1);
    SET @MRId = SCOPE_IDENTITY();

    INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive)
    VALUES (@MRId, N'', GETDATE(), 'pending', 1);

    -- ===================================================================
    -- 3. TRẠNG THÁI: COMPLETED - ĐÃ THANH TOÁN (Tab 2)
    -- ===================================================================
    SET @DocId = ((@PatientId + 2) % 20) + 1;
    SELECT TOP 1 @RoomId = r.id FROM Rooms r JOIN DoctorDepartments dd ON r.departmentId = dd.departmentId WHERE dd.doctorId = @DocId;

    INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive)
    VALUES (@PatientId, @DocId, @RoomId, DATEADD(day, -5, GETDATE()), DATEADD(hour, 1, DATEADD(day, -5, GETDATE())), 'completed', DATEADD(day, -6, GETDATE()), 1);
    SET @AppId = SCOPE_IDENTITY();

    INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive)
    VALUES (@AppId, N'Viêm họng hạt cấp tính', N'Nghỉ ngơi nhiều, súc miệng nước muối thường xuyên, tránh uống nước đá.', DATEADD(day, -5, GETDATE()), 1);
    SET @MRId = SCOPE_IDENTITY();

    INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive)
    VALUES (@MRId, N'Uống thuốc sau bữa ăn no 30 phút.', DATEADD(day, -5, GETDATE()), 'active', 1);
    SET @PresId = SCOPE_IDENTITY();

    -- Kê 2 loại thuốc
    INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration)
    VALUES
    (@PresId, 2, 10, N'1 viên', N'Sáng, Tối', N'5 ngày'), -- Amoxicillin
    (@PresId, 4, 10, N'1 viên', N'Trưa', N'10 ngày');      -- Vitamin C

    -- Hóa đơn ĐÃ THANH TOÁN
    INSERT INTO Payments (medicalRecordId, totalAmount, paymentMethod, status, paidAt, isActive)
    VALUES (@MRId, 240000.00, 'banking', 'paid', DATEADD(day, -5, GETDATE()), 1);

    -- ===================================================================
    -- 4. TRẠNG THÁI: COMPLETED - CHƯA THANH TOÁN (Tab 2)
    -- Để sếp test nút hiện mã QR Code
    -- ===================================================================
    SET @DocId = ((@PatientId + 5) % 20) + 1;
    SELECT TOP 1 @RoomId = r.id FROM Rooms r JOIN DoctorDepartments dd ON r.departmentId = dd.departmentId WHERE dd.doctorId = @DocId;

    INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive)
    VALUES (@PatientId, @DocId, @RoomId, DATEADD(day, -2, GETDATE()), DATEADD(hour, 1, DATEADD(day, -2, GETDATE())), 'completed', DATEADD(day, -3, GETDATE()), 1);
    SET @AppId = SCOPE_IDENTITY();

    INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive)
    VALUES (@AppId, N'Rối loạn tiêu hóa / Đau dạ dày', N'Hạn chế đồ chua cay, thức khuya.', DATEADD(day, -2, GETDATE()), 1);
    SET @MRId = SCOPE_IDENTITY();

    INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive)
    VALUES (@MRId, N'Uống trước khi ăn 30 phút.', DATEADD(day, -2, GETDATE()), 'active', 1);
    SET @PresId = SCOPE_IDENTITY();

    INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration)
    VALUES (@PresId, 3, 14, N'1 viên', N'Sáng, Tối', N'7 ngày'); -- Omeprazole

    -- Hóa đơn CHƯA THANH TOÁN
    INSERT INTO Payments (medicalRecordId, totalAmount, paymentMethod, status, paidAt, isActive)
    VALUES (@MRId, 262000.00, 'cash', 'unpaid', NULL, 1);

    -- ===================================================================
    -- 5. TRẠNG THÁI: CANCELLED (Đã hủy - Nằm trong Tab 2)
    -- Lưu ý: Phải để isActive = 1 thì nó mới hiện lên Web, còn isActive = 0 là Xóa vĩnh viễn (ẩn)
    -- ===================================================================
    SELECT TOP 1 @RoomId = id FROM Rooms WHERE roomNumber = 1000 + ((@PatientId % 10) + 1);
    
    INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt, isActive)
    VALUES (@PatientId, NULL, @RoomId, DATEADD(day, -10, GETDATE()), DATEADD(hour, 1, DATEADD(day, -10, GETDATE())), 'cancelled', DATEADD(day, -12, GETDATE()), 1);
    SET @AppId = SCOPE_IDENTITY();

    INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive)
    VALUES (@AppId, N'Chờ khám', N'Bệnh nhân đã chủ động hủy lịch qua hệ thống', DATEADD(day, -10, GETDATE()), 1);
    SET @MRId = SCOPE_IDENTITY();

    INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive)
    VALUES (@MRId, N'', DATEADD(day, -10, GETDATE()), 'cancelled', 1);

    -- Chuyển sang Bệnh nhân tiếp theo
    SET @PatientId = @PatientId + 1;
END

PRINT N'--- HOÀN THÀNH! ĐÃ BƠM ĐỦ DỮ LIỆU ĐẸP CHO TOÀN BỘ 20 BỆNH NHÂN ---';
GO

PRINT N'--- ĐANG CẬP NHẬT: NHỒI NHIỀU LOẠI THUỐC VÀO CÙNG MỘT ĐƠN THUỐC ---';

-- 1. Xóa các chi tiết thuốc cũ để làm sạch dữ liệu test
DELETE FROM PrescriptionItems;

-- 2. Dùng Cursor để duyệt qua tất cả các Đơn thuốc 'active' (đã hoàn thành)
DECLARE @CurrentPresId01 INT;
DECLARE pres_cursor CURSOR FOR 
    SELECT id FROM Prescriptions WHERE status = 'active';

OPEN pres_cursor;
FETCH NEXT FROM pres_cursor INTO @CurrentPresId01;

WHILE @@FETCH_STATUS = 0
BEGIN
    -- MÓN 1: Thuốc đặc trị (Ví dụ: Amoxicillin - ID 2)
    INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration)
    VALUES (@CurrentPresId01, 2, 14, N'1 viên', N'Sáng 1 - Tối 1', N'7 ngày');

    -- MÓN 2: Thuốc hỗ trợ triệu chứng (Ví dụ: Paracetamol - ID 1)
    INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration)
    VALUES (@CurrentPresId01, 1, 10, N'1 viên', N'Uống khi sốt trên 38.5 độ', N'3 ngày');

    -- MÓN 3: Vitamin bổ trợ (Ví dụ: Vitamin C - ID 4)
    INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration)
    VALUES (@CurrentPresId01, 4, 10, N'1 viên sủi', N'Sáng sau ăn', N'10 ngày');

    FETCH NEXT FROM pres_cursor INTO @CurrentPresId01;
END;

CLOSE pres_cursor;
DEALLOCATE pres_cursor;

PRINT N'--- THÀNH CÔNG! MỖI ĐƠN THUỐC BÂY GIỜ ĐỀU CÓ 3 MÓN THUỐC CHI TIẾT ---';
GO