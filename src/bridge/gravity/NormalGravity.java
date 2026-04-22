package bridge.gravity;

/**
 * NormalGravity — Standart Tetris Yerçekimi Implementasyonu.
 *
 * Klasik Tetris hız eğrisi:
 *   Seviye n → ticksPerDrop = max(1, 48 - (n-1) * 5)
 *
 *   Lv 1  → 48 tick  ≈ 0.8 saniye (60 fps varsayımı)
 *   Lv 5  → 28 tick
 *   Lv 10 →  3 tick
 *   Lv 10+→  1 tick  (minimum)
 */
public class NormalGravity implements GravityImplementor {

    @Override
    public int getTicksPerDrop(int level) {
        if (level < 1) level = 1;
        return Math.max(1, 48 - (level - 1) * 5);
    }

    @Override
    public String getDescription() { return "Normal Yerçekimi"; }
}
