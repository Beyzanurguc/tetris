package pieces;

import core.Cell;

/**
 * TPiece — T parçası (Purple)
 *
 *  [ ][ ][ ]
 *     [ ]
 */
public class TPiece extends AbstractPiece {

    public TPiece() {
        super(4);
    }

    @Override
    protected int[][] initialMatrix() {
        return new int[][] {
            {3, 3, 3},
            {0, 3, 0}
        };
    }

    @Override public int    getColorId()   { return Cell.COLOR_T; }
    @Override public String getPieceName() { return "T"; }

    @Override
    public Piece clonePiece() {
        TPiece copy = new TPiece();
        copyStateTo(copy);
        return copy;
    }
}
