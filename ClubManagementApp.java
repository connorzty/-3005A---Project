import java.sql.*;
import java.util.Scanner;

public class ClubManagementApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        try (Connection connection = getConnection()) {
            while (true) {
                System.out.println("\n=============================");
                System.out.println("健身俱乐部管理系统主界面");
                System.out.println("1. 会员信息管理");
                System.out.println("2. 课程管理");
                System.out.println("3. 教练管理");
                System.out.println("4. 设备管理");
                System.out.println("5. 活动管理");
                System.out.println("6. 员工管理");
                System.out.println("7. 财务和账单管理");
                System.out.println("8. 忠诚积分系统");
                System.out.println("9. 进度记录和反馈");
                System.out.println("10. 通知管理");
                System.out.println("11. 退出");
                System.out.println("=============================");
                System.out.print("请选择一个选项: ");

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
                        manageStaff(connection);
                        break;
                    case 7:
                        manageFinance(connection);
                        break;
                    case 8:
                        manageLoyaltyPoints(connection);
                        break;
                    case 9:
                        manageProgressNotes(connection);
                        break;
                    case 10:
                        manageNotifications(connection);
                        break;
                    case 11:
                        System.out.println("退出程序");
                        return;
                    default:
                        System.out.println("无效的选项，请重新输入");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        scanner.close();
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
            System.out.println("\n=== 会员信息管理 ===");
            System.out.println("1. 查看会员列表");
            System.out.println("2. 添加新会员");
            System.out.println("3. 更新会员信息");
            System.out.println("4. 删除会员记录");
            System.out.println("5. 返回上级菜单");
            System.out.print("请选择一个选项: ");
    
            choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    viewMembers(connection);
                    break;
                case 2:
                    addMember(connection);
                    break;
                case 3:
                    updateMember(connection);
                    break;
                case 4:
                    deleteMember(connection);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("无效的选项，请重新输入");
            }
        }
    }
    private static void viewMembers(Connection connection) {
        String sql = "SELECT * FROM Members;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                // 假设Members表包含MemberID, Name, DateOfBirth, Gender, ContactInfo, HealthMetrics, FitnessGoals
                System.out.println("Member ID: " + rs.getInt("MemberID") + ", Name: " + rs.getString("Name") +
                                   ", Date of Birth: " + rs.getDate("DateOfBirth") + ", Gender: " + rs.getString("Gender") +
                                   ", Contact Info: " + rs.getString("ContactInfo") + ", Health Metrics: " + rs.getString("HealthMetrics") +
                                   ", Fitness Goals: " + rs.getString("FitnessGoals"));
            }
        } catch (SQLException e) {
            System.out.println("数据库查询失败: " + e.getMessage());
        }
    }

    private static void addMember(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("请输入会员姓名: ");
        String name = scanner.nextLine();
    
        System.out.print("请输入会员生日 (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
    
        System.out.print("请输入会员性别: ");
        String gender = scanner.nextLine();
    
        System.out.print("请输入会员联系信息: ");
        String contact = scanner.nextLine();
    
        System.out.print("请输入会员健康指标: ");
        String health = scanner.nextLine();
    
        System.out.print("请输入会员健身目标: ");
        String goals = scanner.nextLine();
    
        String sql = "INSERT INTO Members (Name, DateOfBirth, Gender, ContactInfo, HealthMetrics, FitnessGoals) VALUES (?, ?, ?, ?, ?, ?);";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDate(2, Date.valueOf(dob));
            pstmt.setString(3, gender);
            pstmt.setString(4, contact);
            pstmt.setString(5, health);
            pstmt.setString(6, goals);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("会员添加成功！");
            } else {
                System.out.println("会员添加失败。");
            }
        } catch (SQLException e) {
            System.out.println("数据库操作失败: " + e.getMessage());
        }
    }
    
    private static void updateMember(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("请输入要更新的会员ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();  // 清除换行符
    
        System.out.print("请输入新的联系信息: ");
        String newContact = scanner.nextLine();
    
        String sql = "UPDATE Members SET ContactInfo = ? WHERE MemberID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newContact);
            pstmt.setInt(2, memberId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("会员信息更新成功！");
            } else {
                System.out.println("会员信息更新失败。");
            }
        } catch (SQLException e) {
            System.out.println("数据库操作失败: " + e.getMessage());
        }
    }

    
    private static void deleteMember(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("请输入要删除的会员ID: ");
        int memberId = scanner.nextInt();
    
        String sql = "DELETE FROM Members WHERE MemberID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("会员删除成功！");
            } else {
                System.out.println("会员删除失败。");
            }
        } catch (SQLException e) {
            System.out.println("数据库操作失败: " + e.getMessage());
        }
    }
    
    

    private static void manageClasses(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;
    
        while (true) {
            System.out.println("\n=== 课程管理 ===");
            System.out.println("1. 查看课程列表");
            System.out.println("2. 添加新课程");
            System.out.println("3. 更新课程信息");
            System.out.println("4. 删除课程记录");
            System.out.println("5. 返回上级菜单");
            System.out.print("请选择一个选项: ");
    
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
                    System.out.println("无效的选项，请重新输入");
            }
        }
    }
    
    private static void viewClasses(Connection connection) {
        String sql = "SELECT * FROM GroupClasses;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("Class ID: " + rs.getInt("ClassID") + ", Name: " + rs.getString("Name") +
                                   ", Trainer ID: " + rs.getInt("TrainerID") + ", Room ID: " + rs.getInt("RoomID") +
                                   ", Schedule: " + rs.getString("Schedule") + ", Max Capacity: " + rs.getInt("MaxCapacity"));
            }
        } catch (SQLException e) {
            System.out.println("数据库查询失败: " + e.getMessage());
        }
    }
    
    
    private static void addClass(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("请输入课程名称: ");
        String name = scanner.nextLine();
    
        System.out.print("请输入教练ID: ");
        int trainerId = scanner.nextInt();
    
        System.out.print("请输入房间ID: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();  // 清除换行符
    
        System.out.print("请输入课程时间表: ");
        String schedule = scanner.nextLine();
    
        System.out.print("请输入最大容量: ");
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
                System.out.println("课程添加成功！");
            } else {
                System.out.println("课程添加失败。");
            }
        } catch (SQLException e) {
            System.out.println("数据库操作失败: " + e.getMessage());
        }
    }
    
    
    private static void updateClass(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("请输入要更新的课程ID: ");
        int classId = scanner.nextInt();
        scanner.nextLine();  // 清除换行符
    
        System.out.print("请输入新的课程名称: ");
        String newName = scanner.nextLine();
    
        String sql = "UPDATE GroupClasses SET Name = ? WHERE ClassID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, classId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("课程信息更新成功！");
            } else {
                System.out.println("课程信息更新失败。");
            }
        } catch (SQLException e) {
            System.out.println("数据库操作失败: " + e.getMessage());
        }
    }
    
    
    private static void deleteClass(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("请输入要删除的课程ID: ");
        int classId = scanner.nextInt();
    
        String sql = "DELETE FROM GroupClasses WHERE ClassID = ?;";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, classId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("课程删除成功！");
            } else {
                System.out.println("课程删除失败。");
            }
        } catch (SQLException e) {
            System.out.println("数据库操作失败: " + e.getMessage());
        }
    }
    

    private static void manageTrainers(Connection connection) {
        // 教练管理逻辑
    }

    private static void manageEquipment(Connection connection) {
        // 设备管理逻辑
    }

    private static void manageEvents(Connection connection) {
        // 活动管理逻辑
    }

    private static void manageStaff(Connection connection) {
        // 员工管理逻辑
    }

    private static void manageFinance(Connection connection) {
        // 财务和账单管理逻辑
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
