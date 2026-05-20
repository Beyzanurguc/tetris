package behavioral.state;

import engine.GameEngine;

public class MenuState implements GameState {

    public static final int KEY_ENTER = 10;
    public static final int KEY_ESC   = 27;

    @Override
    public void onEnter(GameEngine engine) {
        System.out.println("[MenuState] Ana menü gösteriliyor...");
        System.out.println("  [ENTER] Oyunu Başlat");
        System.out.println("  [ESC]   Çık");
    }

    @Override
    public void onExit(GameEngine engine) {
        System.out.println("[MenuState] Ana menüden çıkılıyor.");
    }

    @Override
    public void handleInput(GameEngine engine, int key) {
        if (key == KEY_ENTER) {
            engine.startGame();
            engine.changeState(new PlayingState());
        } else if (key == KEY_ESC) {
            System.out.println("[MenuState] Uygulama kapatılıyor.");
        } else {
            System.out.println("[MenuState] Bilinmeyen tuş: " + key);
        }
    }

    @Override
    public void update(GameEngine engine) {

    }

    @Override
    public void render(GameEngine engine) {
        System.out.println("[MenuState] render() → Menü ekranı çiziliyor.");
    }

    @Override
    public String toString() { return "MenuState"; }
}
