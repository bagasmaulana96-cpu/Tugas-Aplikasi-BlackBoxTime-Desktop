package ui;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private UserDAO userDAO;
    
    public LoginFrame() {
        userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("TimeBlackBox - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null); // Absolute positioning untuk kontrol penuh
        mainPanel.setBackground(new Color(240, 240, 245));
        
        // Title
        JLabel lblTitle = new JLabel("Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(51, 51, 51));
        lblTitle.setBounds(0, 30, 450, 40);
        mainPanel.add(lblTitle);
        
        // Username Label
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsername.setBounds(80, 100, 100, 25);
        mainPanel.add(lblUsername);
        
        // Username Field
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBounds(180, 100, 190, 30);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(txtUsername);
        
        // Password Label
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setBounds(80, 145, 100, 25);
        mainPanel.add(lblPassword);
        
        // Password Field
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(180, 145, 190, 30);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(txtPassword);
        
        // Login Button
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setBounds(125, 210, 120, 40);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> handleLogin());
        mainPanel.add(btnLogin);
        
        // Register Button
        btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnRegister.setBackground(new Color(100, 180, 100));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setBounds(255, 210, 120, 40);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> openRegisterForm());
        mainPanel.add(btnRegister);
        
        // Enter key untuk login
        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        
        add(mainPanel);
    }
    
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username tidak boleh kosong!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Password tidak boleh kosong!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            txtPassword.requestFocus();
            return;
        }
        
        try {
            User user = userDAO.loginUser(username, password);
            
            if (user != null) {
                JOptionPane.showMessageDialog(this, 
                    "Login berhasil! Selamat datang, " + user.getUsername(), 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                this.dispose();
                new MainFrame(user).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Username atau password salah!", 
                    "Login Gagal", 
                    JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error saat login: " + ex.getMessage(), 
                "System Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void openRegisterForm() {
        this.dispose();
        new RegisterFrame().setVisible(true);
    }
    
public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        util.DatabaseConnection.createTablesIfNotExist(); 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}