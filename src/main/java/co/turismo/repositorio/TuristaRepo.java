package co.turismo.repositorio;


public class TuristaRepo {
}

import co.turismo.modelo.Turista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * =====================================================
 * REPOSITORIO: TuristaRepo.java
 * =====================================================
 * Clase que gestiona el almacenamiento y la logica CRUD
 * de los objetos Turista usando una ObservableList.
 *
 * ¿Por que ObservableList?
 * -----------------------
 * ObservableList es una coleccion especial de JavaFX que
 * permite que la vista (TableView) se actualice AUTOMATICAMENTE
 * cuando se agrega, elimina o modifica un elemento en la lista.
 * Esto elimina la necesidad de refrescar manualmente la tabla.
 *
 * Borrado Logico:
 * ---------------
 * En lugar de eliminar fisicamente un turista de la lista,
 * se cambia su estadoActivo a false. Esto permite:
 * - Conservar el historial de turistas
 * - Poder reactivar turistas en el futuro
 * - Mantener la integridad referencial con otras entidades
 *   (reservas, pagos, etc. que puedan referenciar al turista)
 *
 * Validaciones:
 * -------------
 * Antes de agregar un turista se verifica que:
 * - El numero de documento no exista previamente
 * - El correo electronico no exista previamente
 * Estas validaciones previenen duplicados en el sistema.
 * =====================================================
 */
public class TuristaRepo {

    // =============================================
    // ATRIBUTO PRINCIPAL: Lista Observable
    // =============================================

    /**
     * Lista observable que almacena todos los turistas.
     * Cualquier cambio en esta lista se refleja automaticamente
     * en los componentes JavaFX que la esten observando
     * (como el TableView).
     */
    private final ObservableList<Turista> turistas;

    // =============================================
    // CONSTRUCTOR
    // =============================================

    /**
     * Constructor del repositorio.
     * Inicializa la ObservableList y la carga con datos
     * de prueba quemados en memoria para propositos de
     * demostracion y pruebas del CRUD.
     *
     * Los datos de prueba simulan turistas reales con
     * informacion tipica del contexto colombiano.
     */
    public TuristaRepo() {
        this.turistas = FXCollections.observableArrayList();

        // =============================================
        // DATOS DE PRUEBA (quemados en memoria)
        // =============================================
        // Se agregan turistas de ejemplo para que la tabla
        // no inicie vacia y se pueda probar el CRUD de
        // inmediato al ejecutar la aplicacion.

        turistas.add(new Turista(
                "CC", "1098765432",
                "Juan Carlos", "Perez Garcia",
                "juan.perez@correo.com",
                java.time.LocalDate.of(1990, 5, 15),
                "Maria Garcia", "3001234567",
                true
        ));

        turistas.add(new Turista(
                "CC", "1067890123",
                "Ana Maria", "Rodriguez Lopez",
                "ana.rodriguez@email.com",
                java.time.LocalDate.of(1985, 11, 22),
                "Pedro Rodriguez", "3109876543",
                true
        ));

        turistas.add(new Turista(
                "CE", "E-456789",
                "Carlos Andres", "Martinez Ruiz",
                "carlos.martinez@mail.com",
                java.time.LocalDate.of(1995, 3, 8),
                "Luisa Martinez", "3205551234",
                true
        ));

        turistas.add(new Turista(
                "Pasaporte", "PA1234567",
                "Laura Sofia", "Gonzalez Torres",
                "laura.gonzalez@outlook.com",
                java.time.LocalDate.of(2000, 7, 30),
                "Jose Gonzalez", "3157778899",
                true
        ));
    }

    // =============================================
    // METODO DE ACCESO A LA LISTA
    // =============================================

    /**
     * Retorna la lista observable de turistas.
     * Este metodo es usado por la vista para vincular
     * la lista al TableView mediante setItems().
     *
     * @return ObservableList con todos los turistas registrados
     */
    public ObservableList<Turista> getTuristas() {
        return turistas;
    }

    // =============================================
    // CRUD: CREAR (Agregar)
    // =============================================

