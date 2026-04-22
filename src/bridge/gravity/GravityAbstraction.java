package bridge.gravity;

/**
 * GravityAbstraction — Bridge Deseninin Abstraction Tarafı (Gravity).
 *
 * GravityImplementor'a referans tutar ve yüksek seviyeli API sunar.
 * İmplementor runtime'da değiştirilebilir → Varying Gravity özelliği.
 *
 * Kullanım:
 *   GravityAbstraction g = new GravityAbstraction(new NormalGravity());
 *   g.getDropInterval(3);          // 3. seviyede kaç tick'te bir düşer?
 *   g.setImplementor(new ZeroGravity());  // anında yerçekimini kapat
 */
public class GravityAbstraction {

    private GravityImplementor implementor;

    public GravityAbstraction(GravityImplementor implementor) {
        if (implementor == null)
            throw new IllegalArgumentException("GravityImplementor null olamaz");
        this.implementor = implementor;
    }

    // --------------------------------------------------------------- Abstraction API

    /**
     * Mevcut seviye için drop aralığını (tick) hesapla.
     * GameEngine bu değeri mod operatörü ile kullanır:
     *   if (tickCount % getDropInterval(level) == 0) → düşür
     */
    public int getDropInterval(int level) {
        int ticks = implementor.getTicksPerDrop(level);
        return Math.max(1, ticks);   // en az 1 tick
    }

    /**
     * Yerçekimi modunu açıkla (ThemeManager veya HUD için).
     */
    public String describeGravity() {
        return "Gravity Modu: " + implementor.getDescription() +
               " | Tick/Drop (Lv1): " + implementor.getTicksPerDrop(1);
    }

    // --------------------------------------------------------------- Bridge: implementor değişimi

    /**
     * Runtime'da yerçekimi implementorunu değiştir.
     * Oyun akmaya devam ederken bile çağrılabilir.
     *
     * @param newImpl yeni implementor (null olamaz)
     */
    public void setImplementor(GravityImplementor newImpl) {
        if (newImpl == null)
            throw new IllegalArgumentException("Yeni implementor null olamaz");
        System.out.println("[GravityAbstraction] Implementor değişiyor: " +
                           implementor.getDescription() +
                           " → " + newImpl.getDescription());
        this.implementor = newImpl;
    }

    public GravityImplementor getImplementor() { return implementor; }
}
