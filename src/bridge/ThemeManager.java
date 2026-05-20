package bridge;

import bridge.theme.ThemeAbstraction;
import bridge.theme.ThemeImplementor;
import bridge.theme.ClassicThemeImpl;
import bridge.theme.DarkThemeImpl;
import bridge.theme.NeonThemeImpl;

public class ThemeManager {

    private static ThemeManager instance = null;

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    private static final int MAX_THEMES = 8;

    private final ThemeImplementor[] registeredImpls;
    private final String[]           registeredNames;
    private int                      registeredCount;

    private final ThemeAbstraction   themeAbstraction;
    private int                      activeIndex;

    private ThemeManager() {
        registeredImpls  = new ThemeImplementor[MAX_THEMES];
        registeredNames  = new String[MAX_THEMES];
        registeredCount  = 0;
        activeIndex      = 0;

        register("Klasik", new ClassicThemeImpl());
        register("Dark",   new DarkThemeImpl());
        register("Neon",   new NeonThemeImpl());

        themeAbstraction = new ThemeAbstraction(registeredImpls[1]);
        activeIndex = 1;

        System.out.println("[ThemeManager] Başlatıldı. Aktif tema: " +
                           themeAbstraction.getThemeName());
    }

    public boolean register(String name, ThemeImplementor impl) {
        if (registeredCount >= MAX_THEMES) {
            System.out.println("[ThemeManager] Tema kapasitesi dolu (max " + MAX_THEMES + ")");
            return false;
        }
        if (name == null || impl == null) return false;
        registeredNames[registeredCount] = name;
        registeredImpls[registeredCount] = impl;
        registeredCount++;
        System.out.println("[ThemeManager] Tema kaydedildi: " + name);
        return true;
    }

    public boolean switchTheme(String name) {
        for (int i = 0; i < registeredCount; i++) {
            if (registeredNames[i].equalsIgnoreCase(name)) {
                themeAbstraction.setImplementor(registeredImpls[i]);
                activeIndex = i;
                return true;
            }
        }
        System.out.println("[ThemeManager] Tema bulunamadı: " + name);
        return false;
    }

    public boolean switchThemeByIndex(int index) {
        if (index < 0 || index >= registeredCount) return false;
        themeAbstraction.setImplementor(registeredImpls[index]);
        activeIndex = index;
        return true;
    }

    public void nextTheme() {
        activeIndex = (activeIndex + 1) % registeredCount;
        themeAbstraction.setImplementor(registeredImpls[activeIndex]);
    }

    public String renderEmpty()              { return themeAbstraction.renderEmpty();        }
    public String renderFilled(int colorId)  { return themeAbstraction.renderFilled(colorId); }
    public String renderPortal()             { return themeAbstraction.renderPortal();       }
    public String renderPowerUp()            { return themeAbstraction.renderPowerUp();      }

    public String getActiveThemeName()       { return themeAbstraction.getThemeName(); }
    public int    getRegisteredThemeCount()  { return registeredCount; }
    public ThemeAbstraction getAbstraction() { return themeAbstraction; }

    public String listThemes() {
        StringBuilder sb = new StringBuilder("Kayıtlı Temalar:\n");
        for (int i = 0; i < registeredCount; i++) {
            sb.append("  [").append(i).append("] ")
              .append(registeredNames[i]);
            if (i == activeIndex) sb.append(" ← aktif");
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ThemeManager [aktif=" + getActiveThemeName() +
               ", toplam=" + registeredCount + "]";
    }
}
