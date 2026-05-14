package co.turismo.modelo;

public class Reserva {

    private int         id;
    private Turista     turista;
    private Experiencia experiencia;
    private int         numPersonas;
    private double      valorTotal;
    private String      metodoPago;
    private String      voucherId;
    private String      fechaReserva;

    public Reserva(int id, Turista turista, Experiencia experiencia,
                   int numPersonas, double valorTotal,
                   String metodoPago, String voucherId, String fechaReserva) {
        this.id           = id;
        this.turista      = turista;
        this.experiencia  = experiencia;
        this.numPersonas  = numPersonas;
        this.valorTotal   = valorTotal;
        this.metodoPago   = metodoPago;
        this.voucherId    = voucherId;
        this.fechaReserva = fechaReserva;
    }

    public int         getId()           { return id; }
    public Turista     getTurista()      { return turista; }
    public Experiencia getExperiencia()  { return experiencia; }
    public int         getNumPersonas()  { return numPersonas; }
    public double      getValorTotal()   { return valorTotal; }
    public String      getMetodoPago()   { return metodoPago; }
    public String      getVoucherId()    { return voucherId; }
    public String      getFechaReserva() { return fechaReserva; }
}
