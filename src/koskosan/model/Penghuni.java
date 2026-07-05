package koskosan.model;

import java.time.LocalDate;

public class Penghuni {

    // Private fields (Enkapsulasi)
    private int id;
    private String nama;
    private String noKtp;
    private String noTelp;
    private String nomorKamar;
    private LocalDate tanggalMasuk;
    private LocalDate tanggalKeluar;

    // Constructor
    public Penghuni(int id, String nama, String noKtp, String noTelp,
                    String nomorKamar, LocalDate tanggalMasuk, LocalDate tanggalKeluar) {
        this.id = id;
        this.nama = nama;
        this.noKtp = noKtp;
        this.noTelp = noTelp;
        this.nomorKamar = nomorKamar;
        this.tanggalMasuk = tanggalMasuk;
        this.tanggalKeluar = tanggalKeluar;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNoKtp() { return noKtp; }
    public void setNoKtp(String noKtp) { this.noKtp = noKtp; }

    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }

    public String getNomorKamar() { return nomorKamar; }
    public void setNomorKamar(String nomorKamar) { this.nomorKamar = nomorKamar; }

    public LocalDate getTanggalMasuk() { return tanggalMasuk; }
    public void setTanggalMasuk(LocalDate tanggalMasuk) { this.tanggalMasuk = tanggalMasuk; }

    public LocalDate getTanggalKeluar() { return tanggalKeluar; }
    public void setTanggalKeluar(LocalDate tanggalKeluar) { this.tanggalKeluar = tanggalKeluar; }

    @Override
    public String toString() {
        return nama + " (Kamar " + nomorKamar + ")";
    }
}
