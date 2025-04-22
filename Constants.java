import java.util.*;

public class Constants {
    public static final String[] SUBJECTS = {
        "CCF", "CCF LAB", "OOPS", "OOPS LAB",
        "DCN", "DCN LAB", "Software Engineering", "Linear Algebra"
    };

    public static final String[] EXAMS = {
        "Mid-sem", "End-sem", "Class Test 1", "Class Test 2", "Class Test 3","Viva 1","Viva 2"
    };

    public static final Set<String> SUBJECTS_WITH_EXAMS = new HashSet<>(Arrays.asList(
        "CCF", "OOPS", "DCN", "Software Engineering", "Linear Algebra"
    ));
}
