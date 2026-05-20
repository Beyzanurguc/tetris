package engine;

import core.Board;
import pieces.Piece;
import pieces.PieceFactory;
import pieces.CustomPieceLoader;
import bridge.gravity.GravityAbstraction;
import bridge.gravity.NormalGravity;
import bridge.gravity.GravityImplementor;
import bridge.ThemeManager;
import behavioral.observer.GameEventPublisher;
import behavioral.observer.GameEvent;
import behavioral.state.GameState;
import behavioral.state.MenuState;
import behavioral.command.CommandHistory;

public class GameEngine extends GameEventPublisher {

    private static volatile GameEngine instance = null;

    private GameEngine() {
        board             = new Board();
        customPieceLoader = new CustomPieceLoader();
        gravity           = new GravityAbstraction(new NormalGravity());
        themeManager      = ThemeManager.getInstance();
        gameRunning       = false;
        gamePaused        = false;
        tickCount         = 0;
        nextPiece         = PieceFactory.createRandomPiece();
        commandHistory    = new CommandHistory();

        currentState      = new MenuState();
        currentState.onEnter(this);
    }

    public static GameEngine getInstance() {
        if (instance == null) {
            synchronized (GameEngine.class) {
                if (instance == null) {
                    instance = new GameEngine();
                    System.out.println("[GameEngine] Singleton oluşturuldu.");
                }
            }
        }
        return instance;
    }

    private final Board               board;
    private final CustomPieceLoader   customPieceLoader;
    private       GravityAbstraction  gravity;
    private final ThemeManager        themeManager;

    private Piece  currentPiece;
    private Piece  nextPiece;

    private boolean gameRunning;
    private boolean gamePaused;
    private int     tickCount;

    private GameState currentState;

    private final CommandHistory commandHistory;

    public void changeState(GameState newState) {
        if (newState == null) return;
        System.out.println("[GameEngine] Durum geçişi: " +
                           currentState.getClass().getSimpleName() +
                           " → " + newState.getClass().getSimpleName());
        currentState.onExit(this);
        currentState = newState;
        currentState.onEnter(this);
    }

    public GameState getCurrentState() { return currentState; }

    public CommandHistory getCommandHistory() { return commandHistory; }

    public void startGame() {
        board.reset();
        tickCount   = 0;
        gameRunning = true;
        gamePaused  = false;
        commandHistory.clear();
        spawnNextPiece();
        System.out.println("[GameEngine] Oyun başladı.");
    }

    public void pause() {
        if (!gameRunning) return;
        gamePaused = true;
        System.out.println("[GameEngine] Duraklatıldı.");
    }

    public void resume() {
        if (!gameRunning) return;
        gamePaused = false;
        System.out.println("[GameEngine] Devam ediyor.");
    }

    public void togglePause() {
        if (gamePaused) resume(); else pause();
    }

    public void endGame() {
        gameRunning = false;
        System.out.println("[GameEngine] Oyun bitti. Son skor: " + board.getScore());

        notifyListeners(new GameEvent(GameEvent.EventType.GAME_OVER, board.getScore()));
    }

    public boolean tick() {
        if (!gameRunning || gamePaused) return gameRunning;

        tickCount++;

        int dropInterval = gravity.getDropInterval(board.getLevel());
        if (tickCount % dropInterval == 0) {
            boolean moved = moveCurrentPieceDown();
            if (!moved) {
                lockAndSpawn();
            }
        }

        board.shrinkBoard();

        if (board.isGameOver()) {
            endGame();
            return false;
        }

        return true;
    }

    public boolean moveLeft() {
        if (currentPiece == null || !gameRunning || gamePaused) return false;
        int newCol = currentPiece.getCol() - 1;
        if (board.canPlace(currentPiece.getMatrix(),
                           currentPiece.getRow(), newCol)) {
            currentPiece.setCol(newCol);
            board.setCurrentPieceCol(newCol);
            return true;
        }
        return false;
    }

    public boolean moveRight() {
        if (currentPiece == null || !gameRunning || gamePaused) return false;
        int newCol = currentPiece.getCol() + 1;
        if (board.canPlace(currentPiece.getMatrix(),
                           currentPiece.getRow(), newCol)) {
            currentPiece.setCol(newCol);
            board.setCurrentPieceCol(newCol);
            return true;
        }
        return false;
    }

