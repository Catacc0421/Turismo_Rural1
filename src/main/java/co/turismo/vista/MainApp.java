package co.turismo.vista;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class MainApp {

    public static Stage primaryStage;

    public static void mostrarMenu() {
        VBox root = new VBox(24);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        root.setStyle("-fx-background-color: #FAFAFA;");

        Text titulo = new Text("🌿 Turismo Rural");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 46));
        titulo.setFill(Color.web("#263238"));

        Text sub = new Text("Plataforma de Experiencias Rurales");
        sub.setFont(Font.font("System", 18));
        sub.setFill(Color.web("#546E7A"));

        Button btnReserva  = btn("🗓  Reservar Experiencia",      "#4CAF50");
        Button btnTuristas = btn("👥  Gestionar Turistas (CRUD)", "#2196F3");

        btnReserva.setOnAction(e -> new ReservaVista().mostrar(primaryStage));
        btnTuristas.setOnAction(e -> {
            TuristaVista vista = new TuristaVista();
            Scene scene = new Scene(vista, 950, 750);
            primaryStage.setScene(scene);
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        root.getChildren().addAll(titulo, sub, spacer, btnReserva, btnTuristas);

        primaryStage.setScene(new Scene(root, 900, 600));
    }

    private static Button btn(String texto, String color) {
        Button b = new Button(texto);
        String estiloNormal =
                "-fx-background-color: white;" +
                        "-fx-border-color: " + color + ";" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-text-fill: " + color + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14 44;";
        String estiloHover =
                "-fx-background-color: " + color + ";" +
                        "-fx-border-color: " + color + ";" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14 44;";
        b.setStyle(estiloNormal);
        b.setPrefWidth(360);
        b.setOnMouseEntered(e -> b.setStyle(estiloHover));
        b.setOnMouseExited(e  -> b.setStyle(estiloNormal));
        return b;
    }
}
