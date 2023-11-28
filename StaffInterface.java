import java.sql.*;
import java.util.Scanner;

public class StaffInterface {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection connection = getConnection()) {

            while (true) {
                System.out.println("\n俱乐部员工界面");
                System.out.println("1. 会员信息管理");
                System.out.println("2. 课程管理");
                System.out.println("3. 教练管理");
                System.out.println("4. 设备管理");
                System.out.println("5. 退出");
                System.out.print("请选择一个选项: ");

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        manageMembers(connection, scanner);
                        break;
                    case 2:
                        manageClasses(connection, scanner);
                        break;
                    case 3:
                        manageTrainers(connection, scanner);
                        break;
                    case 4:
                        manageEquipment(connection, scanner);
                        break;
                    case 5:
                        System.out.println("退出程序");
                        return;
                    default:
                        System.out.println("无效选项");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/jam";
        String user = "postgres";
        String password = "987654321XX";
        return DriverManager.getConnection(url, user, password);
    }

    private static void manageMembers(Connection connection, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\n会员信息管理");
            System.out.println("1. 查看会员信息");
            System.out.println("2. 添加新会员");
            System.out.println("3. 更新会员信息");
            System.out.println("4. 删除会员信息");
            System.out.println("5. 返回上一级菜单");
            System.out.print("请选择一个选项: ");
    
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    viewMemberInfo(connection, scanner);
                    break;
                case 2:
                    addNewMember(connection, scanner);
                    break;
                case 3:
                    updateMemberInfo(connection, scanner);
                    break;
                case 4:
                    deleteMember(connection, scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("无效选项");
            }
        }
    }
    

    private static void manageClasses(Connection connection, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\n课程管理");
            System.out.println("1. 查看所有课程");
            System.out.println("2. 添加新课程");
            System.out.println("3. 更新课程信息");
            System.out.println("4. 删除课程");
            System.out.println("5. 返回上一级菜单");
            System.out.print("请选择一个选项: ");
    
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    viewAllClasses(connection);
                    break;
                case 2:
                    addNewClass(connection, scanner);
                    break;
                case 3:
                    updateClassInfo(connection, scanner);
                    break;
                case 4:
                    deleteClass(connection, scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("无效选项");
            }
        }
    }

    private static void manageTrainers(Connection connection, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\n教练管理");
            System.out.println("1. 查看所有教练");
            System.out.println("2. 添加新教练");
            System.out.println("3. 更新教练信息");
            System.out.println("4. 删除教练");
            System.out.println("5. 返回上一级菜单");
            System.out.print("请选择一个选项: ");
    
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    viewAllTrainers(connection);
                    break;
                case 2:
                    addNewTrainer(connection, scanner);
                    break;
                case 3:
                    updateTrainerInfo(connection, scanner);
                    break;
                case 4:
                    deleteTrainer(connection, scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("无效选项");
            }
        }
    }
    private static void manageEquipment(Connection connection, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\n设备管理");
            System.out.println("1. 查看所有设备");
            System.out.println("2. 添加新设备");
            System.out.println("3. 更新设备信息");
            System.out.println("4. 删除设备");
            System.out.println("5. 返回上一级菜单");
            System.out.print("请选择一个选项: ");
    
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    viewAllEquipment(connection);
                    break;
                case 2:
                    addNewEquipment(connection, scanner);
                    break;
                case 3:
                    updateEquipmentInfo(connection, scanner);
                    break;
                case 4:
                    deleteEquipment(connection, scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("无效选项");
            }
        }
    }

    private static void viewMemberInfo(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("输入会员ID以查看信息: ");
        int memberId = scanner.nextInt();
    
        String query = "SELECT * FROM Members WHERE MemberID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, memberId);
    
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("会员信息:");
                System.out.println("姓名: " + resultSet.getString("Name"));
                System.out.println("地址: " + resultSet.getString("Address"));
                System.out.println("电话: " + resultSet.getString("Phone"));
                System.out.println("电邮: " + resultSet.getString("Email"));
                System.out.println("生日: " + resultSet.getDate("DateOfBirth").toString());
            } else {
                System.out.println("未找到会员信息。");
            }
        }
    }

    private static void addNewMember(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("输入新会员姓名: ");
        scanner.nextLine(); // 清除缓冲区
        String name = scanner.nextLine();

        // 假设 Members 表中还有其他字段如 Address, Phone, Email, DateOfBirth
        System.out.print("输入地址: ");
        String address = scanner.nextLine();

        System.out.print("输入电话: ");
        String phone = scanner.nextLine();

        System.out.print("输入电邮: ");
        String email = scanner.nextLine();

        System.out.print("输入生日 (YYYY-MM-DD): ");
        String dob = scanner.nextLine();

        String insertQuery = "INSERT INTO Members (Name, Address, Phone, Email, DateOfBirth) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, email);
            preparedStatement.setDate(5, Date.valueOf(dob));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("新会员添加成功。");
            } else {
                System.out.println("新会员添加失败。");
            }
        }
    }

    private static void updateMemberInfo(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("输入要更新的会员ID: ");
        int memberId = scanner.nextInt();

        scanner.nextLine(); // 清除缓冲区
        System.out.print("输入新的姓名: ");
        String newName = scanner.nextLine();

        System.out.print("输入新的地址: ");
        String newAddress = scanner.nextLine();

        System.out.print("输入新的电话: ");
        String newPhone = scanner.nextLine();

        System.out.print("输入新的电邮: ");
        String newEmail = scanner.nextLine();

        System.out.print("输入新的生日 (YYYY-MM-DD): ");
        String newDob = scanner.nextLine();

        String updateQuery = "UPDATE Members SET Name = ?, Address = ?, Phone = ?, Email = ?, DateOfBirth = ? WHERE MemberID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newAddress);
            preparedStatement.setString(3, newPhone);
            preparedStatement.setString(4, newEmail);
            preparedStatement.setDate(5, Date.valueOf(newDob));
            preparedStatement.setInt(6, memberId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("会员信息更新成功。");
            } else {
                System.out.println("会员信息更新失败。");
            }
        }
    }

    private static void deleteMember(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("输入要删除的会员ID: ");
        int memberId = scanner.nextInt();

        String deleteQuery = "DELETE FROM Members WHERE MemberID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, memberId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("会员删除成功。");
            } else {
                System.out.println("会员删除失败。");
            }
        }
    }

    private static void viewAllClasses(Connection connection) throws SQLException {
        String query = "SELECT * FROM FitnessClasses";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                System.out.println("课程ID: " + resultSet.getInt("ClassID") + ", 课程名称: " + resultSet.getString("ClassName") + ", 教练ID: " + resultSet.getInt("TrainerID") + ", 时间安排: " + resultSet.getString("Schedule"));
            }
        }
    }
    
    // 添加新课程
    private static void addNewClass(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("输入课程名称: ");
        scanner.nextLine();
        String className = scanner.nextLine();
    
        System.out.print("输入教练ID: ");
        int trainerId = scanner.nextInt();
    
        System.out.print("输入课程安排信息: ");
        scanner.nextLine();
        String schedule = scanner.nextLine();
    
        String insertQuery = "INSERT INTO FitnessClasses (ClassName, TrainerID, Schedule) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, className);
            preparedStatement.setInt(2, trainerId);
            preparedStatement.setString(3, schedule);
    
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("课程添加成功。");
            } else {
                System.out.println("课程添加失败。");
            }
        }
    }
    
    // 更新课程信息
    private static void updateClassInfo(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("输入要更新的课程ID: ");
        int classId = scanner.nextInt();
    
        System.out.print("输入新的课程名称: ");
        scanner.nextLine();
        String newClassName = scanner.nextLine();
    
        System.out.print("输入新的教练ID: ");
        int newTrainerId = scanner.nextInt();
    
        System.out.print("输入新的课程安排信息: ");
        scanner.nextLine();
        String newSchedule = scanner.nextLine();
    
        String updateQuery = "UPDATE FitnessClasses SET ClassName = ?, TrainerID = ?, Schedule = ? WHERE ClassID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newClassName);
            preparedStatement.setInt(2, newTrainerId);
            preparedStatement.setString(3, newSchedule);
            preparedStatement.setInt(4, classId);
    
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("课程信息更新成功。");
            } else {
                System.out.println("课程信息更新失败。");
            }
        }
    }
    
    // 删除课程
    private static void deleteClass(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("输入要删除的课程ID: ");
        int classId = scanner.nextInt();
    
        String deleteQuery = "DELETE FROM FitnessClasses WHERE ClassID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, classId);
    
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("课程删除成功。");
            } else {
                System.out.println("课程删除失败。");
            }
        }
    }



    private static void viewAllTrainers(Connection connection) throws SQLException {
        String query = "SELECT * FROM Trainers";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                System.out.println("教练ID: " + resultSet.getInt("TrainerID") + ", 姓名: " + resultSet.getString("Name") + ", 资格: " + resultSet.getString("Qualifications") + ", 专长: " + resultSet.getString("Specialization"));
            }
        }
    }
    
    // 添加新教练
    private static void addNewTrainer(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("输入教练姓名: ");
        scanner.nextLine();
        String name = scanner.nextLine();
    
        System.out.print("输入教练资格: ");
        String qualifications = scanner.nextLine();
    
        System.out.print("输入教练专长: ");
        String specialization = scanner.nextLine();
    
        String insertQuery = "INSERT INTO Trainers (Name, Qualifications, Specialization) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, qualifications);
            preparedStatement.setString(3, specialization);
    
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("教练添加成功。");
            } else {
                System.out.println("教练添加失败。");
            }
        }
    }
    
    // 更新教练信息
    private static void updateTrainerInfo(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("输入要更新的教练ID: ");
        int trainerId = scanner.nextInt();
    
        scanner.nextLine(); // 清除缓冲区
        System.out.print("输入新的姓名: ");
        String newName = scanner.nextLine();
    
        System.out.print("输入新的资格: ");
        String newQualifications = scanner.nextLine();
    
        System.out.print("输入新的专长: ");
        String newSpecialization = scanner.nextLine();
    
        String updateQuery = "UPDATE Trainers SET Name = ?, Qualifications = ?, Specialization = ? WHERE TrainerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newQualifications);
            preparedStatement.setString(3, newSpecialization);
            preparedStatement.setInt(4, trainerId);
    
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("教练信息更新成功。");
            } else {
                System.out.println("教练信息更新失败。");
            }
        }
    }
    
    // 删除教练
    private static void deleteTrainer(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("输入要删除的教练ID: ");
        int trainerId = scanner.nextInt();
    
        String deleteQuery = "DELETE FROM Trainers WHERE TrainerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, trainerId);
    
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("教练删除成功。");
            } else {
                System.out.println("教练删除失败。");
            }
        }
    }



    // 查看所有设备
