package behavioral.chain;

import behavioral.observer.GameEvent;
import core.Cell;
import core.Grid;
import engine.GameEngine;

public class PortalEffectHandler extends EffectHandler {

    @Override
    public void handle(GameEvent event, GameEngine engine) {
        if (event.getType() == GameEvent.EventType.PIECE_LANDED) {
            checkPortals(engine);
        }
        passToNext(event, engine);
    }

    private void checkPortals(GameEngine engine) {
        Grid grid = engine.getBoard().getGrid();

        boolean found = false;
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                Cell cell = grid.getCell(r, c);
                if (cell.getColorId() == Cell.COLOR_PORTAL) {
                    System.out.println("[PortalEffectHandler] Portal hücresi tespit edildi: (" +
                                       r + ", " + c + ") — teleport tetiklendi!");
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("[PortalEffectHandler] Portal hücresi yok, atla.");
        }
    }

    @Override
    public String getHandlerName() { return "PortalEffectHandler"; }
}
