package com.example.model;

public class SudokuBoardLL {

    private CellNode head;

    public SudokuBoardLL() {
        build81();
    }

    private void build81() {
        CellNode prev = null;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                CellNode n = new CellNode(r, c);
                if (head == null) head = n;
                if (prev != null) prev.next = n;
                prev = n;
            }
        }
    }

    public CellNode getNode(int r, int c) {
        CellNode cur = head;
        while (cur != null) {
            if (cur.row == r && cur.col == c) return cur;
            cur = cur.next;
        }
        return null;
    }

    public void clearAll() {
        CellNode cur = head;
        while (cur != null) {
            cur.value = 0;
            cur.fixed = false;
            cur = cur.next;
        }
    }

    // ✅ Validador usando SOLO nodos
    public boolean isValidMove(int r, int c, int value) {
        if (value < 1 || value > 9) return false;

        CellNode target = getNode(r, c);
        if (target == null) return false;
        if (target.fixed) return false;

        int br = (r / 3) * 3;
        int bc = (c / 3) * 3;

        CellNode cur = head;
        while (cur != null) {
            if (cur != target && cur.value == value) {

                boolean sameRow = (cur.row == r);
                boolean sameCol = (cur.col == c);

                boolean sameBlock =
                        cur.row >= br && cur.row < br + 3 &&
                        cur.col >= bc && cur.col < bc + 3;

                if (sameRow || sameCol || sameBlock) return false;
            }
            cur = cur.next;
        }
        return true;
    }
}
