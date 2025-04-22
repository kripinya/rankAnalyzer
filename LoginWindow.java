import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LoginWindow extends JFrame {
    private JComboBox<String> roleCombo;
    private JLabel            label1, label2, label3;
    private JTextField        field1, field2;
    private JPasswordField    field3;
    private JButton           loginButton, registerButton, instrButton;

    private List<Student> students = DataStore.loadStudents();
    private List<Teacher> teachers = DataStore.loadTeachers();

    public LoginWindow() {
        initUI();
    }

    private void initUI() {
        setTitle("📈 Rank Analyzer Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLocationRelativeTo(null);

        // Main panel with light-pink background
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(new Color(255, 240, 245));
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 13);

        // Row 0: Role
        gbc.gridx = 0; gbc.gridy = 0;
        label1 = new JLabel("Role:"); label1.setFont(labelFont);
        panel.add(label1, gbc);

        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"Student", "Teacher"});
        roleCombo.setFont(fieldFont);
        panel.add(roleCombo, gbc);

        // Row 1: ID
        gbc.gridx = 0; gbc.gridy = 1;
        label1 = new JLabel("SAP ID:"); label1.setFont(labelFont);
        panel.add(label1, gbc);

        gbc.gridx = 1;
        field1 = new JTextField(); field1.setFont(fieldFont);
        field1.setBackground(new Color(255, 228, 235));
        panel.add(field1, gbc);

        // Row 2: Codename (only for students)
        gbc.gridx = 0; gbc.gridy = 2;
        label2 = new JLabel("Codename:"); label2.setFont(labelFont);
        panel.add(label2, gbc);

        gbc.gridx = 1;
        field2 = new JTextField(); field2.setFont(fieldFont);
        field2.setBackground(new Color(255, 228, 235));
        panel.add(field2, gbc);

        // Row 3: Password
        gbc.gridx = 0; gbc.gridy = 3;
        label3 = new JLabel("Password:"); label3.setFont(labelFont);
        panel.add(label3, gbc);

        gbc.gridx = 1;
        field3 = new JPasswordField(); field3.setFont(fieldFont);
        field3.setBackground(new Color(255, 228, 235));
        panel.add(field3, gbc);

        // Row 4: Login button
        gbc.gridx = 1; gbc.gridy = 4;
        loginButton = new JButton("🔖 Login");
        styleButton(loginButton);
        panel.add(loginButton, gbc);

        // Row 5: Register button (students only)
        gbc.gridx = 1; gbc.gridy = 5;
        registerButton = new JButton("📝 Register");
        styleButton(registerButton);
        panel.add(registerButton, gbc);

        // Row 6: Instructions button (always visible)
        gbc.gridx = 1; gbc.gridy = 6;
        instrButton = new JButton("📖Instructions");
        styleButton(instrButton);
        panel.add(instrButton, gbc);

        add(panel);

        // Listeners
        roleCombo.addActionListener(e -> updateForm());
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());
        instrButton.addActionListener(e -> showInstructionsDialog());

        updateForm();
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(255, 182, 193));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 105, 180)));
    }

    private void updateForm() {
        boolean isStudent = "Student".equals(roleCombo.getSelectedItem());
        label1.setText(isStudent ? "SAP ID:" : "Teacher ID:");
        label2.setVisible(isStudent);
        field2.setVisible(isStudent);
        registerButton.setVisible(isStudent);
    }

    private void handleLogin() {
        String role     = (String) roleCombo.getSelectedItem();
        String id       = field1.getText().trim();
        String codename = field2.getText().trim();
        String pwd      = new String(field3.getPassword());

        if (role.equals("Student")) {
            for (Student s : students) {
                if (s.getSapId().equals(id)) {
                    if (s.getCodename().isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                            "You are not registered yet. Please register first.",
                            "Not Registered", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (s.getCodename().equalsIgnoreCase(codename)
                     && s.getPassword().equals(pwd)) {
                        new StudentPanel(s, students).setVisible(true);
                        dispose();
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(this,
                "Invalid SAP ID, codename, or password.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);

        } else {
            for (Teacher t : teachers) {
                if (t.getTeacherId().equalsIgnoreCase(id)
                 && t.getPassword().equals(pwd)) {
                    new TeacherPanel(t, students).setVisible(true);
                    dispose();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this,
                "Invalid Teacher ID or password.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        new RegistrationWindow(students).setVisible(true);
    }

    /** Shows a scrollable dialog with usage instructions. */
    private void showInstructionsDialog() {
        String instructions =
            "📘 Instructions for Using This Application\n\n" +
            "👨‍🎓 For Students:\n" +
            "1. Password Policy:\n" +
            "   • Your password cannot be changed once registered.\n" +
            "   • Store it securely. If you forget it, email " +
            "lakshitadixit5@gmail.com or Ananyakripinya@gmail.com (include your SAP ID).\n\n" +
            "2. Codename Update:\n" +
            "   • Use the “Change Codename” option after login.\n\n" +
            "3. Missing/Zero Marks:\n" +
            "   • “0” may mean you scored zero or marks\n" +
            "     haven’t been entered yet.\n" +
            "   • Verify with “View All Marks”.\n\n" +
            "4. Support:\n" +
            "   • For issues, email lakshitadixit5@gmail.com or Ananyakripinya@gmail.com\n\n" +
            "👩‍🏫 For Teachers:\n" +
            "1. Credentials:\n" +
            "   • Your Teacher ID & password are fixed.\n\n" +
            "2. No Changes:\n" +
            "   • You cannot change your ID or password.\n\n" +
            "3. Recovery:\n" +
            "   • If lost, email lakshitadixit5@gmail.com or Ananyakripinya@gmail.com\n" +
            "     (include your subject).\n";

        JTextArea textArea = new JTextArea(instructions);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Instructions",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }
}
