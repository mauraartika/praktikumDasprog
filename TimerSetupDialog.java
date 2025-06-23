import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TimerSetupDialog extends JDialog {
    private JTextField timerInputField;
    private int timerDuration = -1; 

    public TimerSetupDialog(JFrame parent) {
        super(parent, "Atur Waktu per Giliran", true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        JLabel instructionLabel = new JLabel("Waktu per giliran (1-60 detik):"); 

        timerInputField = new JTextField("15", 5); 
        timerInputField.setHorizontalAlignment(JTextField.CENTER);
        timerInputField.setFont(new Font("SansSerif", Font.BOLD, 14));

        inputPanel.add(instructionLabel);
        inputPanel.add(timerInputField);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton startButton = new JButton("Mulai dengan Timer");
        JButton noTimerButton = new JButton("Main Tanpa Timer");

        buttonPanel.add(startButton);
        buttonPanel.add(noTimerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        startButton.addActionListener((ActionEvent e) -> {
            validateAndClose();
        });

        timerInputField.addActionListener((ActionEvent e) -> {
            validateAndClose();
        });

        noTimerButton.addActionListener((ActionEvent e) -> {
            this.timerDuration = -1;
            setVisible(false);
        });

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void validateAndClose() {
        String inputText = timerInputField.getText();
        try {
            int duration = Integer.parseInt(inputText);

            if (duration >= 1 && duration <= 60) {
                this.timerDuration = duration;
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Harap masukkan angka antara 1 dan 60.",
                    "Input Tidak Valid",
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Harap masukkan angka yang valid.",
                "Input Salah",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public int showDialog() {
        setVisible(true);
        return timerDuration;
    }
}