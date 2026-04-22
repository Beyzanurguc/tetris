package bridge.theme;

import core.Cell;

/**
 * ClassicThemeImpl — Klasik Siyah/Beyaz Tetris Teması.
 *
 * ANSI renk kodu kullanmaz; sade ASCII karakterlerle çalışır.
 * En düşük sistem gereksinimi — her terminalde çalışır.
 */
public class ClassicThemeImpl implements ThemeImplementor {

    // Parça harflerine göre basit ASCII etiketler
    private static final String[] LABELS = {
        "  ",   // 0 - boş
        "II",   // 1 - I
        "OO",   // 2 - O
        "TT",   // 3 - T
        "SS",   // 4 - S
        "ZZ",   // 5 - Z
        "LL",   // 6 - L
        "JJ",   // 7 - J
        "CC",   // 8 - Custom
        "PP",   // 9 - Portal
        "**",   // 10- PowerUp
    };

    @Override
    public String getColorCode(int colorId)  { return ""; }   // ANSI yok

    @Override
    public String getBackgroundCode()        { return ""; }

    @Override
    public String getReset()                 { return ""; }

    @Override
    public String renderEmptyCell()          { return ".."; }

    @Override
    public String renderFilledCell(int colorId) {
        if (colorId < 0 || colorId >= LABELS.length) return "##";
        return LABELS[colorId];
    }

    @Override
    public String renderPortalCell()         { return "PP"; }

    @Override
    public String renderPowerUpCell()        { return "**"; }

    @Override
    public String getThemeName()             { return "Klasik"; }
}
