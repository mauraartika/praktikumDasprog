import javax.swing.*;
import java.awt.*;

public class Achivement extends JFrame {

    public Achivement(String username,UserStats stats, Database db, int userId) {
        setTitle("🏆 ACHIEVEMENT RASYID");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("🏆 Achivement of " +username.substring(0,1).toUpperCase() +username.substring(1).toLowerCase(), SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(44, 62, 80));
        mainPanel.add(title, BorderLayout.NORTH);

        // Stats panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(new Color(245, 248, 250));

        Font sectionFont = new Font("Segoe UI", Font.BOLD, 15);
        Font statFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color labelColor = new Color(60, 60, 60);

        // SOLO Stats
        JLabel soloLabel = new JLabel("🎯 Solo Stats");
        soloLabel.setFont(sectionFont);
        soloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        soloLabel.setForeground(labelColor);
        statsPanel.add(soloLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(createStatRow("🥇 Solo Wins:", stats.getSoloWin(), statFont, labelColor));
        statsPanel.add(createStatRow("❌ Solo Losses:", stats.getSoloLose(), statFont, labelColor));
        statsPanel.add(createStatRow("🤝 Solo Draws:", stats.getSoloDraw(), statFont, labelColor));
        statsPanel.add(createStatRow("📊 Total Solo Games:", stats.getSoloGame(), statFont, labelColor));

        statsPanel.add(Box.createVerticalStrut(15));

        // DUO Stats
        JLabel duoLabel = new JLabel("👥 Duo Stats");
        duoLabel.setFont(sectionFont);
        duoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        duoLabel.setForeground(labelColor);
        statsPanel.add(duoLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(createStatRow("👥 Duo Wins:", stats.getDuoWin(), statFont, labelColor));
        statsPanel.add(createStatRow("💔 Duo Losses:", stats.getDuoLose(), statFont, labelColor));
        statsPanel.add(createStatRow("🔁 Duo Draws:", stats.getDuoDraw(), statFont, labelColor));
        statsPanel.add(createStatRow("📊 Total Duo Games:", stats.getDuoGame(), statFont, labelColor));

        mainPanel.add(statsPanel, BorderLayout.CENTER);

        // Total Games Label (Bottom Center)
        int totalGames = stats.getSoloGame() + stats.getDuoGame();
        JLabel totalGamesLabel = new JLabel("🎯 Total Games Played: " + totalGames, SwingConstants.CENTER);
        totalGamesLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalGamesLabel.setForeground(new Color(52, 73, 94));
        totalGamesLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(231, 76, 60));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        backButton.setFocusable(false);
        backButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                 Home hm= new Home();
                 hm.displayHome(username, db, userId);
            });
        });
      
        backButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        // Bottom panel with total games + back button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.WHITE);
        totalGamesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(totalGamesLabel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(backButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createStatRow(String labelText, int value, Font font, Color color) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(245, 248, 250));

        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(color);

        JLabel val = new JLabel(String.valueOf(value), SwingConstants.RIGHT);
        val.setFont(font);
        val.setForeground(new Color(33, 37, 41));

        row.add(label, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);

        return row;
    }

   
}
