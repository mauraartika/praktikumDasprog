import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import io.github.cdimascio.dotenv.Dotenv;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;
    
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(187, 216, 163);
    public static final Color COLOR_NOUGHT = new Color(111, 130, 106);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private GameMode gameMode;
    
    private Database db;
    private int userId;

    private PionShape player1Shape;
    private PionShape player2Shape;

    private JLabel statusBar;
    private Timer turnTimer;
    private int timeLeft;
    private int timerSetting;
    private JButton backButton;
    
    public GameMain(String username,Database db, int userId) {
        this.db = db;
        this.userId = userId;
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
        
        if (choice == -1) {
            System.exit(0);
        }
        gameMode = (choice == 0) ? GameMode.SOLO : GameMode.DUO;



        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        TimerSetupDialog timerDialog = new TimerSetupDialog(parentFrame);
        this.timerSetting = timerDialog.showDialog();
        if (this.timerSetting == 0) {
            this.timerSetting = -1;
        }


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS &&
                        board.cells[row][col].content == Seed.NO_SEED) {
                            
                        currentState = board.stepGame(currentPlayer, row, col, gameMode == GameMode.SOLO);

                        if (currentState == State.PLAYING) {
                            if (gameMode == GameMode.SOLO) {
                                currentPlayer = Seed.NOUGHT;
                                makeBotMove();
                            } else {
                                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                                startTimerForTurn();
                            }
                        } else {
                            PlaySound.stopGameSound();
                            if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                                PlaySound.playWinSound();
                            } else if (currentState == State.DRAW) {
                                PlaySound.playDrawSound();
                            }
                        }
                    }
                } else {
                    newGame();
                }
                repaint();
            }
        });

        backButton = new JButton("Back"); 
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setFocusable(false);
        backButton.setBackground(Color.RED); 
        backButton.setForeground(Color.WHITE);
        
        statusBar = new JLabel("Selamat Datang!");
        statusBar.setFont(FONT_STATUS);
        statusBar.setOpaque(false);
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(COLOR_BG_STATUS); 
        bottomPanel.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, 35)); 

        bottomPanel.add(statusBar, BorderLayout.CENTER); 
        bottomPanel.add(backButton, BorderLayout.EAST);  

        setLayout(new BorderLayout());
        add(bottomPanel, BorderLayout.PAGE_END);
        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 35)); 
        setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));
        backButton.addActionListener(e -> { 
            if (turnTimer != null) {
                turnTimer.stop();
            }
            PlaySound.stopGameSound();

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this); 
        
            if (frame != null) { 
                frame.dispose(); 
            } 

            SwingUtilities.invokeLater(() -> { 
                Home hm = new Home(); 
                hm.displayHome(username ,db, userId);  
            }); 
        });
        initGame();
        newGame();
    }
    

    private void makeBotMove() {
        Timer botDelay = new Timer(500, e -> {
            Random rand = new Random();
            int row, col;
            do {
                row = rand.nextInt(Board.ROWS);
                col = rand.nextInt(Board.COLS);
            } while (board.cells[row][col].content != Seed.NO_SEED);

            currentState = board.stepGame(currentPlayer, row, col, true);
            currentPlayer = Seed.CROSS;
            startTimerForTurn();
            repaint();
        });
        botDelay.setRepeats(false);
        botDelay.start();
    }

    private void startTimerForTurn() {
        if (turnTimer != null) {
            turnTimer.stop();
        }
 
        if (timerSetting <= 0 || currentState != State.PLAYING) {
            updateStatusText();
            return;
        }

        timeLeft = timerSetting; 
        updateStatusText();

        turnTimer = new Timer(1000, e -> {
            timeLeft--;
            updateStatusText();
            if (timeLeft <= 0) {
                ((Timer)e.getSource()).stop();
                JOptionPane.showMessageDialog(this, "Waktu habis! Giliran dilewati.", "Waktu Habis", JOptionPane.WARNING_MESSAGE);
                
                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                
                if (gameMode == GameMode.SOLO) {
                    makeBotMove();
                } else {
                    startTimerForTurn();
                    repaint();
                }
            }
        });
        turnTimer.start();
    }
    
    public void initGame() {
        board = new Board(db, userId);
    }

    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;

        PlaySound.playGameStartSound();
        startTimerForTurn();
 
        repaint();
    }

    public void setPlayerShapes(PionShape player1, PionShape player2) {
        this.player1Shape = player1;
        this.player2Shape = player2;
        if (board != null) {
            board.setShape(player1, player2);
        }
    }
    
    private void updateStatusText() {
        String text;
        if (currentState == State.PLAYING) {
            text = (currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn";
            if (timerSetting > 0) {
                text += " | Waktu: " + timeLeft + "s";
            }
        } else {
             switch (currentState) {
                case DRAW:       text = "Seri! Klik untuk main lagi."; break;
                case CROSS_WON:  text = "'X' Menang! Klik untuk main lagi."; break;
                case NOUGHT_WON: text = "'O' Menang! Klik untuk main lagi."; break;
                default:         text = ""; break;
            }
        }
        statusBar.setText(text);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g, player1Shape, player2Shape);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
        } else {
            statusBar.setForeground(Color.RED);
        }
        updateStatusText();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Dotenv dotenv = Dotenv.load();
                Database db = new Database(
                        dotenv.get("DB_USER", "root"),
                        dotenv.get("DB_PASSWORD", ""),
                        Integer.parseInt(dotenv.get("DB_PORT", "3306")),
                        dotenv.get("DB_HOST", "localhost"),
                        dotenv.get("DB_NAME", "tic_tac_toe")
                );
                LoginPage lg = new LoginPage();
                lg.displayLogin(db);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan fatal saat memulai aplikasi.\n" + e.getMessage(), "Application Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}