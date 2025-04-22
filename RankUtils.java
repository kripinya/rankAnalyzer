import java.util.*;

public class RankUtils {

    // Calculates exam-wise ranks based on total marks across all subjects for that exam
    public static Map<Student, Integer> calculateExamRanks(List<Student> students, String examName) {
        Map<Student, Integer> ranks = new HashMap<>();

        // Step 1: Make a list of Student → total marks for that exam
        List<Map.Entry<Student, Integer>> totalList = new ArrayList<>();
        for (Student s : students) {
            int total = s.getTotalForExam(examName);
            totalList.add(new AbstractMap.SimpleEntry<>(s, total));
        }

        // Step 2: Sort list in descending order of marks
        totalList.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        // Step 3: Assign ranks (handle ties too)
        int currentRank = 1;
        for (int i = 0; i < totalList.size(); i++) {
            if (i > 0 && totalList.get(i).getValue().equals(totalList.get(i - 1).getValue())) {
                ranks.put(totalList.get(i).getKey(), ranks.get(totalList.get(i - 1).getKey()));
            } else {
                ranks.put(totalList.get(i).getKey(), currentRank);
            }
            currentRank++;
        }

        return ranks;
    }
}
