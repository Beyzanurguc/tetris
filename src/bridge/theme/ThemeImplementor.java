package bridge.theme;

/**
 * ThemeImplementor — Bridge Deseninin Implementor Arayüzü (Tema Tarafı).
 *
 * Bridge Deseni Yapısı (Tema):
 *
 *   Abstraction  : ThemeAbstraction
 *   Implementor  : ThemeImplementor  ← bu arayüz
 *
 *   ConcreteImplementor'lar:
 *       ├── ClassicThemeImpl  — klasik siyah/beyaz Tetris
 *       ├── DarkThemeImpl     — modern koyu tema
 *       └── NeonThemeImpl     — neon parlayan renkler
 *
 * Renk değerleri ANSI escape kodu olarak döndürülür
 * (konsol renderlama için). GUI adaptasyonu ThemeAbstraction tarafında yapılır.
 *
 * Oyun SEVİYESİNDEN bağımsızdır — sadece renk / stil sağlar.
 */
public interface ThemeImplementor {

    /** Parça renk ID'sine karşılık gelen ANSI renk kodunu döndür */
    String getColorCode(int colorId);

    /** Arka plan rengi (Board çerçevesi) */
    String getBackgroundCode();

    /** Boş hücre gösterimi */
    String renderEmptyCell();

    /** Dolu hücre gösterimi (renk ID verilir) */
    String renderFilledCell(int colorId);

    /** Portal hücresi gösterimi */
    String renderPortalCell();

    /** Bonus / PowerUp hücresi gösterimi */
    String renderPowerUpCell();

    /** ANSI sıfırlama kodu */
    String getReset();

    /** Temanın kullanıcıya gösterilen adı */
    String getThemeName();
}
