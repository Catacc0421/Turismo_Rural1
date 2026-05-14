package co.turismo.modelo;

public class Experiencia {

    private int    id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int    cuposDisponibles;
    private String fecha;
    private String anfitrion;

    public Experiencia(int id, String nombre, String descripcion,
                       double precio, int cuposDisponibles,
                       String fecha, String anfitrion) {
        this.id               = id;
        this.nombre           = nombre;
        this.descripcion      = descripcion;
        this.precio           = precio;
        this.cuposDisponibles = cuposDisponibles;
        this.fecha            = fecha;
        this.anfitrion        = anfitrion;
    }

    public int    getId()                { return id; }
    public String getNombre()            { return nombre; }
    public String getDescripcion()       { return descripcion; }
    public double getPrecio()            { return precio; }
    public int    getCuposDisponibles()  { return cuposDisponibles; }
    public void   setCuposDisponibles(int v) { this.cuposDisponibles = v; }
    public String getFecha()             { return fecha; }
    public String getAnfitrion()         { return anfitrion; }

    @Override
    public String toString() {
        return nombre + " — $" + String.format("%,.0f", precio)
                + " (" + cuposDisponibles + " cupos) | " + fecha;
    }
}
