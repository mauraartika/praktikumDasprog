import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GameMain extends JPanel {
   private static final long serialVersionUID = 1L;
   private GameMode gameMode;

   public static final String TITLE = "Tic Tac Toe";
   public static final Color COLOR_BG = Color.WHITE;
   public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
   public static final Color COLOR_CROSS = new Color(239, 105, 80);
   public static final Color COLOR_NOUGHT = new Color(64, 154, 225);
   public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

   private Board board;
   private State currentState;
   private Seed currentPlayer;
   private JLabel statusBar;
   private Timer turnTimer;
   private int timeLeft;
   private boolean timerEnabled = true;
   private JComboBox<String> timerSelector;


   public GameMain() {
      // START RASYID
      String[] options = { "Solo (vs. Bot)", "Duo (2 Players)" };
      int choice = JOptionPane.showOptionDialog(
            null,
            "Pilih mode permainan:",
            "Mode Permainan",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
      if (choice == 0) {
         gameMode = GameMode.SOLO;
      } else {
         gameMode = GameMode.DUO;
      }
      // END RASYID

      super.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            int row = mouseY / Cell.SIZE;
            int col = mouseX / Cell.SIZE;

            // START RAE
            if (currentState == State.PLAYING) {
               if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                     && board.cells[row][col].content == Seed.NO_SEED) {
                  currentState = board.stepGame(currentPlayer, row, col);
                  repaint();
                  if (currentState == State.PLAYING) {
                     if (gameMode == GameMode.SOLO) {
                        currentPlayer = Seed.NOUGHT;
                        makeBotMove();
                     } else if (gameMode == GameMode.DUO) {
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        startTimerForTurn();
                     }
                  }
               }
            } else {
               newGame();
            }
            // END RAE
            repaint();
         }
      });

      statusBar = new JLabel();
      statusBar.setFont(FONT_STATUS);
      statusBar.setBackground(COLOR_BG_STATUS);
      statusBar.setOpaque(true);
      statusBar.setPreferredSize(new Dimension(300, 30));
      statusBar.setHorizontalAlignment(JLabel.LEFT);
      statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

      timerSelector = new JComboBox<>(new String[]{"Tanpa Batas", "5", "10", "15", "30"});
      timerSelector.setSelectedIndex(1); // Default 5 detik
      timerSelector.addActionListener(e -> {
         String selected = (String) timerSelector.getSelectedItem();
         if (selected.equals("Tanpa Batas")) {
            timerEnabled = false;
            statusBar.setText("Mode: Tanpa Batas Waktu");
         } else {
            timerEnabled = true;
            timeLeft = Integer.parseInt(selected);
         }
      });

      super.setLayout(new BorderLayout());
      super.add(timerSelector, BorderLayout.PAGE_START);
      super.add(statusBar, BorderLayout.PAGE_END);
      super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
      super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

      turnTimer = new Timer(1000, new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            if (!timerEnabled || currentState != State.PLAYING) return;

            timeLeft--;
            statusBar.setText((currentPlayer == Seed.CROSS ? "X" : "O") + "'s Turn - Waktu: " + timeLeft + "s");

            if (timeLeft <= 0) {
               turnTimer.stop();
               if (gameMode == GameMode.SOLO && currentPlayer == Seed.NOUGHT) {
                  makeBotMove(); // bot langsung main
               } else {
                  // Ganti giliran player manual
                  currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
               }
               repaint();
               startTimerForTurn(); // reset untuk giliran selanjutnya
            }
         }
      });
      initGame();
      newGame();
   }
   // START YOSSI
   private void makeBotMove() {
      Random rand = new Random();
      int row, col;

      do {
         row = rand.nextInt(Board.ROWS);
         col = rand.nextInt(Board.COLS);
      } while (board.cells[row][col].content != Seed.NO_SEED);

      board.cells[row][col].content = currentPlayer;
      currentState = board.stepGame(currentPlayer, row, col);

      if (currentState == State.PLAYING) {
         currentPlayer = Seed.CROSS;
         startTimerForTurn(); //Mulai timer setelah bot selesai
      }

      repaint();
   }

   
   private void startTimerForTurn() {
      if (!timerEnabled || currentState != State.PLAYING) {
         if (turnTimer != null) {
            turnTimer.stop();
         }
         return;
      }

      String selected = (String) timerSelector.getSelectedItem();
      if (selected.equals("Tanpa Batas")) {
         timerEnabled = false;
         statusBar.setText((currentPlayer == Seed.CROSS ? "X" : "O") + "'s Turn - ∞");
         if (turnTimer != null) {
            turnTimer.stop();
         }
      } else {
         timerEnabled = true;
         timeLeft = Integer.parseInt(selected);
         statusBar.setText((currentPlayer == Seed.CROSS ? "X" : "O") + "'s Turn - Waktu: " + timeLeft + "s");

         if (turnTimer != null) {
            turnTimer.stop();
         }

         // Mulai ulang timer
         turnTimer = new Timer(1000, e -> {
            timeLeft--;
            if (timeLeft <= 0) {
               ((Timer) e.getSource()).stop();
               statusBar.setText("Waktu habis! Otomatis lewati giliran.");
               if (gameMode == GameMode.DUO) {
                  currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
               } else if (gameMode == GameMode.SOLO && currentPlayer == Seed.CROSS) {
                  currentPlayer = Seed.NOUGHT;
                  makeBotMove();
               }
               startTimerForTurn();
               repaint();
            } else {
               statusBar.setText((currentPlayer == Seed.CROSS ? "X" : "O") + "'s Turn - Waktu: " + timeLeft + "s");
            }
         });
         turnTimer.start();
      }
   }

   // END YOSSI


   public void initGame() {
      board = new Board();
   }

   public void newGame() {
      for (int row = 0; row < Board.ROWS; ++row) {
         for (int col = 0; col < Board.COLS; ++col) {
            board.cells[row][col].content = Seed.NO_SEED;
         }
      }
      currentPlayer = Seed.CROSS;
      currentState = State.PLAYING;
      startTimerForTurn();
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      setBackground(COLOR_BG);
      board.paint(g);

      if (currentState == State.PLAYING) {
         statusBar.setForeground(Color.BLACK);
         statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
      } else if (currentState == State.DRAW) {
         statusBar.setForeground(Color.RED);
         statusBar.setText("It's a Draw! Click to play again.");
      } else if (currentState == State.CROSS_WON) {
         statusBar.setForeground(Color.RED);
         statusBar.setText("'X' Won! Click to play again.");
      } else if (currentState == State.NOUGHT_WON) {
         statusBar.setForeground(Color.RED);
         statusBar.setText("'O' Won! Click to play again.");
      }
   }

   public static void main(String[] args) {
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(new GameMain());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
         }
      });
   }
}
