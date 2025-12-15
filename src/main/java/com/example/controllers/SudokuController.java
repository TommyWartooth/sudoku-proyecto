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
import com.example.model.SudokuGenerator;
import com.example.model.SudokuSolver;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class SudokuController implements Initializable {
@FXML private ToggleButton btnEasy;
@FXML private ToggleButton btnMedium;
@FXML private ToggleButton btnHard;
@FXML private ToggleButton btnExpert;

private final ToggleGroup grupoDificultad = new ToggleGroup();
private boolean partidaGuardada = false;

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
    private final Deque<Move> pilaMovimientos = new ArrayDeque<>();

    // =======================
    // TIMER
    // =======================
    private Timeline temporizador;
    private int segundosTranscurridos = 0;

    private String dificultadActual = "EASY"; //de momento está así hasta q haya la lógica para elegir dificultad unu

    // ====== DB (ajusta a tu BD) ======
    private final PartidaSudokuDAO dao = new PartidaSudokuDAO(
        "jdbc:postgresql://localhost:5432/Sudoku",
        "postgres",
        "frisky"
    );

    @Override

public void initialize(URL url, ResourceBundle rb) {
    
    construirTableroUI();
    conectarTecladoNumerico();
    conectarBotonDeshacer();
    conectarBotonNuevoJuego();
    iniciarTemporizador();

    configurarDificultadUI();
    aplicarDificultadInicial();
}


private void configurarDificultadUI() {
    btnEasy.setToggleGroup(grupoDificultad);
    btnMedium.setToggleGroup(grupoDificultad);
    btnHard.setToggleGroup(grupoDificultad);
    btnExpert.setToggleGroup(grupoDificultad);
}

private void aplicarDificultadInicial() {
    switch (dificultadActual) {
        case "EASY"   -> btnEasy.setSelected(true);
        case "HARD"   -> btnHard.setSelected(true);
        case "EXPERT" -> btnExpert.setSelected(true);
        default       -> btnMedium.setSelected(true);
    }
    refrescarClaseActiva();
}

@FXML
private void cambiarDificultad() {
    ToggleButton sel = (ToggleButton) grupoDificultad.getSelectedToggle();
    if (sel == null) return;

    dificultadActual = sel.getText().toUpperCase(); // EASY, MEDIUM...

    refrescarClaseActiva();
    nuevoJuegoConDificultad(dificultadActual);
}

private void refrescarClaseActiva() {
    btnEasy.getStyleClass().remove("top-pill--active");
    btnMedium.getStyleClass().remove("top-pill--active");
    btnHard.getStyleClass().remove("top-pill--active");
    btnExpert.getStyleClass().remove("top-pill--active");

    ToggleButton sel = (ToggleButton) grupoDificultad.getSelectedToggle();
    if (sel != null) sel.getStyleClass().add("top-pill--active");
}


    // =========================================================
    // TABLERO (UI)
    // =========================================================
    private void construirTableroUI() {
        board.getChildren().clear();

        for (int fila = 0; fila < 9; fila++) {
            for (int columna = 0; columna < 9; columna++) {

                Button botonCelda = new Button("");
                botonCelda.setFocusTraversable(false);

                botonCelda.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setHgrow(botonCelda, Priority.ALWAYS);
                GridPane.setVgrow(botonCelda, Priority.ALWAYS);

                botonCelda.getStyleClass().add("cell");

                // bordes 3x3
                if (fila % 3 == 0) botonCelda.getStyleClass().add("cell-b3-top");
                if (columna % 3 == 0) botonCelda.getStyleClass().add("cell-b3-left");
                if (fila % 3 == 2) botonCelda.getStyleClass().add("cell-b3-bottom");
                if (columna % 3 == 2) botonCelda.getStyleClass().add("cell-b3-right");

                botonCelda.setUserData(new int[]{fila, columna});
                botonCelda.setOnAction(e -> seleccionarCelda(botonCelda));

                board.add(botonCelda, columna, fila);
            }
        }
    }

    private void seleccionarCelda(Button botonCelda) {
        if (celdaSeleccionada != null) {
            celdaSeleccionada.getStyleClass().remove("cell--selected");
        }
        celdaSeleccionada = botonCelda;
        celdaSeleccionada.getStyleClass().add("cell--selected");
    }

    // =========================================================
    // KEYPAD
    // =========================================================
    private void conectarTecladoNumerico() {
        for (Node nodo : keypad.getChildren()) {
            if (nodo instanceof Button) {
                Button botonNumero = (Button) nodo;
                String texto = botonNumero.getText();

                if (texto != null && texto.matches("[1-9]")) {
                    botonNumero.setOnAction(e -> intentarColocarNumero(texto));
                }
            }
        }
    }

    private void intentarColocarNumero(String numeroTexto) {
        if (celdaSeleccionada == null) return;

        int[] pos = (int[]) celdaSeleccionada.getUserData();
        int fila = pos[0];
        int columna = pos[1];

        // ✅ nombres obvios del modelo
        CellNode nodoCelda = model.obtenerCelda(fila, columna);
        if (nodoCelda == null || nodoCelda.fixed) return;

        int numeroNuevo = Integer.parseInt(numeroTexto);
        int numeroAnterior = nodoCelda.value;

        if (numeroAnterior == numeroNuevo) return;

        // ✅ validación Sudoku normal (nombre obvio)
        if (!model.movimientoEsValido(fila, columna, numeroNuevo)) {
            return;
        }

        // ✅ guardar en pila (Undo)
        pilaMovimientos.push(new Move(fila, columna, numeroAnterior, numeroNuevo));

        // aplicar cambio en modelo + UI
        nodoCelda.value = numeroNuevo;
        celdaSeleccionada.setText(numeroTexto);

        // ✅ chequear si ya se resolvió
       if (model.sudokuResuelto() && !partidaGuardada) {
        System.out.println("🎉 Sudoku resuelto correctamente!");
        guardarPartidaEnBD();
        partidaGuardada = true;
    }
    }

    // =========================================================
    // UNDO
    // =========================================================
    private void conectarBotonDeshacer() {
        if (btnUndo != null) {
            btnUndo.setOnAction(e -> deshacerMovimiento());
        }
    }

    private void deshacerMovimiento() {
        if (pilaMovimientos.isEmpty()) return;

        Move movimiento = pilaMovimientos.pop();

        // ✅ nombre obvio
        CellNode nodoCelda = model.obtenerCelda(movimiento.row, movimiento.col);
        if (nodoCelda == null) return;

        nodoCelda.value = movimiento.before;

        Button botonCelda = getBotonCelda(movimiento.row, movimiento.col);
        if (botonCelda != null) {
            botonCelda.setText(movimiento.before == 0 ? "" : String.valueOf(movimiento.before));
            seleccionarCelda(botonCelda);
        }
    }

    // =========================================================
    // NEW GAME
    // =========================================================
    private void conectarBotonNuevoJuego() {
        if (btnNewGame != null) {
            btnNewGame.setOnAction(e -> nuevoJuego());
        }
    }
private final SudokuGenerator generator = new SudokuGenerator();
private void nuevoJuego() {
       nuevoJuegoConDificultad(dificultadActual);
}
private void pintarTableroDesdeModelo() {
    for (Node nodo : board.getChildren()) {
        if (!(nodo instanceof Button)) continue;
        Button b = (Button) nodo;

        int[] pos = (int[]) b.getUserData();
        int fila = pos[0], col = pos[1];

        CellNode celda = model.obtenerCelda(fila, col);

        b.setText(celda.value == 0 ? "" : String.valueOf(celda.value));

        b.getStyleClass().remove("cell--fixed");
        if (celda.fixed) b.getStyleClass().add("cell--fixed");
    }
}


    // =========================================================
    // TIMER
    // =========================================================
    private void iniciarTemporizador() {
        if (temporizador != null) temporizador.stop();

        temporizador = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            segundosTranscurridos++;
            actualizarLabelTiempo();
        }));
        temporizador.setCycleCount(Timeline.INDEFINITE);
        temporizador.play();

        actualizarLabelTiempo();
    }

    private void resetearTemporizador() {
        segundosTranscurridos = 0;
        actualizarLabelTiempo();
    }

    private void actualizarLabelTiempo() {
        if (lblTime == null) return;

        int minutos = segundosTranscurridos / 60;
        int segundos = segundosTranscurridos % 60;
        lblTime.setText(String.format("%02d:%02d", minutos, segundos));
    }

    // =========================================================
    // HELPERS
    // =========================================================
    private Button getBotonCelda(int fila, int columna) {
        for (Node nodo : board.getChildren()) {
            if (nodo instanceof Button) {
                Button b = (Button) nodo;

                int filaUI = GridPane.getRowIndex(b) == null ? 0 : GridPane.getRowIndex(b);
                int colUI  = GridPane.getColumnIndex(b) == null ? 0 : GridPane.getColumnIndex(b);

                if (filaUI == fila && colUI == columna) return b;
            }
        }
        return null;
    }

    // =======================
    // GUARDAR PARTIDA EN BD
    // =======================
    private void guardarPartidaEnBD() {
        try {
            dao.insertar(dificultadActual, segundosTranscurridos);

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
        segundosTranscurridos = 123;
        guardarPartidaEnBD();
    }

   


    @FXML
private void resolverSudoku() {

    // 1) pedir una copia resuelta
    SudokuBoardLL solucion = SudokuSolver.resolverCopia(model);

    if (solucion == null) {
        System.out.println("❌ Este Sudoku NO tiene solución");
        return;
    }

    System.out.println("✅ Sudoku solucionado correctamente");

    // 2) pintar la solución en la UI
    CellNode n = solucion.getPrimerNodo();
    while (n != null) {
        Button boton = getBotonCelda(n.row, n.col);
        if (boton != null) {
            boton.setText(String.valueOf(n.value));
        }
        n = n.next;
    }
}
public void setDificultadInicial(String diff) {
    if (diff == null || diff.isBlank()) diff = "MEDIUM";

    this.dificultadActual = diff.toUpperCase();

    // marcar el toggle correcto (si ya existe en UI)
    if (btnEasy != null) {
        switch (dificultadActual) {
            case "EASY" -> btnEasy.setSelected(true);
            case "HARD" -> btnHard.setSelected(true);
            case "EXPERT" -> btnExpert.setSelected(true);
            default -> btnMedium.setSelected(true);
        }
        refrescarClaseActiva();
    }

    // ✅ aquí se genera el tablero al entrar
    nuevoJuegoConDificultad(dificultadActual);
}


private void nuevoJuegoConDificultad(String diff) {
    pilaMovimientos.clear();
    model.limpiarTablero();
    resetearTemporizador();

    partidaGuardada = false;
    SudokuGenerator.Dificultad d = SudokuGenerator.Dificultad.valueOf(diff);
    generator.generarNuevoPuzzle(model, d);

    pintarTableroDesdeModelo();
}

}
