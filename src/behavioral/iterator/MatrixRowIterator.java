package behavioral.iterator;

import core.Matrix;

public class MatrixRowIterator implements GameIterator<int[]> {

    private final Matrix matrix;
    private int currentRow;

    public MatrixRowIterator(Matrix matrix) {
        if (matrix == null)
            throw new IllegalArgumentException("MatrixRowIterator: matrix null olamaz");
        this.matrix     = matrix;
        this.currentRow = 0;
    }

    @Override
    public boolean hasNext() {
        return currentRow < matrix.getRows();
    }

    @Override
    public int[] next() {
        if (!hasNext())
            throw new RuntimeException("MatrixRowIterator: eleman yok");

        int cols = matrix.getCols();
        int[] row = new int[cols];
        for (int c = 0; c < cols; c++) {
            row[c] = matrix.get(currentRow, c);
        }
        currentRow++;
        return row;
    }

    @Override
    public void reset() {
        currentRow = 0;
    }

    public int getCurrentRow() { return currentRow; }
}
