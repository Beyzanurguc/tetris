package bridge;

import bridge.theme.ThemeAbstraction;
import bridge.theme.ThemeImplementor;
import bridge.theme.ClassicThemeImpl;
import bridge.theme.DarkThemeImpl;
import bridge.theme.NeonThemeImpl;

/**
 * ThemeManager — Tema Yönetim Sınıfı.
 *
 * Tasarım Deseni: Bridge + Singleton benzeri tek-örnek kullanım.
 * (Gerçek Singleton değil — GameEngine üzerinden erişilir,
 *  ama oyun boyunca tek bir ThemeManager nesnesi yaşar.)
 *
 * Oyun mantığından tam bağımsızlık:
 *   - GameEngine ThemeManager'ı yalnızca render için çağırır.
 *   - Tema değişikliği GameEngine'i durdurmaz veya etkilemez.
 *   - Yeni tema eklemek için yalnızca ThemeImplementor eklenir;
 *     ThemeManager'da sadece register/switch çağrısı yapılır.
 *
 * Tema Kaydı:
 *   java.util.HashMap YASAK → sabit boyutlu dizi çifti kullanıldı:
 *   String[] themeNames + ThemeImplementor[] themeImpls (max 8 tema)
 */
public class ThemeManager {

    // --------------------------------------------------------------- Singleton (lazy)
    private static ThemeManager instance = null;

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    // --------------------------------------------------------------- Sabitler
    private static final int MAX_THEMES = 8;

    // --------------------------------------------------------------- Tema Kaydı (HashMap yerine paralel diziler)
    private final ThemeImplementor[] registeredImpls;
    private final String[]           registeredNames;
    private int                      registeredCount;

    // --------------------------------------------------------------- Aktif Tema (Bridge Abstraction)
    private final ThemeAbstraction   themeAbstraction;
    private int                      activeIndex;

    // --------------------------------------------------------------- Ctor (private)
    private ThemeManager() {
        registeredImpls  = new ThemeImplementor[MAX_THEMES];
        registeredNames  = new String[MAX_THEMES];
        registeredCount  = 0;
        activeIndex      = 0;

        // Yerleşik temaları kaydet
        register("Klasik", new ClassicThemeImpl());
        register("Dark",   new DarkThemeImpl());
        register("Neon",   new NeonThemeImpl());

        // Varsayılan tema: Dark
        themeAbstraction = new ThemeAbstraction(registeredImpls[1]);
        activeIndex = 1;

        System.out.println("[ThemeManager] Başlatıldı. Aktif tema: " +
                           themeAbstraction.getThemeName());
    }

    // --------------------------------------------------------------- Public API

    /**
     * Yeni tema kaydet (maks MAX_THEMES).
     *
     * @param name  kullanıcıya gösterilen ad
     * @param impl  implementor nesnesi
     * @return true → kayıt başarılı
     */
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

    /**
     * İsme göre tema değiştir.
     *
     * @param name kayıtlı tema adı
     * @return true → değişiklik başarılı
     */
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

    /**
     * Index'e göre tema değiştir.
     *
     * @param index 0 tabanlı tema dizini
     */
    public boolean switchThemeByIndex(int index) {
        if (index < 0 || index >= registeredCount) return false;
        themeAbstraction.setImplementor(registeredImpls[index]);
        activeIndex = index;
        return true;
    }

    /**
     * Sıradaki temaya geç (döngüsel).
     */
    public void nextTheme() {
        activeIndex = (activeIndex + 1) % registeredCount;
        themeAbstraction.setImplementor(registeredImpls[activeIndex]);
    }

    // --------------------------------------------------------------- Render API (Bridge'e delege)

    public String renderEmpty()              { return themeAbstraction.renderEmpty();        }
    public String renderFilled(int colorId)  { return themeAbstraction.renderFilled(colorId); }
    public String renderPortal()             { return themeAbstraction.renderPortal();       }
    public String renderPowerUp()            { return themeAbstraction.renderPowerUp();      }

    // --------------------------------------------------------------- Info

    public String getActiveThemeName()       { return themeAbstraction.getThemeName(); }
    public int    getRegisteredThemeCount()  { return registeredCount; }
    public ThemeAbstraction getAbstraction() { return themeAbstraction; }

    /** Kayıtlı tüm tema adlarını satır satır döndür */
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
