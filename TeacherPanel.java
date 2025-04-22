import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class TeacherPanel extends JFrame {
    private Teacher teacher;
    private List<Student> students;

    public TeacherPanel(Teacher teacher, List<Student> students) {
        this.teacher  = teacher;
        this.students = students;
        initUI();
    }

    private void initUI() {
        setTitle("Teacher Dashboard – " + teacher.getTeacherId());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Row 0: Student picker (SAP ID - Codename)
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        String[] studentOptions = students.stream()
                .map(s -> s.getSapId() + " - " + s.getCodename())
                .toArray(String[]::new);
        JComboBox<String> comboStudent = new JComboBox<>(studentOptions);
        form.add(comboStudent, gbc);

        // Row 1: Subject picker (only allowed subjects)
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Subject:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> comboSub = new JComboBox<>(
                teacher.getAllowedSubjects().toArray(new String[0])
        );
        form.add(comboSub, gbc);

        // Row 2: Exam picker
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Exam:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> comboExam = new JComboBox<>(Constants.EXAMS);
        form.add(comboExam, gbc);

        // Row 3: Marks field
        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Marks:"), gbc);
        gbc.gridx = 1;
        JTextField txtMarks = new JTextField();
        form.add(txtMarks, gbc);

        // Row 4: Save button
        gbc.gridx = 1; gbc.gridy = 4;
        JButton btnSave = new JButton("Save Marks");
        form.add(btnSave, gbc);

        // Row 5: Delete button
        gbc.gridx = 1; gbc.gridy = 5;
        JButton btnDelete = new JButton("Delete Marks");
        form.add(btnDelete, gbc);

        // Row 6: View Assigned Marks button
        gbc.gridx = 1; gbc.gridy = 6;
        JButton btnViewAssigned = new JButton("View Assigned Marks");
        form.add(btnViewAssigned, gbc);

        add(form, BorderLayout.CENTER);

        // --- Save Marks Handler ---
        btnSave.addActionListener(e -> {
            String selectedItem = (String) comboStudent.getSelectedItem();
            String sapId        = selectedItem.split(" - ")[0];
            String sub          = (String) comboSub.getSelectedItem();
            String exam         = (String) comboExam.getSelectedItem();

            int marks;
            try {
                marks = Integer.parseInt(txtMarks.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "❌ Enter a valid integer for marks.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            students.stream()
                .filter(s -> s.getSapId().equals(sapId))
                .findFirst()
                .ifPresent(target -> {
                    target.setMark(sub, exam, marks);
                    DataStore.saveStudents(students);
                    JOptionPane.showMessageDialog(this,
                        "✅ Saved " + marks + " for " + target.getCodename() +
                        " (SAP ID: " + sapId + ") in " + sub + " (" + exam + ")");
                });
        });

        // --- Delete Marks Handler ---
        btnDelete.addActionListener(e -> {
            String selectedItem = (String) comboStudent.getSelectedItem();
            String sapId        = selectedItem.split(" - ")[0];
            String sub          = (String) comboSub.getSelectedItem();
            String exam         = (String) comboExam.getSelectedItem();

            students.stream()
                .filter(s -> s.getSapId().equals(sapId))
                .findFirst()
                .ifPresent(target -> {
                    Map<String, Integer> examMap = target.getAllMarks().get(sub);
                    if (examMap != null && examMap.containsKey(exam)) {
                        examMap.remove(exam);
                        if (examMap.isEmpty()) {
                            target.getAllMarks().remove(sub);
                        }
                        DataStore.saveStudents(students);
                        JOptionPane.showMessageDialog(this,
                            "✅ Deleted marks for " + target.getCodename() +
                            " (SAP ID: " + sapId + ") in " + sub + " (" + exam + ")");
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "❌ No marks found for " + sub + " (" + exam + ").",
                            "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                });
        });

        // --- View Assigned Marks Handler ---
        btnViewAssigned.addActionListener(e -> {
            String subject = (String) comboSub.getSelectedItem();

            // Build column names: first column is "SAP ID", then one for each exam
            String[] columnNames = new String[Constants.EXAMS.length + 1];
            columnNames[0] = "SAP ID";
            System.arraycopy(Constants.EXAMS, 0, columnNames, 1, Constants.EXAMS.length);

            // Build data rows: one per student
            String[][] data = new String[students.size()][columnNames.length];
            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                data[i][0] = s.getSapId();
                for (int j = 0; j < Constants.EXAMS.length; j++) {
                    Integer mark = s.getMark(subject, Constants.EXAMS[j]);
                    data[i][j + 1] = (mark != null) ? mark.toString() : "N/A";
                }
            }

            // Show in a JTable
            JTable table = new JTable(data, columnNames);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int i = 0; i < columnNames.length; i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(100);
            }

            JScrollPane scroll = new JScrollPane(table);
            scroll.setPreferredSize(new Dimension(700, 300));

            JOptionPane.showMessageDialog(this, scroll,
                "Assigned Marks for Subject: " + subject,
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
