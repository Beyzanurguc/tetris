package behavioral.command;

import pieces.Piece;
import core.Board;

public class MoveRightCommand implements Command {

    private final Piece piece;
    private final Board board;
    private boolean moved;

    public MoveRightCommand(Piece piece, Board board) {
        this.piece = piece;
        this.board = board;
        this.moved = false;
    }

    @Override
    public boolean execute() {
        int newCol = piece.getCol() + 1;
        if (board.canPlace(piece.getMatrix(), piece.getRow(), newCol)) {
            piece.setCol(newCol);
            board.setCurrentPieceCol(newCol);
            moved = true;
            System.out.println("[MoveRightCommand] Parça sağa taşındı → col=" + newCol);
            return true;
        }
        moved = false;
        return false;
    }

    @Override
    public void undo() {
        if (!moved) return;
        int prevCol = piece.getCol() - 1;   
        piece.setCol(prevCol);
        board.setCurrentPieceCol(prevCol);
        moved = false;
        System.out.println("[MoveRightCommand] Undo → col=" + prevCol);
    }

    @Override
    public String getDescription() { return "MoveRight"; }
}
