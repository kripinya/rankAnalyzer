// StudentDataViewer.java
import java.util.*;


public class StudentDataViewer {
    public static void main(String[] args) {
        List<Student> students = DataStore.loadStudents();
        for (Student s : students) {
            System.out.println("SAP ID: " + s.getSapId());
            System.out.println("Codename: " + s.getCodename());
            System.out.println("Password: " + s.getPassword());
            System.out.println("Marks: " + s.getAllMarks());
            System.out.println("------------");
        }
    }
}
