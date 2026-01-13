package service;

import model.TimeBlock;
import java.util.List;
import java.util.ArrayList;

public class TimeAnalyzer {
    // [OOP - Polymorphism]
    // List ini menampung tipe 'TimeBlock', tapi di dalamnya bisa berisi objek
    // FocusSession ATAU BreakSession. Ini disebut 'Upcasting'.
    private List<TimeBlock> sessions;

    public TimeAnalyzer() {
        this.sessions = new ArrayList<>();
    }

    public void addSession(TimeBlock session) {
        sessions.add(session);
    }

    public int getTotalScore() {
        int total = 0;
        for (TimeBlock session : sessions) {
            // [OOP - Polymorphism (Dynamic Binding)]
            // Saat baris ini dijalankan, Java akan mengecek secara otomatis:
            // "Apakah ini objek FocusSession? Jika ya, pakai rumus skor Focus."
            // "Apakah ini objek BreakSession? Jika ya, pakai rumus skor Break."
            // Kita tidak perlu pakai 'if-else' untuk cek tipe object.
            total += session.calculateProductivityScore();
        }
        return total;
    }
    
    public List<TimeBlock> getSessions() {
        return sessions;
    }
}