package koskosan.dao;

import koskosan.model.Transaksi;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransaksiDAO {

    public List<Transaksi> getAllTransaksi() throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT * FROM transaksi ORDER BY tanggal_bayar DESC")) {
            while (rs.next()) list.add(buildTransaksi(rs));
        }
        return list;
    }

    public List<Transaksi> getTransaksiByPenghuni(int idPenghuni) throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM transaksi WHERE id_penghuni = ? ORDER BY tanggal_bayar DESC")) {
            ps.setInt(1, idPenghuni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(buildTransaksi(rs));
            }
        }
        return list;
    }

    public boolean tambahTransaksi(Transaksi t) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO transaksi(id_penghuni,nama_penghuni,nomor_kamar,jumlah_bayar," +
                 "tanggal_bayar,bulan_tagihan,status,keterangan) VALUES(?,?,?,?,?,?,?,?)")) {
            ps.setInt(1, t.getIdPenghuni());
            ps.setString(2, t.getNamaPenghuni());
            ps.setString(3, t.getNomorKamar());
            ps.setDouble(4, t.getJumlahBayar());
            ps.setString(5, t.getTanggalBayar().toString());
            ps.setString(6, t.getBulanTagihan());
            ps.setString(7, t.getStatus().name());
            ps.setString(8, t.getKeterangan());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hapusTransaksi(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM transaksi WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public double getTotalPendapatan() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT SUM(jumlah_bayar) FROM transaksi WHERE status='LUNAS'")) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0;
    }

    private Transaksi buildTransaksi(ResultSet rs) throws SQLException {
        return new Transaksi(
            rs.getInt("id"),
            rs.getInt("id_penghuni"),
            rs.getString("nama_penghuni"),
            rs.getString("nomor_kamar"),
            rs.getDouble("jumlah_bayar"),
            LocalDate.parse(rs.getString("tanggal_bayar")),
            rs.getString("bulan_tagihan"),
            Transaksi.StatusTransaksi.valueOf(rs.getString("status")),
            rs.getString("keterangan")
        );
    }
}