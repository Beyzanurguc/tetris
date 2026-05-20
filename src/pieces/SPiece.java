package pieces;

import core.Cell;

public class SPiece extends AbstractPiece {

    public SPiece() {
        super(2);   
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
