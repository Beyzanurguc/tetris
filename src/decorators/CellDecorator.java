package decorators;

import core.Cell;

public abstract class CellDecorator extends Cell {

    protected Cell wrapped;   

    protected CellDecorator(Cell cell) {
        super(cell.isFilled(), cell.getColorId());
        if (cell == null)
            throw new IllegalArgumentException("Dekoratöre null Cell verilemez");
        this.wrapped = cell;
    }

    @Override
    public boolean isFilled()  { return wrapped.isFilled(); }

    @Override
    public int getColorId()    { return wrapped.getColorId(); }

    @Override
    public void activate()     { wrapped.activate(); }

    @Override
    public String render()     { return wrapped.render(); }

    public Cell getWrapped()   { return wrapped; }

    public Cell getBase() {
        Cell current = wrapped;
        while (current instanceof CellDecorator) {
            current = ((CellDecorator) current).wrapped;
        }
        return current;
    }

    public void dispose() {
        if (wrapped instanceof CellDecorator) {
            ((CellDecorator) wrapped).dispose();
        }
        wrapped = null;
    }
}
