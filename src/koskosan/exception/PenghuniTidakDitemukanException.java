package koskosan.exception;

public class PenghuniTidakDitemukanException extends Exception {
    private String idPenghuni;

    public PenghuniTidakDitemukanException(String idPenghuni) {
        super("Penghuni dengan ID " + idPenghuni + " tidak ditemukan!");
        this.idPenghuni = idPenghuni;
    }

    public String getIdPenghuni() { return idPenghuni; }
}
