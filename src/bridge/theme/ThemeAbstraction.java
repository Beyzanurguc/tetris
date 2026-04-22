package bridge.theme;

/**
 * ThemeAbstraction — Bridge Deseninin Abstraction Tarafı (Tema).
 *
 * Oyun mantığından tamamen bağımsız:
 *   - GameEngine ve Board bu sınıfa referans verir
 *   - ThemeImplementor'u ne zaman değiştireceğini bilmez
 *   - Yalnızca "bana bu hücreyi render et" der
 *
 * Runtime tema değiştirme:
 *   themeAbstraction.setImplementor(new NeonThemeImpl());
 *   → Oyun durmadan tema değişir (Bridge avantajı)
 */
public class ThemeAbstraction {

    protected ThemeImplementor implementor;

    public ThemeAbstraction(ThemeImplementor implementor) {
        if (implementor == null)
            throw new IllegalArgumentException("ThemeImplementor null olamaz");
        this.implementor = implementor;
    }

    // --------------------------------------------------------------- Yüksek Seviye API

    /**
     * Boş bir hücreyi temanın stilinde render et.
     */
    public String renderEmpty() {
        return implementor.getBackgroundCode() +
               implementor.renderEmptyCell() +
               implementor.getReset();
    }

    /**
     * Dolu bir hücreyi temanın stilinde render et.
     * @param colorId Cell.COLOR_* sabiti
     */
    public String renderFilled(int colorId) {
        return implementor.getColorCode(colorId) +
               implementor.renderFilledCell(colorId) +
               implementor.getReset();
    }

    /** Portal hücresini render et */
    public String renderPortal() {
        return implementor.getColorCode(9) +
               implementor.renderPortalCell() +
               implementor.getReset();
    }

    /** PowerUp hücresini render et */
    public String renderPowerUp() {
        return implementor.getColorCode(10) +
               implementor.renderPowerUpCell() +
               implementor.getReset();
    }

    /** Aktif temanın adını döndür */
    public String getThemeName() {
        return implementor.getThemeName();
    }

    // --------------------------------------------------------------- Bridge: Implementor

    /**
     * Tema implementorunu runtime'da değiştir.
     * Oyun aktifken çağrılabilir — GameEngine durdurmaya gerek yok.
     */
    public void setImplementor(ThemeImplementor newImpl) {
        if (newImpl == null)
            throw new IllegalArgumentException("Yeni tema implementor null olamaz");
        System.out.println("[ThemeAbstraction] Tema değişiyor: " +
                           implementor.getThemeName() +
                           " → " + newImpl.getThemeName());
        this.implementor = newImpl;
    }

    public ThemeImplementor getImplementor() { return implementor; }
}
