package com.example.utils;

import com.example.model.PartidaSudoku;

public class OrdenamientoSudoku {

    public static void ordenarPorTiempoAsc(PartidaSudoku[] a) {
        for (int i = 1; i < a.length; i++) {
            PartidaSudoku key = a[i];
            int j = i - 1;

            while (j >= 0 && esPeor(a[j], key)) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    private static boolean esPeor(PartidaSudoku x, PartidaSudoku y) {
        if (x.getTiempoSegundos() != y.getTiempoSegundos()) {
            return x.getTiempoSegundos() > y.getTiempoSegundos();
        }
   
        return x.getFecha().isBefore(y.getFecha());
    }
}