private static void viewAllEquipment(Connection connection) throws SQLException {
    String query = "SELECT * FROM Equipment";
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {
        while (resultSet.next()) {
            System.out.println("设备ID: " + resultSet.getInt("EquipmentID") + ", 名称: " + resultSet.getString("Name") + ", 类型: " + resultSet.getString("Type") + ", 购买日期: " + resultSet.getDate("PurchaseDate") + ", 维护计划: " + resultSet.getString("MaintenanceSchedule"));
        }
    }
}

// 添加新设备
private static void addNewEquipment(Connection connection, Scanner scanner) throws SQLException {
    System.out.print("输入设备名称: ");
    scanner.nextLine();
    String name = scanner.nextLine();

    System.out.print("输入设备类型: ");
    String type = scanner.nextLine();

    System.out.print("输入购买日期 (YYYY-MM-DD): ");
    String purchaseDate = scanner.nextLine();

    System.out.print("输入维护计划: ");
    String maintenanceSchedule = scanner.nextLine();

    String insertQuery = "INSERT INTO Equipment (Name, Type, PurchaseDate, MaintenanceSchedule) VALUES (?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, type);
        preparedStatement.setDate(3, Date.valueOf(purchaseDate));
        preparedStatement.setString(4, maintenanceSchedule);

        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("设备添加成功。");
        } else {
            System.out.println("设备添加失败。");
        }
    }
}

