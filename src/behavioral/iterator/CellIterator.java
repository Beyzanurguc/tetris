package behavioral.iterator;

import core.Cell;
import core.Grid;

public class CellIterator implements GameIterator<Cell> {

    private final Grid grid;
    private int row;
    private int col;

    public CellIterator(Grid grid) {
        if (grid == null)
            throw new IllegalArgumentException("CellIterator: grid null olamaz");
        this.grid = grid;
        this.row  = 0;
        this.col  = 0;
    }

    @Override
    public boolean hasNext() {
        return row < grid.getRows();
    }

    @Override
    public Cell next() {
        if (!hasNext())
            throw new RuntimeException("CellIterator: eleman yok");

        Cell cell = grid.getCell(row, col);

        col++;
        if (col >= grid.getCols()) {
            col = 0;
            row++;
        }
        return cell;
    }

    @Override
    public void reset() {
        row = 0;
        col = 0;
    }

    public int getCurrentRow() { return row; }

    public int getCurrentCol() { return col; }
}
