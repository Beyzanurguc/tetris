package core;

/**
 * CompositeLayer — Composite Deseninin Composite Rolü.
 *
 * Alt BoardLayer'ları (hem SimpleLayer hem başka CompositeLayer)
 * tek bir Board katmanı gibi davranarak birleştirir.
 *
 * Bileşik sorgu kuralı:
 *   Birden fazla alt katman aynı koordinata sahipse,
 *   en üstteki (son eklenen, en yüksek öncelikli) katmanın rengi
 *   döndürülür. Bu "painter's algorithm" benzeri bir katmanlama sağlar.
 *
 * Koleksiyon Yasağı:
 *   Alt katmanlar DynamicArray ile tutulur.
 *
 * Kullanım Örneği:
 *   CompositeLayer board = new CompositeLayer("BoardRoot");
 *   board.add(new SimpleLayer("Base",  20, 10));
 *   board.add(new SimpleLayer("Ghost", 20, 10));
 *   board.add(new SimpleLayer("FX",    20, 10));
 *
 *   int color = board.getCellColorAt(5, 3);  // üst katmandan başlayarak sorgular
 */
public class CompositeLayer extends BoardLayer {

    /** Alt katmanlar — DynamicArray (koleksiyon yasağı) */
    private final DynamicArray children;

    public CompositeLayer(String name) {
        super(name);
        this.children = new DynamicArray(4);
    }

    // ---------------------------------------------------------------- Composite API

    /**
     * Alt katman ekle.
     *
     * @param layer eklenecek BoardLayer (SimpleLayer veya CompositeLayer)
     */
    public void add(BoardLayer layer) {
        if (layer == null) return;
        children.add(layer);
        System.out.println("[CompositeLayer/" + name + "] Katman eklendi: " + layer.getName());
    }

    /**
     * Alt katmanı kaldır (sondaki eşleşen ilk öğe).
     *
     * @param layerName kaldırılacak katmanın adı
     * @return kaldırıldıysa true
     */
    public boolean remove(String layerName) {
        for (int i = 0; i < children.size(); i++) {
            BoardLayer child = (BoardLayer) children.get(i);
            if (child.getName().equals(layerName)) {
                children.remove(i);
                System.out.println("[CompositeLayer/" + name + "] Katman kaldırıldı: " + layerName);
                return true;
            }
        }
        return false;
    }

    /** Alt katman sayısı */
    public int childCount() { return children.size(); }

    /** İndex ile alt katmana eriş */
    public BoardLayer getChild(int index) {
        return (BoardLayer) children.get(index);
    }

    // ---------------------------------------------------------------- Component API

    /**
     * Painter's algorithm: en üst katmandan başlayarak ilk dolu rengi döndür.
     * Tüm katmanlar boşsa Cell.COLOR_EMPTY döner.
     */
    @Override
    public int getCellColorAt(int row, int col) {
        if (!visible) return Cell.COLOR_EMPTY;
        // Ters sırada tara (son eklenen = en üstte)
        for (int i = children.size() - 1; i >= 0; i--) {
            BoardLayer child = (BoardLayer) children.get(i);
            int color = child.getCellColorAt(row, col);
            if (color != Cell.COLOR_EMPTY) return color;
        }
        return Cell.COLOR_EMPTY;
    }

    @Override
    public boolean isFilledAt(int row, int col) {
        if (!visible) return false;
        for (int i = 0; i < children.size(); i++) {
            if (((BoardLayer) children.get(i)).isFilledAt(row, col)) return true;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < children.size(); i++) {
            ((BoardLayer) children.get(i)).clear();
        }
        System.out.println("[CompositeLayer/" + name + "] Tüm katmanlar temizlendi.");
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("CompositeLayer[").append(name).append("] ")
          .append(children.size()).append(" alt katman | visible=").append(visible).append("\n");
        for (int i = 0; i < children.size(); i++) {
            sb.append("  └─ ").append(((BoardLayer) children.get(i)).describe()).append("\n");
        }
        return sb.toString();
    }

    // ---------------------------------------------------------------- convenience

    /**
     * İsme göre alt katman bul (recursive).
     *
     * @param layerName aranacak katman adı
     * @return bulunan BoardLayer veya null
     */
    public BoardLayer find(String layerName) {
        for (int i = 0; i < children.size(); i++) {
            BoardLayer child = (BoardLayer) children.get(i);
            if (child.getName().equals(layerName)) return child;
            if (child instanceof CompositeLayer) {
                BoardLayer found = ((CompositeLayer) child).find(layerName);
                if (found != null) return found;
            }
        }
        return null;
    }
}