// 更新设备信息
private static void updateEquipmentInfo(Connection connection, Scanner scanner) throws SQLException {
    System.out.print("输入要更新的设备ID: ");
    int equipmentId = scanner.nextInt();

    scanner.nextLine(); // 清除缓冲区
    System.out.print("输入新的设备名称: ");
    String newName = scanner.nextLine();

    System.out.print("输入新的设备类型: ");
    String newType = scanner.nextLine();

    System.out.print("输入新的购买日期 (YYYY-MM-DD): ");
    String newPurchaseDate = scanner.nextLine();

    System.out.print("输入新的维护计划: ");
    String newMaintenanceSchedule = scanner.nextLine();

    String updateQuery = "UPDATE Equipment SET Name = ?, Type = ?, PurchaseDate = ?, MaintenanceSchedule = ? WHERE EquipmentID = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
        preparedStatement.setString(1, newName);
        preparedStatement.setString(2, newType);
        preparedStatement.setDate(3, Date.valueOf(newPurchaseDate));
        preparedStatement.setString(4, newMaintenanceSchedule);
        preparedStatement.setInt(5, equipmentId);

        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("设备信息更新成功。");
        } else {
            System.out.println("设备信息更新失败。");
        }
    }
}

// 删除设备
private static void deleteEquipment(Connection connection, Scanner scanner) throws SQLException {
    System.out.print("输入要删除的设备ID: ");
    int equipmentId = scanner.nextInt();

    String deleteQuery = "DELETE FROM Equipment WHERE EquipmentID = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
        preparedStatement.setInt(1, equipmentId);

        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("设备删除成功。");
        } else {
            System.out.println("设备删除失败。");
        }
    }
}
}
