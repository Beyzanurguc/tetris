package pieces;

import core.Board;
import core.Matrix;

public abstract class AbstractPiece implements Piece {

    protected static final int DEFAULT_MAX_ROTATIONS = 4;

    protected Matrix matrix;          
    protected int    maxRotations;    
    protected int    usedRotations;   

    protected int row;
    protected int col;

    protected AbstractPiece(int maxRotations) {
        this.maxRotations  = maxRotations;
        this.usedRotations = 0;
        this.matrix        = new Matrix(initialMatrix());
        this.row           = 0;
        this.col           = 0;
    }

    protected AbstractPiece() {
        this(DEFAULT_MAX_ROTATIONS);
    }

    protected abstract int[][] initialMatrix();

    public final void update(Board board) {
        applyGravity();
        if (checkCollision(board)) {
            lock(board);
            onLocked(board);
        }
    }

    protected void applyGravity() {
        this.row += 1;
    }

    protected boolean checkCollision(Board board) {
        return !board.canPlace(getMatrix(), this.row, this.col);
    }

    protected void lock(Board board) {
        this.row -= 1;   
        board.lockPiece(getMatrix(), getColorId());
        System.out.println("[" + getPieceName() + "-Piece] Kilitlendi ("
                           + this.row + "," + this.col + ")");
    }

    protected void onLocked(Board board) {

    }

    @Override
    public int[][] getMatrix() {

        int[][] result = new int[matrix.getRows()][matrix.getCols()];
        for (int r = 0; r < matrix.getRows(); r++) {
            for (int c = 0; c < matrix.getCols(); c++) {
                result[r][c] = matrix.get(r, c);
            }
        }
        return result;
    }

    @Override
    public Piece rotate() {
        if (!canRotate()) {
            System.out.println("[" + getPieceName() + "] Rotasyon hakki kalmadi!");
            return this;
        }
        matrix = matrix.rotateCW();
        usedRotations++;
        System.out.println("[" + getPieceName() + "] Donduruldu. Kalan hak: " +
                           getRemainingRotations());
        return this;
    }

    @Override
    public boolean canRotate() {
        return usedRotations < maxRotations;
    }

    @Override
    public int getRemainingRotations() {
        return Math.max(0, maxRotations - usedRotations);
    }

    @Override
    public int getUsedRotations() {
        return usedRotations;
    }

    @Override public int  getRow()       { return row; }
    @Override public int  getCol()       { return col; }
    @Override public void setRow(int r)  { this.row = r; }
    @Override public void setCol(int c)  { this.col = c; }

    protected void copyStateTo(AbstractPiece target) {
        target.matrix        = this.matrix.clone();
        target.usedRotations = this.usedRotations;
        target.maxRotations  = this.maxRotations;
        target.row           = this.row;
        target.col           = this.col;
    }

    @Override
    public String toString() {
        return getPieceName() + "-Piece (" + row + "," + col + ") " +
               "rot:" + usedRotations + "/" + maxRotations + "\n" + matrix;
    }
}
