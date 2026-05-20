package behavioral.state;

import engine.GameEngine;
import behavioral.command.CommandHistory;
import behavioral.command.MoveLeftCommand;
import behavioral.command.MoveRightCommand;
import behavioral.command.RotateCommand;
import behavioral.command.HardDropCommand;

public class PlayingState implements GameState {

    public static final int KEY_LEFT      = 37;
    public static final int KEY_RIGHT     = 39;
    public static final int KEY_UP        = 38;
    public static final int KEY_DOWN      = 40;
    public static final int KEY_PAUSE     = 80;   
    public static final int KEY_ESC       = 27;
    public static final int KEY_UNDO      = 90;   

    @Override
    public void onEnter(GameEngine engine) {
        System.out.println("[PlayingState] Oyun başladı / devam ediyor.");
    }

    @Override
    public void onExit(GameEngine engine) {
        System.out.println("[PlayingState] Oyun durumu değişiyor.");
    }

    @Override
    public void handleInput(GameEngine engine, int key) {
        CommandHistory history = engine.getCommandHistory();

        switch (key) {
            case KEY_LEFT:
                MoveLeftCommand left = new MoveLeftCommand(
                        engine.getCurrentPiece(), engine.getBoard());
                if (left.execute()) history.push(left);
                break;
            case KEY_RIGHT:
                MoveRightCommand right = new MoveRightCommand(
                        engine.getCurrentPiece(), engine.getBoard());
                if (right.execute()) history.push(right);
                break;
            case KEY_UP:
                RotateCommand rot = new RotateCommand(engine.getCurrentPiece());
                if (rot.execute()) history.push(rot);
                break;
            case KEY_DOWN:
                HardDropCommand drop = new HardDropCommand(
                        engine.getCurrentPiece(), engine.getBoard());
                drop.execute();

                break;
            case KEY_UNDO:
                history.undo();
                break;
            case KEY_PAUSE:
                engine.changeState(new PausedState());
                break;
            case KEY_ESC:
                engine.endGame();
                engine.changeState(new GameOverState());
                break;
            default:
                System.out.println("[PlayingState] Bilinmeyen tuş: " + key);
        }
    }

    @Override
    public void update(GameEngine engine) {
        boolean running = engine.tick();
        if (!running) {
            engine.changeState(new GameOverState());
        }
    }

    @Override
    public void render(GameEngine engine) {
        System.out.println("[PlayingState] render() → Tahta çiziliyor. " +
                           "Skor: " + engine.getBoard().getScore() +
                           " Seviye: " + engine.getBoard().getLevel());
    }

    @Override
    public String toString() { return "PlayingState"; }
}
