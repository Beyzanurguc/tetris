package pieces;

public interface Piece {

    int[][] getMatrix();

    int getColorId();

    String getPieceName();

    Piece rotate();

    boolean canRotate();

    int getRemainingRotations();

    int getUsedRotations();

    Piece clonePiece();

    int getRow();
    int getCol();
    void setRow(int row);
    void setCol(int col);
}
