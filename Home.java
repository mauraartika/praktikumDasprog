import javax.swing.*;
import java.awt.*;

public class Home extends JFrame {
    private JButton playButton;
    private JButton achievementButton;
    private UserStats stats;
    public Home() {
        super("Home");
    }
    public void displayHome(String username, Database db, int userId) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(GameMain.COLOR_BG);

        JLabel titleLabel = new JLabel("🎮 Selamat datang, " + username + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.BLACK);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel Pilihan Pion
        JPanel pionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pionPanel.setBackground(GameMain.COLOR_BG);

        JLabel pionLabel1 = new JLabel("Pilih Pion P1:");
        pionLabel1.setFont(new Font("Arial", Font.PLAIN, 14));

        String[] pionOptions = {"X", "O", "♥", "★", "☀"};
        JComboBox<String> pionCombo1 = new JComboBox<>(pionOptions);
        pionCombo1.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

        JLabel pionLabel2 = new JLabel("Pion P2:");
        pionLabel2.setFont(new Font("Arial", Font.PLAIN, 14));

        JComboBox<String> pionCombo2 = new JComboBox<>(pionOptions);
        pionCombo2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

        pionPanel.add(pionLabel1);
        pionPanel.add(pionCombo1);
        pionPanel.add(Box.createHorizontalStrut(10));
        pionPanel.add(pionLabel2);
        pionPanel.add(pionCombo2);
        mainPanel.add(pionPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(GameMain.COLOR_BG);

        playButton = new JButton("Play");
        playButton.setPreferredSize(new Dimension(120, 40));
        playButton.setBackground(new Color(220, 53, 69));
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(playButton);

        achievementButton = new JButton("Achievements");
        achievementButton.setPreferredSize(new Dimension(120, 40));
        achievementButton.setBackground(new Color(0, 123, 255));
        achievementButton.setForeground(Color.WHITE);
        achievementButton.setFocusPainted(false);
        achievementButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(achievementButton);

        mainPanel.add(buttonPanel);
        add(mainPanel);

        // ActionListener tombol
        playButton.addActionListener(e -> {
            String p1Pion = (String) pionCombo1.getSelectedItem();
            String p2Pion = (String) pionCombo2.getSelectedItem();

            if (p1Pion.equals(p2Pion)) {
                JOptionPane.showMessageDialog(this, "Pion Player 1 dan Player 2 tidak boleh sama!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PionShape player1Shape = PionShape.fromString(p1Pion);
            PionShape player2Shape = PionShape.fromString(p2Pion);

            dispose();
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Tic Tac Toe");
                GameMain gameMain = new GameMain(username, db, userId);
                gameMain.setPlayerShapes(player1Shape, player2Shape);
                frame.setContentPane(gameMain);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
            PlaySound.playGameStartSound();
        });

        achievementButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                stats = db.getUserStats(userId);
                new Achivement(username, stats, db, userId);
            });
        });

        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}
