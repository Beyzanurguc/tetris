package pieces;

import core.Cell;

public class OPiece extends AbstractPiece {

    public OPiece() {
        super(1);   
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
