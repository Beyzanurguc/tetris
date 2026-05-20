package behavioral.chain;

import behavioral.observer.GameEvent;
import engine.GameEngine;

public abstract class EffectHandler {

    protected EffectHandler nextHandler;

    public EffectHandler setNext(EffectHandler next) {
        this.nextHandler = next;
        return next;
    }

    public abstract void handle(GameEvent event, GameEngine engine);

    protected void passToNext(GameEvent event, GameEngine engine) {
        if (nextHandler != null) {
            nextHandler.handle(event, engine);
        }
    }

    public abstract String getHandlerName();
}
