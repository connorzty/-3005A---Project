import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ClubManagementApp {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) { 
        try (Connection connection = getConnection()) {
            while (true) {
                System.out.println("Choose different System to enter: ");
                System.out.println("1. Members System");
                System.out.println("2. Staff System");
                int systemchoose = scanner.nextInt();
                switch (systemchoose) {
                    case 1:
                        System.out.println("+--------------------------------------+");
                        System.out.println("You choose Members System");
                        System.out.println("Please login your account,Please enter your MemberID");
                        int memberid = Userlogin(connection);
                        System.out.println("Your MemberID is: "+memberid);
                        System.out.println("+--------------------------------------+");
                        while(true){
                            System.out.println("+--------------------------------------+");
                            System.out.println("menu: ");
                            System.out.println("+--------------------------------------+");
                            System.out.println("|1. View your Information");//done
                            System.out.println("|2. Change your information");//done
                            System.out.println("|3. Caculate your fee");//done
                            System.out.println("|4. View your class"); //done
                            System.out.println("|5. Join a class");// done
                            System.out.println("|6. Quit a class");//done
                            System.out.println("|7. View your points");//done
                            System.out.println("|8. Quit");//done
                            System.out.println("+--------------------------------------+");
                            int choice = scanner.nextInt();
                            switch (choice) {
                                case 1:
                                    System.out.println("You choose to view your information");
                                    System.out.println("Your MemberID is: "+memberid);
                                    showMemberDetails( connection,  memberid);
                                    break;
                                case 2:
                                    System.out.println("You choose to change your information");
                                    System.out.println("Your MemberID is: "+memberid);
                                    updateMemberDetails( connection,  memberid) ;
                                    break;
                                    
                                case 3:
                                    System.out.println("You choose to caculate your fee");
                                    System.out.println("Your MemberID is: "+memberid);
                                    calculateAndUpdateBilling( connection,  memberid);
                                    break;
                                case 4:
                                    System.out.println("You choose to view your class");
                                    System.out.println("Your MemberID is: "+memberid);
                                    viewmemberclassmenu( connection,  memberid);
                                    break;
                                case 5:
                                    System.out.println("You choose to join a class");
                                    System.out.println("Choose class type: ");
                                    System.out.println("1. Group Class");
                                    System.out.println("2. One-to-One Class");
                                    int classchoose =  scanner.nextInt();
                                    switch(classchoose){
                                        case 1:
                                            enrollInClass( connection,  memberid);  
                                            break;
                                        case 2:
                                            enrollInOneToOneClass( connection,  memberid);
                                            break;
                                        default:
                                            System.out.println("Invalid option, please try again.");  
                                            break;                                  
                                    }
                                    break;
                                case 6:
                                    System.out.println("You choose to quit a class");
                                    System.out.println("Choose class type: ");
                                    System.out.println("1. Group Class");
                                    System.out.println("2. One-to-One Class");
                                    int quitclasschoose = scanner.nextInt();
                                    switch(quitclasschoose){
                                        case 1:
                                            withdrawFromClass( connection,  memberid);  
                                            break;
                                        case 2:
                                            withdrawFromOneToOneClass( connection,  memberid);
                                            break;
                                        default:
                                            System.out.println("Invalid option, please try again.");
                                            break;   
                                    }
                                    break;
                                case 7:
                                    System.out.println("You choose to view your points");
                                    System.out.println("Your MemberID is: " + memberid);
                                    updateAndShowLoyaltyPoints(connection, memberid);
                                    break;
                                case 8:
                                    System.out.println("Thank you for using our system!");
                                   return;
                                default:
                                    System.out.println("Invalid option, please try again.");
                                    break;
                            }                        
                        }   
                    case 2:
                    while(true){
                        System.out.println("You choose Staff System");
                        System.out.println("+--------------------------------------+");
                            System.out.println("menu: ");
                            System.out.println("+--------------------------------------+");
                            System.out.println("|1. Class");//done
                            System.out.println("|2. Trainer");//done
                            System.out.println("|3. Equipment");//done
                            System.out.println("|4. Room"); //done
                            System.out.println("|5. Quit");//done
                            System.out.println("+--------------------------------------+");
                            int staffchoice = scanner.nextInt();
                        switch(staffchoice){
                            case 1:
                                System.out.println("You choose to manage class");
                                manageGroupClasses( connection);
                                break;
                            case 2:
                                System.out.println("You choose to manage trainer");
                                managetrainer( connection);
                                break;
                            case 3:
                                System.out.println("You choose to manage equipment");
                                manageEquipment( connection);
                                break;
                            case 4:
                                System.out.println("You choose to manage room");
                                manageRooms( connection);
                                break;
                            case 5:
                                System.out.println("Thank you for using our system!");
                                return;
                            default:
                                System.out.println("Invalid option, please try again.");
                                break;
                        }
                    }   
                    default:
                        System.out.println("Invalid option, please try again.");
                        break;
                }     
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }


    }
    //get member information

    // Database connection
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/Health_and_Fitness_Club_Management_System";
        String user = "postgres";
        String password = "ll001108";
        return DriverManager.getConnection(url, user, password);
    }
    //member function 1 : For member login
    private static int Userlogin(Connection connection) {
        int memberid1 = scanner.nextInt();//get userinput for memberid
        scanner.nextLine();
        String sql = "SELECT * FROM Members WHERE MemberID = ?;";//sql query
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);//prepare sql statement
            pstmt.setInt(1, memberid1); //set memberid
            ResultSet rs = pstmt.executeQuery(); //execute query
            if (rs.next()) {//if memberid exists
                System.out.println("Loging in to the system...");//login
                System.out.println("Welcome to the Members System!"+rs.getString("Name"));//welcome
                System.out.println("Get Member Information Successfully!");//get member information
                System.out.println("Here is your information: ");//print member information
                System.out.println("Member ID: " + rs.getInt("MemberID"));
                System.out.println("Date of Birth: " + rs.getDate("DateOfBirth"));
                System.out.println("Gender: " + rs.getString("Gender"));
                System.out.println("ContactInfo: " + rs.getString("ContactInfo"));
                System.out.println("HealthMetrics: " + rs.getString("HealthMetrics"));
                System.out.println("FitnessGoals: " + rs.getString("FitnessGoals"));
                return memberid1;//return memberid for sign in
            } else {
                System.out.println("Invalid MemberID, sign up.");//if memberid not exists, sign up
                return register( connection);//call register function
            }
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
            
        }
        return -1;
    }
    private static int register(Connection connection) {
        System.out.println("Please enter your member information");//get member information
            System.out.println("Please enter member id");
            String id = scanner.nextLine();//member id
            int intid = Integer.parseInt(id);
            String sqlCheck = "SELECT COUNT(*) FROM Members WHERE MemberID = ?";
            try (PreparedStatement pstmtCheck = connection.prepareStatement(sqlCheck)) {//duplicate check
                pstmtCheck.setInt(1, intid);
                ResultSet rs = pstmtCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Member ID already exists. Please use a different ID.");
                    return -1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
            System.out.println("Please enter your Name");
            String name = scanner.nextLine();//member name
            System.out.println("Please enter your Date of Birth (yyyy-mm-dd)");
            String dob = scanner.nextLine();//member date of birth
            java.sql.Date sqlDob = java.sql.Date.valueOf(dob);
            System.out.println("Please enter your Gender");
            String gender = scanner.nextLine();//member gender
            System.out.println("Please enter your Contact Info");
            String contactInfo = scanner.nextLine();//member contact information
            System.out.println("Please enter your Health Metrics");
            String healthMetrics = scanner.nextLine();//member health metrics
            System.out.println("Please enter your Fitness goal");
            String FitnessGoals = scanner.nextLine();//member fitness goals
            String sqlInsert = "INSERT INTO Members (MemberID,Name, DateOfBirth, Gender, ContactInfo, HealthMetrics,FitnessGoals) VALUES (?,?, ?, ?, ?, ?, ?);";//sql insert
    
            try (PreparedStatement pstmtInsert = connection.prepareStatement(sqlInsert)) {
                pstmtInsert.setInt(1, intid);
                pstmtInsert.setString(2, name);
                pstmtInsert.setDate(3, sqlDob);
                pstmtInsert.setString(4, gender);
                pstmtInsert.setString(5, contactInfo);
                pstmtInsert.setString(6, healthMetrics);
                pstmtInsert.setString(7, FitnessGoals);
    
                int rowsAffected = pstmtInsert.executeUpdate();
    
                if (rowsAffected > 0) {//output after insert successfully
                    System.out.println("New member registered successfully!");
                    System.out.println("Please login again: ");
                    System.out.println("Loging in to the system...");
                    System.out.println("Welcome to the Members System!"+name);
                    return intid;
                } else {//if insert failed
                    System.out.println("Registration failed. Please try again.");
                    return -1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        
            
        
        
    }
    
    //member function 2 : For member view class
    private static void viewmemberclassmenu(Connection connection, int memberid) {
        System.out.println("Select which kind of class you want to view: ");//Ask user to choose which kind of class to view
        System.out.println("1. View Group Class");
        System.out.println("2. View One-to-One Class");
        int menuchoose = scanner.nextInt();
        if(menuchoose == 1){//view group class
            viewmembergroup(connection, memberid);
        }
        else if(menuchoose == 2){//view one-to-one class
            viewOneToOneClasses( connection,  memberid);
        }
        else{//invalid input
            System.out.println("Invalid option, please try again.");
        }
    }
    private static void viewmembergroup(Connection connection, int memberid) {
        System.out.println("Viewing group classes for member ID: " + memberid);//output member id
        String sql = "SELECT g.ClassID, g.Name, g.Schedule, g.MaxCapacity "//sql query
                   + "FROM GroupClasses g JOIN MemberClassParticipation m ON g.ClassID = m.ClassID "
                   + "WHERE m.MemberID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, memberid);
            ResultSet rs = pstmt.executeQuery();//get class that member enrolled in
            while (rs.next()) {//print all the group class that member enrolled in
                int classId = rs.getInt("ClassID");
                String name = rs.getString("Name");
                String schedule = rs.getString("Schedule");
                int maxCapacity = rs.getInt("MaxCapacity");
    
                System.out.println("Class ID: " + classId + ", Name: " + name + ", Schedule: " + schedule + ", Max Capacity: " + maxCapacity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    
    private static void viewOneToOneClasses(Connection connection, int memberid) {
        System.out.println("Viewing one-to-one classes for member ID: " + memberid);//output member id
        String sql = "SELECT ClassID, TrainerID, DateTime, Description "//sql query
                   + "FROM OneToOneClasses "
                   + "WHERE MemberID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, memberid);
            ResultSet rs = pstmt.executeQuery();//get one to one class that member enrolled in
            while (rs.next()) {//output one to one class that member enrolled in
                int classId = rs.getInt("ClassID");
                int trainerId = rs.getInt("TrainerID");
                Timestamp dateTime = rs.getTimestamp("DateTime");
                String description = rs.getString("Description");
                System.out.println("Class ID: " + classId + ", Trainer ID: " + trainerId + ", Date/Time: " + dateTime + ", Description: " + description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //member function 3 : For member enroll class
    private static void enrollInClass(Connection connection, int memberid) {
        try (Statement stmt = connection.createStatement();) {
            viewmembergroup( connection,  memberid);//outputall the group class that member has already enrolled in
            String queryAllClasses = "SELECT ClassID, Name, Schedule FROM GroupClasses";
            ResultSet rs = stmt.executeQuery(queryAllClasses);//get result set
            System.out.println("Available Classes:");
            while (rs.next()) {//output all the class that available for member to enroll in
                System.out.println("Class ID: " + rs.getInt("ClassID") + ", Name: " + rs.getString("Name") + ", Schedule: " + rs.getString("Schedule"));
            }
            System.out.println("Enter the Class ID you want to enroll in:");
            int classId = scanner.nextInt();//user make choice
            String sqlInsert = "INSERT INTO MemberClassParticipation (MemberID, ClassID, DateJoined) VALUES (?, ?, CURRENT_DATE)";//sql insert
            try (PreparedStatement pstmt = connection.prepareStatement(sqlInsert)) {//insert into database
                pstmt.setInt(1, memberid);
                pstmt.setInt(2, classId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Enrolled in class successfully!");
                } else {
                    System.out.println("Enrollment failed. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("You have already enrolled in this class.");
        }
    }
    private static void enrollInOneToOneClass(Connection connection, int memberid) {
    try (Statement stmt = connection.createStatement();) {
        viewOneToOneClasses( connection,  memberid);//output all the one to one class that member has already enrolled in
        String queryAllTrainers = "SELECT TrainerID, Name, Qualifications FROM Trainers";
        ResultSet rs = stmt.executeQuery(queryAllTrainers);//get result of all the trainers
        System.out.println("Available Trainers:");
        while (rs.next()) {//output all the trainers
            System.out.println("Trainer ID: " + rs.getInt("TrainerID") + ", Name: " + rs.getString("Name") + ", Qualifications: " + rs.getString("Qualifications"));
        }
        System.out.println("Enter the Trainer ID for your one-to-one class:");
        int trainerId = scanner.nextInt();//ask for user input for trainer id that user want to enroll in
        scanner.nextLine();  
        System.out.println("Enter a brief description of the class:");
        String description = scanner.nextLine();//ask for user input for description of the class
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//get current date and time
        String formattedDateTime = now.format(formatter); // format the date and time

        String sqlInsert = "INSERT INTO OneToOneClasses (TrainerID, MemberID, DateTime, Description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlInsert)) {
            pstmt.setInt(1, trainerId);
            pstmt.setInt(2, memberid);
            pstmt.setTimestamp(3, Timestamp.valueOf(formattedDateTime));//add current date and time to database
            pstmt.setString(4, description);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("One-to-one class scheduled successfully!");//sucess message
                viewOneToOneClasses( connection,  memberid);
            } else {
                System.out.println("Scheduling failed. Please try again.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        System.out.println("An error occurred: " + e.getMessage());
    }
}
    
    //member function 4 : For member withdraw class
    private static void withdrawFromOneToOneClass(Connection connection, int memberid) {
        try (Statement stmt = connection.createStatement()) {
            String queryEnrolledClasses = "SELECT ClassID, Description FROM OneToOneClasses WHERE MemberID = " + memberid;//get all the one to one class that member has already enrolled in
            ResultSet rs = stmt.executeQuery(queryEnrolledClasses);
            System.out.println("Your One-to-One Classes:");
            while (rs.next()) {//output all the one to one class that member has already enrolled in
                System.out.println("Class ID: " + rs.getInt("ClassID") + ", Description: " + rs.getString("Description"));
            }
            System.out.println("Enter the Class ID you want to withdraw from:");
            int classId = scanner.nextInt();//get user input for class id that user want to withdraw from
            String sqlDelete = "DELETE FROM OneToOneClasses WHERE ClassID = ? AND MemberID = ?";//delete sql statement
            try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
                pstmt.setInt(1, classId);
                pstmt.setInt(2, memberid);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Successfully withdrawn from class!");//sucess delete message
                } else {
                    System.out.println("Withdrawal failed. Please check the Class ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void withdrawFromClass(Connection connection, int memberid) {
        try (Statement stmt = connection.createStatement()) {
            String queryEnrolledClasses = "SELECT mc.ClassID, gc.Name FROM MemberClassParticipation mc JOIN GroupClasses gc ON mc.ClassID = gc.ClassID WHERE mc.MemberID = " + memberid;//get all the group class that member has already enrolled in
            ResultSet rs = stmt.executeQuery(queryEnrolledClasses);
            System.out.println("Your Group Classes:");
            while (rs.next()) {//output all the group class that member has already enrolled in
                System.out.println("Class ID: " + rs.getInt("ClassID") + ", Name: " + rs.getString("Name"));
            }
            System.out.println("Enter the Class ID you want to withdraw from:");
            int classId = scanner.nextInt();//get user input for class id that user want to withdraw from
            String sqlDelete = "DELETE FROM MemberClassParticipation WHERE ClassID = ? AND MemberID = ?";//delete sql statement
            try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
                pstmt.setInt(1, classId);
                pstmt.setInt(2, memberid);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Successfully withdrawn from the class!");//sucess delete message
                } else {
                    System.out.println("Withdrawal failed. Please check the Class ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //member function 5 : For member caculate fee
    private static void calculateAndUpdateBilling(Connection connection, int memberid) {
        System.out.println("One to one class :50 ");
        System.out.println("Group class :20 ");
        System.out.println("Basic membership fee :30 ");//output price for each class
        final BigDecimal pricePerPrivateClass = new BigDecimal("50.00");//set price for each variable
        final BigDecimal pricePerGroupClass = new BigDecimal("20.00");
        final BigDecimal basicMembershipFee = new BigDecimal("30.00");
        try (Statement stmt = connection.createStatement()) {
            String queryPrivateClasses = "SELECT COUNT(*) FROM OneToOneClasses WHERE MemberID = " + memberid;//for user to view how many one to one class they have enrolled in
            ResultSet rsPrivate = stmt.executeQuery(queryPrivateClasses);
            rsPrivate.next();
            BigDecimal privateClassFees = pricePerPrivateClass.multiply(new BigDecimal(rsPrivate.getInt(1)));//caculate the amout for one to one class
            String queryGroupClasses = "SELECT COUNT(*) FROM MemberClassParticipation WHERE MemberID = " + memberid;//for user to view how many group class they have enrolled in
            ResultSet rsGroup = stmt.executeQuery(queryGroupClasses);
            rsGroup.next();
            BigDecimal groupClassFees = pricePerGroupClass.multiply(new BigDecimal(rsGroup.getInt(1)));//caculate the amount for group class
            BigDecimal totalFee = privateClassFees.add(groupClassFees).add(basicMembershipFee);//add all the amount together
            String checkBilling = "SELECT COUNT(*) FROM Billing WHERE MemberID = " + memberid;//check if the member has already have billing information
            ResultSet rsBilling = stmt.executeQuery(checkBilling);
            rsBilling.next();
    
            if (rsBilling.getInt(1) > 0) {//if member has already have billing information, update the billing information
                String updateBilling = "UPDATE Billing SET Amount = ?, Date = CURRENT_DATE WHERE MemberID = ?";
                try (PreparedStatement pstmtUpdate = connection.prepareStatement(updateBilling)) {
                    pstmtUpdate.setBigDecimal(1, totalFee);
                    pstmtUpdate.setInt(2, memberid);
                    pstmtUpdate.executeUpdate();
                }
            } else {//if member does not have billing information, insert the billing information
                String insertBilling = "INSERT INTO Billing (MemberID, Amount, Date) VALUES (?, ?, CURRENT_DATE)";
                try (PreparedStatement pstmtInsert = connection.prepareStatement(insertBilling)) {
                    pstmtInsert.setInt(1, memberid);
                    pstmtInsert.setBigDecimal(2, totalFee);
                    pstmtInsert.executeUpdate();
                }
            }
            String queryBilling = "SELECT Amount, Date FROM Billing WHERE MemberID = " + memberid;
            ResultSet rsBillingInfo = stmt.executeQuery(queryBilling);//after update or insert, output the billing information
            viewmembergroup( connection,  memberid);//check the group class that member has enrolled in again
            viewOneToOneClasses( connection,  memberid) ;//check the one to one class that member has enrolled in again
            if (rsBillingInfo.next()) {//out put the billing information
                BigDecimal amount = rsBillingInfo.getBigDecimal("Amount");
                Date date = rsBillingInfo.getDate("Date");
                System.out.println("Billing Information: ");
                System.out.println("Date: " + date + ", Amount: $" + amount);
            } else {
                System.out.println("No billing information found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //member function 6 : For member update information
    private static void updateMemberDetails(Connection connection, int memberid) {
        try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM Members WHERE MemberID = " + memberid);
            if (rs.next()) {
                System.out.println("Current Details:");
                System.out.println("1. Name: " + rs.getString("Name"));
                System.out.println("2. Date of Birth: " + rs.getDate("DateOfBirth"));
                System.out.println("3. Gender: " + rs.getString("Gender"));
                System.out.println("4. Contact Info: " + rs.getString("ContactInfo"));
                System.out.println("5. Health Metrics: " + rs.getString("HealthMetrics"));
                System.out.println("6. Fitness Goals: " + rs.getString("FitnessGoals"));
            } else {
                System.out.println("Member not found.");
                return;
            }
            System.out.println("Enter the number corresponding to the attribute you want to update:");
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            String attribute = "";
            switch (choice) {
                case 1:
                    attribute = "Name";
                    break;
                case 2:
                    attribute = "DateOfBirth";
                    break;
                case 3:
                    attribute = "Gender";
                    break;
                case 4:
                    attribute = "ContactInfo";
                    break;
                case 5:
                    attribute = "HealthMetrics";
                    break;
                case 6:
                    attribute = "FitnessGoals";
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            System.out.println("Enter the new value for " + attribute + ":");
            String newValue = scanner.nextLine();
            String sqlUpdate = "UPDATE Members SET " + attribute + " = ? WHERE MemberID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
                if (attribute.equals("DateOfBirth")) {
                    pstmt.setDate(1, Date.valueOf(newValue)); // 处理日期格式
                } else {
                    pstmt.setString(1, newValue);
                }
                pstmt.setInt(2, memberid);
    
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Member details updated successfully!");
                } else {
                    System.out.println("Update failed. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input for DateOfBirth. Please use the format yyyy-MM-dd.");
        }
    }
    //member function 7 : For member view points
    private static void updateAndShowLoyaltyPoints(Connection connection, int memberid) {
        try (Statement stmt = connection.createStatement()) {
            calculateAndUpdateBilling( connection,  memberid);
            String queryBill = "SELECT SUM(Amount) FROM Billing WHERE MemberID = " + memberid;
            ResultSet rsBill = stmt.executeQuery(queryBill);
            BigDecimal totalBill = rsBill.next() ? rsBill.getBigDecimal(1) : BigDecimal.ZERO;
            int pointsEarned = totalBill.multiply(new BigDecimal("10")).intValue();
            String checkPoints = "SELECT COUNT(*) FROM LoyaltyPoints WHERE MemberID = " + memberid;
            ResultSet rsPoints = stmt.executeQuery(checkPoints);
            rsPoints.next();
    
            if (rsPoints.getInt(1) > 0) {
                String updatePoints = "UPDATE LoyaltyPoints SET PointsEarned = ? WHERE MemberID = ?";
                try (PreparedStatement pstmtUpdate = connection.prepareStatement(updatePoints)) {
                    pstmtUpdate.setInt(1, pointsEarned);
                    pstmtUpdate.setInt(2, memberid);
                    pstmtUpdate.executeUpdate();
                }
            } else {
                String insertPoints = "INSERT INTO LoyaltyPoints (MemberID, PointsEarned) VALUES (?, ?)";
                try (PreparedStatement pstmtInsert = connection.prepareStatement(insertPoints)) {
                    pstmtInsert.setInt(1, memberid);
                    pstmtInsert.setInt(2, pointsEarned);
                    pstmtInsert.executeUpdate();
                }
            }
            String queryPoints = "SELECT PointsEarned FROM LoyaltyPoints WHERE MemberID = " + memberid;
            ResultSet rsPointsShow = stmt.executeQuery(queryPoints);
            if (rsPointsShow.next()) {
                int currentPoints = rsPointsShow.getInt("PointsEarned");
                System.out.println("Current Loyalty Points: " + currentPoints);
            } else {
                System.out.println("No loyalty points record found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //member function 8 : For member view information
    private static void showMemberDetails(Connection connection, int memberid) {
        try (Statement stmt = connection.createStatement()) {
            String query = "SELECT * FROM Members WHERE MemberID = " + memberid;
            ResultSet rs = stmt.executeQuery(query);
    
            if (rs.next()) {

                System.out.println("Member Details:");
                System.out.println("Member ID: " + rs.getInt("MemberID"));
                System.out.println("Name: " + rs.getString("Name"));
                System.out.println("Date of Birth: " + rs.getDate("DateOfBirth"));
                System.out.println("Gender: " + rs.getString("Gender"));
                System.out.println("Contact Info: " + rs.getString("ContactInfo"));
                System.out.println("Health Metrics: " + rs.getString("HealthMetrics"));
                System.out.println("Fitness Goals: " + rs.getString("FitnessGoals"));
            } else {
                System.out.println("Member not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //staff function 1 : view, add, update, delete class
    private static void manageGroupClasses(Connection connection) {
    while (true) {
        System.out.println("\n=== Group Class Management ===");
        System.out.println("1. Add New Group Class");
        System.out.println("2. Update Group Class");
        System.out.println("3. Delete Group Class");
        System.out.println("4. Return to Previous Menu");
        System.out.print("Select an option: ");
        int choice = scanner.nextInt();//get user input for choice
        scanner.nextLine(); 
        switch (choice) {
            case 1:
                addClass(connection); //call add class function
                break;
            case 2:
                updateClass(connection);//call update class function
                break;
            case 3:
                deleteClass(connection);//call delete class function
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid option, please try again.");
        }
    }
}
    private static void addClass(Connection connection) {
    System.out.print("Enter class name: ");
    String name = scanner.nextLine();//ask for class name
    System.out.print("Enter trainer ID: ");
    System.out.println("Trainer List:\n");
    viewTrainers( connection);//print all the trainers
    int trainerId = scanner.nextInt();//ask for trainer id that user want to add into the class
    scanner.nextLine();
    System.out.print("Enter room ID:");
    viewRooms( connection);//print all the rooms
    int roomId = scanner.nextInt();//ask for room id that user want to add into the class
    scanner.nextLine();
    System.out.print("Enter class schedule: ");
    String schedule = scanner.nextLine();//ask for class schedule
    System.out.print("Enter max capacity: ");
    int maxCapacity = scanner.nextInt();//ask for max capacity
    scanner.nextLine();

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
    System.out.println("Here is the list of classes: ");
    viewGroupClasses( connection);//print all the classes
    System.out.print("Enter the class ID to update: ");
    int classId = scanner.nextInt();//ask for class id that user want to update
    scanner.nextLine(); 
    System.out.print("Enter new class name: ");
    String newName = scanner.nextLine();//ask for new class name
    System.out.print("Enter new trainer ID: ");
    viewTrainers( connection);
    int newTrainerId = scanner.nextInt();//ask for new trainer id
    scanner.nextLine(); 
    System.out.print("Enter new room ID: ");
    viewRooms( connection);//ask for new room id
    int newRoomId = scanner.nextInt();//ask for new room id
    scanner.nextLine();
    System.out.print("Enter new schedule: ");
    String newSchedule = scanner.nextLine(); //ask for new schedule
    System.out.print("Enter new max capacity: ");
    int newMaxCapacity = scanner.nextInt();//ask for new max capacity
    scanner.nextLine(); 
    String sql = "UPDATE GroupClasses SET Name = ?, TrainerID = ?, RoomID = ?, Schedule = ?, MaxCapacity = ? WHERE ClassID = ?;";//sql update

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, newName);
        pstmt.setInt(2, newTrainerId);
        pstmt.setInt(3, newRoomId);
        pstmt.setString(4, newSchedule);
        pstmt.setInt(5, newMaxCapacity);
        pstmt.setInt(6, classId);

        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {//output success message
            System.out.println("Class information updated successfully!");
        } else {//output fail message
            System.out.println("Failed to update class information.");
        }
    } catch (SQLException e) {
        System.out.println("Database operation failed: " + e.getMessage());
    }
}
    private static void deleteClass(Connection connection) {
    System.out.println("Here is the list of classes: ");
    viewGroupClasses( connection);//print all the classes
    System.out.print("Enter the class ID to delete: ");
    int classId = scanner.nextInt();//ask for class id that user want to delete

    String sql = "DELETE FROM GroupClasses WHERE ClassID = ?;";//delete sql statement

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, classId);

        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {//output success message
            System.out.println("Class deleted successfully!");
        } else {//output fail message
            System.out.println("Failed to delete class.");
        }
    } catch (SQLException e) {
        System.out.println("Database operation failed: " + e.getMessage());
    }
}
    private static void viewTrainers(Connection connection) {
    String sql = "SELECT * FROM Trainers;";//sql query
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        System.out.println("Trainer List:");
        while (rs.next()) {//print all the trainers
            int trainerId = rs.getInt("TrainerID");
            String name = rs.getString("Name");
            String qualifications = rs.getString("Qualifications");
            System.out.println("Trainer ID: " + trainerId + ", Name: " + name + ", Qualifications: " + qualifications);
        }
    } catch (SQLException e) {
        System.out.println("Database query failed: " + e.getMessage());
    }
}
    private static void viewRooms(Connection connection) {
    String sql = "SELECT * FROM Room;";//sql query
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        System.out.println("Room List:");
        while (rs.next()) {//print all the rooms
            int roomId = rs.getInt("RoomID");
            String name = rs.getString("Name");
            int capacity = rs.getInt("Capacity");
            String type = rs.getString("Type");
            boolean isAvailable = rs.getBoolean("IsAvailable");
            System.out.println("Room ID: " + roomId + ", Name: " + name + ", Capacity: " + capacity + ", Type: " + type + ", Is Available: " + isAvailable);
        }
    } catch (SQLException e) {
        System.out.println("Database query failed: " + e.getMessage());
    }
}
    private static void viewGroupClasses(Connection connection) {
    String sql = "SELECT * FROM GroupClasses;";//sql query
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        System.out.println("Group Classes List:");
        while (rs.next()) {//print all the group classes
            int classId = rs.getInt("ClassID");
            String name = rs.getString("Name");
            String schedule = rs.getString("Schedule");
            int trainerId = rs.getInt("TrainerID");
            int maxCapacity = rs.getInt("MaxCapacity");
            System.out.println("Class ID: " + classId + ", Name: " + name + ", Schedule: " + schedule + ", Trainer ID: " + trainerId + ", Max Capacity: " + maxCapacity);
        }
    } catch (SQLException e) {
        System.out.println("Database query failed: " + e.getMessage());
    }
}
    //staff function 2 : view, add, update, delete trainer
    private static void managetrainer(Connection connection) {
    int choice;//get user input for choice
    while (true) {
        System.out.println("\n=== Function Selection Menu ===");
        System.out.println("1. Add Trainer");
        System.out.println("2. Update Trainer");
        System.out.println("3. Delete Trainer");
        System.out.println("4. Return to Previous Menu");
        System.out.print("Select an option: ");
        choice = scanner.nextInt();
        scanner.nextLine(); 
        switch (choice) {
            case 1:
                addTrainer(connection);//call add trainer function
                break;
            case 2:
                updateTrainer(connection);//call update trainer function
                break;
            case 3:
                deleteTrainer(connection);//call delete trainer function
                break;
            case 4:
                System.out.println("Exiting...");//exit the method
                return; 
            default:
                System.out.println("Invalid option, please try again.");
        }
    }
}
    private static void addTrainer(Connection connection) {
    System.out.print("Enter trainer's name: ");
    String name = scanner.nextLine();//ask for trainer name
    System.out.print("Enter trainer's qualifications: ");
    String qualifications = scanner.nextLine();//ask for trainer qualifications
    String sql = "INSERT INTO Trainers (Name, Qualifications) VALUES (?, ?);";//sql insert
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, name);
        pstmt.setString(2, qualifications);
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {//sucess message
            System.out.println("Trainer added successfully!");
        } else {//fail message
            System.out.println("Failed to add trainer.");
        }
    } catch (SQLException e) {
        System.out.println("Database operation failed: " + e.getMessage());
    }
}
    private static void updateTrainer(Connection connection) {
    viewTrainers( connection);//print all the trainers
    System.out.print("Enter the trainer ID to update: ");
    int trainerId = scanner.nextInt();//user input for trainer id that user want to update
    scanner.nextLine();
    System.out.print("Enter new name: ");
    String newName = scanner.nextLine();//user input for new name
    System.out.print("Enter new qualifications: ");
    String newQualifications = scanner.nextLine();//user input for new qualifications
    String sql = "UPDATE Trainers SET Name = ?, Qualifications = ? WHERE TrainerID = ?;";//sql update
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, newName);
        pstmt.setString(2, newQualifications);
        pstmt.setInt(3, trainerId);
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {//sucess message
            System.out.println("Trainer information updated successfully!");
        } else {//fail message
            System.out.println("Failed to update trainer information.");
        }
    } catch (SQLException e) {
        System.out.println("Database operation failed: " + e.getMessage());
    }
}
    private static void deleteTrainer(Connection connection) {
    viewTrainers( connection);
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
    //staff function 3 : view, add, update, delete equipment
    private static void manageEquipment(Connection connection) {
        while (true) {
            System.out.println("\n=== Equipment Management ===");
            System.out.println("1. Add New Equipment");
            System.out.println("2. Update Equipment Information");
            System.out.println("3. Delete Equipment");
            System.out.println("4. Return to Previous Menu");
            System.out.print("Select an option: ");
            int choice;
            choice = scanner.nextInt();//get user input for choice
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    addEquipment(connection);//call add equipment function
                    break;
                case 2:
                    updateEquipment(connection);//call update equipment function
                    break;
                case 3:
                    deleteEquipment(connection);//call delete equipment function
                    break;
                case 4:
                    System.out.println("Returning to previous menu...");//exit the method
                    return; 
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    private static void addEquipment(Connection connection) {
        System.out.print("Enter equipment type: ");
        String type = scanner.nextLine();//ask for equipment type
        System.out.print("Enter equipment status: ");
        String status = scanner.nextLine();//ask for equipment status
        System.out.print("Enter last maintenance date (YYYY-MM-DD): ");
        String lastMaintenanceDate = scanner.nextLine();//ask for last maintenance date
        String sql = "INSERT INTO Equipment (Type, Status, LastMaintenanceDate) VALUES (?, ?, ?);";//sql insert
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.setString(2, status);
            pstmt.setDate(3, Date.valueOf(lastMaintenanceDate));
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {//sucess message
                System.out.println("Equipment added successfully!");
            } else {//fail message
                System.out.println("Failed to add equipment.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }  
    private static void updateEquipment(Connection connection) {
        viewEquipment( connection);//print all the equipments
        System.out.print("Enter the equipment ID to update: ");
        int equipmentId = scanner.nextInt();//user input for equipment id that user want to update
        scanner.nextLine(); 
        System.out.print("Enter new equipment name: ");
        String newName = scanner.nextLine();//user input for new equipment name
        System.out.print("Enter new status: ");
        String newStatus = scanner.nextLine();//user input for new status
        System.out.print("Enter new last maintenance date (YYYY-MM-DD): ");
        String newLastMaintenanceDate = scanner.nextLine();//user input for new last maintenance date
        String sql = "UPDATE Equipment SET Type = ?, Status = ?, LastMaintenanceDate = ? WHERE EquipmentID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newStatus);
            pstmt.setDate(3, Date.valueOf(newLastMaintenanceDate));
            pstmt.setInt(4, equipmentId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {//sucess message
                System.out.println("Equipment information updated successfully!");
            } else {//fail message
                System.out.println("Failed to update equipment information.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input for Last Maintenance Date. Please use the format YYYY-MM-DD.");
        }
    }  
    private static void deleteEquipment(Connection connection) {
        viewEquipment( connection);//print all the equipments
        System.out.print("Enter the equipment ID to delete: ");
        int equipmentId = scanner.nextInt();//user input for equipment id that user want to delete
        String sql = "DELETE FROM Equipment WHERE EquipmentID = ?;";//sql delete
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, equipmentId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {//sucess message
                System.out.println("Equipment deleted successfully!");
            } else {//fail message
                System.out.println("Failed to delete equipment.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }   
    private static void viewEquipment(Connection connection) {
        String sql = "SELECT * FROM Equipment;";//sql query
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {//print all the equipments
                System.out.println("Equipment ID: " + rs.getInt("EquipmentID") + 
                                   ", Type: " + rs.getString("Type") +
                                   ", Status: " + rs.getString("Status") +
                                   ", Last Maintenance Date: " + rs.getDate("LastMaintenanceDate"));
            }
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
        }
    }
    //staff function 4 : view, add, update, delete room
    private static void manageRooms(Connection connection) {
        int choice;
        while (true) {
            System.out.println("\n=== Room Management ===");
            System.out.println("1. Add New Room");
            System.out.println("2. Update Room Information");
            System.out.println("3. Delete Room");
            System.out.println("4. Return to Previous Menu");
            System.out.print("Select an option: ");
            choice = scanner.nextInt();//get user input for choice
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    addRoom(connection);//call add room function
                    break;
                case 2:
                    updateRoom(connection);//call update room function
                    break;
                case 3:
                    deleteRoom(connection);//call delete room function
                    break;
                case 4:
                    System.out.println("Returning to previous menu...");//exit the method
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    private static void addRoom(Connection connection) {
        System.out.print("Enter room name: ");
        String name = scanner.nextLine();//ask for room name
        System.out.print("Enter room capacity: ");
        int capacity = scanner.nextInt();//ask for room capacity
        scanner.nextLine();  
        System.out.print("Enter room type: ");
        String type = scanner.nextLine();//ask for room type
        String sql = "INSERT INTO Room (Name, Capacity, Type, IsAvailable) VALUES (?, ?, ?, TRUE);"; //sql insert
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, capacity);
            pstmt.setString(3, type);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {//sucess message
                System.out.println("Room added successfully!");
            } else {//fail message
                System.out.println("Failed to add room.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void updateRoom(Connection connection) {
        viewRooms( connection);
        System.out.print("Enter the room ID to update: ");
        int roomId = scanner.nextInt();//user input for room id that user want to update
        scanner.nextLine(); 
        System.out.print("Enter new room name: ");
        String newName = scanner.nextLine();//user input for new room name
        System.out.print("Enter new capacity: ");
        int newCapacity = scanner.nextInt();//user input for new capacity
        scanner.nextLine(); 
        System.out.print("Enter new room type: ");
        String newType = scanner.nextLine();//user input for new room type
        System.out.print("Is the room available (true/false): ");
        boolean isAvailable = scanner.nextBoolean();//user input for is the room available
        String sql = "UPDATE Room SET Name = ?, Capacity = ?, Type = ?, IsAvailable = ? WHERE RoomID = ?;";//sql update
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newCapacity);
            pstmt.setString(3, newType);
            pstmt.setBoolean(4, isAvailable);
            pstmt.setInt(5, roomId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {//sucess message
                System.out.println("Room information updated successfully!");
            } else {//fail message
                System.out.println("Failed to update room information.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
    private static void deleteRoom(Connection connection) {  
        viewRooms( connection); //print all the rooms
        System.out.print("Enter the room ID to delete: ");
        int roomId = scanner.nextInt();//user input for room id that user want to delete
        String sql = "DELETE FROM Room WHERE RoomID = ?;";//sql delete
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {//sucess message
                System.out.println("Room deleted successfully!");
            } else {//fail message
                System.out.println("Failed to delete room.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
}

