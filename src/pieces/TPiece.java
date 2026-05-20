package pieces;

import core.Board;
import core.Cell;

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
    protected void onLocked(Board board) {
        int centerRow = this.row + 1;   
        int centerCol = this.col + 1;   

        int filled = 0;
        int[][] corners = {
            {centerRow - 1, centerCol - 1},
            {centerRow - 1, centerCol + 1},
            {centerRow + 1, centerCol - 1},
            {centerRow + 1, centerCol + 1}
        };
        for (int[] corner : corners) {
            int cr = corner[0];
            int cc = corner[1];
            if (board.getGrid().isFilled(cr, cc)) {
                filled++;
            }
        }

        if (filled >= 3) {
            System.out.println("[TPiece] onLocked() -> T-SPIN tespit edildi! ("
                               + filled + "/4 kose dolu)");
        } else {
            System.out.println("[TPiece] onLocked() -> Normal kilitlenme ("
                               + filled + "/4 kose dolu, T-spin degil)");
        }
    }

    @Override
    public Piece clonePiece() {
        TPiece copy = new TPiece();
        copyStateTo(copy);
        return copy;
    }
}
