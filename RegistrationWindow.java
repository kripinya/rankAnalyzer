import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class RegistrationWindow extends JFrame {
    private JLabel label1, label2, label3, label4;
    private JTextField field1, field2;
    private JPasswordField field3;
    private JButton registerButton;
    private JComboBox<String> sapIdCombo;

    private List<Student> students;

    public RegistrationWindow(List<Student> students) {
        this.students = students;
        initUI();
    }

    private void initUI() {
        setTitle("Student Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Field1: Select SAP ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        label1 = new JLabel("Select SAP ID:");
        panel.add(label1, gbc);
        gbc.gridx = 1;
        sapIdCombo = new JComboBox<>(getAvailableSapIds());
        panel.add(sapIdCombo, gbc);

        // Field2: Codename
        gbc.gridx = 0;
        gbc.gridy = 1;
        label2 = new JLabel("Codename:");
        panel.add(label2, gbc);
        gbc.gridx = 1;
        field2 = new JTextField();
        panel.add(field2, gbc);

        // Field3: Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        label3 = new JLabel("Password:");
        panel.add(label3, gbc);
        gbc.gridx = 1;
        field3 = new JPasswordField();
        panel.add(field3, gbc);

        // Field4: Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        label4 = new JLabel("Confirm Password:");
        panel.add(label4, gbc);
        gbc.gridx = 1;
        JPasswordField confirmPasswordField = new JPasswordField();
        panel.add(confirmPasswordField, gbc);

        // Register button
        gbc.gridx = 1;
        gbc.gridy = 4;
        registerButton = new JButton("Register");
        panel.add(registerButton, gbc);

        add(panel);

        // Register button listener
        registerButton.addActionListener(e -> handleRegister(confirmPasswordField));

    }

    private String[] getAvailableSapIds() {
        // Get a list of SAP IDs that are unregistered
        // Assuming that unregistered students have an empty codename
        return students.stream()
                .filter(s -> s.getCodename().isEmpty())
                .map(Student::getSapId)
                .toArray(String[]::new);
    }

    private void handleRegister(JPasswordField confirmPasswordField) {
        String selectedSapId = (String) sapIdCombo.getSelectedItem();
        String codename = field2.getText().trim();
        String password = new String(field3.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (codename.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the SAP ID is already registered
        for (Student s : students) {
            if (s.getSapId().equals(selectedSapId)) {
                if (!s.getCodename().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "This SAP ID is already registered.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                s.setCodename(codename);
                s.setPassword(password);
                // Save the updated students list
                DataStore.saveStudents(students);
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the registration window
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid SAP ID.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationWindow(DataStore.loadStudents()).setVisible(true));
    }
}
