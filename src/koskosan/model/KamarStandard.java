package koskosan.model;

// Pewarisan (Inheritance)
public class KamarStandard extends Kamar {

    private static final double HARGA_STANDARD = 500_000.0;

    public KamarStandard(int id, String nomorKamar) {
        super(id, nomorKamar, TipeKamar.STANDARD);
    }

    // Polimorfisme - override
    @Override
    public double getHarga() {
        return HARGA_STANDARD;
    }

    @Override
    public String getDescription() {
        return "Kamar Standard tanpa AC. Fasilitas: kasur, lemari, meja belajar. " +
               "Harga: Rp " + String.format("%,.0f", getHarga()) + "/bulan.";
    }
}
