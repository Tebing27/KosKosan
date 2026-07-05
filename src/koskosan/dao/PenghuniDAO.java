package koskosan.dao;

import koskosan.model.Penghuni;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PenghuniDAO {

    public List<Penghuni> getAllPenghuni() throws SQLException {
        List<Penghuni> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM penghuni ORDER BY nama")) {
            while (rs.next()) {
                list.add(buildPenghuni(rs));
            }
        }
        return list;
    }

    public Map<Integer, Penghuni> getPenghuniMap() throws SQLException {
        Map<Integer, Penghuni> map = new HashMap<>();
        for (Penghuni p : getAllPenghuni()) {
            map.put(p.getId(), p);
        }
        return map;
    }

    public Penghuni getPenghuniById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM penghuni WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return buildPenghuni(rs);
            }
        }
        return null;
    }

    public Penghuni getPenghuniByKamar(String nomorKamar) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM penghuni WHERE nomor_kamar = ?")) {
            ps.setString(1, nomorKamar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return buildPenghuni(rs);
            }
        }
        return null;
    }

    public boolean tambahPenghuni(Penghuni p) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO penghuni(nama,no_ktp,no_telp,nomor_kamar,tanggal_masuk,tanggal_keluar) VALUES(?,?,?,?,?,?)")) {
            ps.setString(1, p.getNama());
            ps.setString(2, p.getNoKtp());
            ps.setString(3, p.getNoTelp());
            ps.setString(4, p.getNomorKamar());
            ps.setString(5, p.getTanggalMasuk()  != null ? p.getTanggalMasuk().toString()  : null);
            ps.setString(6, p.getTanggalKeluar() != null ? p.getTanggalKeluar().toString() : null);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePenghuni(Penghuni p) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE penghuni SET nama=?,no_ktp=?,no_telp=?,nomor_kamar=?,tanggal_masuk=?,tanggal_keluar=? WHERE id=?")) {
            ps.setString(1, p.getNama());
            ps.setString(2, p.getNoKtp());
            ps.setString(3, p.getNoTelp());
            ps.setString(4, p.getNomorKamar());
            ps.setString(5, p.getTanggalMasuk()  != null ? p.getTanggalMasuk().toString()  : null);
            ps.setString(6, p.getTanggalKeluar() != null ? p.getTanggalKeluar().toString() : null);
            ps.setInt(7, p.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hapusPenghuni(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM penghuni WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Penghuni buildPenghuni(ResultSet rs) throws SQLException {
        return new Penghuni(
            rs.getInt("id"),
            rs.getString("nama"),
            rs.getString("no_ktp"),
            rs.getString("no_telp"),
            rs.getString("nomor_kamar"),
            rs.getString("tanggal_masuk")  != null ? LocalDate.parse(rs.getString("tanggal_masuk"))  : null,
            rs.getString("tanggal_keluar") != null ? LocalDate.parse(rs.getString("tanggal_keluar")) : null
        );
    }
}