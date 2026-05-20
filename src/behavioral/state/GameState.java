package behavioral.state;

import engine.GameEngine;

public interface GameState {

    void onEnter(GameEngine engine);

    void onExit(GameEngine engine);

    void handleInput(GameEngine engine, int key);

    void update(GameEngine engine);

    void render(GameEngine engine);
}
