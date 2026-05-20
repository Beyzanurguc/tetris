package bridge.theme;

import core.Cell;

public class NeonThemeImpl implements ThemeImplementor {

    private static final String[] NEON_CODES = {
        "\u001B[0m",          
        "\u001B[1;96m",       
        "\u001B[1;93m",       
        "\u001B[1;95m",       
        "\u001B[1;92m",       
        "\u001B[1;91m",       
        "\u001B[1;33m",       
        "\u001B[1;94m",       
        "\u001B[1;97m",       
        "\u001B[1;96m",       
        "\u001B[1;93m",       
    };

    private static final String BG    = "\u001B[40m";     
    private static final String RESET = "\u001B[0m";
    private static final String BLINK = "\u001B[5m";      

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
        return BG + "\u001B[90m" + "··" + RESET;   
    }

    @Override
    public String renderFilledCell(int colorId) {
        return getColorCode(colorId) + BG + "▓▓" + RESET;
    }

    @Override
    public String renderPortalCell() {

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
