package koskosan.gui;

import koskosan.dao.KamarDAO;
import koskosan.dao.PenghuniDAO;
import koskosan.exception.KamarPenuhException;
import koskosan.exception.PenghuniTidakDitemukanException;
import koskosan.model.Kamar;
import koskosan.model.Penghuni;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PanelPenghuni extends JPanel {

    private PenghuniDAO penghuniDAO = new PenghuniDAO();
    private KamarDAO kamarDAO = new KamarDAO();

    private JTable tabel;
    private DefaultTableModel modelTabel;

    public PanelPenghuni() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
        loadData();
    }

    private void initComponents() {
        String[] kolom = {"ID", "Nama", "No. KTP", "No. Telp", "Kamar", "Tgl Masuk", "Tgl Keluar"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.getColumnModel().getColumn(0).setMaxWidth(40);

        JScrollPane scroll = new JScrollPane(tabel);

        JButton btnRefresh  = new JButton("🔄 Refresh");
        JButton btnTambah   = new JButton("➕ Tambah Penghuni");
        JButton btnEdit     = new JButton("✏️ Edit");
        JButton btnHapus    = new JButton("🗑 Hapus");
        JButton btnCari     = new JButton("🔍 Cari");

        btnRefresh.addActionListener(e -> loadData());
        btnTambah.addActionListener(e -> tambahPenghuni());
        btnEdit.addActionListener(e -> editPenghuni());
        btnHapus.addActionListener(e -> hapusPenghuni());
        btnCari.addActionListener(e -> cariPenghuni());

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBtn.add(btnRefresh);
        panelBtn.add(btnTambah);
        panelBtn.add(btnEdit);
        panelBtn.add(btnHapus);
        panelBtn.add(btnCari);

        add(scroll, BorderLayout.CENTER);
        add(panelBtn, BorderLayout.NORTH);
    }

    public void loadData() {
        modelTabel.setRowCount(0);
        try {
            List<Penghuni> list = penghuniDAO.getAllPenghuni();
            for (Penghuni p : list) {
                modelTabel.addRow(new Object[]{
                    p.getId(), p.getNama(), p.getNoKtp(), p.getNoTelp(),
                    p.getNomorKamar(),
                    p.getTanggalMasuk() != null ? p.getTanggalMasuk().toString() : "-",
                    p.getTanggalKeluar() != null ? p.getTanggalKeluar().toString() : "-"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tambahPenghuni() {
        JTextField txtNama   = new JTextField();
        JTextField txtKtp    = new JTextField();
        JTextField txtTelp   = new JTextField();
        JTextField txtKamar  = new JTextField();
        JTextField txtMasuk  = new JTextField(LocalDate.now().toString());
        JTextField txtKeluar = new JTextField(LocalDate.now().plusMonths(1).toString());

        Object[] form = {
            "Nama:", txtNama, "No. KTP:", txtKtp, "No. Telp:", txtTelp,
            "Nomor Kamar:", txtKamar, "Tgl Masuk (YYYY-MM-DD):", txtMasuk,
            "Tgl Keluar (YYYY-MM-DD):", txtKeluar
        };

        int res = JOptionPane.showConfirmDialog(this, form, "Tambah Penghuni", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            String nomorKamar = txtKamar.getText().trim();
            Kamar kamar = kamarDAO.getKamarByNomor(nomorKamar);
            if (kamar == null) { JOptionPane.showMessageDialog(this, "Nomor kamar tidak ditemukan!"); return; }
            if (!kamar.isTersedia()) {
                throw new KamarPenuhException(nomorKamar);
            }

            Penghuni p = new Penghuni(0,
                txtNama.getText().trim(), txtKtp.getText().trim(), txtTelp.getText().trim(),
                nomorKamar, LocalDate.parse(txtMasuk.getText().trim()),
                LocalDate.parse(txtKeluar.getText().trim()));

            penghuniDAO.tambahPenghuni(p);
            kamarDAO.updateStatusKamar(nomorKamar, false, txtNama.getText().trim());

            JOptionPane.showMessageDialog(this, "Penghuni berhasil ditambahkan!");
            loadData();
        } catch (KamarPenuhException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Kamar Penuh", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editPenghuni() {
        int row = tabel.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih penghuni terlebih dahulu!"); return; }
        int id = (int) modelTabel.getValueAt(row, 0);

        try {
            Penghuni p = penghuniDAO.getPenghuniById(id);
            if (p == null) throw new PenghuniTidakDitemukanException(String.valueOf(id));

            JTextField txtNama  = new JTextField(p.getNama());
            JTextField txtKtp   = new JTextField(p.getNoKtp());
            JTextField txtTelp  = new JTextField(p.getNoTelp());
            JTextField txtKeluar = new JTextField(p.getTanggalKeluar() != null ? p.getTanggalKeluar().toString() : "");

            Object[] form = {"Nama:", txtNama, "No. KTP:", txtKtp, "No. Telp:", txtTelp,
                             "Tgl Keluar (YYYY-MM-DD):", txtKeluar};
            int res = JOptionPane.showConfirmDialog(this, form, "Edit Penghuni", JOptionPane.OK_CANCEL_OPTION);
            if (res != JOptionPane.OK_OPTION) return;

            p.setNama(txtNama.getText().trim());
            p.setNoKtp(txtKtp.getText().trim());
            p.setNoTelp(txtTelp.getText().trim());
            if (!txtKeluar.getText().isBlank()) p.setTanggalKeluar(LocalDate.parse(txtKeluar.getText().trim()));

            penghuniDAO.updatePenghuni(p);
            JOptionPane.showMessageDialog(this, "Data penghuni berhasil diupdate!");
            loadData();
        } catch (PenghuniTidakDitemukanException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusPenghuni() {
        int row = tabel.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih penghuni terlebih dahulu!"); return; }

        int id = (int) modelTabel.getValueAt(row, 0);
        String nama = (String) modelTabel.getValueAt(row, 1);
        String kamar = (String) modelTabel.getValueAt(row, 4);

        int konfirm = JOptionPane.showConfirmDialog(this, "Hapus penghuni " + nama + "?");
        if (konfirm != JOptionPane.YES_OPTION) return;

        try {
            penghuniDAO.hapusPenghuni(id);
            if (kamar != null && !kamar.isEmpty()) {
                kamarDAO.updateStatusKamar(kamar, true, null);
            }
            JOptionPane.showMessageDialog(this, "Penghuni " + nama + " berhasil dihapus!");
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariPenghuni() {
        String keyword = JOptionPane.showInputDialog(this, "Cari nama penghuni:", "Cari", JOptionPane.QUESTION_MESSAGE);
        if (keyword == null || keyword.trim().isEmpty()) { loadData(); return; }

        modelTabel.setRowCount(0);
        try {
            List<Penghuni> list = penghuniDAO.getAllPenghuni();
            for (Penghuni p : list) {
                if (p.getNama().toLowerCase().contains(keyword.toLowerCase())) {
                    modelTabel.addRow(new Object[]{
                        p.getId(), p.getNama(), p.getNoKtp(), p.getNoTelp(),
                        p.getNomorKamar(),
                        p.getTanggalMasuk() != null ? p.getTanggalMasuk().toString() : "-",
                        p.getTanggalKeluar() != null ? p.getTanggalKeluar().toString() : "-"
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
