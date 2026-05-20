package behavioral.state;

import engine.GameEngine;

public class PausedState implements GameState {

    public static final int KEY_PAUSE = 80;   
    public static final int KEY_ESC   = 27;

    @Override
    public void onEnter(GameEngine engine) {
        engine.pause();   
        System.out.println("[PausedState] Oyun duraklatıldı.");
        System.out.println("  [P]   Devam Et");
        System.out.println("  [ESC] Oyunu Bitir");
    }

    @Override
    public void onExit(GameEngine engine) {
        System.out.println("[PausedState] Duraklatma sona eriyor.");
    }

    @Override
    public void handleInput(GameEngine engine, int key) {
        if (key == KEY_PAUSE) {
            engine.resume();
            engine.changeState(new PlayingState());
        } else if (key == KEY_ESC) {
            engine.endGame();
            engine.changeState(new GameOverState());
        }
    }

    @Override
    public void update(GameEngine engine) {

        System.out.println("[PausedState] update() → donduruldu.");
    }

    @Override
    public void render(GameEngine engine) {
        System.out.println("[PausedState] render() → DURAKLATILDI ekranı.");
    }

    @Override
    public String toString() { return "PausedState"; }
}
