/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package koskosan.gui;

import koskosan.dao.KamarDAO;
import koskosan.dao.PenghuniDAO;
import koskosan.exception.KamarPenuhException;
import koskosan.model.Kamar;
import koskosan.model.KamarAC;
import koskosan.model.KamarStandard;
import koskosan.model.Penghuni;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelKamar extends JPanel {

    private KamarDAO kamarDAO = new KamarDAO();
    private PenghuniDAO penghuniDAO = new PenghuniDAO();

    private JTable tabel;
    private DefaultTableModel modelTabel;
    private JButton btnRefresh, btnPesan, btnKosongkan, btnTambah, btnHapus;
    private JTextArea txtDetail;

    public PanelKamar() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
        loadData();
    }

    private void initComponents() {
        // Tabel
        String[] kolom = {"ID", "Nomor Kamar", "Tipe", "Harga/Bulan", "Status", "Penghuni"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.getSelectionModel().addListSelectionListener(e -> tampilkanDetail());
        tabel.getColumnModel().getColumn(0).setMaxWidth(40);

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setPreferredSize(new Dimension(0, 300));

        // Detail panel
        txtDetail = new JTextArea(5, 30);
        txtDetail.setEditable(false);
        txtDetail.setLineWrap(true);
        txtDetail.setWrapStyleWord(true);
        txtDetail.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane scrollDetail = new JScrollPane(txtDetail);
        scrollDetail.setBorder(BorderFactory.createTitledBorder("Detail Kamar"));

        // Tombol
        btnRefresh   = new JButton("🔄 Refresh");
        btnTambah    = new JButton("➕ Tambah Kamar");
        btnPesan     = new JButton("📋 Pesan Kamar");
        btnKosongkan = new JButton("🚪 Kosongkan Kamar");
        btnHapus     = new JButton("🗑 Hapus Kamar");

        btnRefresh.addActionListener(e -> loadData());
        btnTambah.addActionListener(e -> tambahKamar());
        btnPesan.addActionListener(e -> pesanKamar());
        btnKosongkan.addActionListener(e -> kosongkanKamar());
        btnHapus.addActionListener(e -> hapusKamar());

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBtn.add(btnRefresh);
        panelBtn.add(btnTambah);
        panelBtn.add(btnPesan);
        panelBtn.add(btnKosongkan);
        panelBtn.add(btnHapus);

        add(scroll, BorderLayout.CENTER);
        add(panelBtn, BorderLayout.NORTH);
        add(scrollDetail, BorderLayout.SOUTH);
    }

    public void loadData() {
        modelTabel.setRowCount(0);
        try {
            List<Kamar> kamarList = kamarDAO.getAllKamar();
            for (Kamar k : kamarList) {
                modelTabel.addRow(new Object[]{
                    k.getId(),
                    k.getNomorKamar(),
                    k.getTipeKamar().getLabel(),
                    String.format("Rp %,.0f", k.getHarga()),
                    k.isTersedia() ? "✅ Tersedia" : "❌ Terisi",
                    k.getPenghuniSaatIni() != null ? k.getPenghuniSaatIni() : "-"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanDetail() {
        int row = tabel.getSelectedRow();
        if (row < 0) return;
        try {
            String nomor = (String) modelTabel.getValueAt(row, 1);
            Kamar k = kamarDAO.getKamarByNomor(nomor);
            if (k != null) txtDetail.setText(k.getDescription() + "\n\n" + k.generateLaporan());
        } catch (SQLException e) {
            txtDetail.setText("Error: " + e.getMessage());
        }
    }

    private void tambahKamar() {
        JTextField txtNomor = new JTextField();
        String[] tipes = {"STANDARD", "AC"};
        JComboBox<String> cmbTipe = new JComboBox<>(tipes);

        Object[] form = {"Nomor Kamar:", txtNomor, "Tipe Kamar:", cmbTipe};
        int res = JOptionPane.showConfirmDialog(this, form, "Tambah Kamar", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        String nomor = txtNomor.getText().trim();
        if (nomor.isEmpty()) { JOptionPane.showMessageDialog(this, "Nomor kamar tidak boleh kosong!"); return; }

        try {
            Kamar k = "AC".equals(cmbTipe.getSelectedItem()) ? new KamarAC(0, nomor) : new KamarStandard(0, nomor);
            if (kamarDAO.tambahKamar(k)) {
                JOptionPane.showMessageDialog(this, "Kamar " + nomor + " berhasil ditambahkan!");
                loadData();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesanKamar() {
        int row = tabel.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih kamar terlebih dahulu!"); return; }

        String status = (String) modelTabel.getValueAt(row, 4);
        if (status.contains("Terisi")) { JOptionPane.showMessageDialog(this, "Kamar sudah terisi!"); return; }

        String nomorKamar = (String) modelTabel.getValueAt(row, 1);
        String nama = JOptionPane.showInputDialog(this, "Masukkan nama penghuni:", "Pesan Kamar", JOptionPane.QUESTION_MESSAGE);
        if (nama == null || nama.trim().isEmpty()) return;

        try {
            Kamar k = kamarDAO.getKamarByNomor(nomorKamar);
            if (k != null) {
                k.pesan(nama.trim());
                kamarDAO.updateStatusKamar(nomorKamar, false, nama.trim());
                JOptionPane.showMessageDialog(this, "Kamar " + nomorKamar + " berhasil dipesan oleh " + nama);
                loadData();
            }
        } catch (KamarPenuhException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void kosongkanKamar() {
        int row = tabel.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih kamar terlebih dahulu!"); return; }

        String nomorKamar = (String) modelTabel.getValueAt(row, 1);
        int konfirm = JOptionPane.showConfirmDialog(this, "Kosongkan kamar " + nomorKamar + "?");
        if (konfirm != JOptionPane.YES_OPTION) return;

        try {
            kamarDAO.updateStatusKamar(nomorKamar, true, null);
            JOptionPane.showMessageDialog(this, "Kamar " + nomorKamar + " berhasil dikosongkan!");
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusKamar() {
        int row = tabel.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih kamar terlebih dahulu!"); return; }

        int id = (int) modelTabel.getValueAt(row, 0);
        String nomor = (String) modelTabel.getValueAt(row, 1);
        int konfirm = JOptionPane.showConfirmDialog(this, "Hapus kamar " + nomor + "? Data tidak bisa dikembalikan!");
        if (konfirm != JOptionPane.YES_OPTION) return;

        try {
            if (kamarDAO.hapusKamar(id)) {
                JOptionPane.showMessageDialog(this, "Kamar " + nomor + " berhasil dihapus!");
                loadData();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}