    public boolean moveDown() {
        if (currentPiece == null || !gameRunning || gamePaused) return false;
        return moveCurrentPieceDown();
    }

    public void hardDrop() {
        if (currentPiece == null || !gameRunning || gamePaused) return;
        int dropped = 0;
        while (moveCurrentPieceDown()) {
            dropped++;
        }
        lockAndSpawn();
        System.out.println("[GameEngine] Hard Drop: " + dropped + " satır düştü.");
    }

    public boolean rotatePiece() {
        if (currentPiece == null || !gameRunning || gamePaused) return false;
        if (!currentPiece.canRotate()) {
            System.out.println("[GameEngine] " + currentPiece.getPieceName() +
                               " için rotasyon hakkı kalmadı.");
            return false;
        }
        Piece rotated = currentPiece.clonePiece();
        rotated.rotate();
        if (board.canPlace(rotated.getMatrix(),
                           currentPiece.getRow(), currentPiece.getCol())) {
            currentPiece.rotate();   
            return true;
        }
        return false;   
    }

    public CustomPieceLoader getCustomPieceLoader() {
        return customPieceLoader;
    }

    public void useNextCustomPiece() {
        if (customPieceLoader.getLoadedCount() > 0) {
            int idx = (int)(Math.random() * customPieceLoader.getLoadedCount());
            nextPiece = customPieceLoader.getPieceAt(idx);
            System.out.println("[GameEngine] Sonraki parça: Custom → " +
                               nextPiece.getPieceName());
        }
    }

    public void setGravityImplementor(GravityImplementor impl) {
        gravity.setImplementor(impl);
        System.out.println("[GameEngine] Gravity değiştirildi: " +
                           impl.getClass().getSimpleName());
    }

    public GravityAbstraction getGravity() { return gravity; }

    public Board   getBoard()        { return board; }
    public Piece   getCurrentPiece() { return currentPiece; }
    public Piece   getNextPiece()    { return nextPiece; }
    public boolean isGameRunning()   { return gameRunning; }
    public boolean isGamePaused()    { return gamePaused; }
    public int     getTickCount()    { return tickCount; }
    public ThemeManager getThemeManager() { return themeManager; }

    private void spawnNextPiece() {
        currentPiece = nextPiece;

        currentPiece.setRow(0);
        currentPiece.setCol(board.getGrid().getCols() / 2
                            - currentPiece.getMatrix()[0].length / 2);
        board.setCurrentPieceRow(currentPiece.getRow());
        board.setCurrentPieceCol(currentPiece.getCol());

        nextPiece = PieceFactory.createRandomPiece();
        System.out.println("[GameEngine] Yeni parça: " + currentPiece.getPieceName());
    }

    private boolean moveCurrentPieceDown() {
        int newRow = currentPiece.getRow() + 1;
        if (board.canPlace(currentPiece.getMatrix(), newRow, currentPiece.getCol())) {
            currentPiece.setRow(newRow);
            board.setCurrentPieceRow(newRow);
            return true;
        }
        return false;
    }

    private void lockAndSpawn() {
        board.lockPiece(currentPiece.getMatrix(), currentPiece.getColorId());

        notifyListeners(new GameEvent(GameEvent.EventType.PIECE_LANDED, 0));

        int cleared = board.clearLines();
        if (cleared > 0) {
            System.out.println("[GameEngine] " + cleared +
                               " satır temizlendi. Skor: " + board.getScore());

            notifyListeners(new GameEvent(GameEvent.EventType.LINE_CLEARED, cleared));
        }

        int newLevel = board.getLevel();
        if (newLevel > 1 && cleared > 0) {
            notifyListeners(new GameEvent(GameEvent.EventType.LEVEL_UP, newLevel));
        }

        currentPiece = null;
        spawnNextPiece();
    }

    @Override
    public String toString() {
        return "GameEngine [Tick:" + tickCount +
               " | Running:" + gameRunning +
               " | Paused:" + gamePaused +
               " | State:" + currentState.getClass().getSimpleName() +
               " | " + board + "]";
    }
}
