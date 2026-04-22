package core;

/**
 * Grid — Tetris oyun ızgarası.
 *
 * Matrix'i miras alır; hücreler Cell[][] olarak üst katmanda tutulur.
 * (Matrix int tabanlı bitboard; Grid Cell nesnelerini yönetir.)
 *
 * Sorumluluklar:
 *  - Hücre yerleşimi ve sorgulaması
 *  - Tam satır tespiti ve temizlenmesi
 *  - Dynamic Shrinking için gerekli veri desteği (Board tarafından kullanılır)
 */
public class Grid {

    // ------------------------------------------------------------------ fields
    private Cell[][] cells;   // [row][col] — hücre nesneleri
    private int      rows;
    private int      cols;

    // koleksiyon yasağı: temizlenen satır sayısını sayaç ile izliyoruz
    private int totalClearedLines;

    // --------------------------------------------------------------- ctor/init
    public Grid(int rows, int cols) {
        if (rows <= 0 || cols <= 0)
            throw new IllegalArgumentException("Grid boyutları pozitif olmalı");
        this.rows = rows;
        this.cols = cols;
        this.totalClearedLines = 0;
        initCells();
    }

    /** Tüm hücreleri EmptyCell ile doldur */
    private void initCells() {
        cells = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new EmptyCell();
            }
        }
    }

    // -------------------------------------------------------------- public API

    /** Hücreyi getir */
    public Cell getCell(int row, int col) {
        checkBounds(row, col);
        return cells[row][col];
    }

    /** Hücreyi değiştir */
    public void setCell(int row, int col, Cell cell) {
        checkBounds(row, col);
        if (cell == null) cell = new EmptyCell();
        cells[row][col] = cell;
    }

    /** (row, col) konumu dolu mu? */
    public boolean isFilled(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return true; // sınır dışı = engel
        return cells[row][col].isFilled();
    }

    /**
     * Belirtilen parça matrisini Grid'e yerleştir.
     *
     * @param pieceMatrix  parçanın 2D formu (0=boş, renk_no=dolu)
     * @param startRow     üst-sol köşenin satır koordinatı
     * @param startCol     üst-sol köşenin sütun koordinatı
     * @param colorId      kullanılacak renk
     */
    public void placePiece(int[][] pieceMatrix, int startRow, int startCol, int colorId) {
        for (int r = 0; r < pieceMatrix.length; r++) {
            for (int c = 0; c < pieceMatrix[r].length; c++) {
                if (pieceMatrix[r][c] != 0) {
                    int gr = startRow + r;
                    int gc = startCol + c;
                    if (gr >= 0 && gr < rows && gc >= 0 && gc < cols) {
                        cells[gr][gc] = new FilledCell(colorId);
                    }
                }
            }
        }
    }

    /**
     * Tam satırları bul ve temizle — gravity uygulandıktan sonra çağrılır.
     *
     * @return kaç satır temizlendi
     */
    public int clearFullLines() {
        int cleared = 0;
        // Aşağıdan yukarıya tara (gravity yukarıdan aşağıya düşer)
        int writeRow = rows - 1;
        // Manuel iki-pointer yaklaşımı — koleksiyon yok
        for (int r = rows - 1; r >= 0; r--) {
            if (!isLineFull(r)) {
                // satırı writeRow'a kopyala
                if (r != writeRow) {
                    copyRow(r, writeRow);
                }
                writeRow--;
            } else {
                cleared++;
            }
        }
        // kalan üst satırları boşalt
        for (int r = writeRow; r >= 0; r--) {
            clearRow(r);
        }
        totalClearedLines += cleared;
        return cleared;
    }

    /** Satır tam mı? */
    public boolean isLineFull(int row) {
        for (int c = 0; c < cols; c++) {
            if (!cells[row][c].isFilled()) return false;
        }
        return true;
    }

    /** Satırın hiçbir hücresi dolu değil mi? */
    public boolean isLineEmpty(int row) {
        for (int c = 0; c < cols; c++) {
            if (cells[row][c].isFilled()) return false;
        }
        return true;
    }

    /**
     * Dynamic Shrinking için: en alttaki kaç boş satır var?
     * Board.shrinkBoard() bu sayıyı kullanarak Grid'i küçültür.
     */
    public int countEmptyRowsAtBottom() {
        int count = 0;
        for (int r = rows - 1; r >= 0; r--) {
            if (isLineEmpty(r)) count++;
            else break;
        }
        return count;
    }

    /**
     * Grid'i alt N satırdan kes (Dynamic Shrinking).
     * Board tarafından çağrılır; orijinal içerik korunur.
     *
     * @param newRows yeni satır sayısı (newRows < rows)
     */
    public void shrinkRows(int newRows) {
        if (newRows <= 0 || newRows >= rows) return;
        Cell[][] newCells = new Cell[newRows][cols];
        int offset = rows - newRows;   // üstten kaç satır atlanacak
        for (int r = 0; r < newRows; r++) {
            for (int c = 0; c < cols; c++) {
                newCells[r][c] = cells[r + offset][c];
            }
        }
        // eski hücreleri temizle (bellek yönetimi)
        nullifyCells();
        cells = newCells;
        rows  = newRows;
    }

    /**
     * Grid'e üstten yeni boş satırlar ekle (Dynamic Shrinking'in tersi:
     * örn. ekrana yeni alan eklendiğinde).
     *
     * @param extraRows eklenecek satır sayısı
     */
    public void expandRows(int extraRows) {
        if (extraRows <= 0) return;
        int newRows = rows + extraRows;
        Cell[][] newCells = new Cell[newRows][cols];
        // üst satırları boş doldur
        for (int r = 0; r < extraRows; r++) {
            for (int c = 0; c < cols; c++) {
                newCells[r][c] = new EmptyCell();
            }
        }
        // mevcut satırları kopyala
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newCells[r + extraRows][c] = cells[r][c];
            }
        }
        nullifyCells();
        cells = newCells;
        rows  = newRows;
    }

    /** Tüm Grid'i sıfırla */
    public void reset() {
        nullifyCells();
        initCells();
        totalClearedLines = 0;
    }

    public int getRows()             { return rows; }
    public int getCols()             { return cols; }
    public int getTotalClearedLines(){ return totalClearedLines; }

    // --------------------------------------------------------- private helpers

    private void copyRow(int fromRow, int toRow) {
        for (int c = 0; c < cols; c++) {
            cells[toRow][c] = cells[fromRow][c];
        }
    }

    private void clearRow(int row) {
        for (int c = 0; c < cols; c++) {
            cells[row][c] = new EmptyCell();
        }
    }

    /** Bellek temizliği: tüm Cell referanslarını null'la */
    private void nullifyCells() {
        if (cells == null) return;
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                cells[r][c] = null;   // GC'ye bırak
            }
        }
    }

    private void checkBounds(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols)
            throw new IndexOutOfBoundsException(
                "Grid sınırı dışı: (" + row + ", " + col + ")");
    }

    // ------------------------------------------------------------- debug print
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        for (int c = 0; c < cols; c++) sb.append("--");
        sb.append("+\n");
        for (int r = 0; r < rows; r++) {
            sb.append("|");
            for (int c = 0; c < cols; c++) {
                sb.append(cells[r][c].render());
            }
            sb.append("|\n");
        }
        sb.append("+");
        for (int c = 0; c < cols; c++) sb.append("--");
        sb.append("+");
        return sb.toString();
    }
}
