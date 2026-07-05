package koskosan.interface_;

public interface Bookable {
    boolean pesan(String namaPenghuni) throws koskosan.exception.KamarPenuhException;
    boolean kosongkan();
    boolean isTersedia();
}
