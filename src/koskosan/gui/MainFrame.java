package koskosan.gui;

import koskosan.dao.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// GUI Swing - MainFrame with tabs
public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private PanelKamar panelKamar;
    private PanelPenghuni panelPenghuni;
    private PanelTransaksi panelTransaksi;

    public MainFrame() {
        super("🏠 Sistem Manajemen Kos-Kosan");
        initComponents();
        setupFrame();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));

        panelKamar     = new PanelKamar();
        panelPenghuni  = new PanelPenghuni();
        panelTransaksi = new PanelTransaksi();

        tabbedPane.addTab("🛏 Data Kamar",      panelKamar);
        tabbedPane.addTab("👤 Data Penghuni",    panelPenghuni);
        tabbedPane.addTab("💰 Transaksi",        panelTransaksi);

        // Refresh panel saat tab berganti
        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 0) panelKamar.loadData();
            else if (idx == 1) panelPenghuni.loadData();
            else if (idx == 2) panelTransaksi.loadData();
        });

        // Header
        JLabel header = new JLabel("🏠 SISTEM MANAJEMEN KOS-KOSAN", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));

        // Status bar
        JLabel statusBar = new JLabel("  Selamat datang di Sistem Manajemen Kos-Kosan | Java OOP + SQLite");
        statusBar.setFont(new Font("SansSerif", Font.PLAIN, 11));
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        add(header, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    MainFrame.this, "Yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DatabaseConnection.closeConnection();
                    System.exit(0);
                }
            }
        });

        setMinimumSize(new Dimension(900, 600));
        setSize(1000, 680);
        setLocationRelativeTo(null);
    }
}
