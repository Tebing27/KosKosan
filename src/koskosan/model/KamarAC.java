package koskosan.model;

// Pewarisan (Inheritance)
public class KamarAC extends Kamar {

    private static final double HARGA_AC = 750_000.0;

    public KamarAC(int id, String nomorKamar) {
        super(id, nomorKamar, TipeKamar.AC);
    }

    // Polimorfisme - override
    @Override
    public double getHarga() {
        return HARGA_AC;
    }

    @Override
    public String getDescription() {
        return "Kamar ber-AC. Fasilitas: kasur, lemari, meja belajar, AC, kamar mandi dalam. " +
               "Harga: Rp " + String.format("%,.0f", getHarga()) + "/bulan.";
    }
}
