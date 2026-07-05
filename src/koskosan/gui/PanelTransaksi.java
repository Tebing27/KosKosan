package koskosan.gui;

import koskosan.dao.PenghuniDAO;
import koskosan.dao.TransaksiDAO;
import koskosan.exception.PembayaranGagalException;
import koskosan.model.Penghuni;
import koskosan.model.Transaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PanelTransaksi extends JPanel {

    private TransaksiDAO transaksiDAO = new TransaksiDAO();
    private PenghuniDAO penghuniDAO = new PenghuniDAO();

    private JTable tabel;
    private DefaultTableModel modelTabel;
    private JLabel lblTotal;

    public PanelTransaksi() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
        loadData();
    }

    private void initComponents() {
        String[] kolom = {"ID", "Penghuni", "Kamar", "Jumlah Bayar", "Tgl Bayar", "Bulan Tagihan", "Status", "Ket."};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.getColumnModel().getColumn(0).setMaxWidth(40);
        JScrollPane scroll = new JScrollPane(tabel);

        JButton btnRefresh = new JButton("🔄 Refresh");
        JButton btnBayar   = new JButton("💳 Catat Pembayaran");
        JButton btnHapus   = new JButton("🗑 Hapus");
        JButton btnLaporan = new JButton("📊 Laporan");

        btnRefresh.addActionListener(e -> loadData());
        btnBayar.addActionListener(e -> catatPembayaran());
        btnHapus.addActionListener(e -> hapusTransaksi());
        btnLaporan.addActionListener(e -> tampilkanLaporan());

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBtn.add(btnRefresh);
        panelBtn.add(btnBayar);
        panelBtn.add(btnHapus);
        panelBtn.add(btnLaporan);

        lblTotal = new JLabel("Total Pendapatan: Rp 0");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(panelBtn, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(lblTotal, BorderLayout.SOUTH);
    }

    public void loadData() {
        modelTabel.setRowCount(0);
        try {
            List<Transaksi> list = transaksiDAO.getAllTransaksi();
            for (Transaksi t : list) {
                modelTabel.addRow(new Object[]{
                    t.getId(), t.getNamaPenghuni(), t.getNomorKamar(),
                    String.format("Rp %,.0f", t.getJumlahBayar()),
                    t.getTanggalBayar(), t.getBulanTagihan(), t.getStatus(), t.getKeterangan()
                });
            }
            double total = transaksiDAO.getTotalPendapatan();
            lblTotal.setText("Total Pendapatan Lunas: Rp " + String.format("%,.0f", total));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void catatPembayaran() {
        try {
            List<Penghuni> listPenghuni = penghuniDAO.getAllPenghuni();
            if (listPenghuni.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Belum ada penghuni terdaftar!");
                return;
            }

            JComboBox<Penghuni> cmbPenghuni = new JComboBox<>(listPenghuni.toArray(new Penghuni[0]));
            JTextField txtJumlah   = new JTextField();
            JTextField txtBulan    = new JTextField(LocalDate.now().getYear() + "-" +
                String.format("%02d", LocalDate.now().getMonthValue()));
            String[] statuses = {"LUNAS", "BELUM_LUNAS", "CICILAN"};
            JComboBox<String> cmbStatus = new JComboBox<>(statuses);
            JTextField txtKet = new JTextField();

            Object[] form = {
                "Penghuni:", cmbPenghuni,
                "Jumlah Bayar (Rp):", txtJumlah,
                "Bulan Tagihan (YYYY-MM):", txtBulan,
                "Status:", cmbStatus,
                "Keterangan:", txtKet
            };

            int res = JOptionPane.showConfirmDialog(this, form, "Catat Pembayaran", JOptionPane.OK_CANCEL_OPTION);
            if (res != JOptionPane.OK_OPTION) return;

            Penghuni p = (Penghuni) cmbPenghuni.getSelectedItem();
            double jumlah;
            try {
                jumlah = Double.parseDouble(txtJumlah.getText().replace(",", "").replace(".", "").trim());
                if (jumlah <= 0) throw new PembayaranGagalException(jumlah);
            } catch (NumberFormatException ex) {
                throw new PembayaranGagalException(0);
            }

            Transaksi t = new Transaksi(0, p.getId(), p.getNama(), p.getNomorKamar(),
                jumlah, LocalDate.now(), txtBulan.getText().trim(),
                Transaksi.StatusTransaksi.valueOf((String) cmbStatus.getSelectedItem()),
                txtKet.getText().trim());

            transaksiDAO.tambahTransaksi(t);
            JOptionPane.showMessageDialog(this, "Pembayaran berhasil dicatat!");
            loadData();

        } catch (PembayaranGagalException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Pembayaran Gagal", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusTransaksi() {
        int row = tabel.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih transaksi terlebih dahulu!"); return; }
        int id = (int) modelTabel.getValueAt(row, 0);
        int konfirm = JOptionPane.showConfirmDialog(this, "Hapus transaksi #" + id + "?");
        if (konfirm != JOptionPane.YES_OPTION) return;
        try {
            transaksiDAO.hapusTransaksi(id);
            JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanLaporan() {
        try {
            double total = transaksiDAO.getTotalPendapatan();
            List<Transaksi> list = transaksiDAO.getAllTransaksi();
            long lunas = list.stream().filter(t -> t.getStatus() == Transaksi.StatusTransaksi.LUNAS).count();
            long belum = list.stream().filter(t -> t.getStatus() == Transaksi.StatusTransaksi.BELUM_LUNAS).count();

            String laporan = String.format("""
                ╔══════════════════════════════════╗
                  LAPORAN KEUANGAN KOS-KOSAN
                ╚══════════════════════════════════╝
                
                Total Transaksi    : %d
                Status Lunas       : %d
                Status Belum Lunas : %d
                
                TOTAL PENDAPATAN   : Rp %,.0f
                """, list.size(), lunas, belum, total);

            JTextArea area = new JTextArea(laporan);
            area.setFont(new Font("Monospaced", Font.PLAIN, 13));
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Laporan Keuangan", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
