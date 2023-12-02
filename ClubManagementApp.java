import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class ClubManagementApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        try (Connection connection = getConnection()) {
            while (true) {
                System.out.println("\n=============================");
                System.out.println("Health and Fitness Club Management System");
                System.out.println("1. Member Management");
                System.out.println("2. Class Management");
                System.out.println("3. Trainer Management");
                System.out.println("4. Equipment Management");
                System.out.println("5. Event Management");
                System.out.println("6. Room Management");
                System.out.println("7. One-to-One Class Management");
                System.out.println("8. Staff Management");
                System.out.println("9. Finance and Billing Management");
                System.out.println("10. Loyalty Points System");
                System.out.println("11. Progress Notes and Feedback");
                System.out.println("12. Notification Management");
                System.out.println("13. Exit");
                System.out.println("=============================");
                System.out.print("Select an option: ");

                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        manageMembers(connection);
                        break;
                    case 2:
                        manageClasses(connection);
                        break;
                    case 3:
                        manageTrainers(connection);
                        break;
                    case 4:
                        manageEquipment(connection);
                        break;
                    case 5:
                        manageEvents(connection);
                        break;
                    case 6:
                        manageRooms(connection);
                        break;
                    case 7:
                        manageOneToOneClasses(connection);
                        break;
                    case 8:
                        manageStaff(connection);
                        break;
                    case 9:
                        manageBilling(connection);
                        break;
                    case 10:
                        manageLoyaltyPoints(connection);
                        break;
                    case 11:
                        manageProgressNotes(connection);
                        break;
                    case 12:
                        manageNotifications(connection);
                        break;
                    case 13:
                        System.out.println("Exiting the program.");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 数据库连接方法
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/gym";
        String user = "postgres";
        String password = "987654321XX";
        return DriverManager.getConnection(url, user, password);
    }

    // 以下是各管理功能的桩方法
    private static void manageMembers(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;
    
        while (true) {
            System.out.println("\n=== Member Management ===");
            System.out.println("1. View Member List");
            System.out.println("2. Return to Previous Menu");
            System.out.print("Select an option: ");
    
            choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    viewMembers(connection);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    
    private static void viewMembers(Connection connection) {
        String sql = "SELECT * FROM Members;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("Member ID: " + rs.getInt("MemberID") + ", Name: " + rs.getString("Name") +
                                   ", Date of Birth: " + rs.getDate("DateOfBirth") + ", Gender: " + rs.getString("Gender") +
                                   ", Contact Info: " + rs.getString("ContactInfo") + ", Health Metrics: " + rs.getString("HealthMetrics") +
                                   ", Fitness Goals: " + rs.getString("FitnessGoals"));
            }
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
        }
    }
    

 

    private static void manageClasses(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;
    
        while (true) {
            System.out.println("\n=== Class Management ===");
            System.out.println("1. View Class List");
            System.out.println("2. Add New Class");
            System.out.println("3. Update Class Information");
            System.out.println("4. Delete Class Record");
            System.out.println("5. Return to Previous Menu");
            System.out.print("Select an option: ");
    
            choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    viewClasses(connection);
                    break;
                case 2:
                    addClass(connection);
                    break;
                case 3:
                    updateClass(connection);
                    break;
                case 4:
                    deleteClass(connection);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    
    
    private static void viewClasses(Connection connection) {
        String sql = "SELECT c.*, r.IsAvailable FROM GroupClasses c JOIN Room r ON c.RoomID = r.RoomID;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("Class ID: " + rs.getInt("ClassID") + ", Name: " + rs.getString("Name") +
                                   ", Trainer ID: " + rs.getInt("TrainerID") + ", Room ID: " + rs.getInt("RoomID") +
                                   ", Schedule: " + rs.getString("Schedule") + ", Max Capacity: " + rs.getInt("MaxCapacity") +
                                   ", Room Available: " + rs.getBoolean("IsAvailable"));
            }
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
        }
    }
    
    private static void addClass(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter class name: ");
        String name = scanner.nextLine();
    
        System.out.print("Enter trainer ID: ");
        int trainerId = scanner.nextInt();
    
        System.out.print("Enter room ID: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        // Check if the selected room is available
        if (!isRoomAvailable(connection, roomId)) {
            System.out.println("The selected room is not available.");
            return;
        }
    
        System.out.print("Enter class schedule: ");
        String schedule = scanner.nextLine();
    
        System.out.print("Enter max capacity: ");
        int maxCapacity = scanner.nextInt();
    
        String sql = "INSERT INTO GroupClasses (Name, TrainerID, RoomID, Schedule, MaxCapacity) VALUES (?, ?, ?, ?, ?);";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, trainerId);
            pstmt.setInt(3, roomId);
            pstmt.setString(4, schedule);
            pstmt.setInt(5, maxCapacity);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Class added successfully!");
            } else {
                System.out.println("Failed to add class.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    
    
    private static void updateClass(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the class ID to update: ");
        int classId = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        System.out.print("Enter new class name: ");
        String newName = scanner.nextLine();
    
        System.out.print("Enter new trainer ID: ");
        int newTrainerId = scanner.nextInt();
    
        System.out.print("Enter new room ID: ");
        int newRoomId = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        // Check if the new room is available
        if (!isRoomAvailable(connection, newRoomId)) {
            System.out.println("The selected new room is not available.");
            return;
        }
    
        System.out.print("Enter new schedule: ");
        String newSchedule = scanner.nextLine();
    
        System.out.print("Enter new max capacity: ");
        int newMaxCapacity = scanner.nextInt();
    
        String sql = "UPDATE GroupClasses SET Name = ?, TrainerID = ?, RoomID = ?, Schedule = ?, MaxCapacity = ? WHERE ClassID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newTrainerId);
            pstmt.setInt(3, newRoomId);
            pstmt.setString(4, newSchedule);
            pstmt.setInt(5, newMaxCapacity);
            pstmt.setInt(6, classId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Class information updated successfully!");
            } else {
                System.out.println("Failed to update class information.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    
    
    
    
    private static void deleteClass(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the class ID to delete: ");
        int classId = scanner.nextInt();
    
        String sql = "DELETE FROM GroupClasses WHERE ClassID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, classId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Class deleted successfully!");
            } else {
                System.out.println("Failed to delete class.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    
    private static boolean isRoomAvailable(Connection connection, int roomId) {
        String sql = "SELECT IsAvailable FROM Room WHERE RoomID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("IsAvailable");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
        return false;
    }

    private static void manageTrainers(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;
    
        while (true) {
            System.out.println("\n=== Trainer Management ===");
            System.out.println("1. View Trainer List");
            System.out.println("2. Add New Trainer");
            System.out.println("3. Update Trainer Information");
            System.out.println("4. Delete Trainer Record");
            System.out.println("5. Return to Previous Menu");
            System.out.print("Select an option: ");
    
            choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    viewTrainers(connection);
                    break;
                case 2:
                    addTrainer(connection);
                    break;
                case 3:
                    updateTrainer(connection);
                    break;
                case 4:
                    deleteTrainer(connection);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    private static void viewTrainers(Connection connection) {
        String sql = "SELECT * FROM Trainers;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("Trainer ID: " + rs.getInt("TrainerID") + ", Name: " + rs.getString("Name") +
                                   ", Qualifications: " + rs.getString("Qualifications"));
            }
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
        }
    }
    private static void addTrainer(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter trainer's name: ");
        String name = scanner.nextLine();
    
        System.out.print("Enter trainer's qualifications: ");
        String qualifications = scanner.nextLine();
    
        String sql = "INSERT INTO Trainers (Name, Qualifications) VALUES (?, ?);";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, qualifications);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Trainer added successfully!");
            } else {
                System.out.println("Failed to add trainer.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void updateTrainer(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the trainer ID to update: ");
        int trainerId = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        System.out.print("Enter new qualifications: ");
        String newQualifications = scanner.nextLine();
    
        String sql = "UPDATE Trainers SET Qualifications = ? WHERE TrainerID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newQualifications);
            pstmt.setInt(2, trainerId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Trainer information updated successfully!");
            } else {
                System.out.println("Failed to update trainer information.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void deleteTrainer(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the trainer ID to delete: ");
        int trainerId = scanner.nextInt();
    
        String sql = "DELETE FROM Trainers WHERE TrainerID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, trainerId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Trainer deleted successfully!");
            } else {
                System.out.println("Failed to delete trainer.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
                    

    private static void manageEquipment(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;
    
        while (true) {
            System.out.println("\n=== Equipment Management ===");
            System.out.println("1. View Equipment List");
            System.out.println("2. Add New Equipment");
            System.out.println("3. Update Equipment Information");
            System.out.println("4. Delete Equipment Record");
            System.out.println("5. Return to Previous Menu");
            System.out.print("Select an option: ");
    
            choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    viewEquipment(connection);
                    break;
                case 2:
                    addEquipment(connection);
                    break;
                case 3:
                    updateEquipment(connection);
                    break;
                case 4:
                    deleteEquipment(connection);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    private static void viewEquipment(Connection connection) {
        String sql = "SELECT * FROM Equipment;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("Equipment ID: " + rs.getInt("EquipmentID") + ", Type: " + rs.getString("Type") +
                                   ", Status: " + rs.getString("Status") + ", Last Maintenance Date: " + rs.getDate("LastMaintenanceDate"));
            }
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
        }
    }
    private static void addEquipment(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter equipment type: ");
        String type = scanner.nextLine();
    
        System.out.print("Enter equipment status: ");
        String status = scanner.nextLine();
    
        System.out.print("Enter last maintenance date (YYYY-MM-DD): ");
        String lastMaintenanceDate = scanner.nextLine();
    
        String sql = "INSERT INTO Equipment (Type, Status, LastMaintenanceDate) VALUES (?, ?, ?);";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.setString(2, status);
            pstmt.setDate(3, Date.valueOf(lastMaintenanceDate));
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Equipment added successfully!");
            } else {
                System.out.println("Failed to add equipment.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void updateEquipment(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the equipment ID to update: ");
        int equipmentId = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        System.out.print("Enter new status: ");
        String newStatus = scanner.nextLine();
    
        String sql = "UPDATE Equipment SET Status = ? WHERE EquipmentID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, equipmentId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Equipment information updated successfully!");
            } else {
                System.out.println("Failed to update equipment information.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void deleteEquipment(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the equipment ID to delete: ");
        int equipmentId = scanner.nextInt();
    
        String sql = "DELETE FROM Equipment WHERE EquipmentID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, equipmentId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Equipment deleted successfully!");
            } else {
                System.out.println("Failed to delete equipment.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
        

    private static void manageEvents(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;
    
        while (true) {
            System.out.println("\n=== Event Management ===");
            System.out.println("1. View Event List");
            System.out.println("2. Add New Event");
            System.out.println("3. Update Event Information");
            System.out.println("4. Delete Event Record");
            System.out.println("5. Return to Previous Menu");
            System.out.print("Select an option: ");
    
            choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    viewEvents(connection);
                    break;
                case 2:
                    addEvent(connection);
                    break;
                case 3:
                    updateEvent(connection);
                    break;
                case 4:
                    deleteEvent(connection);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    private static void viewEvents(Connection connection) {
        String sql = "SELECT * FROM Event;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("Event ID: " + rs.getInt("EventID") + ", Name: " + rs.getString("Name") +
                                   ", Description: " + rs.getString("Description") + ", Date and Time: " + rs.getTimestamp("DateTime") +
                                   ", Location: " + rs.getString("Location"));
            }
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
        }
    }
    private static void addEvent(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter event name: ");
        String name = scanner.nextLine();
    
        System.out.print("Enter event description: ");
        String description = scanner.nextLine();
    
        System.out.print("Enter event date and time (YYYY-MM-DD HH:MM:SS): ");
        String dateTime = scanner.nextLine();
    
        System.out.print("Enter event location: ");
        String location = scanner.nextLine();
    
        String sql = "INSERT INTO Event (Name, Description, DateTime, Location) VALUES (?, ?, ?, ?);";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setTimestamp(3, Timestamp.valueOf(dateTime));
            pstmt.setString(4, location);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Event added successfully!");
            } else {
                System.out.println("Failed to add event.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void updateEvent(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the event ID to update: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        System.out.print("Enter new event description: ");
        String newDescription = scanner.nextLine();
    
        String sql = "UPDATE Event SET Description = ? WHERE EventID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newDescription);
            pstmt.setInt(2, eventId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Event information updated successfully!");
            } else {
                System.out.println("Failed to update event information.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void deleteEvent(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the event ID to delete: ");
        int eventId = scanner.nextInt();
    
        String sql = "DELETE FROM Event WHERE EventID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Event deleted successfully!");
            } else {
                System.out.println("Failed to delete event.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    
    private static void manageRooms(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;
    
        while (true) {
            System.out.println("\n=== Room Management ===");
            System.out.println("1. View Room List");
            System.out.println("2. Add New Room");
            System.out.println("3. Update Room Information");
            System.out.println("4. Delete Room");
            System.out.println("5. Return to Previous Menu");
            System.out.print("Select an option: ");
    
            choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    viewRooms(connection);
                    break;
                case 2:
                    addRoom(connection);
                    break;
                case 3:
                    updateRoom(connection);
                    break;
                case 4:
                    deleteRoom(connection);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    private static void viewRooms(Connection connection) {
        String sql = "SELECT * FROM Room;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("Room ID: " + rs.getInt("RoomID") + ", Name: " + rs.getString("Name") +
                                   ", Capacity: " + rs.getInt("Capacity") + ", Type: " + rs.getString("Type") +
                                   ", Is Available: " + rs.getBoolean("IsAvailable"));
            }
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
        }
    }
    private static void addRoom(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter room name: ");
        String name = scanner.nextLine();
    
        System.out.print("Enter room capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        System.out.print("Enter room type: ");
        String type = scanner.nextLine();
    
        String sql = "INSERT INTO Room (Name, Capacity, Type, IsAvailable) VALUES (?, ?, ?, TRUE);";  // Assuming new rooms are available by default
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, capacity);
            pstmt.setString(3, type);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Room added successfully!");
            } else {
                System.out.println("Failed to add room.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void updateRoom(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the room ID to update: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        System.out.print("Enter new room name: ");
        String newName = scanner.nextLine();
    
        System.out.print("Enter new capacity: ");
        int newCapacity = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        System.out.print("Enter new room type: ");
        String newType = scanner.nextLine();
    
        System.out.print("Is the room available (true/false): ");
        boolean isAvailable = scanner.nextBoolean();
    
        String sql = "UPDATE Room SET Name = ?, Capacity = ?, Type = ?, IsAvailable = ? WHERE RoomID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newCapacity);
            pstmt.setString(3, newType);
            pstmt.setBoolean(4, isAvailable);
            pstmt.setInt(5, roomId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Room information updated successfully!");
            } else {
                System.out.println("Failed to update room information.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void deleteRoom(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the room ID to delete: ");
        int roomId = scanner.nextInt();
    
        String sql = "DELETE FROM Room WHERE RoomID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Room deleted successfully!");
            } else {
                System.out.println("Failed to delete room.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void manageOneToOneClasses(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;
    
        while (true) {
            System.out.println("\n=== One-to-One Class Management ===");
            System.out.println("1. View One-to-One Class List");
            System.out.println("2. Add New One-to-One Class");
            System.out.println("3. Update One-to-One Class Information");
            System.out.println("4. Delete One-to-One Class");
            System.out.println("5. Return to Previous Menu");
            System.out.print("Select an option: ");
    
            choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    viewOneToOneClasses(connection);
                    break;
                case 2:
                    addOneToOneClass(connection);
                    break;
                case 3:
                    updateOneToOneClass(connection);
                    break;
                case 4:
                    deleteOneToOneClass(connection);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    private static void viewOneToOneClasses(Connection connection) {
        String sql = "SELECT * FROM OneToOneClasses;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("Class ID: " + rs.getInt("ClassID") + ", Trainer ID: " + rs.getInt("TrainerID") +
                                   ", Member ID: " + rs.getInt("MemberID") + ", Description: " + rs.getString("Description"));
            }
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
        }
    }
    private static void addOneToOneClass(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter trainer ID: ");
        int trainerId = scanner.nextInt();
    
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        System.out.print("Enter class description: ");
        String description = scanner.nextLine();
    
        String sql = "INSERT INTO OneToOneClasses (TrainerID, MemberID, Description) VALUES (?, ?, ?);";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, trainerId);
            pstmt.setInt(2, memberId);
            pstmt.setString(3, description);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("One-to-One Class added successfully!");
            } else {
                System.out.println("Failed to add One-to-One Class.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void updateOneToOneClass(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the class ID to update: ");
        int classId = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        System.out.print("Enter new description: ");
        String newDescription = scanner.nextLine();
    
        String sql = "UPDATE OneToOneClasses SET Description = ? WHERE ClassID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newDescription);
            pstmt.setInt(2, classId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("One-to-One Class information updated successfully!");
            } else {
                System.out.println("Failed to update One-to-One Class information.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }

    private static void deleteOneToOneClass(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the class ID to delete: ");
        int classId = scanner.nextInt();
    
        String sql = "DELETE FROM OneToOneClasses WHERE ClassID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, classId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("One-to-One Class deleted successfully!");
            } else {
                System.out.println("Failed to delete One-to-One Class.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    
                                    

    private static void manageStaff(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;
    
        while (true) {
            System.out.println("\n=== Staff Management ===");
            System.out.println("1. View Staff List");
            System.out.println("2. Add New Staff Member");
            System.out.println("3. Update Staff Information");
            System.out.println("4. Delete Staff Member");
            System.out.println("5. Return to Previous Menu");
            System.out.print("Select an option: ");
    
            choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    viewStaff(connection);
                    break;
                case 2:
                    addStaffMember(connection);
                    break;
                case 3:
                    updateStaff(connection);
                    break;
                case 4:
                    deleteStaffMember(connection);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    private static void viewStaff(Connection connection) {
        String sql = "SELECT * FROM Staff;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("Staff ID: " + rs.getInt("StaffID") + ", Name: " + rs.getString("Name") +
                                   ", Role: " + rs.getString("Role") + ", Contact Info: " + rs.getString("ContactInfo"));
            }
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
        }
    }
    private static void addStaffMember(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter staff name: ");
        String name = scanner.nextLine();
    
        System.out.print("Enter staff role: ");
        String role = scanner.nextLine();
    
        System.out.print("Enter staff contact information: ");
        String contactInfo = scanner.nextLine();
    
        String sql = "INSERT INTO Staff (Name, Role, ContactInfo) VALUES (?, ?, ?);";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, role);
            pstmt.setString(3, contactInfo);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Staff member added successfully!");
            } else {
                System.out.println("Failed to add staff member.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }

    private static void updateStaff(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the staff ID to update: ");
        int staffId = scanner.nextInt();
        scanner.nextLine();  // Clear the newline character
    
        System.out.print("Enter new role: ");
        String newRole = scanner.nextLine();
    
        System.out.print("Enter new contact information: ");
        String newContactInfo = scanner.nextLine();
    
        String sql = "UPDATE Staff SET Role = ?, ContactInfo = ? WHERE StaffID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newRole);
            pstmt.setString(2, newContactInfo);
            pstmt.setInt(3, staffId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Staff information updated successfully!");
            } else {
                System.out.println("Failed to update staff information.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void deleteStaffMember(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the staff ID to delete: ");
        int staffId = scanner.nextInt();
    
        String sql = "DELETE FROM Staff WHERE StaffID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, staffId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Staff member deleted successfully!");
            } else {
                System.out.println("Failed to delete staff member.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
        

    private static void manageBilling(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;
    
        while (true) {
            System.out.println("\n=== Billing Management ===");
            System.out.println("1. Generate and View Billing Records");
            System.out.println("2. Delete Bill");
            System.out.println("3. Return to Previous Menu");
            System.out.print("Select an option: ");
    
            choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    generateAndViewBillingRecords(connection);
                    break;
                case 2:
                    deleteBill(connection);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    
    private static void generateAndViewBillingRecords(Connection connection) {
        // 首先生成账单
        generateBillingRecords(connection);
    
        // 然后查看所有账单记录
        viewBillingRecords(connection);
    }
    
    // generateBillingRecords 和 viewBillingRecords 方法与之前提供的相同
    
    private static void viewBillingRecords(Connection connection) {
        String sql = "SELECT * FROM Billing;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("Bill ID: " + rs.getInt("BillID") + ", Member ID: " + rs.getInt("MemberID") +
                                   ", Amount: " + rs.getBigDecimal("Amount") + ", Date: " + rs.getDate("Date") +
                                   ", Payment Status: " + rs.getString("PaymentStatus"));
            }
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
        }
    }


// generateBillingRecords 和 viewBillingRecords 方法与之前提供的相同

    
    private static void generateBillingRecords(Connection connection) {
        // SQL 用于计算每个会员的课程总费用
        String sql = "SELECT MemberID, SUM(CASE WHEN ClassType = 'Group' THEN Duration * 30 " +
                     "WHEN ClassType = 'OneToOne' THEN Duration * 80 END) as TotalAmount " +
                     "FROM CourseParticipation GROUP BY MemberID;";
    
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int memberId = rs.getInt("MemberID");
                BigDecimal totalAmount = rs.getBigDecimal("TotalAmount");
    
                // 调用方法创建新账单
                createBillForMember(connection, memberId, totalAmount);
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    
    private static void createBillForMember(Connection connection, int memberId, BigDecimal amount) {

            // 检查是否已存在账单
        if (isBillAlreadyGenerated(connection, memberId)) {
            System.out.println("Billing record already exists for Member ID: " + memberId);
            return; // 如果已存在，则不再生成
        }

        // 当前日期作为账单日期
        Date billDate = new Date(System.currentTimeMillis());
        // 默认支付状态为“Unpaid”
        String paymentStatus = "Unpaid";
    
        String sql = "INSERT INTO Billing (MemberID, Amount, Date, PaymentStatus) VALUES (?, ?, ?, ?);";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setBigDecimal(2, amount);
            pstmt.setDate(3, billDate);
            pstmt.setString(4, paymentStatus);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Billing record created successfully for Member ID: " + memberId);
            } else {
                System.out.println("Failed to create billing record for Member ID: " + memberId);
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    
    private static boolean isBillAlreadyGenerated(Connection connection, int memberId) {
        String sql = "SELECT COUNT(*) FROM Billing WHERE MemberID = ? AND MONTH(Date) = MONTH(CURRENT_DATE) AND YEAR(Date) = YEAR(CURRENT_DATE);";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
    
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // 如果当前月份已存在账单，返回true
                }
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
        return false;  // 默认情况下，如果没有找到账单或出现异常，则假设没有生成账单
    }
    
    
    private static void deleteBill(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the bill ID to delete: ");
        int billId = scanner.nextInt();
    
        String sql = "DELETE FROM Billing WHERE BillID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, billId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Bill deleted successfully!");
            } else {
                System.out.println("Failed to delete bill.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    
    

    private static void manageLoyaltyPoints(Connection connection) {
        // 忠诚积分系统逻辑
    }

    private static void manageProgressNotes(Connection connection) {
        // 进度记录和反馈逻辑
    }

    private static void manageNotifications(Connection connection) {
        // 通知管理逻辑
    }
}
