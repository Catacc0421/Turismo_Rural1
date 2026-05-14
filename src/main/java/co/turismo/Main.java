package co.turismo;

import co.turismo.vista.MainApp;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainApp.primaryStage = primaryStage;
        primaryStage.setTitle("🌿 Turismo Rural - Plataforma de Reservas");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(650);
        MainApp.mostrarMenu();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}