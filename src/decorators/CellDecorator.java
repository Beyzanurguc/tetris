package decorators;

import core.Cell;

/**
 * CellDecorator — Decorator Deseninin Temel Sarmalayıcı Sınıfı.
 *
 * Decorator Deseni Yapısı:
 *
 *   Cell (Component — soyut)
 *     ├── EmptyCell  (ConcreteComponent)
 *     ├── FilledCell (ConcreteComponent)
 *     └── CellDecorator (Decorator — bu sınıf)
 *           ├── PortalDecorator  (ConcreteDecorator)
 *           └── PowerUpDecorator (ConcreteDecorator)
 *
 * Tasarım Kararları:
 *   - CellDecorator'ı abstract yaparak alt sınıfların sadece
 *     değiştirmek istedikleri metotları override etmesi sağlandı.
 *   - wrapped Cell referansı protected → alt dekoratörler erişebilir.
 *   - Zincir: PowerUpDecorator( PortalDecorator( FilledCell ) ) mümkündür.
 */
public abstract class CellDecorator extends Cell {

    protected Cell wrapped;   // sarmalanan hücre

    protected CellDecorator(Cell cell) {
        super(cell.isFilled(), cell.getColorId());
        if (cell == null)
            throw new IllegalArgumentException("Dekoratöre null Cell verilemez");
        this.wrapped = cell;
    }

    // Varsayılan davranış: sarmalanan hücreye delege et
    @Override
    public boolean isFilled()  { return wrapped.isFilled(); }

    @Override
    public int getColorId()    { return wrapped.getColorId(); }

    @Override
    public void activate()     { wrapped.activate(); }

    @Override
    public String render()     { return wrapped.render(); }

    /** Sarmalanan hücreyi döndür (zincir gezinmesi için) */
    public Cell getWrapped()   { return wrapped; }

    /**
     * Zincirin en dibindeki (sarmalanmamış) Cell'i döndür.
     * Örn.: PortalDecorator( FilledCell ) → FilledCell döndürür.
     */
    public Cell getBase() {
        Cell current = wrapped;
        while (current instanceof CellDecorator) {
            current = ((CellDecorator) current).wrapped;
        }
        return current;
    }

    /**
     * Bellek temizliği — zincirleme referansları null'la.
     * Oyun sırasında hücre kaldırılınca çağrılmalı.
     */
    public void dispose() {
        if (wrapped instanceof CellDecorator) {
            ((CellDecorator) wrapped).dispose();
        }
        wrapped = null;
    }
}
