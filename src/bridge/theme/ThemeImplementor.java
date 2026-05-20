package bridge.theme;

public interface ThemeImplementor {

    String getColorCode(int colorId);

    String getBackgroundCode();

    String renderEmptyCell();

    String renderFilledCell(int colorId);

    String renderPortalCell();

    String renderPowerUpCell();

    String getReset();

    String getThemeName();
}
