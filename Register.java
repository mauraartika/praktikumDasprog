import javax.swing.*;
import java.awt.*;

public class Register extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JLabel statusLabel;

    public Register() {
        super("Register User");
    }
    public void displayRegister(Database db){
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(GameMain.COLOR_BG);

        JLabel titleLabel = new JLabel("📝 Buat Akun Baru", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        formPanel.setBackground(GameMain.COLOR_BG);
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(GameMain.COLOR_BG);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(GameMain.FONT_STATUS);
        statusLabel.setForeground(Color.RED);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(60, 130, 200));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        bottomPanel.add(registerButton, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        registerButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                statusLabel.setText("❌ Username & Password wajib diisi.");
                return;
            }

            if (db.insertUser(user, pass)) {
                statusLabel.setForeground(new Color(0, 128, 0));
                statusLabel.setText("✅ Berhasil register!");
                JOptionPane.showMessageDialog(this, "Registrasi berhasil. Silakan login.");
                dispose(); 
                SwingUtilities.invokeLater(() -> {
                    LoginPage lg = new LoginPage();
                    lg.displayLogin(db);
                });
            } else {
                statusLabel.setText("❌ Username mungkin sudah digunakan.");
            }
        });

        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        
        JLabel loginLabel = new JLabel("<HTML><U>Already have an account?</U></HTML>");
        loginLabel.setForeground(new Color(0, 102, 204)); 
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        bottomPanel.add(loginLabel, BorderLayout.SOUTH);
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose(); 
                javax.swing.SwingUtilities.invokeLater(() -> {
                   LoginPage lg =  new LoginPage();
                   lg.displayLogin(db);
                });
            }
        });
    }
}
