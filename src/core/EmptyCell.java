package core;

/**
 * EmptyCell — Boş (doldurulmamış) hücre.
 * Cell hiyerarşisinin temel somut uygulamasıdır.
 */
public class EmptyCell extends Cell {

    public EmptyCell() {
        super(false, Cell.COLOR_EMPTY);
    }

    @Override
    public boolean isFilled()   { return false; }

    @Override
    public int getColorId()     { return Cell.COLOR_EMPTY; }

    @Override
    public void activate()      { /* Boş hücrenin davranışı yok */ }

    @Override
    public String render()      { return "  "; }  // iki boşluk
}
