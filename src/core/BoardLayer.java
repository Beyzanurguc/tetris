package core;

/**
 * BoardLayer — Composite Deseninin Component ve Leaf/Composite rolü.
 *
 * Composite Deseni Yapısı:
 *
 *   BoardLayer (Component — abstract)
 *     ├── SimpleLayer  (Leaf — tek ızgara katmanı)
 *     └── CompositeLayer (Composite — alt katmanları yönetir)
 *
 * Kullanım Amacı:
 *   Oyun tahtası birden fazla mantıksal katmandan oluşabilir:
 *     - BaseLayer   : Yerleşmiş parçaların sabit katmanı
 *     - GhostLayer  : Ghost piece gösterimi
 *     - OverlayLayer: Portal / PowerUp efektleri
 *
 *   CompositeLayer bu katmanları uniform biçimde yönetir;
 *   dışarıdan bir CompositeLayer ile tek bir SimpleLayer aynı
 *   arayüzden sorgulanır.
 *
 * Koleksiyon Yasağı:
 *   Alt katman listesi DynamicArray ile tutulur (ArrayList yasak).
 */
public abstract class BoardLayer {

    protected final String name;     // katman adı (debug için)
    protected boolean visible;       // katman görünür mü?

    protected BoardLayer(String name) {
        this.name    = name;
        this.visible = true;
    }

    // ---------------------------------------------------------------- Component API

    /** Bu koordinattaki hücreyi render et (tema rengi için colorId döndürür) */
    public abstract int getCellColorAt(int row, int col);

    /** Bu koordinat dolu mu? */
    public abstract boolean isFilledAt(int row, int col);

    /** Katmanı komple temizle */
    public abstract void clear();

    /** Katmanla ilgili debug bilgisi */
    public abstract String describe();

    // ---------------------------------------------------------------- ortak

    public String  getName()    { return name; }
    public boolean isVisible()  { return visible; }

    public void show()          { visible = true; }
    public void hide()          { visible = false; }

    @Override
    public String toString() { return "BoardLayer[" + name + ", visible=" + visible + "]"; }
}
