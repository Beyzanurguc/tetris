package bridge.theme;

import core.Cell;

/**
 * NeonThemeImpl — Neon Parlayan Tema (ANSI Bold + Bright Colors).
 *
 * Siyah arka plan üzerinde yüksek kontrastlı neon renkler.
 * Bold + bright ANSI kombinasyonu: \u001B[1;9<n>m
 */
public class NeonThemeImpl implements ThemeImplementor {

    // ANSI bold+bright kombinasyonları (standart 16 renk)
    private static final String[] NEON_CODES = {
        "\u001B[0m",          // 0  - boş
        "\u001B[1;96m",       // 1  - I (parlak cyan)
        "\u001B[1;93m",       // 2  - O (parlak sarı)
        "\u001B[1;95m",       // 3  - T (parlak mor)
        "\u001B[1;92m",       // 4  - S (parlak yeşil)
        "\u001B[1;91m",       // 5  - Z (parlak kırmızı)
        "\u001B[1;33m",       // 6  - L (turuncu)
        "\u001B[1;94m",       // 7  - J (parlak mavi)
        "\u001B[1;97m",       // 8  - Custom (parlak beyaz)
        "\u001B[1;96m",       // 9  - Portal (cyan + yanıp söner efekti)
        "\u001B[1;93m",       // 10 - PowerUp (parlak sarı)
    };

    private static final String BG    = "\u001B[40m";     // siyah arka plan
    private static final String RESET = "\u001B[0m";
    private static final String BLINK = "\u001B[5m";      // yanıp söner (portal)

    @Override
    public String getColorCode(int colorId) {
        int idx = clamp(colorId);
        return NEON_CODES[idx];
    }

    @Override
    public String getBackgroundCode() { return BG; }

    @Override
    public String getReset()          { return RESET; }

    @Override
    public String renderEmptyCell() {
        return BG + "\u001B[90m" + "··" + RESET;   // nokta efekti
    }

    @Override
    public String renderFilledCell(int colorId) {
        return getColorCode(colorId) + BG + "▓▓" + RESET;
    }

    @Override
    public String renderPortalCell() {
        // Portal: yanıp sönen parlak cyan
        return BLINK + "\u001B[1;96m" + BG + "◉◉" + RESET;
    }

    @Override
    public String renderPowerUpCell() {
        return "\u001B[1;93m" + BG + "✦✦" + RESET;
    }

    @Override
    public String getThemeName() { return "Neon"; }

    private int clamp(int id) {
        return (id < 0 || id >= NEON_CODES.length) ? 8 : id;
    }
}
