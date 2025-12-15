package com.example.controllers;

import java.sql.SQLException;

import com.example.dao.PartidaSudokuDAO;
import com.example.model.PartidaSudoku;
import com.example.utils.OrdenamientoSudoku;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RankingController {

    @FXML private VBox rankingBox;

    private final PartidaSudokuDAO dao = new PartidaSudokuDAO(
        "jdbc:postgresql://localhost:5432/Sudoku",
        "postgres",
        "frisky"
    );

    @FXML
    public void initialize() {
        cargarRanking();
    }

    private void cargarRanking() {
        try {
            PartidaSudoku[] partidas = dao.listarTodas();
            OrdenamientoSudoku.ordenarPorTiempoAsc(partidas);

            rankingBox.getChildren().clear();

            for (int i = 0; i < partidas.length; i++) {
                rankingBox.getChildren().add(
                    filaRanking(i + 1, partidas[i])
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox filaRanking(int pos, PartidaSudoku p) {
        HBox fila = new HBox(20);
        fila.getStyleClass().add("ranking-row");

        Label lblPos = new Label(String.valueOf(pos));
        lblPos.setPrefWidth(40);

        if (pos == 1) lblPos.getStyleClass().add("rank-gold");
        else if (pos == 2) lblPos.getStyleClass().add("rank-silver");
        else if (pos == 3) lblPos.getStyleClass().add("rank-bronze");

        Label lblDif = new Label(p.getDificultad());
        lblDif.setPrefWidth(100);

        Label lblTiempo = new Label(p.getTiempoSegundos() + " s");
        lblTiempo.setPrefWidth(100);

        Label lblFecha = new Label(
            p.getFecha().toLocalDate().toString()
        );

        fila.getChildren().addAll(
            lblPos, lblDif, lblTiempo, lblFecha
        );

        return fila;
    }
}
