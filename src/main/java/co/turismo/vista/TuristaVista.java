package co.turismo.vista;


import co.turismo.modelo.Turista;
import co.turismo.repositorio.TuristaRepo;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.util.Optional;


public class TuristaVista extends VBox {

    // =============================================
    // REPOSITORIO (Acceso a datos)
    // =============================================
    private final TuristaRepo turistaRepo;

    // =============================================
    // COMPONENTES DE LA TABLA
    // =============================================
    private TableView<Turista> tablaTuristas;
    private TableColumn<Turista, String> colDocumento;
    private TableColumn<Turista, String> colNombreCompleto;
    private TableColumn<Turista, String> colCorreo;
    private TableColumn<Turista, Number> colEdad;

    // =============================================
    // COMPONENTES DEL FORMULARIO
    // =============================================
    private ComboBox<String> cmbTipoDocumento;
    private TextField txtNumeroDocumento;
    private TextField txtNombres;
    private TextField txtApellidos;
    private TextField txtCorreo;
    private DatePicker dpFechaNacimiento;
    private TextField txtContactoEmergenciaNombre;
    private TextField txtContactoEmergenciaTelefono;

    // =============================================
    // BOTONES
    // =============================================
    private Button btnNuevo;
    private Button btnGuardar;
    private Button btnEditar;
    private Button btnBorrar;
    private Button btnLimpiar;

    // =============================================
    // ESTADO INTERNO
    // =============================================

    /**
     * Turista seleccionado actualmente en la tabla.
     * Es null cuando no hay seleccion o cuando se presiona "Nuevo".
     * Se usa para saber si la accion "Guardar" es una insercion
     * o una actualizacion.
     */
    private Turista turistaSeleccionado;

    // =============================================
    // CONSTRUCTOR
    // =============================================

    /**
     * Constructor de la vista. Inicializa el repositorio,
     * crea todos los componentes visuales, configura los
     * eventos y aplica el diseno.
     */
    public TuristaVista() {
        this.turistaRepo = new TuristaRepo();
        this.turistaSeleccionado = null;

        inicializarComponentes();
        configurarTabla();
        configurarEventos();
        aplicarDiseno();
    }

    // =============================================
    // INICIALIZACION DE COMPONENTES
    // =============================================

    /**
     * Crea e inicializa todos los componentes visuales:
     * - TableView con sus columnas
     * - Campos del formulario
     * - Botones de accion
     */
    private void inicializarComponentes() {

        // ---- Tabla ----
        tablaTuristas = new TableView<>();
        colDocumento = new TableColumn<>("Documento");
        colNombreCompleto = new TableColumn<>("Nombre Completo");
        colCorreo = new TableColumn<>("Correo");
        colEdad = new TableColumn<>("Edad");

        // ---- ComboBox: Tipo de Documento ----
        cmbTipoDocumento = new ComboBox<>();
        cmbTipoDocumento.getItems().addAll("CC", "TI", "CE", "Pasaporte", "NIT");
        cmbTipoDocumento.setPromptText("Seleccione...");
        cmbTipoDocumento.setPrefWidth(200);

        // ---- Campos de texto ----
        txtNumeroDocumento = new TextField();
        txtNumeroDocumento.setPromptText("Ej: 1098765432");
        txtNumeroDocumento.setPrefWidth(200);

        txtNombres = new TextField();
        txtNombres.setPromptText("Ej: Juan Carlos");
        txtNombres.setPrefWidth(200);

        txtApellidos = new TextField();
        txtApellidos.setPromptText("Ej: Perez Garcia");
        txtApellidos.setPrefWidth(200);

        txtCorreo = new TextField();
        txtCorreo.setPromptText("Ej: juan@correo.com");
        txtCorreo.setPrefWidth(200);

        // ---- DatePicker: Fecha de Nacimiento ----
        dpFechaNacimiento = new DatePicker();
        dpFechaNacimiento.setPromptText("Seleccione la fecha");
        dpFechaNacimiento.setPrefWidth(200);

        txtContactoEmergenciaNombre = new TextField();
        txtContactoEmergenciaNombre.setPromptText("Ej: Maria Perez");
        txtContactoEmergenciaNombre.setPrefWidth(200);

        txtContactoEmergenciaTelefono = new TextField();
        txtContactoEmergenciaTelefono.setPromptText("Ej: 3001234567");
        txtContactoEmergenciaTelefono.setPrefWidth(200);

        // ---- Botones ----
        btnNuevo = new Button("Nuevo");
        btnGuardar = new Button("Guardar");
        btnEditar = new Button("Editar");
        btnBorrar = new Button("Borrar");
        btnLimpiar = new Button("Limpiar Formulario");

        // Estilo de botones
        estilizarBoton(btnNuevo, "#2196F3");       // Azul
        estilizarBoton(btnGuardar, "#4CAF50");     // Verde
        estilizarBoton(btnEditar, "#FF9800");      // Naranja
        estilizarBoton(btnBorrar, "#f44336");  // Rojo
        estilizarBoton(btnLimpiar, "#9E9E9E");     // Gris

        // Estado inicial de botones
        btnGuardar.setDisable(true);
        btnEditar.setDisable(true);
        btnBorrar.setDisable(true);
    }

