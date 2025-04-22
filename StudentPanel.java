import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class StudentPanel extends JFrame {
    private Student student;
    private List<Student> allStudents;

    public StudentPanel(Student student, List<Student> allStudents) {
        this.student = student;
        this.allStudents = allStudents;
        initUI();
    }

    private void initUI() {
        setTitle("Student Dashboard - " + student.getCodename());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 5, 5)); // updated to 7 options
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        JButton btnViewSubject     = new JButton("1) View Marks of a Subject");
        JButton btnViewRankSub     = new JButton("2) View Rank in Subject+Exam");
        JButton btnViewAllMarks    = new JButton("3) View All Marks");
        JButton btnViewOverall     = new JButton("4) View Overall Exam Rank");
        JButton btnViewAllStudents = new JButton("5) View All Students' Marks");
        JButton btnChangeCodename  = new JButton("6) Change Codename");
        JButton btnCalculateSGPA   = new JButton("7) Calculate Expected SGPA");

        buttonPanel.add(btnViewSubject);
        buttonPanel.add(btnViewRankSub);
        buttonPanel.add(btnViewAllMarks);
        buttonPanel.add(btnViewOverall);
        buttonPanel.add(btnViewAllStudents);
        buttonPanel.add(btnChangeCodename);
        buttonPanel.add(btnCalculateSGPA);

        add(buttonPanel, BorderLayout.CENTER);

        btnViewSubject.addActionListener(e -> {
            String exam = (String) JOptionPane.showInputDialog(this,
                    "Select Exam:", "Exam", JOptionPane.PLAIN_MESSAGE,
                    null, Constants.EXAMS, Constants.EXAMS[0]);
            String subject = (String) JOptionPane.showInputDialog(this,
                    "Select Subject:", "Subject", JOptionPane.PLAIN_MESSAGE,
                    null, Constants.SUBJECTS, Constants.SUBJECTS[0]);
            if (exam != null && subject != null) {
                Integer m = student.getMark(subject, exam);
                JOptionPane.showMessageDialog(this,
                        subject + " (" + exam + "): " + (m != null ? m : 0) + " marks");
            }
        });

        btnViewRankSub.addActionListener(e -> {
            String exam = (String) JOptionPane.showInputDialog(this,
                    "Select Exam:", "Exam", JOptionPane.PLAIN_MESSAGE,
                    null, Constants.EXAMS, Constants.EXAMS[0]);
            String subject = (String) JOptionPane.showInputDialog(this,
                    "Select Subject:", "Subject", JOptionPane.PLAIN_MESSAGE,
                    null, Constants.SUBJECTS, Constants.SUBJECTS[0]);
            if (exam != null && subject != null) {
                List<Student> copy = new ArrayList<>(allStudents);
                copy.sort((a, b) -> {
                    Integer aMark = a.getMark(subject, exam);
                    Integer bMark = b.getMark(subject, exam);
                    if (aMark == null) aMark = 0;
                    if (bMark == null) bMark = 0;
                    return bMark - aMark;
                });
                int rank = 1;
                for (Student s : copy) {
                    if (s.getSapId().equals(student.getSapId())) break;
                    rank++;
                }
                Integer mark = student.getMark(subject, exam);
                String msg = "Your rank in " + subject + " (" + exam + "): " + rank +
                        "\nYour marks: " + (mark != null ? mark : 0);
                JOptionPane.showMessageDialog(this, msg);
            }
        });

        btnViewAllMarks.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("All marks for each Exam→Subject:\n\n");
            Map<String, Map<String, Integer>> m = student.getAllMarks();
            for (String exam : m.keySet()) {
                sb.append(exam).append(":\n");
                for (Map.Entry<String, Integer> en : m.get(exam).entrySet()) {
                    sb.append("  ").append(en.getKey())
                            .append(" = ").append(en.getValue()).append("\n");
                }
                sb.append("\n");
            }
            JTextArea ta = new JTextArea(sb.toString(), 15, 40);
            ta.setEditable(false);
            JScrollPane scroll = new JScrollPane(ta);
            scroll.setPreferredSize(new Dimension(450, 300));
            JOptionPane.showMessageDialog(this, scroll,
                    "All Marks", JOptionPane.INFORMATION_MESSAGE);
        });

        btnViewOverall.addActionListener(e -> {
            String exam = (String) JOptionPane.showInputDialog(this,
                    "Select Exam:", "Exam", JOptionPane.PLAIN_MESSAGE,
                    null, Constants.EXAMS, Constants.EXAMS[0]);
            if (exam != null) {
                Map<Student, Integer> examRanks = RankUtils.calculateExamRanks(allStudents, exam);
                int total = student.getTotalForExam(exam);
                int rank = examRanks.getOrDefault(student, -1);
                JOptionPane.showMessageDialog(this,
                        "Total in " + exam + ": " + total + "\nOverall Rank: " + rank);
            }
        });

        btnViewAllStudents.addActionListener(e -> {
            String exam = (String) JOptionPane.showInputDialog(this,
                    "Select Exam:", "Exam", JOptionPane.PLAIN_MESSAGE,
                    null, Constants.EXAMS, Constants.EXAMS[0]);
            if (exam != null) {
                String[] columnNames = new String[Constants.SUBJECTS.length + 1];
                columnNames[0] = "Codename";
                System.arraycopy(Constants.SUBJECTS, 0, columnNames, 1, Constants.SUBJECTS.length);

                String[][] tableData = new String[allStudents.size()][columnNames.length];
                for (int i = 0; i < allStudents.size(); i++) {
                    Student s = allStudents.get(i);
                    tableData[i][0] = s.getCodename();
                    for (int j = 0; j < Constants.SUBJECTS.length; j++) {
                        Integer mark = s.getMark(Constants.SUBJECTS[j], exam);
                        tableData[i][j + 1] = mark != null ? mark.toString() : "N/A";
                    }
                }

                JTable table = new JTable(tableData, columnNames);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                for (int i = 0; i < columnNames.length; i++) {
                    table.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                JScrollPane scroll = new JScrollPane(table);
                scroll.setPreferredSize(new Dimension(1100, 500));

                JOptionPane.showMessageDialog(this, scroll,
                        "All Students' Marks (" + exam + ")", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnChangeCodename.addActionListener(e -> {
            String newCode = JOptionPane.showInputDialog(this,
                    "Enter your new codename:", "Change Codename", JOptionPane.PLAIN_MESSAGE);
            if (newCode != null && !newCode.trim().isEmpty()) {
                newCode = newCode.trim();
                student.setCodename(newCode);
                DataStore.updateStudentCodename(student, newCode);
                JOptionPane.showMessageDialog(this,
                        "✅ Your codename has been updated to: " + newCode);
                setTitle("Student Dashboard - " + student.getCodename());
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ Invalid input. Codename cannot be empty.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCalculateSGPA.addActionListener(e -> {
            Map<String, Integer> subjectCredits = new HashMap<>();
            subjectCredits.put("OOPS", 3);
            subjectCredits.put("CCF", 4);
            subjectCredits.put("OOPS LAB", 1);
            subjectCredits.put("DCN", 3);
        
            Map<String, Integer> gradePointMap = new HashMap<>();
            gradePointMap.put("O", 10);
            gradePointMap.put("A+", 9);
            gradePointMap.put("A", 8);
            gradePointMap.put("B+", 7);
            gradePointMap.put("B", 6);
            gradePointMap.put("C+", 5);
            gradePointMap.put("C", 4);
            gradePointMap.put("F", 0);
        
            double totalWeightedScore = 0;
            int totalCredits = 0;
            StringBuilder summary = new StringBuilder("Entered Grades:\n\n");
        
            // Core Subjects
            for (Map.Entry<String, Integer> entry : subjectCredits.entrySet()) {
                String subject = entry.getKey();
                int credit = entry.getValue();
                String grade = (String) JOptionPane.showInputDialog(null,
                        "Enter expected grade for " + subject + ":",
                        "Grade Input", JOptionPane.PLAIN_MESSAGE, null,
                        new String[] {"O", "A+", "A", "B+", "B", "C+", "C", "F"}, "A");
        
                if (grade == null) return;
                int point = gradePointMap.getOrDefault(grade, 0);
                summary.append(subject).append(" (").append(credit).append(" credits): ").append(grade).append("\n");
        
                totalWeightedScore += point * credit;
                totalCredits += credit;
            }
        
            // Ask how many exploratory subjects
            String[] options = {"0", "1", "2"};
            String numExploratory = (String) JOptionPane.showInputDialog(null,
                    "How many Exploratory/Other subjects do you have?",
                    "Exploratory Subjects", JOptionPane.PLAIN_MESSAGE, null,
                    options, "0");
        
            if (numExploratory == null) return;
        
            int countExploratory = Integer.parseInt(numExploratory);
            for (int i = 1; i <= countExploratory; i++) {
                String creditInput = JOptionPane.showInputDialog(null,
                        "Enter credit value for Exploratory Subject " + i + ":",
                        "Exploratory Credit", JOptionPane.PLAIN_MESSAGE);
                if (creditInput == null) return;
        
                int credit = 0;
                try {
                    credit = Integer.parseInt(creditInput.trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "⚠ Invalid number entered. Credit set to 0 for this subject.",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                }
        
                String grade = (String) JOptionPane.showInputDialog(null,
                        "Enter expected grade for Exploratory Subject " + i + ":",
                        "Exploratory Grade", JOptionPane.PLAIN_MESSAGE, null,
                        new String[] {"O", "A+", "A", "B+", "B", "C+", "C", "F"}, "A");
        
                if (grade == null) return;
        
                int point = gradePointMap.getOrDefault(grade, 0);
                totalWeightedScore += point * credit;
                totalCredits += credit;
                summary.append("Exploratory ").append(i).append(" (")
                        .append(credit).append(" credits): ").append(grade).append("\n");
            }
        
            if (totalCredits == 0) {
                JOptionPane.showMessageDialog(null,
                        "❌ Total credits cannot be zero!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            double sgpa = totalWeightedScore / totalCredits;
            summary.append("\n✅ Your expected SGPA: ").append(String.format("%.2f", sgpa));
        
            JTextArea ta = new JTextArea(summary.toString(), 12, 40);
            ta.setEditable(false);
            JScrollPane scroll = new JScrollPane(ta);
            JOptionPane.showMessageDialog(null, scroll,
                    "Expected SGPA", JOptionPane.INFORMATION_MESSAGE);
        });
        
    }
}
