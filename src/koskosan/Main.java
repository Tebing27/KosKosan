package koskosan;

import koskosan.dao.DatabaseConnection;
import koskosan.gui.MainFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
    // Cegah lebih dari 1 instance berjalan
    try {
        java.io.File lockFile = new java.io.File("koskosan.lock");
        java.nio.channels.FileChannel channel = new java.io.RandomAccessFile(lockFile, "rw").getChannel();
        java.nio.channels.FileLock lock = channel.tryLock();
        if (lock == null) {
            JOptionPane.showMessageDialog(null, "Aplikasi sudah berjalan!");
            System.exit(0);
        }
        // Hapus lock file saat aplikasi ditutup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { lock.release(); channel.close(); lockFile.delete(); } catch (Exception ignored) {}
        }));
    } catch (Exception e) {
        // Lanjut saja jika lock gagal
    }

    // Inisialisasi database
    try {
        DatabaseConnection.initDatabase();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
            "Gagal koneksi database: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    SwingUtilities.invokeLater(() -> {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        new MainFrame().setVisible(true);
    });
}
}
