package model;

// [OOP - Inheritance]
public class FocusSession extends TimeBlock {

    public FocusSession(String name, int duration) {
        super(name, duration);
    }

    // [OOP - Polymorphism]
    @Override
    public double calculateProductivityScore() {
        if (duration <= 0) return 0;
        double score = ((double) duration / 240.0) * 100.0;
        return score;
    }

    // [OOP - Polymorphism]
    @Override
    public String getCategoryLabel() {
        if (duration < 5) {
            return "Terdistraksi";
        } else if (duration < 15) {
            return "Sedikit Fokus";
        } else if (duration < 30) {
            // Asumsi 15 s.d 29
            return "Cukup Fokus"; 
        } else if (duration < 50) {
            // Asumsi 30 s.d 49
            return "Fokus";
        } else {
            // 50 menit ke atas
            return "Sangat Fokus";
        }
    }
}