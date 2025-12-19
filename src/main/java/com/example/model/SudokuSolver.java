package com.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuSolver {

    private static final Random rnd = new Random();


    public static boolean resolver(SudokuBoardLL tablero) {
        CellNode mejor = buscarMejorCeldaVacia(tablero);
        if (mejor == null) return true; 

        List<Integer> candidatos = candidatosPara(tablero, mejor.row, mejor.col);

        if (candidatos.isEmpty()) return false;

      
        Collections.shuffle(candidatos, rnd);

        for (int num : candidatos) {
            mejor.value = num;
            if (resolver(tablero)) return true;
            mejor.value = 0; // backtrack
        }
        return false;
    }

    public static SudokuBoardLL resolverCopia(SudokuBoardLL original) {
        SudokuBoardLL copia = copiarTablero(original);
        if (resolver(copia)) return copia;
        return null;
    }

   
    private static CellNode buscarMejorCeldaVacia(SudokuBoardLL tablero) {
        CellNode n = tablero.getPrimerNodo();

        CellNode mejor = null;
        int mejorCant = Integer.MAX_VALUE;
        

        while (n != null) {
            if (n.value == 0) {
                List<Integer> cand = candidatosPara(tablero, n.row, n.col);


                if (cand.isEmpty()) return n; 

                if (cand.size() < mejorCant) {
                    mejorCant = cand.size();
                    mejor = n;
                    if (mejorCant == 1) return mejor; // no se puede mejorar
                }
            }
            n = n.next;
        }
        return mejor; 
    }
    private static List<Integer> candidatosPara(SudokuBoardLL tablero, int fila, int col) {
        boolean[] usado = new boolean[10]; // usado[v] = true si ya está en conflicto

        int filaBloque = (fila / 3) * 3;
        int colBloque  = (col / 3) * 3;

        CellNode n = tablero.getPrimerNodo();
        while (n != null) {
            if (n.value != 0) {
                boolean mismaFila = (n.row == fila);
                boolean mismaCol  = (n.col == col);

                boolean mismoBloque =
                        n.row >= filaBloque && n.row < filaBloque + 3 &&
                        n.col >= colBloque  && n.col < colBloque  + 3;

                if (mismaFila || mismaCol || mismoBloque) {
                    usado[n.value] = true;
                }
            }
            n = n.next;
        }

        List<Integer> cand = new ArrayList<>();
        for (int v = 1; v <= 9; v++) {
            if (!usado[v]) cand.add(v);
        }
        return cand;
    }

  
    private static SudokuBoardLL copiarTablero(SudokuBoardLL original) {
        SudokuBoardLL copia = new SudokuBoardLL();

        CellNode a = original.getPrimerNodo();
        CellNode b = copia.getPrimerNodo();

        while (a != null && b != null) {
            b.value = a.value;
            b.fixed = false;
            a = a.next;
            b = b.next;
        }
        return copia;
    }
}
