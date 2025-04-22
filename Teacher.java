import java.io.Serializable;
import java.util.List;

public class Teacher implements Serializable {
    private String teacherId;
    private String password;
    private List<String> allowedSubjects;

    public Teacher(String teacherId, String password, List<String> allowedSubjects) {
        this.teacherId = teacherId;
        this.password = password;
        this.allowedSubjects = allowedSubjects;
    }

    public String getTeacherId() { return teacherId; }
    public String getPassword() { return password; }
    public List<String> getAllowedSubjects() { return allowedSubjects; }
}
