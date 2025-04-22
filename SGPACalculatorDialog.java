import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SGPACalculatorDialog extends JDialog {

    private final Map<String, Integer> subjectCredits = Map.of(
            "OOPS", 3,
            "CCF", 4,
            "OOPS LAB", 1,
            "DCN", 3
    );

    private final Map<String, JComboBox<String>> gradeSelectors = new HashMap<>();
    private final JTextField extraCreditField = new JTextField();

    public SGPACalculatorDialog(JFrame parent) {
        super(parent, "Expected SGPA Calculator", true);
        setLayout(new GridBagLayout());
        setSize(450, 450);
        setLocationRelativeTo(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        String[] gradeOptions = {"O", "A+", "A", "B+", "B", "C+", "C", "F"};

        // Grade selectors for fixed subjects
        for (String subject : subjectCredits.keySet()) {
            add(new JLabel(subject + " Expected Grade:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> combo = new JComboBox<>(gradeOptions);
            add(combo, gbc);
            gradeSelectors.put(subject, combo);
            gbc.gridx = 0;
            gbc.gridy++;
        }

        // Exploratory Subject Credits
        add(new JLabel("🔸 Additional Exploratory Subject Credits:"), gbc);
        gbc.gridx = 1;
        add(extraCreditField, gbc);
        gbc.gridx = 0; gbc.gridy++;

        // Note
        gbc.gridwidth = 2;
        add(new JLabel("<html><i>Note: Include exploratory subject credits in the above field.</i></html>"), gbc);
        gbc.gridy++;

        // Calculate Button
        JButton btnCalculate = new JButton("🎯 Calculate SGPA");
        add(btnCalculate, gbc);

        btnCalculate.addActionListener(e -> calculateSGPA());
    }

    private void calculateSGPA() {
        double totalWeightedPoints = 0;
        int totalCredits = 0;

        for (Map.Entry<String, JComboBox<String>> entry : gradeSelectors.entrySet()) {
            String subject = entry.getKey();
            int credit = subjectCredits.get(subject);
            int gradePoint = convertGradeToPoint((String) entry.getValue().getSelectedItem());

            totalWeightedPoints += credit * gradePoint;
            totalCredits += credit;
        }

        // Add exploratory credits (if any)
        try {
            int extraCredits = Integer.parseInt(extraCreditField.getText().trim());
            totalCredits += extraCredits;
        } catch (NumberFormatException ex) {
            // Do nothing, treat as 0
        }

        if (totalCredits == 0) {
            JOptionPane.showMessageDialog(this, "⚠️ Total credits cannot be zero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double sgpa = totalWeightedPoints / totalCredits;
        String sgpaFormatted = String.format("%.2f", sgpa);

        JOptionPane.showMessageDialog(this, "📘 Your expected SGPA is: " + sgpaFormatted, "SGPA Calculated", JOptionPane.INFORMATION_MESSAGE);
    }

    private int convertGradeToPoint(String grade) {
        return switch (grade) {
            case "O" -> 10;
            case "A+" -> 9;
            case "A" -> 8;
            case "B+" -> 7;
            case "B" -> 6;
            case "C+" -> 5;
            case "C" -> 4;
            default -> 0; // "F" or invalid
        };
    }
}
