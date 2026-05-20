package core;

import behavioral.iterator.CellIterator;

public class Board {

    public static final int DEFAULT_ROWS = 20;
    public static final int DEFAULT_COLS = 10;

    public static final int MIN_ROWS = 6;

    public static final int MAX_ROWS = 30;

    private static final int SHRINK_THRESHOLD = 4;

    private Grid  grid;

    private int   currentPieceRow;

    private int   currentPieceCol;

    private int   score;

    private int   level;

    private final CompositeLayer layerRoot;

    public Board() {
        this(DEFAULT_ROWS, DEFAULT_COLS);
    }

    public Board(int rows, int cols) {
        this.grid = new Grid(rows, cols);
        this.score = 0;
        this.level = 1;

        this.layerRoot = initLayers(rows, cols);
    }

    private static CompositeLayer initLayers(int rows, int cols) {
        CompositeLayer root = new CompositeLayer("BoardRoot");
        root.add(new SimpleLayer("Base",  rows, cols));   
        root.add(new SimpleLayer("Ghost", rows, cols));   
        root.add(new SimpleLayer("FX",    rows, cols));   
        return root;
    }

    public int clearLines() {
        int cleared = grid.clearFullLines();
        updateScore(cleared);
        return cleared;
    }

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

    public int expandBoard(int extraRows) {
        int available = MAX_ROWS - grid.getRows();
        int actual    = Math.min(extraRows, available);
        if (actual <= 0) return 0;
        grid.expandRows(actual);
        System.out.println("[Board] Expansion: " + actual +
                           " satır eklendi. Yeni yükseklik: " + grid.getRows());
        return actual;
    }

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

    public void lockPiece(int[][] pieceMatrix, int colorId) {
        grid.placePiece(pieceMatrix, currentPieceRow, currentPieceCol, colorId);
    }

    public boolean isGameOver() {
        for (int c = 0; c < grid.getCols(); c++) {
            if (grid.isFilled(0, c)) return true;
        }
        return false;
    }

    public void reset() {
        grid.reset();
        score = 0;
        level = 1;
        currentPieceRow = 0;
        currentPieceCol = grid.getCols() / 2 - 1;
    }

    private void updateScore(int clearedLines) {
        int[] bonuses = {0, 100, 300, 500, 800};
        int idx = Math.min(clearedLines, 4);
        score += bonuses[idx] * level;

        int totalCleared = grid.getTotalClearedLines();
        level = (totalCleared / 10) + 1;
    }

    public Grid getGrid()               { return grid; }
    public int  getScore()              { return score; }
    public int  getLevel()              { return level; }
    public int  getCurrentPieceRow()    { return currentPieceRow; }
    public int  getCurrentPieceCol()    { return currentPieceCol; }

    public void setCurrentPieceRow(int row) { this.currentPieceRow = row; }
    public void setCurrentPieceCol(int col) { this.currentPieceCol = col; }

    public CompositeLayer getLayerRoot() { return layerRoot; }

    public SimpleLayer getBaseLayer() {
        return (SimpleLayer) layerRoot.find("Base");
    }

    public SimpleLayer getGhostLayer() {
        return (SimpleLayer) layerRoot.find("Ghost");
    }

    public SimpleLayer getFXLayer() {
        return (SimpleLayer) layerRoot.find("FX");
    }

    public CellIterator iterator() {
        return new CellIterator(grid);
    }

    @Override
    public String toString() {
        return "Board [" + grid.getRows() + "x" + grid.getCols() +
               "] Skor: " + score + " Seviye: " + level + "\n" + grid;
    }
}
