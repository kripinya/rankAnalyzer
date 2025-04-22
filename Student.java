import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sapId;
    private String codename;
    private String password;
    
    // subject → (exam → marks)
    private Map<String, Map<String, Integer>> marks;

    // Constructor
    public Student(String sapId, String codename, String password) {
        this.sapId = sapId;
        this.codename = codename;
        this.password = password;
        this.marks = new HashMap<>();
    }

    // Getters
    public String getSapId() {
        return sapId;
    }

    public String getCodename() {
        return codename;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, Map<String, Integer>> getAllMarks() {
        return marks;
    }

    // Setters for codename and password
    public void setCodename(String codename) {
        this.codename = codename;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Get specific mark for a subject and exam
    public Integer getMark(String subject, String exam) {
        if (marks.containsKey(subject)) {
            return marks.get(subject).getOrDefault(exam, null);
        }
        return null;
    }

    // Set mark for a subject and exam
    public void setMark(String subject, String exam, int score) {
        marks.putIfAbsent(subject, new HashMap<>());
        marks.get(subject).put(exam, score);
    }

    // ✅ NEW: Get total marks across all subjects for a specific exam
    public int getTotalForExam(String exam) {
        int total = 0;
        for (Map<String, Integer> examMap : marks.values()) {
            if (examMap != null) {
                Integer score = examMap.get(exam);
                if (score != null) {
                    total += score;
                }
            }
        }
        return total;
    }
    
}
