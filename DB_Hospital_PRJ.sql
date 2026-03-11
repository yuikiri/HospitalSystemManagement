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
    isActive Int,
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
    status VARCHAR(30),
    paidAt DATETIME2,
    CONSTRAINT FK_Pay_MR FOREIGN KEY (medicalRecordId) REFERENCES MedicalRecords(id)
);





/* ================================
   SAMPLE DATA
================================ */

/* USERS */
INSERT INTO Users (userName,email,passwordHash,role,isActive,createdAt) VALUES
(N'Admin System','admin@hospital.com','hash_admin','admin',1,GETDATE()),
(N'Dr Nguyen Van A','doctorA@hospital.com','hash_doc1','doctor',1,GETDATE()),
(N'Dr Tran Thi B','doctorB@hospital.com','hash_doc2','doctor',1,GETDATE()),
(N'Staff Le Van C','staffC@hospital.com','hash_staff1','staff',1,GETDATE()),
(N'Patient Pham Van D','patient1@gmail.com','hash_patient1','patient',1,GETDATE()),
(N'Patient Nguyen Thi E','patient2@gmail.com','hash_patient2','patient',1,GETDATE());

/* DEPARTMENTS */
INSERT INTO Departments (name,description) VALUES
(N'Cardiology',N'Heart related treatment'),
(N'Neurology',N'Brain and nervous system'),
(N'Orthopedics',N'Bone and joint treatment');

/* ROOM TYPES */
INSERT INTO RoomType (name,price,createdAt) VALUES
(N'Standard',300000,GETDATE()),
(N'VIP',800000,GETDATE()),
(N'ICU',1500000,GETDATE());

/* DOCTORS */
INSERT INTO Doctors (userId,name,gender,position,phone,licenseNumber) VALUES
(2,N'Nguyen Van A',1,N'Cardiologist','0900000001','LIC001'),
(3,N'Tran Thi B',2,N'Neurologist','0900000002','LIC002');

/* STAFFS */
INSERT INTO Staffs (userId,name,gender,position,phone) VALUES
(4,N'Le Van C',1,N'Nurse','0900000003');

/* PATIENTS */
INSERT INTO Patients (userId,name,dob,gender,phone,address) VALUES
(5,N'Pham Van D','2000-05-12',1,'0900000004',N'Ho Chi Minh City'),
(6,N'Nguyen Thi E','1998-09-21',2,'0900000005',N'Ha Noi'),
(NULL,N'Le Van F','1985-01-10',1,'0900000006',N'Da Nang');

/* DOCTOR DEPARTMENTS */
INSERT INTO DoctorDepartments (doctorId,departmentId) VALUES
(1,1),
(2,2);

/* STAFF DEPARTMENTS */
INSERT INTO StaffDepartments (staffId,departmentId) VALUES
(1,1);

/* ROOMS */
INSERT INTO Rooms (departmentId,roomType,roomNumber,status) VALUES
(1,1,101,'available'),
(2,2,201,'available'),
(3,1,301,'maintenance');

/* SHIFTS */
INSERT INTO Shifts (roomId,startTime,endTime,status,note) VALUES
(1,'2026-03-10 08:00','2026-03-10 12:00','active',N'Morning shift'),
(2,'2026-03-10 13:00','2026-03-10 17:00','active',N'Afternoon shift');

/* DOCTOR SHIFTS */
INSERT INTO DoctorShifts (doctorId,shiftId,role) VALUES
(1,1,'main'),
(2,2,'main');

/* STAFF SHIFTS */
INSERT INTO StaffShifts (staffId,shiftId,role) VALUES
(1,1,N'assistant');

/* APPOINTMENTS */
INSERT INTO Appointments (patientId,doctorId,roomId,startTime,endTime,status,createdAt) VALUES
(1,1,1,'2026-03-10 09:00','2026-03-10 09:30','completed',GETDATE()),
(2,2,2,'2026-03-10 14:00','2026-03-10 14:30','completed',GETDATE());

/* MEDICAL RECORDS */
INSERT INTO MedicalRecords (appointmentId,diagnosis,notes,createdAt) VALUES
(1,N'Hypertension',N'Patient needs blood pressure monitoring',GETDATE()),
(2,N'Migraine',N'Frequent headaches reported',GETDATE());

/* PRESCRIPTIONS */
INSERT INTO Prescriptions (medicalRecordId,notes,createdAt,status) VALUES
(1,N'Take medicine after meal',GETDATE(),'active'),
(2,N'Use when headache occurs',GETDATE(),'active');

/* MEDICINES */
INSERT INTO Medicines (name,unit,price,stockquantity,description) VALUES
(N'Paracetamol','tablet',2000,500,N'Pain relief'),
(N'Aspirin','tablet',3000,300,N'Blood thinner'),
(N'Vitamin C','tablet',1500,800,N'Supplement');

/* PRESCRIPTION ITEMS */
INSERT INTO PrescriptionItems (prescriptionId,medicineId,quantity,dosage,frequency,duration) VALUES
(1,1,10,N'500mg',N'2 times/day',N'5 days'),
(2,2,5,N'100mg',N'1 time/day',N'5 days');

/* PAYMENTS */
INSERT INTO Payments (medicalRecordId,totalAmount,paymentMethod,status,paidAt) VALUES
(1,500000,'cash','paid',GETDATE()),
(2,300000,'card','paid',GETDATE());
