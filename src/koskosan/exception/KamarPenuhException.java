package koskosan.exception;

public class KamarPenuhException extends Exception {
    private String nomorKamar;

    public KamarPenuhException(String nomorKamar) {
        super("Kamar " + nomorKamar + " sudah penuh/terisi!");
        this.nomorKamar = nomorKamar;
    }

    public String getNomorKamar() { return nomorKamar; }
}
