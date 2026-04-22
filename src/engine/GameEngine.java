package engine;

import core.Board;
import pieces.Piece;
import pieces.PieceFactory;
import pieces.CustomPieceLoader;
import bridge.gravity.GravityAbstraction;
import bridge.gravity.NormalGravity;
import bridge.gravity.GravityImplementor;
import bridge.ThemeManager;

/**
 * GameEngine — Oyun Motoru (Singleton Deseni).
 *
 * Singleton Garantisi:
 *   • private static instance — tek kopya
 *   • private constructor    — dışarıdan örnekleme engeli
 *   • static getInstance()   — kontrollü erişim noktası
 *   • Thread-Safety: double-checked locking ile sağlanır
 *
 * Sorumluluklar:
 *   - Board ve aktif parçanın yönetimi
 *   - Oyun döngüsü (tick)
 *   - Skor, seviye, durum
 *   - CustomPieceLoader entegrasyonu
 *   - GravityAbstraction (Bridge) entegrasyonu
 *
 * Behavioral desenler (Strategy, Observer, Command) KULLANILMAZ.
 * Oyun durumu basit boolean bayrakları ile izlenir.
 */
public class GameEngine {

    // ============================================================ Singleton

    private static volatile GameEngine instance = null;

    /** Dışarıdan örnekleme engellenir */
    private GameEngine() {
        board             = new Board();
        customPieceLoader = new CustomPieceLoader();
        gravity           = new GravityAbstraction(new NormalGravity());
        themeManager      = ThemeManager.getInstance();
        gameRunning       = false;
        gamePaused        = false;
        tickCount         = 0;
        nextPiece         = PieceFactory.createRandomPiece();
    }

    /**
     * Double-checked locking ile thread-safe Singleton erişimi.
     * İlk çağrıda instance oluşturulur, sonraki çağrılarda aynı döndürülür.
     */
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

    // ============================================================ Alanlar

    private final Board               board;
    private final CustomPieceLoader   customPieceLoader;
    private       GravityAbstraction  gravity;
    private final ThemeManager        themeManager;

    private Piece  currentPiece;
    private Piece  nextPiece;

    private boolean gameRunning;
    private boolean gamePaused;
    private int     tickCount;

    // ============================================================ Oyun Akışı

    /** Yeni oyun başlat */
    public void startGame() {
        board.reset();
        tickCount   = 0;
        gameRunning = true;
        gamePaused  = false;
        spawnNextPiece();
        System.out.println("[GameEngine] Oyun başladı.");
    }

    /** Oyunu duraklat / devam et */
    public void togglePause() {
        if (!gameRunning) return;
        gamePaused = !gamePaused;
        System.out.println("[GameEngine] " + (gamePaused ? "Duraklatıldı." : "Devam ediyor."));
    }

    /** Oyunu bitir */
    public void endGame() {
        gameRunning = false;
        System.out.println("[GameEngine] Oyun bitti. Son skor: " + board.getScore());
    }

    /**
     * Tek oyun adımı (tick).
     * Oyun döngüsü her frame bu metodu çağırır.
     *
     * @return false → oyun bitti (game over)
     */
    public boolean tick() {
        if (!gameRunning || gamePaused) return gameRunning;

        tickCount++;

        // Yerçekimi hızına göre parçayı düşür
        int dropInterval = gravity.getDropInterval(board.getLevel());
        if (tickCount % dropInterval == 0) {
            boolean moved = moveCurrentPieceDown();
            if (!moved) {
                lockAndSpawn();
            }
        }

        // Tahta küçültme kontrolü (Dynamic Shrinking)
        board.shrinkBoard();

        // Game Over kontrolü
        if (board.isGameOver()) {
            endGame();
            return false;
        }

        return true;
    }

    // ============================================================ Hareket API

    /** Aktif parçayı sola kaydır */
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

    /** Aktif parçayı sağa kaydır */
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

    /** Aktif parçayı bir adım aşağı kaydır */
    public boolean moveDown() {
        if (currentPiece == null || !gameRunning || gamePaused) return false;
        return moveCurrentPieceDown();
    }

