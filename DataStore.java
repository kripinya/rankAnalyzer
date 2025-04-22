import java.io.*;
import java.util.*;

public class DataStore {
    private static final String STUDENTS_FILE = "students.ser";
    private static final String TEACHERS_FILE = "teachers.ser";

    // Load students from file
    public static List<Student> loadStudents() {
        return loadList(STUDENTS_FILE);
    }

    // Load teachers from file
    public static List<Teacher> loadTeachers() {
        return loadList(TEACHERS_FILE);
    }

    // Save students to file
    public static void saveStudents(List<Student> students) {
        saveList(STUDENTS_FILE, students);
    }

    // Save teachers to file
    public static void saveTeachers(List<Teacher> teachers) {
        saveList(TEACHERS_FILE, teachers);
    }

    // Generic loader for List<T>
    @SuppressWarnings("unchecked")
    private static <T> List<T> loadList(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(); // return empty list if file doesn't exist
        }
    }

    // Generic saver for List<T>
    private static <T> void saveList(String fileName, List<T> list) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Update codename persistently
    public static void updateStudentCodename(Student student, String newCodename) {
        List<Student> students = loadStudents();
        for (Student s : students) {
            if (s.getSapId().equals(student.getSapId())) {
                s.setCodename(newCodename);
                break;
            }
        }
        saveStudents(students);  // Save the updated list
    }
}
