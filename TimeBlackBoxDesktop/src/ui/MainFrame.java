package ui;

import dao.TimeBlockDAO;
import model.FocusSession;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// [OOP - Inheritance]
public class MainFrame extends JFrame {
    
    private User currentUser;
    private TimeBlockDAO timeBlockDAO;
    
    // Komponen Timer
    private Timer timer;
    private long startTimeMillis;
    private boolean isRunning = false;
    
    // UI Components
    private JLabel lblTimerDisplay;
    private JLabel lblTotalScore;
    private JButton btnStartStop;
    private JTextField txtActivityName;
    private JTable tableHistory;
    private DefaultTableModel tableModel;

    public MainFrame(User user) {
        this.currentUser = user;
        this.timeBlockDAO = new TimeBlockDAO();
        
        initComponents();
        loadHistoryData();
        updateTotalScore(); // Load score hari ini saat login
        
        // [OOP - Interface (Abstraction)]
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isRunning) {
                    stopTimerAndSave(); // Simpan otomatis saat keluar
                }
                super.windowClosing(e);
            }
        });
    }

    private void initComponents() {
        setTitle("TimeBlackBox - Focus Timer: " + currentUser.getUsername());
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Atas (Input & Timer) ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(new Color(40, 44, 52)); // Dark Theme

        // Input Nama Aktivitas
        JLabel lblTask = new JLabel("Apa yang ingin kamu kerjakan?");
        lblTask.setForeground(Color.WHITE);
        lblTask.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtActivityName = new JTextField();
        txtActivityName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        // Timer Display
        lblTimerDisplay = new JLabel("00:00:00");
        lblTimerDisplay.setFont(new Font("Arial", Font.BOLD, 48));
        lblTimerDisplay.setForeground(Color.CYAN);
        lblTimerDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Tombol Start/Stop
        btnStartStop = new JButton("START FOCUS");
        btnStartStop.setFont(new Font("Arial", Font.BOLD, 16));
        btnStartStop.setBackground(new Color(46, 204, 113)); // Green
        btnStartStop.setForeground(Color.WHITE);
        btnStartStop.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStartStop.setFocusPainted(false);
        
        // [OOP - Interface]
        btnStartStop.addActionListener(e -> handleStartStop());

        topPanel.add(lblTask);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(txtActivityName);
        topPanel.add(Box.createVerticalStrut(20));
        topPanel.add(lblTimerDisplay);
        topPanel.add(Box.createVerticalStrut(20));
        topPanel.add(btnStartStop);

        // --- Panel Tengah (History) ---
        String[] columns = {"Aktivitas", "Durasi", "Status", "Poin"};
        tableModel = new DefaultTableModel(columns, 0);
        tableHistory = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableHistory);
        
        // --- Panel Bawah (Total Score Harian) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalScore = new JLabel("Total Poin Hari Ini: 0 / 100");
        lblTotalScore.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(lblTotalScore);

        // Add ke Frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Setup Swing Timer (Update UI setiap 1 detik)
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimerDisplay();
            }
        });
    }

    private void handleStartStop() {
        if (!isRunning) {
            // Logic Mulai
            String taskName = txtActivityName.getText().trim();
            if (taskName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi nama aktivitas dulu!");
                return;
            }
            
            startTimer();
        } else {
            // Logic Berhenti (Manual Stop)
            stopTimerAndSave();
        }
    }

    private void startTimer() {
        isRunning = true;
        startTimeMillis = System.currentTimeMillis();
        timer.start();
        
        btnStartStop.setText("STOP & SAVE");
        btnStartStop.setBackground(new Color(231, 76, 60)); // Red
        txtActivityName.setEnabled(false); // Lock input
    }

    private void stopTimerAndSave() {
        if (!isRunning) return;

        // Hentikan Timer
        timer.stop();
        isRunning = false;
        long endTimeMillis = System.currentTimeMillis();
        
        // Hitung durasi dalam menit
        long durationMillis = endTimeMillis - startTimeMillis;
        int durationMinutes = (int) (durationMillis / (1000 * 60)); 
        
        // DEBUG: Uncomment baris bawah untuk testing cepat (detik dianggap menit)
        // int durationMinutes = (int) (durationMillis / 1000); 

        // Buat Objek FocusSession
        // [OOP - Polymorphism]
        FocusSession session = new FocusSession(txtActivityName.getText(), durationMinutes);
        
        // Simpan ke Database
        // [OOP - Abstraction]
        timeBlockDAO.saveTimeBlock(currentUser.getUserId(), session);

        // Reset UI
        btnStartStop.setText("START FOCUS");
        btnStartStop.setBackground(new Color(46, 204, 113));
        txtActivityName.setEnabled(true);
        txtActivityName.setText("");
        lblTimerDisplay.setText("00:00:00");
        
        // Refresh Data
        loadHistoryData();
        updateTotalScore();
        
        JOptionPane.showMessageDialog(this, 
            "Sesi Selesai!\n" +
            "Durasi: " + durationMinutes + " menit\n" +
            "Status: " + session.getCategoryLabel() + "\n" +
            "Poin: " + String.format("%.2f", session.calculateProductivityScore())
        );
    }

    private void updateTimerDisplay() {
        long now = System.currentTimeMillis();
        long elapsed = now - startTimeMillis;
        
        long seconds = (elapsed / 1000) % 60;
        long minutes = (elapsed / (1000 * 60)) % 60;
        long hours = (elapsed / (1000 * 60 * 60));
        
        String timeStr = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        lblTimerDisplay.setText(timeStr);
    }

    private void loadHistoryData() {
        tableModel.setRowCount(0);
        java.util.List<String[]> history = timeBlockDAO.getTodayHistory(currentUser.getUserId());
        for (String[] row : history) {
            tableModel.addRow(row);
        }
    }

    private void updateTotalScore() {
        double totalScore = timeBlockDAO.getTodayTotalScore(currentUser.getUserId());
        lblTotalScore.setText(String.format("Total Poin Hari Ini: %.2f / 100", totalScore));
        
        if (totalScore >= 100) {
            lblTotalScore.setForeground(new Color(46, 204, 113)); // Green for success
            lblTotalScore.setText(lblTotalScore.getText() + " (TARGET TERCAPAI!)");
        } else {
            lblTotalScore.setForeground(Color.BLACK);
        }
    }
}