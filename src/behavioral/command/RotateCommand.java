package behavioral.command;

import pieces.Piece;

public class RotateCommand implements Command {

    private final Piece  piece;
    private int[][] prevMatrix;   
    private boolean rotated;

    public RotateCommand(Piece piece) {
        this.piece   = piece;
        this.rotated = false;
    }

    @Override
    public boolean execute() {
        if (!piece.canRotate()) {
            System.out.println("[RotateCommand] Rotasyon hakkı kalmadı: " +
                               piece.getPieceName());
            rotated = false;
            return false;
        }

        prevMatrix = piece.getMatrix();           
        piece.rotate();
        rotated = true;
        System.out.println("[RotateCommand] " + piece.getPieceName() + " döndürüldü.");
        return true;
    }

    @Override
    public void undo() {
        if (!rotated || prevMatrix == null) return;

        System.out.println("[RotateCommand] Undo → rotasyon geri alındı (stub, " +
                           "tam undo için AbstractPiece.setMatrix() gereklidir).");
        rotated = false;
    }

    @Override
    public String getDescription() { return "Rotate(" + piece.getPieceName() + ")"; }
}
