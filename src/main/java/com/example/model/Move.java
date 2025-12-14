package com.example.model;

public class Move {
    public final int row, col;
    public final int before; // 0..9
    public final int after;  // 0..9

    public Move(int row, int col, int before, int after) {
        this.row = row;
        this.col = col;
        this.before = before;
        this.after = after;
    }
}