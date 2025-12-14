package com.example.model;

public class SudokuBoard {

    private final int[][] grid = new int[9][9];     // valores (0 = vacío)
    private final boolean[][] fixed = new boolean[9][9]; // celdas fijas del puzzle

    public void clearAll() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                grid[r][c] = 0;
                fixed[r][c] = false;
            }
        }
    }

    public int get(int r, int c) {
        return grid[r][c];
    }

    public void set(int r, int c, int value) {
        grid[r][c] = value;
    }

    public boolean isFixed(int r, int c) {
        return fixed[r][c];
    }

    public void setFixed(int r, int c, boolean isFixed) {
        fixed[r][c] = isFixed;
    }

    // ✅ Validación básica Sudoku (fila/columna/bloque)
    public boolean isValidMove(int r, int c, int value) {
        if (value < 1 || value > 9) return false;

        // fila
        for (int j = 0; j < 9; j++) {
            if (j != c && grid[r][j] == value) return false;
        }
        // columna
        for (int i = 0; i < 9; i++) {
            if (i != r && grid[i][c] == value) return false;
        }
        // bloque 3x3
        int br = (r / 3) * 3;
        int bc = (c / 3) * 3;
        for (int i = br; i < br + 3; i++) {
            for (int j = bc; j < bc + 3; j++) {
                if ((i != r || j != c) && grid[i][j] == value) return false;
            }
        }
        return true;
    }
}
