package koskosan.model;

import java.time.LocalDate;

public class Transaksi {

    public enum StatusTransaksi {
        LUNAS, BELUM_LUNAS, CICILAN
    }

    // Private fields (Enkapsulasi)
    private int id;
    private int idPenghuni;
    private String namaPenghuni;
    private String nomorKamar;
    private double jumlahBayar;
    private LocalDate tanggalBayar;
    private String bulanTagihan;
    private StatusTransaksi status;
    private String keterangan;

    // Constructor
    public Transaksi(int id, int idPenghuni, String namaPenghuni, String nomorKamar,
                     double jumlahBayar, LocalDate tanggalBayar,
                     String bulanTagihan, StatusTransaksi status, String keterangan) {
        this.id = id;
        this.idPenghuni = idPenghuni;
        this.namaPenghuni = namaPenghuni;
        this.nomorKamar = nomorKamar;
        this.jumlahBayar = jumlahBayar;
        this.tanggalBayar = tanggalBayar;
        this.bulanTagihan = bulanTagihan;
        this.status = status;
        this.keterangan = keterangan;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPenghuni() { return idPenghuni; }
    public void setIdPenghuni(int idPenghuni) { this.idPenghuni = idPenghuni; }

    public String getNamaPenghuni() { return namaPenghuni; }
    public void setNamaPenghuni(String namaPenghuni) { this.namaPenghuni = namaPenghuni; }

    public String getNomorKamar() { return nomorKamar; }
    public void setNomorKamar(String nomorKamar) { this.nomorKamar = nomorKamar; }

    public double getJumlahBayar() { return jumlahBayar; }
    public void setJumlahBayar(double jumlahBayar) { this.jumlahBayar = jumlahBayar; }

    public LocalDate getTanggalBayar() { return tanggalBayar; }
    public void setTanggalBayar(LocalDate tanggalBayar) { this.tanggalBayar = tanggalBayar; }

    public String getBulanTagihan() { return bulanTagihan; }
    public void setBulanTagihan(String bulanTagihan) { this.bulanTagihan = bulanTagihan; }

    public StatusTransaksi getStatus() { return status; }
    public void setStatus(StatusTransaksi status) { this.status = status; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    @Override
    public String toString() {
        return String.format("Transaksi #%d | %s | Kamar %s | Rp %,.0f | %s",
                id, namaPenghuni, nomorKamar, jumlahBayar, status);
    }
}
