package com.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;



public class MenuController {

    @FXML private ComboBox<String> cmbDificultad;

    @FXML
    public void initialize() {
        cmbDificultad.setItems(FXCollections.observableArrayList(
                "EASY", "MEDIUM", "HARD", "EXPERT"
        ));
        cmbDificultad.setValue("MEDIUM");
    }

    @FXML
    private void iniciar() throws Exception {
    FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/example/vistas/SudokuView.fxml")
    );

    Parent root = loader.load();

    // pasar dificultad
    SudokuController ctrl = loader.getController();
    ctrl.setDificultadInicial(cmbDificultad.getValue());

    // cambiar escena SOLO UNA VEZ
    Stage stage = (Stage) cmbDificultad.getScene().getWindow();

    Scene scene = new Scene(root);
    scene.getStylesheets().add(
            getClass().getResource("/com/example/estilos/green-sudoku.css").toExternalForm()
    );

    stage.setScene(scene);
    stage.setTitle("Sudoku - Juego");
    stage.show();
}

    @FXML
    private void salir() {
        Stage stage = (Stage) cmbDificultad.getScene().getWindow();
        stage.close();
    }
@FXML
private void verRanking() throws Exception {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/com/example/vistas/RankingView.fxml")
    );

    Parent root = loader.load();

    Scene scene = new Scene(root);
    scene.getStylesheets().add(
        getClass().getResource("/com/example/estilos/green-sudoku.css").toExternalForm()
    );

    Stage stage = new Stage();
    stage.setTitle("🏆 Ranking Sudoku");
    stage.setScene(scene);
    stage.show();
}

    
}
