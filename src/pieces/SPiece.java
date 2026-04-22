package pieces;

import core.Cell;

/**
 * SPiece — S parçası (Green)
 *
 *     [ ][ ]
 *  [ ][ ]
 */
public class SPiece extends AbstractPiece {

    public SPiece() {
        super(2);   // 2 benzersiz rotasyon (0° ve 90°)
    }

    @Override
    protected int[][] initialMatrix() {
        return new int[][] {
            {0, 4, 4},
            {4, 4, 0}
        };
    }

    @Override public int    getColorId()   { return Cell.COLOR_S; }
    @Override public String getPieceName() { return "S"; }

    @Override
    public Piece clonePiece() {
        SPiece copy = new SPiece();
        copyStateTo(copy);
        return copy;
    }
}
