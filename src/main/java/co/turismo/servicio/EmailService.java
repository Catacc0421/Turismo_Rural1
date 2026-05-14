package co.turismo.servicio;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;


public class EmailService {


    private static final String GMAIL_USUARIO   = "catacc3844@gmail.com";   //
    private static final String GMAIL_APP_PASSWORD = "dgiv uins wgis umcm"; //
    // ─────────────────────────────────────────────────────────────────

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    /**
     * Envía un correo de confirmación de reserva al turista.
     *
     * Se ejecuta en un hilo separado para no bloquear la UI de JavaFX.
     *
     * @param destinatario Correo del turista
     * @param voucher      Código del voucher generado
     * @param nombreTurista Nombre completo del turista
     * @param experiencia  Nombre de la experiencia reservada
     * @param fecha        Fecha de la experiencia
     * @param personas     Número de personas
     * @param metodoPago   Método de pago seleccionado
     * @param total        Valor total pagado
     * @param contactoEmergencia Nombre del contacto de emergencia
     * @param onExito      Callback ejecutado en JavaFX thread si el envío es exitoso
     * @param onError      Callback ejecutado en JavaFX thread si hay error
     */
    public static void enviarConfirmacion(
            String destinatario,
            String voucher,
            String nombreTurista,
            String experiencia,
            String fecha,
            int    personas,
            String metodoPago,
            double total,
            String contactoEmergencia,
            Runnable onExito,
            java.util.function.Consumer<String> onError
    ) {
        // Ejecutar en hilo separado para no congelar la UI
        Thread hilo = new Thread(() -> {
            try {
                // ── Configuración SMTP ─────────────────────────────
                Properties props = new Properties();
                props.put("mail.smtp.auth",            "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host",            SMTP_HOST);
                props.put("mail.smtp.port",            SMTP_PORT);
                props.put("mail.smtp.ssl.trust",       SMTP_HOST);

                // ── Autenticación ──────────────────────────────────
                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(GMAIL_USUARIO, GMAIL_APP_PASSWORD);
                    }
                });

                // ── Construir el mensaje ───────────────────────────
                Message mensaje = new MimeMessage(session);
                mensaje.setFrom(new InternetAddress(GMAIL_USUARIO, "Turismo Rural \uD83C\uDF3F"));
                mensaje.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(destinatario));
                mensaje.setSubject("\uD83C\uDF9F Confirmación de Reserva — " + voucher);
                mensaje.setContent(construirHtml(
                                voucher, nombreTurista, experiencia, fecha,
                                personas, metodoPago, total, contactoEmergencia),
                        "text/html; charset=UTF-8");

                // ── Enviar ─────────────────────────────────────────
                Transport.send(mensaje);

                // Callback de éxito en JavaFX Application Thread
                javafx.application.Platform.runLater(onExito);

            } catch (Exception ex) {
                // Callback de error en JavaFX Application Thread
                javafx.application.Platform.runLater(
                        () -> onError.accept(ex.getMessage()));
            }
        });

        hilo.setDaemon(true);
        hilo.start();
    }

    /**
     * Construye el cuerpo HTML del correo de confirmación.
     * Diseño verde/natural acorde con la identidad del sistema.
     */
    private static String construirHtml(
            String voucher, String nombreTurista, String experiencia,
            String fecha, int personas, String metodoPago,
            double total, String contactoEmergencia) {

        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'></head>" +
                "<body style='font-family:Arial,sans-serif;background:#F1F8E9;margin:0;padding:0;'>" +

                // Header
                "<div style='background:#2C5F2D;padding:28px 40px;'>" +
                "  <h1 style='color:white;margin:0;font-size:26px;'>&#127807; Turismo Rural</h1>" +
                "  <p style='color:#A5D6A7;margin:6px 0 0;font-size:14px;'>Plataforma de Experiencias Rurales</p>" +
                "</div>" +

                // Cuerpo
                "<div style='padding:32px 40px;'>" +
                "  <h2 style='color:#1B5E20;'>&#9989; ¡Reserva Confirmada!</h2>" +
                "  <p style='color:#37474F;font-size:15px;'>Hola <strong>" + nombreTurista + "</strong>," +
                "  tu reserva ha sido procesada exitosamente.</p>" +

                // Voucher destacado
                "  <div style='background:#2E7D32;border-radius:8px;padding:16px 24px;margin:20px 0;display:inline-block;'>" +
                "    <span style='color:#A5D6A7;font-size:13px;'>CÓDIGO DE VOUCHER</span><br>" +
                "    <span style='color:white;font-size:28px;font-weight:bold;letter-spacing:3px;'>" + voucher + "</span>" +
                "  </div>" +

                // Detalles
                "  <table style='width:100%;border-collapse:collapse;margin-top:20px;'>" +
                fila("&#127795; Experiencia",   experiencia) +
                fila("&#128467; Fecha",          fecha) +
                fila("&#128101; N.° de personas", String.valueOf(personas)) +
                fila("&#128179; Método de pago",  metodoPago) +
                fila("&#128176; Total pagado",    "$" + String.format("%,.0f", total)) +
                fila("&#128680; Contacto emergencia", contactoEmergencia) +
                "  </table>" +

                // Pie
                "  <div style='margin-top:30px;padding:16px;background:#E8F5E9;border-radius:8px;" +
                "              border-left:4px solid #4CAF50;'>" +
                "    <p style='margin:0;color:#2E7D32;font-size:13px;'>" +
                "      &#128204; El anfitrión ha sido notificado y preparará la logística." +
                "      Presenta este voucher el día de la experiencia." +
                "    </p>" +
                "  </div>" +

                "  <p style='color:#90A4AE;font-size:12px;margin-top:24px;'>" +
                "    Este correo fue generado automáticamente por el Sistema de Turismo Rural." +
                "  </p>" +
                "</div></body></html>";
    }

    /** Genera una fila de la tabla de detalles del voucher */
    private static String fila(String etiqueta, String valor) {
        return "<tr>" +
                "<td style='padding:10px 14px;background:#E8F5E9;color:#2E7D32;" +
                "           font-weight:bold;width:40%;border-radius:4px;'>" + etiqueta + "</td>" +
                "<td style='padding:10px 14px;color:#37474F;'>" + valor + "</td>" +
                "</tr>";
    }
}
