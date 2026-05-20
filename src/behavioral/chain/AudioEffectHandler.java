package behavioral.chain;

import behavioral.observer.GameEvent;
import engine.GameEngine;

public class AudioEffectHandler extends EffectHandler {

    @Override
    public void handle(GameEvent event, GameEngine engine) {
        switch (event.getType()) {
            case LINE_CLEARED:
                int lines = event.getData();
                if (lines >= 4) {
                    System.out.println("[AudioEffectHandler] ♪ TETRIS SESİ! (4 satır)");
                } else {
                    System.out.println("[AudioEffectHandler] ♪ line-clear.wav (" + lines + " satır)");
                }
                break;
            case LEVEL_UP:
                System.out.println("[AudioEffectHandler] ♪ level-up.wav → Seviye " +
                                   event.getData());
                break;
            case GAME_OVER:
                System.out.println("[AudioEffectHandler] ♪ game-over.wav");
                break;
            case PIECE_LANDED:
                System.out.println("[AudioEffectHandler] ♪ land.wav");
                break;
            case COMBO_ACHIEVED:
                System.out.println("[AudioEffectHandler] ♪ combo-x" + event.getData() + ".wav");
                break;
            default:
                System.out.println("[AudioEffectHandler] Bilinmeyen olay sesi: " + event.getType());
        }

        passToNext(event, engine);
    }

    @Override
    public String getHandlerName() { return "AudioEffectHandler"; }
}
