package com.example.model;

public class SudokuValidator {

    public static boolean movimientoValido(
            SudokuBoardLL tablero,
            int fila,
            int columna,
            int valor
    ) {
        if (valor < 1 || valor > 9) return false;

        CellNode objetivo = tablero.getNode(fila, columna);
        if (objetivo == null || objetivo.fixed) return false;

        int filaBloque = (fila / 3) * 3;
        int colBloque  = (columna / 3) * 3;

        CellNode actual = tablero.getPrimerNodo();
        while (actual != null) {
            if (actual != objetivo && actual.value == valor) {

                boolean mismaFila   = actual.row == fila;
                boolean mismaCol    = actual.col == columna;
                boolean mismoBloque =
                        actual.row >= filaBloque && actual.row < filaBloque + 3 &&
                        actual.col >= colBloque  && actual.col < colBloque  + 3;

                if (mismaFila || mismaCol || mismoBloque) return false;
            }
            actual = actual.next;
        }
        return true;
    }


    public static boolean tableroValido(SudokuBoardLL tablero) {

        for (int i = 0; i < 9; i++) {
            if (!filaValida(tablero, i)) return false;
            if (!columnaValida(tablero, i)) return false;
        }

        for (int f = 0; f < 9; f += 3) {
            for (int c = 0; c < 9; c += 3) {
                if (!bloqueValido(tablero, f, c)) return false;
            }
        }
        return true;
    }

    public static boolean sudokuResuelto(SudokuBoardLL tablero) {
        CellNode actual = tablero.getPrimerNodo();
        while (actual != null) {
            if (actual.value == 0) return false;
            actual = actual.next;
        }
        return tableroValido(tablero);
    }


    private static boolean filaValida(SudokuBoardLL tablero, int fila) {
        boolean[] visto = new boolean[10];
        CellNode n = tablero.getPrimerNodo();

        while (n != null) {
            if (n.row == fila && n.value != 0) {
                if (visto[n.value]) return false;
                visto[n.value] = true;
            }
            n = n.next;
        }
        return true;
    }

    private static boolean columnaValida(SudokuBoardLL tablero, int col) {
        boolean[] visto = new boolean[10];
        CellNode n = tablero.getPrimerNodo();

        while (n != null) {
            if (n.col == col && n.value != 0) {
                if (visto[n.value]) return false;
                visto[n.value] = true;
            }
            n = n.next;
        }
        return true;
    }

    private static boolean bloqueValido(
            SudokuBoardLL tablero,
            int filaInicio,
            int colInicio
    ) {
        boolean[] visto = new boolean[10];
        CellNode n = tablero.getPrimerNodo();

        while (n != null) {
            boolean dentro =
                    n.row >= filaInicio && n.row < filaInicio + 3 &&
                    n.col >= colInicio  && n.col < colInicio + 3;

            if (dentro && n.value != 0) {
                if (visto[n.value]) return false;
                visto[n.value] = true;
            }
            n = n.next;
        }
        return true;
    }
}
