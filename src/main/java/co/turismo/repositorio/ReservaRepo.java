package co.turismo.repositorio;

import co.turismo.modelo.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.concurrent.atomic.AtomicInteger;

public class ReservaRepo {

    private static final ReservaRepo INSTANCIA = new ReservaRepo();
    public static ReservaRepo get() { return INSTANCIA; }

    private final ObservableList<Reserva> reservas =
            FXCollections.observableArrayList();
    private final AtomicInteger contador = new AtomicInteger(1);

    public void crear(Reserva r)              { reservas.add(r); }
    public int  siguienteId()                 { return contador.getAndIncrement(); }
    public ObservableList<Reserva> getReservas() { return reservas; }
}
