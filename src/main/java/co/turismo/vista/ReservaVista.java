package co.turismo.vista;

import co.turismo.modelo.*;
import co.turismo.repositorio.*;
import co.turismo.servicio.EmailService;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ReservaVista {

    private final TuristaRepo turistaRepo = TuristaRepo.get();
    private final ExperienciaRepo expRepo     = ExperienciaRepo.get();
    private final ReservaRepo     reservaRepo = ReservaRepo.get();

    private ComboBox<Turista>     cmbTurista;
    private ComboBox<Experiencia> cmbExperiencia;
    private Spinner<Integer>      spnPersonas;
    private ComboBox<String>      cmbPago;
    private Label                 lblCupos, lblTotal, lblMensaje;

    // ── Campos de emergencia (REQ004 Student Book) ───────────────
    private TextField txtContactoEmergencia;
    private TextField txtTelefonoEmergencia;
    private CheckBox  chkTerminos;

    public void mostrar(Stage stage) {

        // ── Header ──────────────────────────────────────────────────
        HBox header = new HBox(14);
        header.setStyle("-fx-background-color:#2C5F2D;-fx-padding:14 20;");
        header.setAlignment(Pos.CENTER_LEFT);

        Button btnVolver = new Button("← Volver");
        btnVolver.setStyle(
                "-fx-background-color:transparent;-fx-text-fill:white;" +
                        "-fx-font-size:14;-fx-cursor:hand;-fx-font-weight:bold;");
        btnVolver.setOnMouseEntered(e -> btnVolver.setStyle(
                "-fx-background-color:rgba(255,255,255,0.15);-fx-text-fill:white;" +
                        "-fx-font-size:14;-fx-cursor:hand;-fx-font-weight:bold;-fx-background-radius:4;"));
        btnVolver.setOnMouseExited(e -> btnVolver.setStyle(
                "-fx-background-color:transparent;-fx-text-fill:white;" +
                        "-fx-font-size:14;-fx-cursor:hand;-fx-font-weight:bold;"));
        btnVolver.setOnAction(e -> MainApp.mostrarMenu());

        Text titulo = new Text("\uD83C\uDF3F  Reservar Experiencia Turística");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 22));
        titulo.setFill(Color.WHITE);
        header.getChildren().addAll(btnVolver, titulo);

        // ── Formulario ──────────────────────────────────────────────
        GridPane g = new GridPane();
        g.setHgap(14);
        g.setVgap(12);
        g.setPadding(new Insets(28, 44, 28, 44));
        g.setStyle("-fx-background-color:#F1F8E9;");

        // ─── Sección 1: Datos del Turista ───────────────────────────
        g.add(seccion("\uD83D\uDC64  1. Turista Registrado"), 0, 0, 2, 1);
        g.add(lbl("Turista:"), 0, 1);

        cmbTurista = new ComboBox<>(turistaRepo.getTuristas());
        cmbTurista.setPromptText("Seleccione un turista…");
        cmbTurista.setPrefWidth(420);
        cmbTurista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Turista t, boolean empty) {
                super.updateItem(t, empty);
                setText(empty || t == null ? null
                        : t.getNombreCompleto() + " ("
                        + t.getTipoDocumento() + " "
                        + t.getNumeroDocumento() + ")");
            }
        });
        cmbTurista.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Turista t, boolean empty) {
                super.updateItem(t, empty);
                setText(empty || t == null ? null
                        : t.getNombreCompleto() + " ("
                        + t.getTipoDocumento() + " "
                        + t.getNumeroDocumento() + ")");
            }
        });
        g.add(cmbTurista, 1, 1);
        cmbTurista.setOnAction(e -> {
            Turista t = cmbTurista.getValue();
            if (t != null) {
                txtContactoEmergencia.setText(t.getContactoEmergenciaNombre());
                txtTelefonoEmergencia.setText(t.getContactoEmergenciaTelefono());
            } else {
                txtContactoEmergencia.clear();
                txtTelefonoEmergencia.clear();
            }
        });

        // ─── Sección 2: Experiencia ──────────────────────────────────
        g.add(seccion("\uD83C\uDF32  2. Experiencia Turística"), 0, 2, 2, 1);
        g.add(lbl("Experiencia:"), 0, 3);

        cmbExperiencia = new ComboBox<>(expRepo.getExperienciasConCupos());
        cmbExperiencia.setPromptText("Seleccione una experiencia…");
        cmbExperiencia.setPrefWidth(420);
        g.add(cmbExperiencia, 1, 3);

        lblCupos = new Label("");
        lblCupos.setStyle("-fx-text-fill:#2C5F2D;-fx-font-style:italic;");
        g.add(lblCupos, 1, 4);
        cmbExperiencia.setOnAction(e -> actualizarInfo());

        // ─── Sección 3: Detalles de la Reserva ─────────────────────
        g.add(seccion("\uD83D\uDDD3  3. Detalles de la Reserva"), 0, 5, 2, 1);
        g.add(lbl("N.º de personas:"), 0, 6);

        spnPersonas = new Spinner<>(1, 30, 1);
        spnPersonas.setPrefWidth(110);
        spnPersonas.valueProperty().addListener((o, v, n) -> calcularTotal());
        g.add(spnPersonas, 1, 6);

        g.add(lbl("Método de pago:"), 0, 7);
        cmbPago = new ComboBox<>(FXCollections.observableArrayList(
                "\uD83D\uDCB3 Tarjeta de Crédito",
                "\uD83D\uDCB4 Tarjeta Débito",
                "\uD83C\uDFE6 PSE",
                "\uD83D\uDCB5 Efectivo"));
        cmbPago.setPromptText("Seleccione…");
        cmbPago.setPrefWidth(220);
        g.add(cmbPago, 1, 7);

        g.add(lbl("Valor total:"), 0, 8);
        lblTotal = new Label("$0");
        lblTotal.setStyle(
                "-fx-font-size:22px;-fx-font-weight:bold;-fx-text-fill:#2C5F2D;");
        g.add(lblTotal, 1, 8);

        // ─── Sección 4: Datos de Emergencia (REQ004 Student Book) ──
        g.add(seccion("\uD83D\uDEA8  4. Datos de Emergencia  \u2014  requerido antes del pago"), 0, 9, 2, 1);

        g.add(lbl("Contacto emergencia:"), 0, 10);
        txtContactoEmergencia = new TextField();
        txtContactoEmergencia.setPromptText("Nombre completo del contacto");
        txtContactoEmergencia.setPrefWidth(300);
        g.add(txtContactoEmergencia, 1, 10);

        g.add(lbl("Teléfono emergencia:"), 0, 11);
        txtTelefonoEmergencia = new TextField();
        txtTelefonoEmergencia.setPromptText("Ej: 3001234567");
        txtTelefonoEmergencia.setPrefWidth(200);
        g.add(txtTelefonoEmergencia, 1, 11);

        chkTerminos = new CheckBox("Acepto los términos, condiciones y la póliza de seguro de viaje");
        chkTerminos.setStyle("-fx-font-size:12px;-fx-text-fill:#2E7D32;");
        g.add(chkTerminos, 0, 12, 2, 1);

        // ─── Mensaje de error / éxito ────────────────────────────────
        lblMensaje = new Label("");
        lblMensaje.setWrapText(true);
        lblMensaje.setMaxWidth(500);
        g.add(lblMensaje, 0, 13, 2, 1);

        // ─── Botón confirmar ─────────────────────────────────────────
        Button btnConfirmar = new Button("\uD83C\uDF3F  Confirmar Reserva");
        btnConfirmar.setStyle(
                "-fx-background-color:#2E7D32;-fx-text-fill:white;" +
                        "-fx-font-size:15px;-fx-font-weight:bold;" +
                        "-fx-padding:12 34;-fx-cursor:hand;-fx-background-radius:6;");
        btnConfirmar.setOnMouseEntered(e -> btnConfirmar.setStyle(
                "-fx-background-color:#1B5E20;-fx-text-fill:white;" +
                        "-fx-font-size:15px;-fx-font-weight:bold;" +
                        "-fx-padding:12 34;-fx-cursor:hand;-fx-background-radius:6;"));
        btnConfirmar.setOnMouseExited(e -> btnConfirmar.setStyle(
                "-fx-background-color:#2E7D32;-fx-text-fill:white;" +
                        "-fx-font-size:15px;-fx-font-weight:bold;" +
                        "-fx-padding:12 34;-fx-cursor:hand;-fx-background-radius:6;"));
        btnConfirmar.setOnAction(e -> procesarReserva());
        g.add(new HBox(btnConfirmar), 0, 14, 2, 1);

        // ── Layout ──────────────────────────────────────────────────
        ScrollPane scroll = new ScrollPane(g);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:#F1F8E9;-fx-background-color:#F1F8E9;");

        VBox root = new VBox(header, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        stage.setScene(new Scene(root, 900, 720));
    }

    // ── Métodos internos ─────────────────────────────────────────────

    private void actualizarInfo() {
        Experiencia exp = cmbExperiencia.getValue();
        if (exp == null) return;
        lblCupos.setText("\uD83D\uDDD3 Fecha: " + exp.getFecha()
                + "  |  \uD83C\uDF3F Cupos disponibles: " + exp.getCuposDisponibles()
                + "  |  \uD83C\uDFE1 Anfitrión: " + exp.getAnfitrion());
        calcularTotal();
    }

    private void calcularTotal() {
        Experiencia exp = cmbExperiencia.getValue();
        if (exp == null) return;
        lblTotal.setText("$" + String.format("%,.0f",
                exp.getPrecio() * spnPersonas.getValue()));
    }

    private void procesarReserva() {

        // ── Validaciones — flujo alterno bloqueante ──────────────────

        // REQ001: Turista seleccionado
        if (cmbTurista.getValue() == null) {
            error("\u26A0\uFE0F Seleccione un turista registrado."); return;
        }
        // REQ007: Experiencia seleccionada
        if (cmbExperiencia.getValue() == null) {
            error("\u26A0\uFE0F Seleccione una experiencia."); return;
        }
        // REQ017: Método de pago
        if (cmbPago.getValue() == null) {
            error("\u26A0\uFE0F Seleccione un método de pago."); return;
        }
        // REQ004: Contacto de emergencia obligatorio (Student Book RN-004)
        if (txtContactoEmergencia.getText().trim().isEmpty()) {
            error("\uD83D\uDEA8 El nombre del contacto de emergencia es obligatorio."); return;
        }
        if (txtTelefonoEmergencia.getText().trim().isEmpty()) {
            error("\uD83D\uDEA8 El teléfono del contacto de emergencia es obligatorio."); return;
        }
        // REQ005: Aceptar póliza de seguro (Student Book)
        if (!chkTerminos.isSelected()) {
            error("\uD83D\uDCCB Debe aceptar los términos y la póliza de seguro de viaje para continuar."); return;
        }

        Turista     turista  = cmbTurista.getValue();
        Experiencia exp      = cmbExperiencia.getValue();
        int         personas = spnPersonas.getValue();

        // REQ006 / RN-005: Validar mayoría de edad (Student Book)
        if (turista.calcularEdad() < 18) {
            error("\uD83D\uDEAB El turista debe ser mayor de 18 años para reservar esta experiencia (RN-005)."); return;
        }

        // REQ014 / RN-001: Validar cupos disponibles (Student Book)
        if (personas > exp.getCuposDisponibles()) {
            error("\uD83D\uDEAB Cupos insuficientes. Disponibles: "
                    + exp.getCuposDisponibles()
                    + ". Ajuste la cantidad de personas o elija otra fecha (RN-001)."); return;
        }

        // ── Flujo feliz ──────────────────────────────────────────────
        double total   = exp.getPrecio() * personas;
        String voucher = "VCH-" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase();
        String fecha   = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Reserva reserva = new Reserva(
                reservaRepo.siguienteId(), turista, exp,
                personas, total, cmbPago.getValue(), voucher, fecha);
        reservaRepo.crear(reserva);
        expRepo.descontarCupos(exp.getId(), personas);

        // Refrescar combo de experiencias (REQ016: cupos actualizados)
        cmbExperiencia.setItems(expRepo.getExperienciasConCupos());
        cmbExperiencia.setValue(null);
        lblCupos.setText("");
        lblTotal.setText("$0");
        lblMensaje.setText("");
        txtContactoEmergencia.clear();
        txtTelefonoEmergencia.clear();
        chkTerminos.setSelected(false);

        // REQ021: Voucher de confirmación en pantalla
        Alert ok = new Alert(Alert.AlertType.INFORMATION);
        ok.setTitle("\u2705 Reserva Confirmada");
        ok.setHeaderText("\uD83C\uDF3F ¡Reserva realizada exitosamente!");
        ok.setContentText(
                "\uD83C\uDF9F  VOUCHER: "  + voucher                          + "\n\n" +
                        "\uD83D\uDC64 Turista: "     + turista.getNombreCompleto()       + "\n"   +
                        "\uD83D\uDCC4 Documento: "   + turista.getTipoDocumento()
                        + " " + turista.getNumeroDocumento()          + "\n"   +
                        "\uD83C\uDF32 Experiencia: " + exp.getNombre()                   + "\n"   +
                        "\uD83D\uDDD3 Fecha: "        + exp.getFecha()                   + "\n"   +
                        "\uD83D\uDC65 Personas: "    + personas                          + "\n"   +
                        "\uD83D\uDCB3 Pago: "        + cmbPago.getValue()                + "\n"   +
                        "\uD83D\uDCB0 Total: $"      + String.format("%,.0f", total)     + "\n\n" +
                        "\uD83D\uDEA8 Contacto emergencia: " + txtContactoEmergencia.getText().trim() + "\n" +
                        "\uD83D\uDCE7 Enviando confirmación por correo a: " + turista.getCorreo() + "\n" +
                        "\uD83D\uDCF2 Anfitrión notificado (REQ023)."
        );
        ok.showAndWait();

        // REQ022: Enviar correo de confirmación al turista (Gmail SMTP)
        final String contactoFinal = txtContactoEmergencia.getText().trim();
        EmailService.enviarConfirmacion(
                turista.getCorreo(),
                voucher,
                turista.getNombreCompleto(),
                exp.getNombre(),
                exp.getFecha(),
                personas,
                cmbPago.getValue(),
                total,
                contactoFinal,
                // Callback éxito
                () -> {
                    Alert emailOk = new Alert(Alert.AlertType.INFORMATION);
                    emailOk.setTitle("\uD83D\uDCE7 Correo enviado");
                    emailOk.setHeaderText("Notificación enviada");
                    emailOk.setContentText(
                            "\u2705 El correo de confirmación fue enviado a:\n"
                                    + turista.getCorreo() + "\n\n"
                                    + "Revisa tu bandeja de entrada o spam.");
                    emailOk.showAndWait();
                },
                // Callback error
                mensajeError -> {
                    Alert emailErr = new Alert(Alert.AlertType.WARNING);
                    emailErr.setTitle("\u26A0\uFE0F Correo no enviado");
                    emailErr.setHeaderText("La reserva fue creada, pero el correo falló");
                    emailErr.setContentText(
                            "Verifica tu App Password de Gmail en EmailService.java\n\n"
                                    + "Error: " + mensajeError);
                    emailErr.showAndWait();
                }
        );
    }

    private void error(String msg) {
        lblMensaje.setText(msg);
        lblMensaje.setStyle(
                "-fx-text-fill:#c62828;-fx-font-size:13px;-fx-font-weight:bold;");
    }

    private Label lbl(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-weight:bold;-fx-font-size:13px;-fx-text-fill:#2E7D32;");
        l.setMinWidth(180);
        return l;
    }

    private Label seccion(String t) {
        Label l = new Label(t);
        l.setStyle(
                "-fx-font-size:14px;-fx-font-weight:bold;-fx-text-fill:#1B5E20;" +
                        "-fx-border-color:#A5D6A7;-fx-border-width:0 0 2 0;" +
                        "-fx-padding:10 0 4 0;");
        l.setMaxWidth(Double.MAX_VALUE);
        return l;
    }
}