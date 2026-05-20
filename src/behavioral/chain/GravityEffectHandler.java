package behavioral.chain;

import behavioral.observer.GameEvent;
import bridge.gravity.VariableGravity;
import engine.GameEngine;

public class GravityEffectHandler extends EffectHandler {

    private static final double SPEED_INCREMENT = 0.5;

    private static final double BASE_MULTIPLIER = 1.0;

    @Override
    public void handle(GameEvent event, GameEngine engine) {
        if (event.getType() == GameEvent.EventType.LEVEL_UP) {
            int level     = event.getData();
            double mult   = BASE_MULTIPLIER + (level - 1) * SPEED_INCREMENT;

            engine.setGravityImplementor(new VariableGravity(mult, 1, -0.2));
            System.out.println("[GravityEffectHandler] Seviye " + level +
                               " → VariableGravity x" + mult + " devrede.");
        }

        passToNext(event, engine);
    }

    @Override
    public String getHandlerName() { return "GravityEffectHandler"; }
}
