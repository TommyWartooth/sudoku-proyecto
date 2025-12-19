package com.example.model;

public class CellNode {
    public final int row, col;
    public int value;          // 0..9 (0 = vacío)
    public boolean fixed;      // pista
    public CellNode next;      // lista enlazada

    public CellNode(int row, int col) {
        this.row = row;
        this.col = col;
        this.value = 0;
        this.fixed = false;
    }
}
