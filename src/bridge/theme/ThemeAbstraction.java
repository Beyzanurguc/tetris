package bridge.theme;

public class ThemeAbstraction {

    protected ThemeImplementor implementor;

    public ThemeAbstraction(ThemeImplementor implementor) {
        if (implementor == null)
            throw new IllegalArgumentException("ThemeImplementor null olamaz");
        this.implementor = implementor;
    }

    public String renderEmpty() {
        return implementor.getBackgroundCode() +
               implementor.renderEmptyCell() +
               implementor.getReset();
    }

    public String renderFilled(int colorId) {
        return implementor.getColorCode(colorId) +
               implementor.renderFilledCell(colorId) +
               implementor.getReset();
    }

    public String renderPortal() {
        return implementor.getColorCode(9) +
               implementor.renderPortalCell() +
               implementor.getReset();
    }

    public String renderPowerUp() {
        return implementor.getColorCode(10) +
               implementor.renderPowerUpCell() +
               implementor.getReset();
    }

    public String getThemeName() {
        return implementor.getThemeName();
    }

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
