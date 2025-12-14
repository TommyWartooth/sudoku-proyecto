package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/vistas/SudokuView.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("Sudoku FX");
        stage.setScene(scene);
        stage.setResizable(false); // opcional
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
