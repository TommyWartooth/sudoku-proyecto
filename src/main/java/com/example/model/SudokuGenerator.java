package com.example.model;

import java.util.*;

public class SudokuGenerator {

    public enum Dificultad { EASY, MEDIUM, HARD, EXPERT }

    private final Random rnd = new Random();

    public void generarNuevoPuzzle(SudokuBoardLL tablero, Dificultad dificultad) {
        tablero.limpiarTablero();

        if (!llenarTableroCompleto(tablero)) {
            throw new IllegalStateException("No se pudo generar un Sudoku completo.");
        }

        int aEliminar = cantidadCeldasAEliminar(dificultad);
        quitarCeldasManteniendoSolucion(tablero, aEliminar);

        tablero.marcarCeldasFijasSegunValor();
        // al final de generarNuevoPuzzle(...)
SudokuBoardLL copia = SudokuSolver.resolverCopia(tablero);
if (copia == null) {
    throw new IllegalStateException("Generé un puzzle sin solución (imposible).");
}

    }

    // =====================================================
    // 1) Llenar tablero completo (backtracking)
    // =====================================================
    private boolean llenarTableroCompleto(SudokuBoardLL tablero) {
        CellNode vacia = buscarCeldaVacia(tablero);
        if (vacia == null) return true;

        List<Integer> nums = numerosMezclados();
        for (int num : nums) {
            if (movimientoValidoIgnorandoFixed(tablero, vacia.row, vacia.col, num)) {
                vacia.value = num;
                if (llenarTableroCompleto(tablero)) return true;
                vacia.value = 0;
            }
        }
        return false;
    }

    private CellNode buscarCeldaVacia(SudokuBoardLL tablero) {
        CellNode n = tablero.getPrimerNodo();
        while (n != null) {
            if (n.value == 0) return n;
            n = n.next;
        }
        return null;
    }

    private List<Integer> numerosMezclados() {
        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 9; i++) nums.add(i);
        Collections.shuffle(nums, rnd);
        return nums;
    }

    // =====================================================
    // 2) Quitar celdas pero SIN romper la resolubilidad
    // =====================================================
    private void quitarCeldasManteniendoSolucion(SudokuBoardLL tablero, int aEliminar) {
        List<CellNode> celdas = todasLasCeldas(tablero);
        Collections.shuffle(celdas, rnd);

        int quitadas = 0;

        for (CellNode celda : celdas) {
            if (quitadas >= aEliminar) break;
            if (celda.value == 0) continue;

            int backup = celda.value;
            celda.value = 0;

            // ✅ chequeo fuerte: debe seguir teniendo al menos 1 solución
            if (!tieneAlMenosUnaSolucion(tablero)) {
                celda.value = backup; // revertir
                continue;
            }

            quitadas++;
        }
    }

    private List<CellNode> todasLasCeldas(SudokuBoardLL tablero) {
        List<CellNode> list = new ArrayList<>();
        CellNode n = tablero.getPrimerNodo();
        while (n != null) {
            list.add(n);
            n = n.next;
        }
        return list;
    }

    // =====================================================
    // 3) “Tiene al menos una solución”
    // =====================================================
    private boolean tieneAlMenosUnaSolucion(SudokuBoardLL original) {
        SudokuBoardLL copia = copiarTablero(original);
        return resolverBacktracking(copia);
    }

    private SudokuBoardLL copiarTablero(SudokuBoardLL original) {
        SudokuBoardLL copia = new SudokuBoardLL();

        CellNode a = original.getPrimerNodo();
        CellNode b = copia.getPrimerNodo();

        while (a != null && b != null) {
            b.value = a.value;
            b.fixed = false; // solver no usa fixed
            a = a.next;
            b = b.next;
        }
        return copia;
    }

    private boolean resolverBacktracking(SudokuBoardLL tablero) {
        CellNode vacia = buscarCeldaVacia(tablero);
        if (vacia == null) return true;

        for (int num : numerosMezclados()) {
            if (movimientoValidoIgnorandoFixed(tablero, vacia.row, vacia.col, num)) {
                vacia.value = num;
                if (resolverBacktracking(tablero)) return true;
                vacia.value = 0;
            }
        }
        return false;
    }

    // =====================================================
    // Validación SOLO reglas Sudoku (ignora fixed)
    // =====================================================
    private boolean movimientoValidoIgnorandoFixed(SudokuBoardLL tablero, int fila, int col, int valor) {
        if (valor < 1 || valor > 9) return false;

        int filaBloque = (fila / 3) * 3;
        int colBloque  = (col / 3) * 3;

        CellNode n = tablero.getPrimerNodo();
        while (n != null) {
            if (n.row == fila && n.col == col) { n = n.next; continue; }

            if (n.value == valor) {
                boolean mismaFila = (n.row == fila);
                boolean mismaCol  = (n.col == col);

                boolean mismoBloque =
                        n.row >= filaBloque && n.row < filaBloque + 3 &&
                        n.col >= colBloque  && n.col < colBloque  + 3;

                if (mismaFila || mismaCol || mismoBloque) return false;
            }
            n = n.next;
        }
        return true;
    }

    private int cantidadCeldasAEliminar(Dificultad d) {
        return switch (d) {
            case EASY   -> 36;
            case MEDIUM -> 45;
            case HARD   -> 52;
            case EXPERT -> 58;
        };
    }
    
}

