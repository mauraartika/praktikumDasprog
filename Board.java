import java.awt.*;

/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
   // Define named constants
   public static final int ROWS = 3;  // ROWS x COLS cells
   public static final int COLS = 3;
   // Define named constants for drawing
   public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
   public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
   public static final int GRID_WIDTH = 8;  // Grid-line's width
   public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
   public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
   public static final int Y_OFFSET = 1;  // Fine tune for better display
   private Database db;
   private int userId;
   int win_bot = 0;
   int lose_bot = 0;

   // Define properties (package-visible)
   /** Composes of 2D array of ROWS-by-COLS Cell instances */
   Cell[][] cells;

   /** Constructor to initialize the game board */
   public Board(Database db, int userId) {
      this.db = db;
      this.userId = userId;
      initGame();
   }

   /** Initialize the game objects (run once) */
   public void initGame() {
      cells = new Cell[ROWS][COLS]; // allocate the array
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            // Allocate element of the array
            cells[row][col] = new Cell(row, col);
               // Cells are initialized in the constructor
         }
      }
   }

   /** Reset the game board, ready for new game */
   public void newGame() {
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            cells[row][col].newGame(); // clear the cell content
         }
      }
   }

   /**
    *  The given player makes a move on (selectedRow, selectedCol).
    *  Update cells[selectedRow][selectedCol]. Compute and return the
    *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
    */
  public State stepGame(Seed player, int selectedRow, int selectedCol, boolean botGame) {
    cells[selectedRow][selectedCol].content = player;

    boolean win = (
        cells[selectedRow][0].content == player && cells[selectedRow][1].content == player && cells[selectedRow][2].content == player
        || cells[0][selectedCol].content == player && cells[1][selectedCol].content == player && cells[2][selectedCol].content == player
        || selectedRow == selectedCol && cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player
        || selectedRow + selectedCol == 2 && cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player
    );

    if (win && botGame) {
        if (db != null) {
            if (player == Seed.CROSS) {
                db.incrementSoloWin(userId);
            } else {
                db.incrementSoloLose(userId);
            }
        }
        return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
    }
    if (win && !botGame) {
        if (db != null) {
            if (player == Seed.CROSS) {
                db.incrementDuoWin(userId);
            } else {
                db.incrementDuoLose(userId);
            }
        }
        return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
    }

    for (int row = 0; row < ROWS; ++row) {
        for (int col = 0; col < COLS; ++col) {
            if (cells[row][col].content == Seed.NO_SEED) {
                return State.PLAYING;
            }
        }
    }

   if(botGame){
      db.incrementSoloDraw(userId);
   }else{
      db.incrementDuoDraw(userId);
   }
    return State.DRAW;
}


   /** Paint itself on the graphics canvas, given the Graphics context */
   public void paint(Graphics g) {
      // Draw the grid-lines
      g.setColor(COLOR_GRID);
      for (int row = 1; row < ROWS; ++row) {
         g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
               CANVAS_WIDTH - 1, GRID_WIDTH,
               GRID_WIDTH, GRID_WIDTH);
      }
      for (int col = 1; col < COLS; ++col) {
         g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
               GRID_WIDTH, CANVAS_HEIGHT - 1,
               GRID_WIDTH, GRID_WIDTH);
      }

      // Draw all the cells
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            cells[row][col].paint(g);  // ask the cell to paint itself
         }
      }
   }
}