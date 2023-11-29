import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GymManagementSystem {
    private JPanel mainPanel;
    //private JTextField textFieldMemberId;
    private JTextField textFieldName;
    private JTextField textFieldDob;
    private JTextField textFieldGender;
    private JTextField textFieldContact;
    private JTextField textFieldHealthMetrics;
    private JTextField textFieldFitnessGoals;
    private JButton addButton;
    //private JLabel labelStatus;

    public GymManagementSystem() {
        mainPanel = new JPanel();
        mainPanel.setSize(400, 300);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        textFieldName = new JTextField();
        textFieldDob = new JTextField();
        textFieldGender = new JTextField();
        textFieldContact = new JTextField();
        textFieldHealthMetrics = new JTextField();
        textFieldFitnessGoals = new JTextField();
        addButton = new JButton("Add Member");
        // 初始化主面板和其他组件
        
        // 初始化其他组件...
        mainPanel.add(new JLabel("Name:"));
        mainPanel.add(textFieldName);
        mainPanel.add(new JLabel("Date of Birth (yyyy-mm-dd):"));
        mainPanel.add(textFieldDob);
        mainPanel.add(new JLabel("Gender:"));
        mainPanel.add(textFieldGender);
        mainPanel.add(new JLabel("Contact Info:"));
        mainPanel.add(textFieldContact);
        mainPanel.add(new JLabel("Health Metrics:"));
        mainPanel.add(textFieldHealthMetrics);
        mainPanel.add(new JLabel("Fitness Goals:"));
        mainPanel.add(textFieldFitnessGoals);
        mainPanel.add(addButton);
        
        
        // 添加其他组件到mainPanel...
    
        // 添加事件监听器
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewMember(textFieldName.getText(), 
                             textFieldDob.getText(), 
                             textFieldGender.getText(), 
                             textFieldContact.getText(), 
                             textFieldHealthMetrics.getText(), 
                             textFieldFitnessGoals.getText());
            }
        });
    }
    

    

        public void addNewMember(String name, String dateOfBirth, String gender, String contactInfo, String healthMetrics, String fitnessGoals) {
            String sql = "INSERT INTO Members (Name, DateOfBirth, Gender, ContactInfo, HealthMetrics, FitnessGoals) VALUES (?, ?, ?, ?, ?, ?)";
    
            try (Connection conn = new DatabaseConnection().connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
                pstmt.setString(1, name);
                pstmt.setDate(2, java.sql.Date.valueOf(dateOfBirth)); // 假设 dateOfBirth 的格式是 yyyy-[m]m-[d]d
                pstmt.setString(3, gender);
                pstmt.setString(4, contactInfo);
                pstmt.setString(5, healthMetrics);
                pstmt.setString(6, fitnessGoals);
                
                pstmt.executeUpdate();
                System.out.println("Member added successfully!");
            } catch (SQLException e) {
                System.out.println("SQL Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    
    

    public static void main(String[] args) {
        JFrame frame = new JFrame("Gym Management System");
        frame.setContentPane(new GymManagementSystem().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

