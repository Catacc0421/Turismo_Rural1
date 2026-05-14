// Descriptor de modulo Java (JPMS) requerido por JavaFX
module co.turismo {
    requires javafx.controls;
    requires javafx.fxml;

    // Jakarta Mail (angus-mail) para envío de correo por Gmail SMTP
    requires jakarta.mail;

    opens co.turismo to javafx.graphics;
    opens co.turismo.modelo to javafx.base;
    opens co.turismo.vista to javafx.fxml;

    exports co.turismo;
    exports co.turismo.modelo;
    exports co.turismo.repositorio;
    exports co.turismo.vista;
    exports co.turismo.servicio;
}