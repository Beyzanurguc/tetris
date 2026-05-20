package behavioral.command;

import engine.GameEngine;
import pieces.Piece;

public class HoldCommand implements Command {

    private final GameEngine engine;

    private static Piece holdSlot  = null;
    private static boolean usedThisTurn = false;

    private Piece prevHoldSlot;
    private boolean executed;

    public HoldCommand(GameEngine engine) {
        this.engine   = engine;
        this.executed = false;
    }

    @Override
    public boolean execute() {
        if (usedThisTurn) {
            System.out.println("[HoldCommand] Bu turda hold zaten kullanıldı.");
            return false;
        }

        Piece current = engine.getCurrentPiece();
        if (current == null) return false;

        prevHoldSlot = holdSlot;

        if (holdSlot == null) {

            holdSlot = current.clonePiece();
            System.out.println("[HoldCommand] Hold yuvasına alındı: " +
                               holdSlot.getPieceName() +
                               " (yeni parça üretiliyor)");
        } else {

            Piece swapped = holdSlot;
            holdSlot = current.clonePiece();

            System.out.println("[HoldCommand] Hold swap: " +
                               current.getPieceName() + " ↔ " +
                               swapped.getPieceName());
        }

        usedThisTurn = true;
        executed     = true;
        return true;
    }

    @Override
    public void undo() {
        if (!executed) return;
        holdSlot     = prevHoldSlot;
        usedThisTurn = false;
        executed     = false;
        System.out.println("[HoldCommand] Undo → hold geri alındı.");
    }

    public static void resetHoldForNewTurn() {
        usedThisTurn = false;
    }

    public static Piece getHoldSlot() { return holdSlot; }

    @Override
    public String getDescription() {
        return "Hold(slot=" + (holdSlot != null ? holdSlot.getPieceName() : "empty") + ")";
    }
}
