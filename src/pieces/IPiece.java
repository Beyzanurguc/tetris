package pieces;

import core.Board;
import core.Cell;

public class IPiece extends AbstractPiece {

    public IPiece() {
        super(4);
    }

    @Override
    protected int[][] initialMatrix() {
        return new int[][] {
            {1, 1, 1, 1}
        };
    }

    @Override public int    getColorId()   { return Cell.COLOR_I; }
    @Override public String getPieceName() { return "I"; }

    @Override
    protected void onLocked(Board board) {
        System.out.println("[IPiece] onLocked() -> Yatay satir taramasi tetiklendi! "
                           + "clearLines() cagrilabilir.");
        board.clearLines();
    }

    @Override
    public Piece clonePiece() {
        IPiece copy = new IPiece();
        copyStateTo(copy);
        return copy;
    }
}
