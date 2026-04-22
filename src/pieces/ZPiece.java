package pieces;

import core.Cell;

/**
 * ZPiece — Z parçası (Red)
 *
 *  [ ][ ]
 *     [ ][ ]
 */
public class ZPiece extends AbstractPiece {

    public ZPiece() {
        super(2);
    }

    @Override
    protected int[][] initialMatrix() {
        return new int[][] {
            {5, 5, 0},
            {0, 5, 5}
        };
    }

    @Override public int    getColorId()   { return Cell.COLOR_Z; }
    @Override public String getPieceName() { return "Z"; }

    @Override
    public Piece clonePiece() {
        ZPiece copy = new ZPiece();
        copyStateTo(copy);
        return copy;
    }
}
