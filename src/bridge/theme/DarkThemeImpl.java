package bridge.theme;

import core.Cell;

public class DarkThemeImpl implements ThemeImplementor {

    private static final int[] FG_COLORS = {
        240,  
        51,   
        226,  
        135,  
        82,   
        196,  
        208,  
        27,   
        255,  
        45,   
        220,  
    };

    private static final String BG   = "\u001B[48;5;234m";  
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
