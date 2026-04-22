package bridge.theme;

import core.Cell;

/**
 * DarkThemeImpl — Modern Koyu Tema (ANSI 256-Color).
 *
 * Koyu arka plan üzerinde pastel renkler.
 * ANSI kaçış dizileri:  \u001B[38;5;<n>m  (ön plan)
 *                        \u001B[48;5;<n>m  (arka plan)
 */
public class DarkThemeImpl implements ThemeImplementor {

    // ANSI 256-color kodları (her parçaya özel)
    private static final int[] FG_COLORS = {
        240,  // 0 - boş      (koyu gri)
        51,   // 1 - I        (açık cyan)
        226,  // 2 - O        (sarı)
        135,  // 3 - T        (mor)
        82,   // 4 - S        (yeşil)
        196,  // 5 - Z        (kırmızı)
        208,  // 6 - L        (turuncu)
        27,   // 7 - J        (mavi)
        255,  // 8 - Custom   (beyaz)
        45,   // 9 - Portal   (parlak mavi)
        220,  // 10- PowerUp  (altın sarısı)
    };

    private static final String BG   = "\u001B[48;5;234m";  // çok koyu gri arka plan
    private static final String RESET= "\u001B[0m";

    @Override
    public String getColorCode(int colorId) {
        int idx = clamp(colorId);
        return "\u001B[38;5;" + FG_COLORS[idx] + "m";
    }

    @Override
    public String getBackgroundCode() { return BG; }

    @Override
    public String getReset()          { return RESET; }

    @Override
    public String renderEmptyCell() {
        return BG + "\u001B[38;5;236m" + "░░" + RESET;
    }

    @Override
    public String renderFilledCell(int colorId) {
        return getColorCode(colorId) + BG + "██" + RESET;
    }

    @Override
    public String renderPortalCell() {
        return "\u001B[38;5;45m" + BG + "◈◈" + RESET;
    }

    @Override
    public String renderPowerUpCell() {
        return "\u001B[38;5;220m" + BG + "★★" + RESET;
    }

    @Override
    public String getThemeName() { return "Dark"; }

    private int clamp(int id) {
        return (id < 0 || id >= FG_COLORS.length) ? 8 : id;
    }
}
