package com.example.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ResourceBundle;

import com.example.dao.PartidaSudokuDAO;
import com.example.model.CellNode;
import com.example.model.Move;
import com.example.model.PartidaSudoku;
import com.example.model.SudokuBoardLL;
import com.example.utils.OrdenamientoSudoku;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;

public class SudokuController implements Initializable {

    // =======================
    // FXML
    // =======================
    @FXML private GridPane board;
    @FXML private GridPane keypad;
    @FXML private Button btnUndo;
    @FXML private Button btnNewGame;
    @FXML private Label lblTime;

    // =======================
    // UI
    // =======================
    private Button celdaSeleccionada;

    // =======================
    // MODELO (LinkedList)
    // =======================
    private final SudokuBoardLL model = new SudokuBoardLL();

    // =======================
    // PILA (Undo)
    // =======================
    private final Deque<Move> historial = new ArrayDeque<>();

    // =======================
    // TIMER
    // =======================
    private Timeline timer;
    private int secondsElapsed = 0;

    private String dificultadActual = "EASY"; //de momento está así hasta q haya la lógica para elegir dificultad unu

    // ====== DB (ajusta a tu BD) ======
    private final PartidaSudokuDAO dao = new PartidaSudokuDAO(
        "jdbc:postgresql://localhost:5432/Sudoku",
        "postgres",
        " "
    );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        construirTablero();
        conectarKeypad();
        conectarUndo();
        conectarNewGame();
        iniciarTimer();
        testBD();
    }

    // =========================================================
    // TABLERO (UI)
    // =========================================================
    private void construirTablero() {
        board.getChildren().clear();

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {

                Button cell = new Button("");
                cell.setFocusTraversable(false);

                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setHgrow(cell, Priority.ALWAYS);
                GridPane.setVgrow(cell, Priority.ALWAYS);

                cell.getStyleClass().add("cell");

                // bordes 3x3
                if (r % 3 == 0) cell.getStyleClass().add("cell-b3-top");
                if (c % 3 == 0) cell.getStyleClass().add("cell-b3-left");
                if (r % 3 == 2) cell.getStyleClass().add("cell-b3-bottom");
                if (c % 3 == 2) cell.getStyleClass().add("cell-b3-right");

                cell.setUserData(new int[]{r, c});
                cell.setOnAction(e -> seleccionarCelda(cell));

                board.add(cell, c, r);
            }
        }
    }

    private void seleccionarCelda(Button cell) {
        if (celdaSeleccionada != null) {
            celdaSeleccionada.getStyleClass().remove("cell--selected");
        }
        celdaSeleccionada = cell;
        celdaSeleccionada.getStyleClass().add("cell--selected");
    }

    // =========================================================
    // KEYPAD
    // =========================================================
    private void conectarKeypad() {
        for (Node n : keypad.getChildren()) {
            if (n instanceof Button) {
                Button b = (Button) n;
                String txt = b.getText();
                if (txt != null && txt.matches("[1-9]")) {
                    b.setOnAction(e -> intentarPonerNumero(txt));
                }
            }
        }
    }

    private void intentarPonerNumero(String numero) {
        if (celdaSeleccionada == null) return;

        int[] pos = (int[]) celdaSeleccionada.getUserData();
        int r = pos[0], c = pos[1];

        CellNode node = model.getNode(r, c);
        if (node == null || node.fixed) return;

        int despues = Integer.parseInt(numero);
        int antes = node.value;

        if (antes == despues) return;

        if (!model.isValidMove(r, c, despues)) {
            // luego puedes pintar error visual
            return;
        }

        historial.push(new Move(r, c, antes, despues));

        node.value = despues;
        celdaSeleccionada.setText(numero);
    }

    // =========================================================
    // UNDO
    // =========================================================
    private void conectarUndo() {
        if (btnUndo != null) {
            btnUndo.setOnAction(e -> deshacer());
        }
    }

    private void deshacer() {
        if (historial.isEmpty()) return;

        Move m = historial.pop();
        CellNode node = model.getNode(m.row, m.col);
        if (node == null) return;

        node.value = m.before;

        Button cell = getCell(m.row, m.col);
        if (cell != null) {
            cell.setText(m.before == 0 ? "" : String.valueOf(m.before));
            seleccionarCelda(cell);
        }
    }

    // =========================================================
    // NEW GAME
    // =========================================================
    private void conectarNewGame() {
        if (btnNewGame != null) {
            btnNewGame.setOnAction(e -> nuevoJuego());
        }
    }

    private void nuevoJuego() {
        historial.clear();
        model.clearAll();
        resetTimer();

        if (celdaSeleccionada != null) {
            celdaSeleccionada.getStyleClass().remove("cell--selected");
            celdaSeleccionada = null;
        }

        for (Node n : board.getChildren()) {
            if (n instanceof Button) {
                ((Button) n).setText("");
            }
        }
    }

    // =========================================================
    // TIMER
    // =========================================================
    private void iniciarTimer() {
        if (timer != null) timer.stop();

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            actualizarLabelTiempo();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        actualizarLabelTiempo();
    }

    private void resetTimer() {
        secondsElapsed = 0;
        actualizarLabelTiempo();
    }

    private void actualizarLabelTiempo() {
        if (lblTime == null) return;
        int min = secondsElapsed / 60;
        int sec = secondsElapsed % 60;
        lblTime.setText(String.format("%02d:%02d", min, sec));
    }

    // =========================================================
    // HELPERS
    // =========================================================
    private Button getCell(int row, int col) {
        for (Node n : board.getChildren()) {
            if (n instanceof Button) {
                Button b = (Button) n;
                int rr = GridPane.getRowIndex(b) == null ? 0 : GridPane.getRowIndex(b);
                int cc = GridPane.getColumnIndex(b) == null ? 0 : GridPane.getColumnIndex(b);
                if (rr == row && cc == col) return b;
            }
        }
        return null;
    }

    // =======================
    // GUARDAR PARTIDA EN BD
    // =======================
    private void guardarPartidaEnBD() {
        try {
            dao.insertar(dificultadActual, secondsElapsed);

            PartidaSudoku[] partidas = dao.listarTodas();
            OrdenamientoSudoku.ordenarPorTiempoAsc(partidas);

            System.out.println("=== RANKING (menor tiempo mejor) ===");
            for (int i = 0; i < partidas.length; i++) {
                PartidaSudoku p = partidas[i];
                System.out.println((i + 1) + ") " + p.getDificultad()
                        + " - " + p.getTiempoSegundos() + "s - " + p.getFecha());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Solo para prueba rápida (actívalo en initialize si quieres)
    private void probarGuardarRanking() {
        secondsElapsed = 123;
        guardarPartidaEnBD();
    }

    private void testBD() {
    try {
        // 1) Inserta una partida de prueba en la BD
        dao.insertar("EASY", 88);

        // 2) Lee todas las partidas
        PartidaSudoku[] partidas = dao.listarTodas();

        // 3) Ordena por tu algoritmo
        OrdenamientoSudoku.ordenarPorTiempoAsc(partidas);

        // 4) Imprime en consola
        System.out.println("=== PARTIDAS EN BD ===");
        for (PartidaSudoku p : partidas) {
            System.out.println(
                p.getIdPartida() + " | " +
                p.getDificultad() + " | " +
                p.getTiempoSegundos() + "s | " +
                p.getFecha()
            );
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}



}
