package co.turismo.modelo;


public class Turista {
}


import java.time.LocalDate;
import java.time.Period;

/**
 * =====================================================
 * MODELO: Turista.java
 * =====================================================
 * Clase que representa a un Turista dentro del sistema
 * de reservas de Turismo Rural.
 *
 * Contiene la informacion personal del turista,
 * datos de contacto de emergencia y su estado activo.
 *
 * Implementa logica de negocio como el calculo de edad
 * y la obtencion del nombre completo.
 * =====================================================
 */
public class Turista {

    // =============================================
    // ATRIBUTOS
    // =============================================

    /** Tipo de documento de identidad (CC, TI, CE, Pasaporte, etc.) */
    private String tipoDocumento;

    /** Numero unico del documento de identidad */
    private String numeroDocumento;

    /** Nombres del turista */
    private String nombres;

    /** Apellidos del turista */
    private String apellidos;

    /** Direccion de correo electronico */
    private String correo;

    /** Fecha de nacimiento del turista */
    private LocalDate fechaNacimiento;

    /** Nombre completo del contacto de emergencia */
    private String contactoEmergenciaNombre;

    /** Numero de telefono del contacto de emergencia */
    private String contactoEmergenciaTelefono;

    /**
     * Estado activo del turista.
     * true  = turista activo en el sistema
     * false = turista desactivado (borrado logico)
     */
    private boolean estadoActivo;

    // =============================================
    // CONSTRUCTORES
    // =============================================

    /**
     * Constructor vacio.
     * Necesario para frameworks y para instanciar
     * objetos vacios que se llenan despues con setters.
     */
    public Turista() {
        this.estadoActivo = true; // Por defecto un turista nuevo esta activo
    }

    /**
     * Constructor completo con todos los atributos.
     *
     * @param tipoDocumento              Tipo de documento (CC, TI, CE, etc.)
     * @param numeroDocumento            Numero de documento
     * @param nombres                    Nombres del turista
     * @param apellidos                  Apellidos del turista
     * @param correo                     Correo electronico
     * @param fechaNacimiento            Fecha de nacimiento
     * @param contactoEmergenciaNombre   Nombre del contacto de emergencia
     * @param contactoEmergenciaTelefono Telefono del contacto de emergencia
     * @param estadoActivo               Estado activo del turista
     */
    public Turista(String tipoDocumento, String numeroDocumento, String nombres,
                   String apellidos, String correo, LocalDate fechaNacimiento,
                   String contactoEmergenciaNombre, String contactoEmergenciaTelefono,
                   boolean estadoActivo) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.contactoEmergenciaNombre = contactoEmergenciaNombre;
        this.contactoEmergenciaTelefono = contactoEmergenciaTelefono;
        this.estadoActivo = estadoActivo;
    }

    // =============================================
    // GETTERS Y SETTERS
    // =============================================

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getContactoEmergenciaNombre() {
        return contactoEmergenciaNombre;
    }

    public void setContactoEmergenciaNombre(String contactoEmergenciaNombre) {
        this.contactoEmergenciaNombre = contactoEmergenciaNombre;
    }

    public String getContactoEmergenciaTelefono() {
        return contactoEmergenciaTelefono;
    }

    public void setContactoEmergenciaTelefono(String contactoEmergenciaTelefono) {
        this.contactoEmergenciaTelefono = contactoEmergenciaTelefono;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }

    public void setEstadoActivo(boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

    // =============================================
    // METODOS DE NEGOCIO
    // =============================================

    /**
     * Calcula la edad actual del turista basandose en su
     * fecha de nacimiento y la fecha actual del sistema.
     *
     * Utiliza la clase {@link Period} de Java para calcular
     * la diferencia exacta en anos entre ambas fechas.
     *
     * @return Edad en anos. Retorna 0 si la fecha de nacimiento es null
     *         o si es una fecha futura.
     */
    public int calcularEdad() {
        if (fechaNacimiento == null) {
            return 0;
        }
        Period periodo = Period.between(fechaNacimiento, LocalDate.now());
        int edad = periodo.getYears();
        // Si la edad es negativa (fecha futura), retornar 0
        return Math.max(edad, 0);
    }

    /**
     * Retorna el nombre completo del turista concatenando
     * nombres y apellidos separados por un espacio.
     *
     * @return Nombre completo en formato: "nombres apellidos"
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    // =============================================
    // METODO toString (util para depuracion)
    // =============================================

    @Override
    public String toString() {
        return "Turista{" +
                "tipoDocumento='" + tipoDocumento + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correo='" + correo + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", contactoEmergenciaNombre='" + contactoEmergenciaNombre + '\'' +
                ", contactoEmergenciaTelefono='" + contactoEmergenciaTelefono + '\'' +
                ", estadoActivo=" + estadoActivo +
                '}';
    }
}