    // =============================================
    // CONFIGURACION DE LA TABLA
    // =============================================

    /**
     * Configura las columnas del TableView y vincula
     * la lista observable del repositorio.
     *
     * Columna "Documento": Muestra tipoDocumento + numeroDocumento
     * Columna "Nombre Completo": Usa getNombreCompleto()
     * Columna "Correo": Muestra el correo
     * Columna "Edad": Usa calcularEdad()
     */
    private void configurarTabla() {

        // Columna Documento: combina tipo y numero
        colDocumento.setCellValueFactory(cellData -> {
            Turista t = cellData.getValue();
            String doc = t.getTipoDocumento() + " " + t.getNumeroDocumento();
            return new SimpleStringProperty(doc);
        });

        // Columna Nombre Completo: usa el metodo getNombreCompleto()
        colNombreCompleto.setCellValueFactory(
                new PropertyValueFactory<>("nombreCompleto")
        );

        // Columna Correo: usa el getter de correo
        colCorreo.setCellValueFactory(
                new PropertyValueFactory<>("correo")
        );

        // Columna Edad: usa calcularEdad() envuelto en SimpleIntegerProperty
        colEdad.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().calcularEdad())
        );

        // Anchos preferidos de columnas
        colDocumento.setPrefWidth(160);
        colNombreCompleto.setPrefWidth(200);
        colCorreo.setPrefWidth(200);
        colEdad.setPrefWidth(80);

        // Agregar columnas al TableView
        tablaTuristas.getColumns().addAll(
                colDocumento, colNombreCompleto, colCorreo, colEdad
        );

        // Vincular la lista observable del repositorio a la tabla
        tablaTuristas.setItems(turistaRepo.getTuristas());

        // Permitir seleccionar solo una fila a la vez
        tablaTuristas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Altura preferida de la tabla
        tablaTuristas.setPrefHeight(300);
        tablaTuristas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // =============================================
    // CONFIGURACION DE EVENTOS
    // =============================================

    /**
     * Configura todos los manejadores de eventos:
     * - Seleccion en la tabla
     * - Click en cada boton
     */
    private void configurarEventos() {

        // ---- Evento: Seleccionar un turista en la tabla ----
        // Carga los datos del turista seleccionado al formulario
        tablaTuristas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    turistaSeleccionado = newValue;
                    if (newValue != null) {
                        cargarTuristaEnFormulario(newValue);
                        // Habilitar botones de edicion y desactivacion
                        btnEditar.setDisable(false);
                        btnBorrar.setDisable(false);
                        btnGuardar.setDisable(true);
                    } else {
                        // No hay seleccion: deshabilitar botones de edicion
                        btnEditar.setDisable(true);
                        btnBorrar.setDisable(true);
                    }
                }
        );

        // ---- Evento: Boton Nuevo ----
        btnNuevo.setOnAction(event -> accionNuevo());

        // ---- Evento: Boton Guardar ----
        btnGuardar.setOnAction(event -> accionGuardar());

        // ---- Evento: Boton Editar ----
        btnEditar.setOnAction(event -> accionEditar());

        // ---- Evento: Boton Borrar ----
        btnBorrar.setOnAction(event -> accionBorrar());

        // ---- Evento: Boton Limpiar ----
        btnLimpiar.setOnAction(event -> limpiarFormulario());
    }

    // =============================================
    // APLICAR DISENO VISUAL
    // =============================================

    /**
     * Construye la estructura visual completa usando contenedores
     * VBox, HBox y GridPane. Aplica padding, spacing y estilos
     * para lograr una apariencia profesional y organizada.
     */
    private void aplicarDiseno() {

        // ---- Espaciado y padding del contenedor principal ----
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #FAFAFA;");

        // ---- Botón volver ----
        Button btnVolver = new Button("← Volver al Menú");
        btnVolver.setStyle("-fx-background-color:#2C5F2D;-fx-text-fill:white;-fx-font-weight:bold;-fx-cursor:hand;-fx-background-radius:6;-fx-padding:8 18;");
        btnVolver.setOnAction(e -> MainApp.mostrarMenu());
        HBox headerBar = new HBox(btnVolver);
        headerBar.setStyle("-fx-background-color:#2C5F2D;-fx-padding:10 20;");
        headerBar.setAlignment(Pos.CENTER_LEFT);
        // ---- Titulo principal ----
        Label lblTitulo = new Label("Gestion de Turistas");
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 22));
        lblTitulo.setTextFill(Color.web("#263238"));
        HBox contenedorTitulo = new HBox(lblTitulo);
        contenedorTitulo.setAlignment(Pos.CENTER);
        contenedorTitulo.setPadding(new Insets(0, 0, 10, 0));

        // ---- Subtitulo de la tabla ----
        Label lblSubtituloTabla = new Label("Turistas Registrados");
        lblSubtituloTabla.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblSubtituloTabla.setTextFill(Color.web("#546E7A"));

        // ---- Panel de la tabla ----
        VBox panelTabla = new VBox(10, lblSubtituloTabla, tablaTuristas);
        panelTabla.setPadding(new Insets(15));
        panelTabla.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #E0E0E0; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 4, 0, 0, 2);");

        // ---- Subtitulo del formulario ----
        Label lblSubtituloForm = new Label("Datos del Turista");
        lblSubtituloForm.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblSubtituloForm.setTextFill(Color.web("#546E7A"));

        // ---- GridPane para el formulario ----
        GridPane gridFormulario = new GridPane();
        gridFormulario.setHgap(15);
        gridFormulario.setVgap(12);
        gridFormulario.setPadding(new Insets(15));

        // Estilo de las etiquetas del formulario
        String estiloLabel = "-fx-font-weight: bold; -fx-text-fill: #37474F;";

        // Fila 0: Tipo de Documento y Numero de Documento
        Label lblTipoDoc = new Label("Tipo Documento:");
        lblTipoDoc.setStyle(estiloLabel);
        gridFormulario.add(lblTipoDoc, 0, 0);
        gridFormulario.add(cmbTipoDocumento, 1, 0);

        Label lblNumDoc = new Label("Numero Documento:");
        lblNumDoc.setStyle(estiloLabel);
        gridFormulario.add(lblNumDoc, 2, 0);
        gridFormulario.add(txtNumeroDocumento, 3, 0);

        // Fila 1: Nombres y Apellidos
        Label lblNombres = new Label("Nombres:");
        lblNombres.setStyle(estiloLabel);
        gridFormulario.add(lblNombres, 0, 1);
        gridFormulario.add(txtNombres, 1, 1);

        Label lblApellidos = new Label("Apellidos:");
        lblApellidos.setStyle(estiloLabel);
        gridFormulario.add(lblApellidos, 2, 1);
        gridFormulario.add(txtApellidos, 3, 1);

        // Fila 2: Correo y Fecha de Nacimiento
        Label lblCorreo = new Label("Correo:");
        lblCorreo.setStyle(estiloLabel);
        gridFormulario.add(lblCorreo, 0, 2);
        gridFormulario.add(txtCorreo, 1, 2);

        Label lblFechaNac = new Label("Fecha Nacimiento:");
        lblFechaNac.setStyle(estiloLabel);
        gridFormulario.add(lblFechaNac, 2, 2);
        gridFormulario.add(dpFechaNacimiento, 3, 2);

        // Fila 3: Contacto Emergencia Nombre y Telefono
        Label lblContactoNom = new Label("Contacto Emergencia:");
        lblContactoNom.setStyle(estiloLabel);
        gridFormulario.add(lblContactoNom, 0, 3);
        gridFormulario.add(txtContactoEmergenciaNombre, 1, 3);

        Label lblContactoTel = new Label("Telefono Emergencia:");
        lblContactoTel.setStyle(estiloLabel);
        gridFormulario.add(lblContactoTel, 2, 3);
        gridFormulario.add(txtContactoEmergenciaTelefono, 3, 3);

        // ---- Panel del formulario ----
        VBox panelFormulario = new VBox(10, lblSubtituloForm, gridFormulario);
        panelFormulario.setPadding(new Insets(15));
        panelFormulario.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #E0E0E0; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 4, 0, 0, 2);");

        // ---- Contenedor de botones ----
        HBox contenedorBotones = new HBox(12);
        contenedorBotones.setAlignment(Pos.CENTER);
        contenedorBotones.getChildren().addAll(
                btnNuevo, btnGuardar, btnEditar, btnBorrar, btnLimpiar
        );
        contenedorBotones.setPadding(new Insets(10, 0, 0, 0));

        // ---- Ensamblar todo en el VBox principal ----
        this.getChildren().addAll(
                headerBar,
                contenedorTitulo,
                panelTabla,
                panelFormulario,
                contenedorBotones
        );
    }

    // =============================================
    // ESTILO DE BOTONES
    // =============================================

    /**
     * Aplica un estilo visual consistente a los botones:
     * color de fondo blanco con borde del color indicado,
     * texto del color indicado, bordes redondeados y
     * efecto hover cuando el raton pasa sobre el boton.
     *
     * @param boton   Boton a estilizar
     * @param colorHex Color en formato hexadecimal (ej: "#4CAF50")
     */
    private void estilizarBoton(Button boton, String colorHex) {
        boton.setStyle("-fx-background-color: white; " +
                "-fx-border-color: " + colorHex + "; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 6; " +
                "-fx-background-radius: 6; " +
                "-fx-text-fill: " + colorHex + "; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 13px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 18 8 18;");

        // Efecto hover: cambia fondo al color y texto a blanco
        boton.setOnMouseEntered(e ->
                boton.setStyle("-fx-background-color: " + colorHex + "; " +
                        "-fx-border-color: " + colorHex + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 13px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 8 18 8 18;")
        );

        // Efecto al salir: restaura el estilo original
        boton.setOnMouseExited(e ->
                boton.setStyle("-fx-background-color: white; " +
                        "-fx-border-color: " + colorHex + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-text-fill: " + colorHex + "; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 13px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 8 18 8 18;")
        );
    }

    // =============================================
    // ACCIONES CRUD
    // =============================================

    /**
     * ACCION: Nuevo
     * Prepara el formulario para ingresar un nuevo turista.
     * Limpia el formulario, habilita el boton Guardar y
     * deselecciona cualquier turista en la tabla.
     */
    private void accionNuevo() {
        limpiarFormulario();
        turistaSeleccionado = null;

        // Habilitar campos y boton guardar
        btnGuardar.setDisable(false);
        btnEditar.setDisable(true);
        btnBorrar.setDisable(true);

        // Deseleccionar de la tabla
        tablaTuristas.getSelectionModel().clearSelection();

        // Enfocar el primer campo
        cmbTipoDocumento.requestFocus();
    }

    /**
     * ACCION: Guardar
     * Valida los campos del formulario y, si son correctos,
     * crea un nuevo objeto Turista y lo agrega al repositorio.
     *
     * Validaciones realizadas:
     * 1. Campos vacios (todos son obligatorios)
     * 2. Formato de correo electronico valido
     * 3. Fecha de nacimiento obligatoria
     * 4. Documento duplicado (validado por el repositorio)
     * 5. Correo duplicado (validado por el repositorio)
     */
    private void accionGuardar() {
        // Paso 1: Validar campos vacios
        if (!validarCamposVacios()) {
            return;
        }

        // Paso 2: Validar formato de correo
        if (!validarFormatoCorreo()) {
            return;
        }

        // Paso 3: Validar fecha de nacimiento
        if (dpFechaNacimiento.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validacion",
                    "Fecha de nacimiento obligatoria",
                    "Debe seleccionar una fecha de nacimiento.");
            return;
        }

        // Paso 4: Crear objeto Turista con los datos del formulario
        Turista nuevoTurista = new Turista(
                cmbTipoDocumento.getValue(),
                txtNumeroDocumento.getText().trim(),
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                txtCorreo.getText().trim(),
                dpFechaNacimiento.getValue(),
                txtContactoEmergenciaNombre.getText().trim(),
                txtContactoEmergenciaTelefono.getText().trim(),
                true // Nuevo turista siempre activo
        );

        // Paso 5: Intentar agregar al repositorio (valida duplicados)
        boolean agregado = turistaRepo.agregarTurista(nuevoTurista);

        if (agregado) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito",
                    "Turista registrado",
                    "El turista ha sido guardado correctamente.");
            limpiarFormulario();
            btnGuardar.setDisable(true);
        } else {
            // Determinar que tipo de duplicado es
            String mensaje = "";
            if (turistaRepo.getTuristas().stream()
                    .anyMatch(t -> t.getNumeroDocumento()
                            .equals(nuevoTurista.getNumeroDocumento()))) {
                mensaje = "Ya existe un turista con el numero de documento: "
                        + nuevoTurista.getNumeroDocumento();
            } else if (turistaRepo.getTuristas().stream()
                    .anyMatch(t -> t.getCorreo()
                            .equalsIgnoreCase(nuevoTurista.getCorreo()))) {
                mensaje = "Ya existe un turista con el correo: "
                        + nuevoTurista.getCorreo();
            }
            mostrarAlerta(Alert.AlertType.ERROR, "Error - Registro Duplicado",
                    "No se pudo guardar el turista", mensaje);
        }
    }

    /**
     * ACCION: Editar
     * Actualiza los datos del turista seleccionado con los
     * valores actuales del formulario.
     *
     * Solo permite editar si hay un turista seleccionado en la tabla.
     * Valida los campos antes de actualizar.
     */
    private void accionEditar() {
        if (turistaSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin Seleccion",
                    "No hay turista seleccionado",
                    "Seleccione un turista de la tabla para editar.");
            return;
        }

        // Validar campos vacios
        if (!validarCamposVacios()) {
            return;
        }

        // Validar formato de correo
        if (!validarFormatoCorreo()) {
            return;
        }

        // Validar fecha de nacimiento
        if (dpFechaNacimiento.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validacion",
                    "Fecha de nacimiento obligatoria",
                    "Debe seleccionar una fecha de nacimiento.");
            return;
        }

        // Confirmar la edicion
        Optional<ButtonType> resultado = mostrarAlertaConfirmacion(
                "Confirmar Edicion",
                "¿Desea actualizar los datos del turista?",
                "Se modificaran los datos de: "
                        + turistaSeleccionado.getNombreCompleto());

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Actualizar los datos del turista seleccionado
            turistaSeleccionado.setTipoDocumento(cmbTipoDocumento.getValue());
            turistaSeleccionado.setNumeroDocumento(
                    txtNumeroDocumento.getText().trim());
            turistaSeleccionado.setNombres(txtNombres.getText().trim());
            turistaSeleccionado.setApellidos(txtApellidos.getText().trim());
            turistaSeleccionado.setCorreo(txtCorreo.getText().trim());
            turistaSeleccionado.setFechaNacimiento(
                    dpFechaNacimiento.getValue());
            turistaSeleccionado.setContactoEmergenciaNombre(
                    txtContactoEmergenciaNombre.getText().trim());
            turistaSeleccionado.setContactoEmergenciaTelefono(
                    txtContactoEmergenciaTelefono.getText().trim());

            // Actualizar en el repositorio
            turistaRepo.actualizarTurista(turistaSeleccionado);

            // Refrescar la tabla para mostrar los cambios
            tablaTuristas.refresh();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito",
                    "Turista actualizado",
                    "Los datos del turista han sido actualizados correctamente.");

            limpiarFormulario();
        }
    }

    /**
     * ACCION: Borrar
     * Realiza el borrado logico del turista seleccionado,
     * cambiando su estadoActivo a false y eliminandolo
     * de la vista del TableView.
     *
     * Muestra un dialogo de confirmacion antes de proceder.
     */
    private void accionBorrar() {
        if (turistaSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin Seleccion",
                    "No hay turista seleccionado",
                    "Seleccione un turista de la tabla para eliminar.");
            return;
        }

        // Confirmar la desactivacion
        Optional<ButtonType> resultado = mostrarAlertaConfirmacion(
                "Confirmar Eliminacion",
                "¿Desea eliminar al turista?",
                "El turista " + turistaSeleccionado.getNombreCompleto()
                        + " sera eliminado del sistema.");

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            turistaRepo.desactivarTurista(turistaSeleccionado);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado",
                    "Turista eliminado",
                    "El turista ha sido eliminado correctamente.");

            limpiarFormulario();
        }
    }

    // =============================================
    // METODOS AUXILIARES
    // =============================================

    /**
     * Carga los datos de un turista en los campos del formulario.
     * Se utiliza cuando el usuario selecciona un turista en la tabla.
     *
     * @param turista Objeto Turista cuyos datos se cargaran
     */
    private void cargarTuristaEnFormulario(Turista turista) {
        cmbTipoDocumento.setValue(turista.getTipoDocumento());
        txtNumeroDocumento.setText(turista.getNumeroDocumento());
        txtNombres.setText(turista.getNombres());
        txtApellidos.setText(turista.getApellidos());
        txtCorreo.setText(turista.getCorreo());
        dpFechaNacimiento.setValue(turista.getFechaNacimiento());
        txtContactoEmergenciaNombre.setText(
                turista.getContactoEmergenciaNombre());
        txtContactoEmergenciaTelefono.setText(
                turista.getContactoEmergenciaTelefono());
    }

    /**
     * Limpia todos los campos del formulario y reinicia
     * el estado de la seleccion.
     */
    private void limpiarFormulario() {
        cmbTipoDocumento.setValue(null);
        txtNumeroDocumento.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtCorreo.clear();
        dpFechaNacimiento.setValue(null);
        txtContactoEmergenciaNombre.clear();
        txtContactoEmergenciaTelefono.clear();

        turistaSeleccionado = null;
        btnGuardar.setDisable(true);
        btnEditar.setDisable(true);
        btnBorrar.setDisable(true);

        tablaTuristas.getSelectionModel().clearSelection();
    }

    // =============================================
    // VALIDACIONES
    // =============================================

    /**
     * Valida que ningun campo obligatorio este vacio.
     *
     * Campos validados:
     * - Tipo de documento
     * - Numero de documento
     * - Nombres
     * - Apellidos
     * - Correo
     * - Fecha de nacimiento
     * - Nombre del contacto de emergencia
     * - Telefono del contacto de emergencia
     *
     * @return true si todos los campos estan llenos, false si hay vacios
     */
    private boolean validarCamposVacios() {
        StringBuilder camposFaltantes = new StringBuilder();

        if (cmbTipoDocumento.getValue() == null
                || cmbTipoDocumento.getValue().isEmpty()) {
            camposFaltantes.append("- Tipo de documento\n");
        }
        if (txtNumeroDocumento.getText() == null
                || txtNumeroDocumento.getText().trim().isEmpty()) {
            camposFaltantes.append("- Numero de documento\n");
        }
        if (txtNombres.getText() == null
                || txtNombres.getText().trim().isEmpty()) {
            camposFaltantes.append("- Nombres\n");
        }
        if (txtApellidos.getText() == null
                || txtApellidos.getText().trim().isEmpty()) {
            camposFaltantes.append("- Apellidos\n");
        }
        if (txtCorreo.getText() == null
                || txtCorreo.getText().trim().isEmpty()) {
            camposFaltantes.append("- Correo\n");
        }
        if (dpFechaNacimiento.getValue() == null) {
            camposFaltantes.append("- Fecha de nacimiento\n");
        }
        if (txtContactoEmergenciaNombre.getText() == null
                || txtContactoEmergenciaNombre.getText().trim().isEmpty()) {
            camposFaltantes.append("- Nombre contacto emergencia\n");
        }
        if (txtContactoEmergenciaTelefono.getText() == null
                || txtContactoEmergenciaTelefono.getText().trim().isEmpty()) {
            camposFaltantes.append("- Telefono contacto emergencia\n");
        }

        if (camposFaltantes.length() > 0) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos Vacios",
                    "Complete los siguientes campos obligatorios:",
                    camposFaltantes.toString());
            return false;
        }

        return true;
    }

    /**
     * Valida que el correo electronico tenga un formato basico valido.
     *
     * Verifica que el correo contenga al menos un caracter '@'
     * y que tenga al menos un '.' despues del '@'.
     *
     * Esta no es una validacion RFC completa, pero es suficiente
     * para la mayoria de casos practicos en una aplicacion de escritorio.
     *
     * @return true si el correo tiene formato valido, false si no
     */
    private boolean validarFormatoCorreo() {
        String correo = txtCorreo.getText().trim();

        if (!correo.contains("@") || !correo.contains(".")) {
            mostrarAlerta(Alert.AlertType.ERROR, "Correo Invalido",
                    "Formato de correo no valido",
                    "Ingrese un correo electronico valido.\n"
                            + "Ejemplo: usuario@dominio.com");
            return false;
        }

        // Verificar que el '.' este despues del '@'
        int posArroba = correo.indexOf('@');
        int posPunto = correo.lastIndexOf('.');
        if (posPunto < posArroba + 2) {
            mostrarAlerta(Alert.AlertType.ERROR, "Correo Invalido",
                    "Formato de correo no valido",
                    "El dominio del correo no es valido.\n"
                            + "Ejemplo: usuario@dominio.com");
            return false;
        }

        return true;
    }

    // =============================================
    // DIALOGOS DE ALERTA
    // =============================================

    /**
     * Muestra un dialogo de alerta con el tipo, titulo, encabezado
     * y contenido especificados.
     *
     * @param tipo      Tipo de alerta (ERROR, INFORMATION, WARNING, etc.)
     * @param titulo    Titulo de la ventana de alerta
     * @param encabezado Encabezado del mensaje
     * @param contenido Contenido detallado del mensaje
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo,
                               String encabezado, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /**
     * Muestra un dialogo de confirmacion con botones OK y Cancel.
     *
     * @param titulo    Titulo de la ventana
     * @param encabezado Encabezado del mensaje
     * @param contenido Contenido del mensaje
     * @return Optional con el boton presionado por el usuario
     */
    private Optional<ButtonType> mostrarAlertaConfirmacion(
            String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        return alerta.showAndWait();
    }
}

