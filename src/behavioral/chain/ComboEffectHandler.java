package behavioral.chain;

import behavioral.observer.GameEvent;
import engine.GameEngine;

public class ComboEffectHandler extends EffectHandler {

    private int comboCount;   

    public ComboEffectHandler() {
        this.comboCount = 0;
    }

    @Override
    public void handle(GameEvent event, GameEngine engine) {
        if (event.getType() == GameEvent.EventType.LINE_CLEARED) {
            comboCount++;
            System.out.println("[ComboEffectHandler] Combo sayacı: " + comboCount);

            if (comboCount >= 2) {

                GameEvent comboEvent = new GameEvent(
                        GameEvent.EventType.COMBO_ACHIEVED, comboCount);
                engine.notifyListeners(comboEvent);
                System.out.println("[ComboEffectHandler] COMBO x" + comboCount + " yayıldı!");
            }
        } else if (event.getType() == GameEvent.EventType.PIECE_LANDED) {

        }
        passToNext(event, engine);
    }

    public void resetCombo() {
        if (comboCount > 0) {
            System.out.println("[ComboEffectHandler] Combo sıfırlandı (önceki: " + comboCount + ")");
            comboCount = 0;
        }
    }

    public int getComboCount() { return comboCount; }

    @Override
    public String getHandlerName() { return "ComboEffectHandler"; }
}
