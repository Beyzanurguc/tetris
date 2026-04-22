package bridge.gravity;

/**
 * ZeroGravity — Parçaların Düşmediği Özel Mod.
 *
 * getTicksPerDrop() çok büyük bir değer döndürür →
 * oyun döngüsünde hiçbir zaman otomatik düşüş gerçekleşmez.
 * Parça yalnızca kullanıcının moveDown() / hardDrop() ile hareket eder.
 *
 * Kullanım senaryosu: Öğretici mod, debug, "anti-gravity" özel seviye.
 */
public class ZeroGravity implements GravityImplementor {

    private static final int NEVER_DROP = Integer.MAX_VALUE;

    @Override
    public int getTicksPerDrop(int level) {
        return NEVER_DROP;   // otomatik düşüş yok
    }

    @Override
    public String getDescription() { return "Sıfır Yerçekimi (Manuel Mod)"; }
}
