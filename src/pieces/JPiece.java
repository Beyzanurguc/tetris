package pieces;

import core.Cell;

public class JPiece extends AbstractPiece {

    public JPiece() {
        super(4);
    }

    @Override
    protected int[][] initialMatrix() {
        return new int[][] {
            {0, 7},
            {0, 7},
            {7, 7}
        };
    }

    @Override public int    getColorId()   { return Cell.COLOR_J; }
    @Override public String getPieceName() { return "J"; }

    @Override
    public Piece clonePiece() {
        JPiece copy = new JPiece();
        copyStateTo(copy);
        return copy;
    }
}
