import javax.swing.*;

public class ClubManagementApp {
    public static void main(String[] args) {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("Health and Fitness Club Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // 创建组件并添加到 JFrame
        JLabel label = new JLabel("Welcome to the Club Management System");
        frame.getContentPane().add(label);

        // 显示窗口
        frame.setVisible(true);
    }
}
