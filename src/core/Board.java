package core;

/**
 * Board — Oyun tahtası.
 *
 * Grid'i kapsar ve "Dynamic Shrinking" özelliğini yönetir:
 *   - Alt satırlar uzun süre boş kaldığında (veya temizlendiğinde)
 *     tahta boyutu küçültülebilir.
 *   - Gerektiğinde üstten yeni satır eklenebilir.
 *
 * Ayrıca aktif parçanın (currentPiece) konumunu tutar.
 *
 * Tasarım: Composite (Grid + aktif parça + dekoratörlü hücreler bir arada).
 */
public class Board {

    // ------------------------------------------------------------------ sabitler
    public static final int DEFAULT_ROWS = 20;
    public static final int DEFAULT_COLS = 10;

    /** Minimum tahta yüksekliği — Dynamic Shrinking bu değerin altına inemez */
    public static final int MIN_ROWS = 6;

    /** Maksimum tahta yüksekliği */
    public static final int MAX_ROWS = 30;

    /** Kaç boş alt satır birikince küçültme tetiklensin */
    private static final int SHRINK_THRESHOLD = 4;

    // ------------------------------------------------------------------ alanlar
    private Grid  grid;

    /** Aktif parçanın sol-üst köşe sırası */
    private int   currentPieceRow;
    /** Aktif parçanın sol-üst köşe sütunu */
    private int   currentPieceCol;

    /** Oyun skoru */
    private int   score;
    /** Seviye */
    private int   level;

    /**
     * Composite Katman Kökü (Composite Deseni).
     *
     * Alt katmanlar:
     *   "Base"  → yerleşmiş parçalar
     *   "Ghost" → ghost piece gölgesi
     *   "FX"    → portal / powerup efektleri
     *
     * Katmanlar "painter's algorithm" ile birleştirilir:
     * getFX() > getGhost() > getBase() sırasıyla sorgulanır.
     */
    private final CompositeLayer layerRoot;

    // ------------------------------------------------------------------ yapıcı
    public Board() {
        this(DEFAULT_ROWS, DEFAULT_COLS);
    }

    public Board(int rows, int cols) {
        this.grid = new Grid(rows, cols);
        this.score = 0;
        this.level = 1;
        // Composite katman kökünü kur
        this.layerRoot = initLayers(rows, cols);
    }

    /** Composite katman hiyerarşisini oluştur */
    private static CompositeLayer initLayers(int rows, int cols) {
        CompositeLayer root = new CompositeLayer("BoardRoot");
        root.add(new SimpleLayer("Base",  rows, cols));   // Leaf: yerleşik parçalar
        root.add(new SimpleLayer("Ghost", rows, cols));   // Leaf: ghost gölgesi
        root.add(new SimpleLayer("FX",    rows, cols));   // Leaf: portal/powerup efekti
        return root;
    }

    // -------------------------------------------------------------- public API

    /**
     * Satırları temizle ve skoru güncelle.
     *
     * @return temizlenen satır sayısı
     */
    public int clearLines() {
        int cleared = grid.clearFullLines();
        updateScore(cleared);
        return cleared;
    }

    /**
     * Dynamic Shrinking — Ödevde istenen özellik.
     *
     * Altta SHRINK_THRESHOLD veya daha fazla boş satır varsa,
     * Grid'i bu boş satırlardan arındırır (MIN_ROWS sınırına dikkat eder).
     *
     * @return kaç satır kırpıldı
     */
    public int shrinkBoard() {
        int emptyAtBottom = grid.countEmptyRowsAtBottom();
        if (emptyAtBottom < SHRINK_THRESHOLD) return 0;

        int targetRows = Math.max(MIN_ROWS, grid.getRows() - emptyAtBottom);
        int shrinkAmount = grid.getRows() - targetRows;

        if (shrinkAmount <= 0) return 0;

        grid.shrinkRows(targetRows);
        System.out.println("[Board] Dynamic Shrinking: " + shrinkAmount +
                           " satır kırpıldı. Yeni yükseklik: " + grid.getRows());
        return shrinkAmount;
    }

