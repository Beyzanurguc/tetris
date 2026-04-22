package bridge.gravity;

/**
 * GravityImplementor — Bridge Deseninin Implementor Arayüzü (Gravity Tarafı).
 *
 * Bridge Deseni Yapısı:
 *
 *   Abstraction (GravityAbstraction)
 *       └── implementor : GravityImplementor  ← bu arayüz
 *
 *   ConcreteImplementor'lar:
 *       ├── NormalGravity    — klasik Tetris düşüşü
 *       ├── VariableGravity  — seviyeye göre hızlanan düşüş
 *       └── ZeroGravity      — parçalar düşmez (özel mod)
 *
 * Arayüz kasıtlı olarak minimal tutuldu:
 *   Her implementor yalnızca getTicksPerDrop() ve getDescription() sağlar.
 *   Karar mantığı Abstraction tarafında kalır.
 */
public interface GravityImplementor {

    /**
     * Verilen seviyede kaç game-tick'te bir parça bir satır düşmeli?
     *
     * @param level mevcut oyun seviyesi (1'den başlar)
     * @return tick sayısı (küçük = hızlı)
     */
    int getTicksPerDrop(int level);

    /** İnsan tarafından okunabilir açıklama (debug ve UI için) */
    String getDescription();
}
