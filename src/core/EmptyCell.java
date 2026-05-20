package core;

public class EmptyCell extends Cell {

    public EmptyCell() {
        super(false, Cell.COLOR_EMPTY);
    }

    @Override
    public boolean isFilled()   { return false; }

    @Override
    public int getColorId()     { return Cell.COLOR_EMPTY; }

    @Override
    public void activate()      {  }

    @Override
    public String render()      { return "  "; }  
}
