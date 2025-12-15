package com.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.model.PartidaSudoku;

public class PartidaSudokuDAO {
    private final String url;
    private final String user;
    private final String pass;

    public PartidaSudokuDAO(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    // INSERT (guardar partida)
    public void insertar(String dificultad, int tiempoSegundos) throws SQLException {
        String sql = "INSERT INTO partidas_sudoku (dificultad, tiempo_segundos) VALUES (?, ?)";

        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, dificultad);
            ps.setInt(2, tiempoSegundos);
            ps.executeUpdate();
        }
    }

    // SELECT (leer todas)
    public PartidaSudoku[] listarTodas() throws SQLException {
        String sql = "SELECT id_partida, dificultad, tiempo_segundos, fecha FROM partidas_sudoku";

        ArrayList<PartidaSudoku> lista = new ArrayList<>();

        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new PartidaSudoku(
                        rs.getInt("id_partida"),
                        rs.getString("dificultad"),
                        rs.getInt("tiempo_segundos"),
                        rs.getTimestamp("fecha").toLocalDateTime()
                ));
            }
        }

        return lista.toArray(new PartidaSudoku[0]);
    }
}
