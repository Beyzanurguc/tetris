package behavioral.chain;

import behavioral.observer.GameEvent;
import behavioral.observer.GameEventListener;
import engine.GameEngine;

public class EffectManager implements GameEventListener {

    private final GameEngine          engine;
    private final GravityEffectHandler gravityHandler;
    private final PortalEffectHandler  portalHandler;
    private final ComboEffectHandler   comboHandler;
    private final AudioEffectHandler   audioHandler;

    private final EffectHandler chainHead;

    public EffectManager(GameEngine engine) {
        this.engine        = engine;
        gravityHandler     = new GravityEffectHandler();
        portalHandler      = new PortalEffectHandler();
        comboHandler       = new ComboEffectHandler();
        audioHandler       = new AudioEffectHandler();

        gravityHandler.setNext(portalHandler)
                      .setNext(comboHandler)
                      .setNext(audioHandler);

        chainHead = gravityHandler;

        engine.subscribe(this);
        System.out.println("[EffectManager] Zincir kuruldu: " +
                           "Gravity → Portal → Combo → Audio");
    }

    @Override
    public void onEvent(GameEvent event) {
        process(event);
    }

    public void process(GameEvent event) {
        System.out.println("[EffectManager] Olay işleniyor: " + event);
        chainHead.handle(event, engine);
    }

    public GravityEffectHandler getGravityHandler() { return gravityHandler; }
    public ComboEffectHandler   getComboHandler()   { return comboHandler;   }

    @Override
    public String toString() {
        return "EffectManager [chain: Gravity→Portal→Combo→Audio]";
    }
}
