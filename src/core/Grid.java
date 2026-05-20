package core;

public class Grid {

    private Cell[][] cells;   
    private int      rows;
    private int      cols;

    private int totalClearedLines;

    public Grid(int rows, int cols) {
        if (rows <= 0 || cols <= 0)
            throw new IllegalArgumentException("Grid boyutları pozitif olmalı");
        this.rows = rows;
        this.cols = cols;
        this.totalClearedLines = 0;
        initCells();
    }

    private void initCells() {
        cells = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new EmptyCell();
            }
        }
    }

    public Cell getCell(int row, int col) {
        checkBounds(row, col);
        return cells[row][col];
    }

    public void setCell(int row, int col, Cell cell) {
        checkBounds(row, col);
        if (cell == null) cell = new EmptyCell();
        cells[row][col] = cell;
    }

    public boolean isFilled(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return true; 
        return cells[row][col].isFilled();
    }

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

    public int clearFullLines() {
        int cleared = 0;

        int writeRow = rows - 1;

        for (int r = rows - 1; r >= 0; r--) {
            if (!isLineFull(r)) {

                if (r != writeRow) {
                    copyRow(r, writeRow);
                }
                writeRow--;
            } else {
                cleared++;
            }
        }

        for (int r = writeRow; r >= 0; r--) {
            clearRow(r);
        }
        totalClearedLines += cleared;
        return cleared;
    }

    public boolean isLineFull(int row) {
        for (int c = 0; c < cols; c++) {
            if (!cells[row][c].isFilled()) return false;
        }
        return true;
    }

    public boolean isLineEmpty(int row) {
        for (int c = 0; c < cols; c++) {
            if (cells[row][c].isFilled()) return false;
        }
        return true;
    }

    public int countEmptyRowsAtBottom() {
        int count = 0;
        for (int r = rows - 1; r >= 0; r--) {
            if (isLineEmpty(r)) count++;
            else break;
        }
        return count;
    }

    public void shrinkRows(int newRows) {
        if (newRows <= 0 || newRows >= rows) return;
        Cell[][] newCells = new Cell[newRows][cols];
        int offset = rows - newRows;   
        for (int r = 0; r < newRows; r++) {
            for (int c = 0; c < cols; c++) {
                newCells[r][c] = cells[r + offset][c];
            }
        }

        nullifyCells();
        cells = newCells;
        rows  = newRows;
    }

    public void expandRows(int extraRows) {
        if (extraRows <= 0) return;
        int newRows = rows + extraRows;
        Cell[][] newCells = new Cell[newRows][cols];

        for (int r = 0; r < extraRows; r++) {
            for (int c = 0; c < cols; c++) {
                newCells[r][c] = new EmptyCell();
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newCells[r + extraRows][c] = cells[r][c];
            }
        }
        nullifyCells();
        cells = newCells;
        rows  = newRows;
    }

    public void reset() {
        nullifyCells();
        initCells();
        totalClearedLines = 0;
    }

    public int getRows()             { return rows; }
    public int getCols()             { return cols; }
    public int getTotalClearedLines(){ return totalClearedLines; }

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

    private void nullifyCells() {
        if (cells == null) return;
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                cells[r][c] = null;   
            }
        }
    }

    private void checkBounds(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols)
            throw new IndexOutOfBoundsException(
                "Grid sınırı dışı: (" + row + ", " + col + ")");
    }

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
