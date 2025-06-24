import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
public class Board {
    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;
    public static final int Y_OFFSET = 1;
    
    private Database db;
    private int userId;
    private PionShape player1Shape;
    private PionShape player2Shape;
    private Timer turnTimer; 
    public Cell[][] cells; 
    public void setShape(PionShape p1, PionShape p2) {
      this.player1Shape = p1;
      this.player2Shape = p2;
   }

    public Board(Database db, int userId, Timer turnTimer) {
        this.db = db;
        this.userId = userId;
        this.turnTimer = turnTimer;
        initGame();
    }
    

    public void initGame() {
        cells = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    public void reset() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame();
            }
        }
    }

    public State stepGame(Seed player, int selectedRow, int selectedCol, boolean botGame) {
        cells[selectedRow][selectedCol].content = player;

        boolean win = (
            cells[selectedRow][0].content == player && cells[selectedRow][1].content == player && cells[selectedRow][2].content == player
            || cells[0][selectedCol].content == player && cells[1][selectedCol].content == player && cells[2][selectedCol].content == player
            || selectedRow == selectedCol && cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player
            || selectedRow + selectedCol == 2 && cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player
        );

        if (win) {
            updateStatisticsInBackground(player, botGame, false);
            if(turnTimer != null){
                turnTimer.stop();
            }
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            
        }

        boolean isDraw = true;
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    isDraw = false;
                    break;
                }
            }
            if(!isDraw) break;
        }
        
        if (isDraw) {
            updateStatisticsInBackground(player, botGame, true); 
            return State.DRAW;
        }

        return State.PLAYING;
    }
    
    /**
     @param player
     @param isBotGame 
     @param isDraw 
     */
    private void updateStatisticsInBackground(final Seed player, final boolean isBotGame, final boolean isDraw) {
        if (db == null) return;
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (isDraw) {
                    if (isBotGame) db.incrementSoloDraw(userId);
                    else db.incrementDuoDraw(userId);
                } else { 
                    if (isBotGame) {
                        if (player == Seed.CROSS) db.incrementSoloWin(userId);
                        else db.incrementSoloLose(userId);
                    } else {
                        if (player == Seed.CROSS) db.incrementDuoWin(userId);
                        else db.incrementDuoLose(userId);
                    }
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get(); 
                } catch (Exception e) {
                    System.err.println("Gagal mengupdate statistik ke database:");
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    public void paint(Graphics g, PionShape player1Shape, PionShape player2Shape) {
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF, CANVAS_WIDTH - 1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET, GRID_WIDTH, CANVAS_HEIGHT - 1, GRID_WIDTH, GRID_WIDTH);
        }
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g, player1Shape, player2Shape);
            }
        }
    }

    public boolean isValidCell(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    public int[] findRandomEmptyCell() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    emptyCells.add(new int[]{row, col});
                }
            }
        }
        if (emptyCells.isEmpty()) return null;
        Random rand = new Random();
        int randomIndex = rand.nextInt(emptyCells.size());
        return emptyCells.get(randomIndex);
    }
}