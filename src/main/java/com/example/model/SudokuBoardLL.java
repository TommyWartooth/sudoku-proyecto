package com.example.model;

public class SudokuBoardLL {

    // =======================
    // LISTA ENLAZADA (81 nodos)
    // =======================
    private CellNode primerNodo;

    // =======================
    // CONSTRUCTOR
    // =======================
    public SudokuBoardLL() {
        construirTableroCon81Celdas();
    }

    // =======================
    // CREACIÓN DEL TABLERO
    // =======================
    private void construirTableroCon81Celdas() {
        CellNode nodoAnterior = null;

        for (int fila = 0; fila < 9; fila++) {
            for (int columna = 0; columna < 9; columna++) {

                CellNode nuevoNodo = new CellNode(fila, columna);

                if (primerNodo == null) {
                    primerNodo = nuevoNodo;
                }

                if (nodoAnterior != null) {
                    nodoAnterior.next = nuevoNodo;
                }

                nodoAnterior = nuevoNodo;
            }
        }
    }

    // =======================
    // GETTERS IMPORTANTES
    // =======================
    public CellNode getPrimerNodo() {
        return primerNodo;
    }

    // (alias por si tu controller/validator usa getNode)
    public CellNode getNode(int fila, int columna) {
        return obtenerCelda(fila, columna);
    }

    // =======================
    // ACCESO A CELDAS
    // =======================
    public CellNode obtenerCelda(int fila, int columna) {
        CellNode actual = primerNodo;

        while (actual != null) {
            if (actual.row == fila && actual.col == columna) {
                return actual;
            }
            actual = actual.next;
        }
        return null;
    }

    // =======================
    // LIMPIAR TABLERO
    // =======================
    public void limpiarTablero() {
        CellNode actual = primerNodo;

        while (actual != null) {
            actual.value = 0;
            actual.fixed = false;
            actual = actual.next;
        }
    }

    // =======================
    // MARCAR FIJAS
    // =======================
    public void marcarCeldasFijasSegunValor() {
        CellNode actual = primerNodo;
        while (actual != null) {
            actual.fixed = (actual.value != 0);
            actual = actual.next;
        }
    }

    // =========================================================
    // VALIDACIÓN DE JUGADA (Sudoku normal)
    // =========================================================
    public boolean movimientoEsValido(int fila, int columna, int numero) {

        if (numero < 1 || numero > 9) return false;

        CellNode celdaObjetivo = obtenerCelda(fila, columna);
        if (celdaObjetivo == null) return false;
        if (celdaObjetivo.fixed) return false;

        int filaBloque = (fila / 3) * 3;
        int columnaBloque = (columna / 3) * 3;

        CellNode actual = primerNodo;
        while (actual != null) {

            if (actual != celdaObjetivo && actual.value == numero) {

                boolean mismaFila = (actual.row == fila);
                boolean mismaColumna = (actual.col == columna);

                boolean mismoBloque =
                        actual.row >= filaBloque && actual.row < filaBloque + 3 &&
                        actual.col >= columnaBloque && actual.col < columnaBloque + 3;

                if (mismaFila || mismaColumna || mismoBloque) {
                    return false;
                }
            }
            actual = actual.next;
        }
        return true;
    }

    // =========================================================
    // VALIDACIÓN DEL TABLERO COMPLETO
    // =========================================================
    public boolean tableroEsValido() {

        for (int fila = 0; fila < 9; fila++) {
            if (!filaEsValida(fila)) return false;
        }

        for (int columna = 0; columna < 9; columna++) {
            if (!columnaEsValida(columna)) return false;
        }

        for (int filaBloque = 0; filaBloque < 9; filaBloque += 3) {
            for (int colBloque = 0; colBloque < 9; colBloque += 3) {
                if (!bloqueEsValido(filaBloque, colBloque)) return false;
            }
        }

        return true;
    }

    public boolean sudokuResuelto() {
        CellNode actual = primerNodo;

        while (actual != null) {
            if (actual.value == 0) return false;
            actual = actual.next;
        }
        return tableroEsValido();
    }

    // =========================================================
    // VALIDACIONES INTERNAS
    // =========================================================
    private boolean filaEsValida(int fila) {
        boolean[] usado = new boolean[10];
        CellNode actual = primerNodo;

        while (actual != null) {
            if (actual.row == fila) {
                int v = actual.value;
                if (v != 0) {
                    if (usado[v]) return false;
                    usado[v] = true;
                }
            }
            actual = actual.next;
        }
        return true;
    }

    private boolean columnaEsValida(int columna) {
        boolean[] usado = new boolean[10];
        CellNode actual = primerNodo;

        while (actual != null) {
            if (actual.col == columna) {
                int v = actual.value;
                if (v != 0) {
                    if (usado[v]) return false;
                    usado[v] = true;
                }
            }
            actual = actual.next;
        }
        return true;
    }

    private boolean bloqueEsValido(int filaInicio, int columnaInicio) {
        boolean[] usado = new boolean[10];
        CellNode actual = primerNodo;

        while (actual != null) {
            boolean dentroDelBloque =
                    actual.row >= filaInicio && actual.row < filaInicio + 3 &&
                    actual.col >= columnaInicio && actual.col < columnaInicio + 3;

            if (dentroDelBloque) {
                int v = actual.value;
                if (v != 0) {
                    if (usado[v]) return false;
                    usado[v] = true;
                }
            }
            actual = actual.next;
        }
        return true;
    }
    
    
}