    /**
     * Hard Drop — parçayı anında en alta indir.
     * Drop skoru: her atlanan satır için +2 puan (sonraki geliştirme).
     */
    public void hardDrop() {
        if (currentPiece == null || !gameRunning || gamePaused) return;
        int dropped = 0;
        while (moveCurrentPieceDown()) {
            dropped++;
        }
        lockAndSpawn();
        System.out.println("[GameEngine] Hard Drop: " + dropped + " satır düştü.");
    }

    /**
     * Aktif parçayı saat yönünde döndür.
     * Rotation Counter: canRotate() false ise döndürülmez.
     */
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
            currentPiece.rotate();   // orijinali döndür
            return true;
        }
        return false;   // duvar pozisyonunda döndürülemiyor
    }

    // ============================================================ Custom Piece

    /** CustomPieceLoader'ı döndür (kullanıcı parçası eklemek için) */
    public CustomPieceLoader getCustomPieceLoader() {
        return customPieceLoader;
    }

    /**
     * Bir sonraki parçayı custom havuzdan seç.
     * Havuz boşsa standart rastgele parça kullanılır.
     */
    public void useNextCustomPiece() {
        if (customPieceLoader.getLoadedCount() > 0) {
            int idx = (int)(Math.random() * customPieceLoader.getLoadedCount());
            nextPiece = customPieceLoader.getPieceAt(idx);
            System.out.println("[GameEngine] Sonraki parça: Custom → " +
                               nextPiece.getPieceName());
        }
    }

    // ============================================================ Bridge: Gravity

    /**
     * Yerçekimi implementorunu runtime'da değiştir (Bridge deseni).
     * Oyun devam ederken çağrılabilir.
     */
    public void setGravityImplementor(GravityImplementor impl) {
        gravity.setImplementor(impl);
        System.out.println("[GameEngine] Gravity değiştirildi: " +
                           impl.getClass().getSimpleName());
    }

    public GravityAbstraction getGravity() { return gravity; }

    // ============================================================ Getters

    public Board   getBoard()        { return board; }
    public Piece   getCurrentPiece() { return currentPiece; }
    public Piece   getNextPiece()    { return nextPiece; }
    public boolean isGameRunning()   { return gameRunning; }
    public boolean isGamePaused()    { return gamePaused; }
    public int     getTickCount()    { return tickCount; }
    public ThemeManager getThemeManager() { return themeManager; }

    // ============================================================ Private

    /** Sıradaki parçayı aktif yap, yenisini üret */
    private void spawnNextPiece() {
        currentPiece = nextPiece;
        // Başlangıç konumu: üst-orta
        currentPiece.setRow(0);
        currentPiece.setCol(board.getGrid().getCols() / 2
                            - currentPiece.getMatrix()[0].length / 2);
        board.setCurrentPieceRow(currentPiece.getRow());
        board.setCurrentPieceCol(currentPiece.getCol());

        // Sonraki parçayı belirle
        nextPiece = PieceFactory.createRandomPiece();
        System.out.println("[GameEngine] Yeni parça: " + currentPiece.getPieceName());
    }

    /** Parçayı bir satır aşağı indir */
    private boolean moveCurrentPieceDown() {
        int newRow = currentPiece.getRow() + 1;
        if (board.canPlace(currentPiece.getMatrix(), newRow, currentPiece.getCol())) {
            currentPiece.setRow(newRow);
            board.setCurrentPieceRow(newRow);
            return true;
        }
        return false;
    }

    /** Parçayı kilitle, satırları temizle, yenisini üret */
    private void lockAndSpawn() {
        board.lockPiece(currentPiece.getMatrix(), currentPiece.getColorId());
        int cleared = board.clearLines();
        if (cleared > 0) {
            System.out.println("[GameEngine] " + cleared +
                               " satır temizlendi. Skor: " + board.getScore());
        }
        currentPiece = null;
        spawnNextPiece();
    }

    // ============================================================ Debug

    @Override
    public String toString() {
        return "GameEngine [Tick:" + tickCount +
               " | Running:" + gameRunning +
               " | Paused:" + gamePaused +
               " | " + board + "]";
    }
}
