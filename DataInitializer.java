// DataInitializer.java
import java.util.*;

public class DataInitializer {

    public static void initialize() {
        // Load existing students
        List<Student> existingStudents = DataStore.loadStudents();
        Set<String> existingSapIds = new HashSet<>();
        for (Student s : existingStudents) {
            existingSapIds.add(s.getSapId());
        }

        // SAP IDs to add
        String[] newSapIds = {
            "500125823", "500125205", "500119184",
            "500119188", "500119567", "500119884",
            "500125637"
        };

        boolean added = false;
        for (String sapId : newSapIds) {
            if (!existingSapIds.contains(sapId)) {
                existingStudents.add(new Student(sapId, "", ""));
                added = true;
            }
        }

        if (added) {
            DataStore.saveStudents(existingStudents);
            System.out.println("✅ Student data updated. New SAP IDs added.");
        } else {
            System.out.println("ℹ️ All SAP IDs already exist. No new entries added.");
        }

        // Reinitialize teacher data (always fresh since teachers rarely change)
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher("T001", "teacher1", Arrays.asList("OOPS","OOPS LAB")));
        teachers.add(new Teacher("T002", "teacher2", Arrays.asList("DCN","DCN LAB")));
        teachers.add(new Teacher("T003", "teacher3", Arrays.asList("CCF","CCF LAB")));
        teachers.add(new Teacher("T004", "teacher4", Arrays.asList("Software Engineering")));
        teachers.add(new Teacher("T005", "teacher5", Arrays.asList("Linear Algebra")));
        DataStore.saveTeachers(teachers);

        System.out.println("✅ Teacher data initialized successfully.");
    }

    public static void main(String[] args) {
        initialize();
    }
}
