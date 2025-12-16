package com.example.model;

import java.util.List;
import com.example.model.arbol.ArbolBinarioCandidatos;

public class SudokuHelper {

    public static String ayudaComoTexto(SudokuBoardLL tablero, int fila, int columna) {
        List<Integer> candidatos = tablero.obtenerCandidatos(fila, columna);

        if (candidatos.isEmpty()) {
            return "No hay candidatos (o la celda no está vacía / es fija).";
        }

        ArbolBinarioCandidatos arbol = ArbolBinarioCandidatos.desdeLista(candidatos);

        StringBuilder sb = new StringBuilder();
        sb.append("Celda (").append(fila).append(", ").append(columna).append(")\n\n");
        sb.append("Candidatos: ").append(candidatos).append("\n\n");
        sb.append("Árbol binario (preorden):\n");
        sb.append(arbol.imprimirPreorden());

        return sb.toString();
    }
}
