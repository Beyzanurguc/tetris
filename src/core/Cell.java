package core;

/**
 * Cell — Tetris tahtasındaki tek bir hücreyi temsil eden soyut sınıf.
 *
 * Decorator deseninin bileşeni (Component) rolündedir.
 * Alt sınıflar:
 *   • EmptyCell   — boş hücre
 *   • FilledCell  — yerleşmiş parça hücresi
 * Dekoratörler (decorators/):
 *   • CellDecorator     — dekoratör base
 *   • PortalDecorator   — teleport
 *   • PowerUpDecorator  — bonus
 */
public abstract class Cell {

    // ---------------------------------------------------- renk sabitleri
    public static final int COLOR_EMPTY   = 0;
    public static final int COLOR_I       = 1;   // Cyan
    public static final int COLOR_O       = 2;   // Yellow
    public static final int COLOR_T       = 3;   // Purple
    public static final int COLOR_S       = 4;   // Green
    public static final int COLOR_Z       = 5;   // Red
    public static final int COLOR_L       = 6;   // Orange
    public static final int COLOR_J       = 7;   // Blue
    public static final int COLOR_CUSTOM  = 8;   // Kullanıcı tanımlı
    public static final int COLOR_PORTAL  = 9;   // Portal efekti
    public static final int COLOR_POWERUP = 10;  // Bonus hücre

    // ---------------------------------------------------- state
    protected boolean filled;   // hücre dolu mu?
    protected int     colorId;  // renk kimliği (yukarıdaki sabitler)

    // --------------------------------------------------------- ctor
    protected Cell(boolean filled, int colorId) {
        this.filled  = filled;
        this.colorId = colorId;
    }

    // --------------------------------------------------------- abstract API
    /** Hücrenin dolu olup olmadığını döndür */
    public abstract boolean isFilled();

    /** Hücrenin renk kimliğini döndür */
    public abstract int getColorId();

    /**
     * Hücrenin özel davranışını tetikle.
     * Normal hücrelerde hiçbir şey yapmaz;
     * dekoratörler bu metodu override ederek davranış katar.
     */
    public abstract void activate();

    /** Hücreyi görsel temsil için stringe dönüştür */
    public abstract String render();

    // --------------------------------------------------------- convenience
    public void setFilled(boolean filled) { this.filled  = filled;  }
    public void setColorId(int colorId)   { this.colorId = colorId; }
}
