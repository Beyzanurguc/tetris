package pieces;

import core.Cell;

/**
 * OPiece — Kare parça (Yellow)
 *
 *  [ ][ ]
 *  [ ][ ]
 *
 * maxRotations = 1  — kare döndürülmez, rotasyon anlamı yok
 */
public class OPiece extends AbstractPiece {

    public OPiece() {
        super(1);   // döndürülmez
    }

    @Override
    protected int[][] initialMatrix() {
        return new int[][] {
            {2, 2},
            {2, 2}
        };
    }

    @Override public int    getColorId()   { return Cell.COLOR_O; }
    @Override public String getPieceName() { return "O"; }

    @Override
    public Piece clonePiece() {
        OPiece copy = new OPiece();
        copyStateTo(copy);
        return copy;
    }
}
