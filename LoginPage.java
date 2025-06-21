import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    public LoginPage() {
        super("Login User");
    }
    public void displayLogin(Database db){
        // Panel utama dengan padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(GameMain.COLOR_BG);

        // Panel judul
        JLabel titleLabel = new JLabel("🧑 Login ke Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel (username & password)
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        formPanel.setBackground(GameMain.COLOR_BG);
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel bawah (status + tombol)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(GameMain.COLOR_BG);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(GameMain.FONT_STATUS);
        statusLabel.setForeground(Color.RED);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(60, 130, 200));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(loginButton, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        // Event login
        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            if (db.checkLogin(user, pass)) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    Home hm = new Home();
                    hm.displayHome(user, db, db.getLoggedInUserId());
                });
            } else {
                statusLabel.setText("❌ Login gagal. Coba lagi.");
            }
        });
        JLabel registerLabel = new JLabel("<HTML><U>Don't have an account? Register</U></HTML>");
        registerLabel.setForeground(new Color(0, 102, 204)); // biru link
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        bottomPanel.add(registerLabel, BorderLayout.SOUTH);
        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose(); // Tutup jendela login
                javax.swing.SwingUtilities.invokeLater(() -> {
                    Register rg = new Register();
                    rg.displayRegister(db);
                });
            }
        });

        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
 
}
