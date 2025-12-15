package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/vistas/Menu.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root);

        // 👉 CSS DEL MENÚ (AQUÍ)
        scene.getStylesheets().add(
                getClass().getResource("/com/example/estilos/menu.css").toExternalForm()
        );

        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
