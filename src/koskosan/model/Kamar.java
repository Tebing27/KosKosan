package koskosan.model;

import koskosan.interface_.Bookable;
import koskosan.interface_.Reportable;
import koskosan.exception.KamarPenuhException;

public abstract class Kamar implements Bookable, Reportable {

    // Inner enum (Inner Class)
    public enum TipeKamar {
        STANDARD("Standard"),
        AC("AC / Ber-AC");

        private final String label;
        TipeKamar(String label) { this.label = label; }
        public String getLabel() { return label; }

        @Override
        public String toString() { return label; }
    }

    // Static Final field
    public static final int MAX_PENGHUNI = 1;

    // Private fields (Enkapsulasi)
    private int id;
    private String nomorKamar;
    private TipeKamar tipeKamar;
    private boolean tersedia;
    private String penghuniSaatIni;

    // Constructor
    public Kamar(int id, String nomorKamar, TipeKamar tipeKamar) {
        this.id = id;
        this.nomorKamar = nomorKamar;
        this.tipeKamar = tipeKamar;
        this.tersedia = true;
        this.penghuniSaatIni = null;
    }

    // Abstract method (Abstraksi) - Polimorfisme
    public abstract double getHarga();

    @Override
    public abstract String getDescription();

    // Bookable implementation
    @Override
    public boolean pesan(String namaPenghuni) throws KamarPenuhException {
        if (!tersedia) {
            throw new KamarPenuhException(nomorKamar);
        }
        this.tersedia = false;
        this.penghuniSaatIni = namaPenghuni;
        return true;
    }

    @Override
    public boolean kosongkan() {
        this.tersedia = true;
        this.penghuniSaatIni = null;
        return true;
    }

    @Override
    public boolean isTersedia() { return tersedia; }

    // Reportable implementation
    @Override
    public String generateLaporan() {
        return String.format("Kamar %s [%s] - Harga: Rp %,.0f - Status: %s%s",
                nomorKamar, tipeKamar.getLabel(), getHarga(),
                tersedia ? "Tersedia" : "Terisi",
                penghuniSaatIni != null ? " (" + penghuniSaatIni + ")" : "");
    }

    // Getters & Setters (Enkapsulasi)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomorKamar() { return nomorKamar; }
    public void setNomorKamar(String nomorKamar) { this.nomorKamar = nomorKamar; }

    public TipeKamar getTipeKamar() { return tipeKamar; }
    public void setTipeKamar(TipeKamar tipeKamar) { this.tipeKamar = tipeKamar; }

    public boolean isTersediaField() { return tersedia; }
    public void setTersedia(boolean tersedia) { this.tersedia = tersedia; }

    public String getPenghuniSaatIni() { return penghuniSaatIni; }
    public void setPenghuniSaatIni(String penghuniSaatIni) { this.penghuniSaatIni = penghuniSaatIni; }

    @Override
    public String toString() {
        return "Kamar " + nomorKamar + " (" + tipeKamar.getLabel() + ")";
    }
}
