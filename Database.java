import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private String username;
    private String password;
    private int port;
    private String host;
    private String databaseName;
    private String jdbcURL;
    private int loggedInUserId = -1;
    public Database(String username, String password, int port, String host, String databaseName) {
        this.username = username;
        this.password = password;
        this.port = port;
        this.host = host;
        this.databaseName = databaseName;
        this.jdbcURL = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.databaseName + "?useSSL=true&requireSSL=true";
    }

    public Connection connect() {
        try {
            return DriverManager.getConnection(this.jdbcURL, this.username, this.password);
        } catch (SQLException e) {
            System.out.println("Gagal konek: " + e.getMessage());
            return null;
        }
    }
    public boolean checkLogin(String username, String password) {
        String sql = "SELECT user_id FROM user WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(this.jdbcURL, this.username, this.password);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                loggedInUserId = rs.getInt("user_id"); // Simpan ID
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    public boolean insertUser(String username, String password) {
        String sql = "INSERT INTO `defaultdb`.`user` (`username`, `password`) VALUES (?, ?)" ;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); 

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gagal insert user: " + e.getMessage());
            return false;
        }
    }
    // Tambah 1 ke kolom solo_win
    public void incrementSoloWin(int userId) {
        incrementColumn("solo_win", userId);
    }

    // Tambah 1 ke kolom solo_draw
    public void incrementSoloDraw(int userId) {
        incrementColumn("solo_draw", userId);
    }

    // Tambah 1 ke kolom solo_lose
    public void incrementSoloLose(int userId) {
        incrementColumn("solo_lose", userId);
    }

    // Tambah 1 ke kolom duo_win
    public void incrementDuoWin(int userId) {
        incrementColumn("duo_win", userId);
    }

    // Tambah 1 ke kolom duo_draw
    public void incrementDuoDraw(int userId) {
        incrementColumn("duo_draw", userId);
    }

    // Tambah 1 ke kolom duo_lose
    public void incrementDuoLose(int userId) {
        incrementColumn("duo_lose", userId);
    }

    // Fungsi umum untuk update kolom apa pun
    private void incrementColumn(String columnName, int userId) {
        String sql = "UPDATE `defaultdb`.`user` SET " + columnName + " = " + columnName + " + 1 WHERE user_id = ?";
        try (Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Gagal update " + columnName + ": " + e.getMessage());
        }
    }
    public UserStats getUserStats(int userId) {
        try (Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT solo_win, solo_lose, solo_draw, duo_win, duo_lose, duo_draw FROM user WHERE user_id = ?")) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserStats(
                    rs.getInt("solo_win"),
                    rs.getInt("solo_lose"),
                    rs.getInt("solo_draw"),
                    rs.getInt("duo_win"),
                    rs.getInt("duo_lose"),
                    rs.getInt("duo_draw")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getLoggedInUserId() {
        return this.loggedInUserId;
    }
    public String getUsername(){
        return this.username;
    }
}
