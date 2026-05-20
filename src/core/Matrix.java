package core;

public class Matrix {

    protected int[] data;  
    protected int   rows;
    protected int   cols;

    public Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0)
            throw new IllegalArgumentException("rows ve cols pozitif olmalı");
        this.rows = rows;
        this.cols = cols;
        this.data = new int[rows * cols];
    }

    public Matrix(int[][] source) {
        if (source == null || source.length == 0)
            throw new IllegalArgumentException("Kaynak dizi boş olamaz");
        this.rows = source.length;
        this.cols = source[0].length;
        this.data = new int[rows * cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                data[r * cols + c] = source[r][c];
            }
        }
    }

    public int get(int row, int col) {
        checkBounds(row, col);
        return data[row * cols + col];
    }

    public void set(int row, int col, int value) {
        checkBounds(row, col);
        data[row * cols + col] = value;
    }

    public Matrix clone() {
        Matrix copy = new Matrix(rows, cols);
        for (int i = 0; i < data.length; i++) {
            copy.data[i] = this.data[i];
        }
        return copy;
    }

    public Matrix rotateCW() {
        Matrix rotated = new Matrix(cols, rows);   
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int value = get(r, c);
                rotated.set(c, rows - 1 - r, value);
            }
        }
        return rotated;
    }

    public Matrix rotateCCW() {
        Matrix rotated = new Matrix(cols, rows);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int value = get(r, c);
                rotated.set(cols - 1 - c, r, value);
            }
        }
        return rotated;
    }

    public Matrix rotate180() {
        Matrix rotated = new Matrix(rows, cols);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rotated.set(rows - 1 - r, cols - 1 - c, get(r, c));
            }
        }
        return rotated;
    }

    public void fill(int value) {
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }
    }

    public void resize(int newRows, int newCols) {
        int[] newData = new int[newRows * newCols];
        int copyRows = Math.min(rows, newRows);
        int copyCols = Math.min(cols, newCols);
        for (int r = 0; r < copyRows; r++) {
            for (int c = 0; c < copyCols; c++) {
                newData[r * newCols + c] = get(r, c);
            }
        }

        for (int i = 0; i < data.length; i++) data[i] = 0;
        this.data = newData;
        this.rows = newRows;
        this.cols = newCols;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    private void checkBounds(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols)
            throw new IndexOutOfBoundsException(
                "Matrix sınırları dışı: (" + row + ", " + col + ")");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            sb.append("| ");
            for (int c = 0; c < cols; c++) {
                sb.append(String.format("%2d ", get(r, c)));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
}
