import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminHome {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin Home - Rank Analyzer System");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Gradient pink-themed panel
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.PINK, getWidth(), getHeight(), new Color(255, 182, 193));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(false);

        // Title bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.PINK);
        menuBar.setForeground(Color.DARK_GRAY);
        JLabel titleLabel = new JLabel("🎓 Student Rank Analyzer Dashboard");
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        menuBar.add(titleLabel);

        // Constraints for layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Buttons
        JButton studentPanelBtn = createButton("📘 Student Panel");
        JButton teacherPanelBtn = createButton("🧑‍🏫 Teacher Panel");

        panel.add(studentPanelBtn, gbc);
        gbc.gridy++;
        panel.add(teacherPanelBtn, gbc);

        // Student Panel action
        studentPanelBtn.addActionListener(e -> {
            List<Student> students = DataStore.loadStudents();

            if (!students.isEmpty()) {
                // Pick first student for demo OR implement login logic here
                Student selectedStudent = students.get(0);
                new StudentPanel(selectedStudent, students).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "No students found!");
            }
        });

        // Teacher Panel action (optional)
        teacherPanelBtn.addActionListener(e -> {
            List<Teacher> teachers = DataStore.loadTeachers();
            JOptionPane.showMessageDialog(null, "Teacher Panel coming soon!");
            // You can build a TeacherPanel class similarly to StudentPanel
        });

        frame.setJMenuBar(menuBar);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.PINK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
    }
}
