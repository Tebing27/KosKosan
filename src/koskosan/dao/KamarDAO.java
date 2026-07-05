package koskosan.dao;

import koskosan.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KamarDAO {

    public List<Kamar> getAllKamar() throws SQLException {
        List<Kamar> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM kamar ORDER BY nomor_kamar")) {
            while (rs.next()) {
                Kamar k = buildKamar(rs);
                if (k != null) list.add(k);
            }
        }
        return list;
    }

    public List<Kamar> getKamarTersedia() throws SQLException {
        List<Kamar> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM kamar WHERE tersedia = 1 ORDER BY nomor_kamar")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Kamar k = buildKamar(rs);
                    if (k != null) list.add(k);
                }
            }
        }
        return list;
    }

    public Kamar getKamarByNomor(String nomor) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM kamar WHERE nomor_kamar = ?")) {
            ps.setString(1, nomor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return buildKamar(rs);
            }
        }
        return null;
    }

    public boolean updateStatusKamar(String nomorKamar, boolean tersedia, String penghuni) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE kamar SET tersedia = ?, penghuni_saat_ini = ? WHERE nomor_kamar = ?")) {
            ps.setInt(1, tersedia ? 1 : 0);
            ps.setString(2, penghuni);
            ps.setString(3, nomorKamar);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean tambahKamar(Kamar kamar) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO kamar(nomor_kamar, tipe_kamar, tersedia) VALUES (?,?,1)")) {
            ps.setString(1, kamar.getNomorKamar());
            ps.setString(2, kamar.getTipeKamar().name());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hapusKamar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM kamar WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Kamar buildKamar(ResultSet rs) throws SQLException {
        int id         = rs.getInt("id");
        String nomor   = rs.getString("nomor_kamar");
        String tipe    = rs.getString("tipe_kamar");
        boolean tersedia = rs.getInt("tersedia") == 1;
        String penghuni  = rs.getString("penghuni_saat_ini");

        Kamar k = "AC".equals(tipe) ? new KamarAC(id, nomor) : new KamarStandard(id, nomor);
        k.setTersedia(tersedia);
        k.setPenghuniSaatIni(penghuni);
        return k;
    }
}