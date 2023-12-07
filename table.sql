-- Health and Fitness Club Management System Schema

-- Members Table
CREATE TABLE Members (
    MemberID SERIAL PRIMARY KEY,
    Name VARCHAR(255),
    DateOfBirth DATE,
    Gender VARCHAR(50),
    ContactInfo VARCHAR(255),
    HealthMetrics VARCHAR(255),
    FitnessGoals VARCHAR(255)
);

-- Trainers Table
CREATE TABLE Trainers (
    TrainerID SERIAL PRIMARY KEY,
    Name VARCHAR(255),
    Qualifications VARCHAR(255)
);

-- Staff Table
CREATE TABLE Staff (
    StaffID SERIAL PRIMARY KEY,
    Name VARCHAR(255),
    Role VARCHAR(100),
    ContactInfo VARCHAR(255)
);

-- Room Table
CREATE TABLE Room (
    RoomID SERIAL PRIMARY KEY,
    Name VARCHAR(255),
    Capacity INT,
    Type VARCHAR(100)
);

-- GroupClasses Table
CREATE TABLE GroupClasses (
    ClassID SERIAL PRIMARY KEY,
    Name VARCHAR(255),
    TrainerID INT,
    RoomID INT,
    Schedule TEXT,
    MaxCapacity INT,
    FOREIGN KEY (TrainerID) REFERENCES Trainers(TrainerID),
    FOREIGN KEY (RoomID) REFERENCES Room(RoomID)
);

-- Equipment Table
CREATE TABLE Equipment (
    EquipmentID SERIAL PRIMARY KEY,
    Type VARCHAR(100),
    Status VARCHAR(50),
    LastMaintenanceDate DATE
);

-- Event Table
CREATE TABLE Event (
    EventID SERIAL PRIMARY KEY,
    Name VARCHAR(255),
    Description TEXT,
    DateTime TIMESTAMP,
    Location VARCHAR(255)
);

-- MemberClassParticipation Table
CREATE TABLE MemberClassParticipation (
    MemberID INT,
    ClassID INT,
    DateJoined DATE,
    PRIMARY KEY (MemberID, ClassID),
    FOREIGN KEY (MemberID) REFERENCES Members(MemberID),
    FOREIGN KEY (ClassID) REFERENCES GroupClasses(ClassID)
);

-- Billing Table
CREATE TABLE Billing (
    BillID SERIAL PRIMARY KEY,
    MemberID INT,
    Amount DECIMAL(10, 2),
    Date DATE,
    PaymentMethod VARCHAR(100),
    FOREIGN KEY (MemberID) REFERENCES Members(MemberID)
);

-- LoyaltyPoints Table
CREATE TABLE LoyaltyPoints (
    MemberID INT PRIMARY KEY,
    PointsEarned INT,
    FOREIGN KEY (MemberID) REFERENCES Members(MemberID)
);

-- ProgressNotes Table
CREATE TABLE ProgressNotes (
    NoteID SERIAL PRIMARY KEY,
    ClassID INT,
    TrainerID INT,
    Content TEXT,
    Date DATE,
    FOREIGN KEY (ClassID) REFERENCES GroupClasses(ClassID),
    FOREIGN KEY (TrainerID) REFERENCES Trainers(TrainerID)
);

-- Feedback Table
CREATE TABLE Feedback (
    FeedbackID SERIAL PRIMARY KEY,
    MemberID INT,
    ClassID INT,
    Content TEXT,
    Date DATE,
    FOREIGN KEY (MemberID) REFERENCES Members(MemberID),
    FOREIGN KEY (ClassID) REFERENCES GroupClasses(ClassID)
);

-- Notifications Table
CREATE TABLE Notifications (
    NotificationID SERIAL PRIMARY KEY,
    MemberID INT,
    Content TEXT,
    DateSent DATE,
    FOREIGN KEY (MemberID) REFERENCES Members(MemberID)
);

-- MemberEventParticipation Table
CREATE TABLE MemberEventParticipation (
    MemberID INT,
    EventID INT,
    PRIMARY KEY (MemberID, EventID),
    FOREIGN KEY (MemberID) REFERENCES Members(MemberID),
    FOREIGN KEY (EventID) REFERENCES Event(EventID)
);

-- TrainerEventParticipation Table
CREATE TABLE TrainerEventParticipation (
    TrainerID INT,
    EventID INT,
    PRIMARY KEY (TrainerID, EventID),
    FOREIGN KEY (TrainerID) REFERENCES Trainers(TrainerID),
    FOREIGN KEY (EventID) REFERENCES Event(EventID)
);

-- Alter Room Table to Add IsAvailable Column
ALTER TABLE Room
ADD COLUMN IsAvailable BOOLEAN DEFAULT TRUE;

-- OneToOneClasses Table
CREATE TABLE OneToOneClasses (
    ClassID SERIAL PRIMARY KEY,
    TrainerID INT,
    MemberID INT,
    DateTime TIMESTAMP,
    Description TEXT,
    FOREIGN KEY (TrainerID) REFERENCES Trainers(TrainerID),
    FOREIGN KEY (MemberID) REFERENCES Members(MemberID)
);
