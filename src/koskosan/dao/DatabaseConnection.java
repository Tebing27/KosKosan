package koskosan.dao;
import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:koskosan.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver tidak ditemukan: " + e.getMessage());
        }

        Connection conn = DriverManager.getConnection(DB_URL);

        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA journal_mode=DELETE"); // ← ganti WAL ke DELETE
            st.execute("PRAGMA busy_timeout=5000");
            st.execute("PRAGMA locking_mode=NORMAL");
        }

        return conn;
    }

    public static void initDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS kamar (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nomor_kamar TEXT NOT NULL UNIQUE,
                    tipe_kamar TEXT NOT NULL,
                    tersedia INTEGER NOT NULL DEFAULT 1,
                    penghuni_saat_ini TEXT
                )
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS penghuni (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nama TEXT NOT NULL,
                    no_ktp TEXT NOT NULL UNIQUE,
                    no_telp TEXT,
                    nomor_kamar TEXT,
                    tanggal_masuk TEXT,
                    tanggal_keluar TEXT
                )
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS transaksi (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_penghuni INTEGER NOT NULL,
                    nama_penghuni TEXT NOT NULL,
                    nomor_kamar TEXT NOT NULL,
                    jumlah_bayar REAL NOT NULL,
                    tanggal_bayar TEXT NOT NULL,
                    bulan_tagihan TEXT NOT NULL,
                    status TEXT NOT NULL,
                    keterangan TEXT,
                    FOREIGN KEY (id_penghuni) REFERENCES penghuni(id)
                )
            """);

            // Seed data kamar jika kosong
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM kamar")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String[] kamarStd = {"101", "102", "103", "104", "105"};
                    String[] kamarAC  = {"201", "202", "203", "204", "205"};
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO kamar(nomor_kamar, tipe_kamar, tersedia) VALUES (?,?,1)")) {
                        for (String k : kamarStd) {
                            ps.setString(1, k); ps.setString(2, "STANDARD"); ps.executeUpdate();
                        }
                        for (String k : kamarAC) {
                            ps.setString(1, k); ps.setString(2, "AC"); ps.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    public static void closeConnection() {}
}