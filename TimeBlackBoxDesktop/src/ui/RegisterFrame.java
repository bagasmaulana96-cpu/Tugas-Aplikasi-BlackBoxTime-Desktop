package ui;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterFrame extends JFrame {

    private UserDAO userDAO;

    // Komponen UI
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnRegister;
    private JButton btnBackToLogin;

    public RegisterFrame() {
        this.userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Register - TimeBlackBox");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel Utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);

        // Judul
        JLabel lblTitle = new JLabel("Register", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(50, 50, 50));
        lblTitle.setBounds(0, 30, 400, 40);
        mainPanel.add(lblTitle);

        // Label & Input Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsername.setBounds(50, 90, 100, 25);
        mainPanel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(150, 90, 180, 30);
        mainPanel.add(txtUsername);

        // Label & Input Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPassword.setBounds(50, 140, 100, 25);
        mainPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 140, 180, 30);
        mainPanel.add(txtPassword);

        // --- PERBAIKAN TOMBOL ---
        
        // Tombol Register (Warna Biru)
        btnRegister = new JButton("Register");
        btnRegister.setBounds(50, 200, 130, 40);
        btnRegister.setBackground(new Color(52, 152, 219)); // Biru
        btnRegister.setForeground(Color.WHITE); // Tulisan Putih
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setFocusPainted(false);
        // Baris ini penting agar warna background muncul di beberapa OS
        btnRegister.setOpaque(true); 
        btnRegister.setBorderPainted(false);
        
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });
        mainPanel.add(btnRegister);

        // Tombol Back (Warna Putih)
        btnBackToLogin = new JButton("Back to Login");
        btnBackToLogin.setBounds(200, 200, 130, 40);
        btnBackToLogin.setBackground(new Color(236, 240, 241)); // Abu-abu muda
        btnBackToLogin.setForeground(new Color(52, 152, 219)); // Tulisan Biru
        btnBackToLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnBackToLogin.setFocusPainted(false);
        
        btnBackToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBackToLogin();
            }
        });
        mainPanel.add(btnBackToLogin);

        add(mainPanel);
    }

    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // Validasi Input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username dan Password tidak boleh kosong!", 
                "Validasi Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, 
                "Username minimal 3 karakter!", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cek apakah username sudah ada
        if (userDAO.isUsernameExists(username)) {
            JOptionPane.showMessageDialog(this, 
                "Username sudah digunakan!\nSilakan gunakan username lain.", 
                "Username Exists", 
                JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }

        // Proses Register
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        if (userDAO.registerUser(newUser)) {
            JOptionPane.showMessageDialog(this, 
                "Register berhasil!\nSilakan login dengan akun Anda.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            handleBackToLogin(); // Kembali ke login screen
        } else {
            JOptionPane.showMessageDialog(this, 
                "Register gagal! Silakan coba lagi.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBackToLogin() {
        new LoginFrame().setVisible(true);
        this.dispose();
    }
}