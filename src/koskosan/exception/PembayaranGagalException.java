package koskosan.exception;

public class PembayaranGagalException extends Exception {
    private double jumlah;

    public PembayaranGagalException(double jumlah) {
        super("Pembayaran sebesar Rp " + String.format("%,.0f", jumlah) + " gagal diproses!");
        this.jumlah = jumlah;
    }

    public double getJumlah() { return jumlah; }
}
