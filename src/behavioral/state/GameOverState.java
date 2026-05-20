package behavioral.state;

import engine.GameEngine;

public class GameOverState implements GameState {

    public static final int KEY_ENTER = 10;
    public static final int KEY_ESC   = 27;

    @Override
    public void onEnter(GameEngine engine) {
        System.out.println("[GameOverState] === OYUN BİTTİ ===");
        System.out.println("[GameOverState] Son Skor: " + engine.getBoard().getScore());
        System.out.println("[GameOverState] Son Seviye: " + engine.getBoard().getLevel());
        System.out.println("  [ENTER] Tekrar Oyna → Menü");
        System.out.println("  [ESC]   Çık");
    }

    @Override
    public void onExit(GameEngine engine) {
        System.out.println("[GameOverState] GameOver ekranından çıkılıyor.");
    }

    @Override
    public void handleInput(GameEngine engine, int key) {
        if (key == KEY_ENTER) {

            engine.getBoard().reset();
            engine.changeState(new MenuState());
        } else if (key == KEY_ESC) {
            System.out.println("[GameOverState] Uygulama kapatılıyor.");
        }
    }

    @Override
    public void update(GameEngine engine) {

    }

    @Override
    public void render(GameEngine engine) {
        System.out.println("[GameOverState] render() → GameOver ekranı çiziliyor.");
    }

    @Override
    public String toString() { return "GameOverState"; }
}
