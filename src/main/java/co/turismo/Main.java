package co.turismo;

public class Main {
}

import co.turismo.vista.TuristaVista;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * =====================================================
 * CLASE PRINCIPAL: Main.java
 * =====================================================
 * Punto de entrada de la aplicacion JavaFX.
 *
 * Crea el Stage principal, instancia la vista TuristaVista
 * y la coloca como raiz de la escena.
 *
 * IMPORTANTE: Esta clase DEBE estar en el paquete base
 * co.turismo (sin subpaquete) para que el modulo JavaFX
 * la encuentre como clase principal.
 * =====================================================
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear la vista principal de gestion de turistas
        TuristaVista turistaVista = new TuristaVista();

        // Crear la escena con la vista
        Scene scene = new Scene(turistaVista, 950, 750);

        // Configurar el stage principal
        primaryStage.setTitle("Sistema de Turismo Rural - Gestion de Turistas");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(850);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