    /**
     * Dynamic Expansion — Yeni parçaların girebileceği alan yok ise
     * üstten satır ekle (MAX_ROWS sınırına dikkat eder).
     *
     * @param extraRows eklenmek istenen satır sayısı
     * @return gerçekte eklenen satır sayısı
     */
    public int expandBoard(int extraRows) {
        int available = MAX_ROWS - grid.getRows();
        int actual    = Math.min(extraRows, available);
        if (actual <= 0) return 0;
        grid.expandRows(actual);
        System.out.println("[Board] Expansion: " + actual +
                           " satır eklendi. Yeni yükseklik: " + grid.getRows());
        return actual;
    }

    /**
     * Parçanın geçerli konuma yerleşip yerleşemeyeceğini kontrol et.
     *
     * @param pieceMatrix  parça formu
     * @param startRow     üst-sol satır
     * @param startCol     üst-sol sütun
     */
    public boolean canPlace(int[][] pieceMatrix, int startRow, int startCol) {
        for (int r = 0; r < pieceMatrix.length; r++) {
            for (int c = 0; c < pieceMatrix[r].length; c++) {
                if (pieceMatrix[r][c] == 0) continue;
                int gr = startRow + r;
                int gc = startCol + c;
                if (gr < 0 || gr >= grid.getRows() ||
                    gc < 0 || gc >= grid.getCols()) return false;
                if (grid.isFilled(gr, gc)) return false;
            }
        }
        return true;
    }

    /**
     * Aktif parçayı Grid'e yerleştir.
     *
     * @param pieceMatrix  parça formu
     * @param colorId      parçanın rengi
     */
    public void lockPiece(int[][] pieceMatrix, int colorId) {
        grid.placePiece(pieceMatrix, currentPieceRow, currentPieceCol, colorId);
    }

    /**
     * Tahtanın oyun sona erip ermediğini kontrol et.
     * (en üst görünür satırda dolu hücre varsa game over)
     */
    public boolean isGameOver() {
        for (int c = 0; c < grid.getCols(); c++) {
            if (grid.isFilled(0, c)) return true;
        }
        return false;
    }

    /** Tahtayı başlangıç durumuna getir */
    public void reset() {
        grid.reset();
        score = 0;
        level = 1;
        currentPieceRow = 0;
        currentPieceCol = grid.getCols() / 2 - 1;
    }

    // --------------------------------------------------------- skor mantığı

    /**
     * Tetris skor tablosu (klasik):
     *   1 satır → 100 × level
     *   2 satır → 300 × level
     *   3 satır → 500 × level
     *   4 satır (Tetris!) → 800 × level
     */
    private void updateScore(int clearedLines) {
        int[] bonuses = {0, 100, 300, 500, 800};
        int idx = Math.min(clearedLines, 4);
        score += bonuses[idx] * level;

        // Her 10 satırda bir seviye atla
        int totalCleared = grid.getTotalClearedLines();
        level = (totalCleared / 10) + 1;
    }

    // --------------------------------------------------------- getter/setter
    public Grid getGrid()               { return grid; }
    public int  getScore()              { return score; }
    public int  getLevel()              { return level; }
    public int  getCurrentPieceRow()    { return currentPieceRow; }
    public int  getCurrentPieceCol()    { return currentPieceCol; }

    public void setCurrentPieceRow(int row) { this.currentPieceRow = row; }
    public void setCurrentPieceCol(int col) { this.currentPieceCol = col; }

    // --------------------------------------------------------- Composite API
    /** Katman kökünü döndür (Composite deseni erişim noktası) */
    public CompositeLayer getLayerRoot() { return layerRoot; }

    /** "Base" katmanını döndür (yerleşik parçalar) */
    public SimpleLayer getBaseLayer() {
        return (SimpleLayer) layerRoot.find("Base");
    }

    /** "Ghost" katmanını döndür (ghost piece gölgesi) */
    public SimpleLayer getGhostLayer() {
        return (SimpleLayer) layerRoot.find("Ghost");
    }

    /** "FX" katmanını döndür (portal/powerup efektleri) */
    public SimpleLayer getFXLayer() {
        return (SimpleLayer) layerRoot.find("FX");
    }

    @Override
    public String toString() {
        return "Board [" + grid.getRows() + "x" + grid.getCols() +
               "] Skor: " + score + " Seviye: " + level + "\n" + grid;
    }
}
