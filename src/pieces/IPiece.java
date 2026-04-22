package pieces;

import core.Cell;

/**
 * IPiece — Çubuk parça (Cyan)
 *
 *  [ ][ ][ ][ ]
 *
 * maxRotations = 2  (dikey / yatay — 4'te tekrar aynı forma döner,
 *                    2 yeterli ama 4 de geçerli; tasarım tercihine göre)
 */
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
    public Piece clonePiece() {
        IPiece copy = new IPiece();
        copyStateTo(copy);
        return copy;
    }
}
