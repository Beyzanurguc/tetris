package core;

public class SimpleLayer extends BoardLayer {

    private final int rows;
    private final int cols;

    private final int[][] data;

    public SimpleLayer(String name, int rows, int cols) {
        super(name);
        if (rows <= 0 || cols <= 0)
            throw new IllegalArgumentException("SimpleLayer boyutları pozitif olmalı");
        this.rows = rows;
        this.cols = cols;
        this.data = new int[rows][cols];
    }

    @Override
    public int getCellColorAt(int row, int col) {
        if (!inBounds(row, col)) return Cell.COLOR_EMPTY;
        return visible ? data[row][col] : Cell.COLOR_EMPTY;
    }

    @Override
    public boolean isFilledAt(int row, int col) {
        if (!inBounds(row, col)) return false;
        return visible && (data[row][col] != Cell.COLOR_EMPTY);
    }

    @Override
    public void clear() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                data[r][c] = Cell.COLOR_EMPTY;
            }
        }
    }

    @Override
    public String describe() {
        int filled = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (data[r][c] != Cell.COLOR_EMPTY) filled++;
        return "SimpleLayer[" + name + "] " + rows + "×" + cols +
               " | doluhücre=" + filled + " | visible=" + visible;
    }

    public void setColor(int row, int col, int colorId) {
        if (inBounds(row, col)) data[row][col] = colorId;
    }

    public void paint(int[][] pieceMatrix, int startRow, int startCol, int colorId) {
        for (int r = 0; r < pieceMatrix.length; r++) {
            for (int c = 0; c < pieceMatrix[r].length; c++) {
                if (pieceMatrix[r][c] != 0) {
                    setColor(startRow + r, startCol + c, colorId);
                }
            }
        }
    }

    public void clearRow(int row) {
        if (row < 0 || row >= rows) return;
        for (int c = 0; c < cols; c++) data[row][c] = Cell.COLOR_EMPTY;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
}
