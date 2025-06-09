import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TTTConsoleNonOO extends JFrame {
    // Define named constants for:
    //  1. Player: using CROSS and NOUGHT
    //  2. Cell contents: using CROSS, NOUGHT and NO_SEED
    public static final int CROSS   = 0;
    public static final int NOUGHT  = 1;
    public static final int NO_SEED = 2;

    // The game board buttons
    private JButton[][] buttons = new JButton[3][3];
    private boolean isXTurn = true; // X plays first
    private boolean isSoloMode = false;

    // The entry main method (the program starts here)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TTTConsoleNonOO game = new TTTConsoleNonOO();
            game.setVisible(true);
        });
    }

    /** Constructor sets up the GUI */
    public TTTConsoleNonOO() {
        setTitle("Tic Tac Toe");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));

        selectGameMode();
        initBoard();
    }

    /** Prompt user to select solo or multiplayer mode */
    private void selectGameMode() {
        String[] options = {"Solo vs Bot", "Multiplayer"};
        int choice = JOptionPane.showOptionDialog(this, "Choose Game Mode:", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        isSoloMode = (choice == 0);
    }

    /** Initialize the game board */
    public void initBoard() {
        Font font = new Font("Arial", Font.BOLD, 60);
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                JButton button = new JButton("");
                button.setFont(font);
                button.setFocusPainted(false);
                button.setForeground(Color.BLACK);
                button.addActionListener(new CellListener(row, col));
                buttons[row][col] = button;
                add(button);
            }
        }
    }

    /** Listener for button clicks */
    private class CellListener implements ActionListener {
        private int row, col;

        public CellListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void actionPerformed(ActionEvent e) {
            JButton button = buttons[row][col];
            if (!button.getText().equals("")) {
                return; // already marked
            }

            button.setText(isXTurn ? "X" : "O");
            button.setForeground(isXTurn ? Color.RED : Color.BLUE);

            if (checkWin()) {
                JOptionPane.showMessageDialog(null, (isXTurn ? "X" : "O") + " wins!");
                resetGame();
            } else if (isBoardFull()) {
                JOptionPane.showMessageDialog(null, "It's a draw!");
                resetGame();
            } else {
                isXTurn = !isXTurn;
                // Solo mode: skip O turn if bot (not implemented)
            }
        }
    }

    /** Check if current player has won */
    private boolean checkWin() {
        String player = isXTurn ? "X" : "O";

        for (int i = 0; i < 3; ++i) {
            if (player.equals(buttons[i][0].getText()) &&
                    player.equals(buttons[i][1].getText()) &&
                    player.equals(buttons[i][2].getText())) return true;

            if (player.equals(buttons[0][i].getText()) &&
                    player.equals(buttons[1][i].getText()) &&
                    player.equals(buttons[2][i].getText())) return true;
        }

        if (player.equals(buttons[0][0].getText()) &&
                player.equals(buttons[1][1].getText()) &&
                player.equals(buttons[2][2].getText())) return true;

        if (player.equals(buttons[0][2].getText()) &&
                player.equals(buttons[1][1].getText()) &&
                player.equals(buttons[2][0].getText())) return true;

        return false;
    }

    /** Check if the board is full */
    private boolean isBoardFull() {
        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 3; ++col)
                if (buttons[row][col].getText().equals(""))
                    return false;
        return true;
    }

    /** Reset the game board */
    private void resetGame() {
        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 3; ++col)
                buttons[row][col].setText("");
        isXTurn = true;
    }
}