package com.example.model;

import java.time.LocalDateTime;

public class PartidaSudoku {

    private final int idPartida;
    private final String dificultad;
    private final int tiempoSegundos;
    private final LocalDateTime fecha;

    public PartidaSudoku(int idPartida, String dificultad, int tiempoSegundos, LocalDateTime fecha) {
        this.idPartida = idPartida;
        this.dificultad = dificultad;
        this.tiempoSegundos = tiempoSegundos;
        this.fecha = fecha;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public String getDificultad() {
        return dificultad;
    }

    public int getTiempoSegundos() {
        return tiempoSegundos;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
