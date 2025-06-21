public class UserStats {
    private int soloWin, soloLose, soloDraw;
    private int duoWin, duoLose, duoDraw;

    public UserStats(int sw, int sl, int sd, int dw, int dl, int dd) {
        this.soloWin = sw;
        this.soloLose = sl;
        this.soloDraw = sd;
        this.duoWin = dw;
        this.duoLose = dl;
        this.duoDraw = dd;
    }

    public int getSoloWin() { return soloWin; }
    public int getSoloLose() { return soloLose; }
    public int getSoloDraw() { return soloDraw; }
    public int getSoloGame(){return soloDraw + soloLose + soloWin;}
    public int getDuoWin() { return duoWin; }
    public int getDuoLose() { return duoLose; }
    public int getDuoDraw() { return duoDraw; }
    public int getDuoGame() { return duoWin + duoDraw + duoLose;}
}
