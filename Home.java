import javax.swing.*;
import java.awt.*;

public class Home extends JFrame {
    private JButton playButton;
    private JButton achievementButton;
    private UserStats stats;
    public Home() {
        super("Home");
    }
    public void  displayHome(String username, Database db , int userId){
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(GameMain.COLOR_BG);

        JLabel titleLabel = new JLabel("🎮 Selamat datang, " + username + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBackground(GameMain.COLOR_BG);

        playButton = new JButton("Play");
        playButton.setBackground(new Color(220, 53, 69)); // Merah (seperti bootstrap danger)
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(playButton);

        achievementButton = new JButton("Achievements");
        achievementButton.setBackground(new Color(0, 123, 255)); // Biru (seperti bootstrap primary)
        achievementButton.setForeground(Color.WHITE);
        achievementButton.setFocusPainted(false);
        achievementButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(achievementButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        playButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                 JFrame frame = new JFrame("Tic tac toe.");
                 frame.setContentPane(new GameMain(db , userId));
                 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                 frame.pack();
                 frame.setLocationRelativeTo(null);
                 frame.setVisible(true);
            });
        });
        achievementButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                 stats = db.getUserStats(userId);
                 new Achivement(username,stats, db , userId);
            });
        });
      

        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}
