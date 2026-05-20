package bridge.theme;

import core.Cell;

public class ClassicThemeImpl implements ThemeImplementor {

    private static final String[] LABELS = {
        "  ",   
        "II",   
        "OO",   
        "TT",   
        "SS",   
        "ZZ",   
        "LL",   
        "JJ",   
        "CC",   
        "PP",   
        "**",   
    };

    @Override
    public String getColorCode(int colorId)  { return ""; }   

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
