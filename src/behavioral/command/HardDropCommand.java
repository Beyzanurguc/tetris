package behavioral.command;

import pieces.Piece;
import core.Board;

public class HardDropCommand implements Command {

    private final Piece piece;
    private final Board board;
    private int droppedRows;   

    public HardDropCommand(Piece piece, Board board) {
        this.piece       = piece;
        this.board       = board;
        this.droppedRows = 0;
    }

    @Override
    public boolean execute() {
        droppedRows = 0;

        while (true) {
            int nextRow = piece.getRow() + 1;
            if (board.canPlace(piece.getMatrix(), nextRow, piece.getCol())) {
                piece.setRow(nextRow);
                board.setCurrentPieceRow(nextRow);
                droppedRows++;
            } else {
                break;
            }
        }
        System.out.println("[HardDropCommand] Hard Drop: " + droppedRows +
                           " satır düştü. Son satır: " + piece.getRow());
        return true;
    }

    @Override
    public void undo() {

        System.out.println("[HardDropCommand] Hard Drop geri alınamaz.");
    }

    @Override
    public String getDescription() {
        return "HardDrop(dropped=" + droppedRows + ")";
    }
}