    /**
     * Agrega un nuevo turista a la lista despues de validar
     * que no exista un turista con el mismo numero de documento
     * o correo electronico.
     *
     * VALIDACIONES:
     * 1. Verifica que no exista otro turista con el mismo numeroDocumento.
     *    Esto garantiza la unicidad del documento como identificador.
     * 2. Verifica que no exista otro turista con el mismo correo.
     *    Esto evita enviar comunicaciones duplicadas.
     *
     * Si alguna validacion falla, retorna false sin agregar el turista.
     * Si todas las validaciones pasan, agrega el turista y retorna true.
     *
     * @param turista Objeto Turista a agregar
     * @return true si se agrego correctamente, false si hay duplicados
     */
    public boolean agregarTurista(Turista turista) {
        // Validar que el numero de documento no exista
        if (existeDocumento(turista.getNumeroDocumento())) {
            return false;
        }

        // Validar que el correo no exista
        if (existeCorreo(turista.getCorreo())) {
            return false;
        }

        // Ambas validaciones pasaron: agregar el turista
        turistas.add(turista);
        return true;
    }

    // =============================================
    // CRUD: ACTUALIZAR (Editar)
    // =============================================

    /**
     * Actualiza los datos de un turista existente en la lista.
     *
     * Busca al turista por su numeroDocumento (identificador unico)
     * y reemplaza todos sus datos con los del objeto turistaActualizado.
     *
     * IMPORTANTE: Se usa el numero de documento como clave de busqueda
     * porque es el identificador natural e inmutable del turista.
     * El numeroDocumento NO debe cambiar durante una edicion.
     *
     * @param turistaActualizado Objeto Turista con los datos actualizados.
     *                           Su numeroDocumento debe coincidir con uno existente.
     */
    public void actualizarTurista(Turista turistaActualizado) {
        for (int i = 0; i < turistas.size(); i++) {
            Turista actual = turistas.get(i);
            // Buscar por numero de documento (clave unica)
            if (actual.getNumeroDocumento().equals(turistaActualizado.getNumeroDocumento())) {
                // Reemplazar el objeto completo en la posicion encontrada
                turistas.set(i, turistaActualizado);
                return; // Salir del metodo tras actualizar
            }
        }
    }

    // =============================================
    // CRUD: BORRAR (Borrado Logico)
    // =============================================

    /**
     * Realiza un borrado logico del turista cambiando su
     * estadoActivo a false y eliminandolo de la lista observable.
     *
     * ¿Por que borrado logico + eliminacion de la lista?
     * --------------------------------------------------
     * 1. Se establece estadoActivo = false para marcar el registro
     *    como inactivo en la base de datos/persistencia futura.
     * 2. Se elimina de la ObservableList para que desaparezca
     *    visualmente del TableView, dando la impresion de eliminacion.
     *
     * En un sistema con base de datos real, solo se cambiaria
     * el campo estadoActivo sin eliminar el registro fisicamente.
     * Al usar lista en memoria, simulamos esto quitandolo de la
     * vista pero marcandolo como inactivo antes.
     *
     * @param turista Objeto Turista a borrar
     */
    public void desactivarTurista(Turista turista) {
        // Borrado logico: cambiar estado a inactivo
        turista.setEstadoActivo(false);
        // Eliminar de la lista observable para que desaparezca de la tabla
        turistas.remove(turista);
    }

    // =============================================
    // METODOS AUXILIARES DE VALIDACION
    // =============================================

    /**
     * Verifica si ya existe un turista con el numero de documento dado.
     *
     * Recorre toda la lista y compara el numeroDocumento de cada turista
     * con el parametro proporcionado. La comparacion es exacta
     * (sensible a mayusculas/minusculas).
     *
     * @param documento Numero de documento a buscar
     * @return true si ya existe un turista con ese documento, false si no
     */
    private boolean existeDocumento(String documento) {
        for (Turista t : turistas) {
            if (t.getNumeroDocumento().equals(documento)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si ya existe un turista con el correo electronico dado.
     *
     * Recorre toda la lista y compara el correo de cada turista
     * con el parametro proporcionado. La comparacion ignora
     * mayusculas/minusculas para evitar duplicados por formato.
     *
     * @param correo Correo electronico a buscar
     * @return true si ya existe un turista con ese correo, false si no
     */
    private boolean existeCorreo(String correo) {
        for (Turista t : turistas) {
            if (t.getCorreo().equalsIgnoreCase(correo)) {
                return true;
            }
        }
        return false;
    }
}


