package pieces;

import core.Cell;

public class LPiece extends AbstractPiece {

    public LPiece() {
        super(4);
    }

    @Override
    protected int[][] initialMatrix() {
        return new int[][] {
            {6, 0},
            {6, 0},
            {6, 6}
        };
    }

    @Override public int    getColorId()   { return Cell.COLOR_L; }
    @Override public String getPieceName() { return "L"; }

    @Override
    public Piece clonePiece() {
        LPiece copy = new LPiece();
        copyStateTo(copy);
        return copy;
    }
}
