package bridge.gravity;

/**
 * VariableGravity — Ödevde İstenen "Varying Gravity" Implementasyonu.
 *
 * Yerçekimi katsayısı (multiplier) ile temel eğri çarpılır.
 * GameEngine runtime'da multiplier'ı değiştirerek:
 *   - speedy mod : multiplier < 1  (daha hızlı düşüş)
 *   - slow mo    : multiplier > 1  (daha yavaş düşüş)
 *   - chaos mod  : periyodik olarak değişen multiplier
 *
 * Ayrıca belirli tick aralıklarında katsayı kendi kendine
 * değişebilir (applyVariation() ile).
 */
public class VariableGravity implements GravityImplementor {

    private double multiplier;          // 0.25 → 4.0 arası önerilen aralık
    private int    variationPeriod;     // kaç tick'te bir otomatik değişim
    private int    internalTick;        // dahili sayaç
    private double variationDelta;      // her değişimde multiplier değişimi

    private static final double MIN_MULTIPLIER = 0.1;
    private static final double MAX_MULTIPLIER = 5.0;

    /**
     * @param initialMultiplier başlangıç katsayısı (1.0 = normal hız)
     * @param variationPeriod   0 → sabit; >0 → periyodik otomatik değişim tick sayısı
     * @param variationDelta    her periyotta multiplier'a eklenen değer (negatif olabilir)
     */
    public VariableGravity(double initialMultiplier, int variationPeriod, double variationDelta) {
        this.multiplier      = clamp(initialMultiplier);
        this.variationPeriod = variationPeriod;
        this.variationDelta  = variationDelta;
        this.internalTick    = 0;
    }

    /** Sabit (non-varying) VariableGravity */
    public VariableGravity(double multiplier) {
        this(multiplier, 0, 0.0);
    }

    @Override
    public int getTicksPerDrop(int level) {
        if (level < 1) level = 1;
        // Temel eğri
        int base = Math.max(1, 48 - (level - 1) * 5);
        // Katsayı uygula
        int result = (int) Math.round(base * multiplier);
        // Periyodik değişim
        applyVariation();
        return Math.max(1, result);
    }

    @Override
    public String getDescription() {
        return String.format("Değişken Yerçekimi (x%.2f)", multiplier);
    }

    // --------------------------------------------------------------- API

    public void setMultiplier(double m) {
        this.multiplier = clamp(m);
        System.out.println("[VariableGravity] Multiplier → " +
                           String.format("%.2f", this.multiplier));
    }

    public double getMultiplier() { return multiplier; }

    /** Manuel otomatik değişim tetikle */
    public void applyVariation() {
        if (variationPeriod <= 0) return;
        internalTick++;
        if (internalTick % variationPeriod == 0) {
            double newVal = multiplier + variationDelta;
            // Sınıra ulaşınca delta tersine çevir (ping-pong)
            if (newVal > MAX_MULTIPLIER || newVal < MIN_MULTIPLIER) {
                variationDelta = -variationDelta;
                newVal = clamp(newVal);
            }
            multiplier = newVal;
            System.out.println("[VariableGravity] Otomatik değişim → x" +
                               String.format("%.2f", multiplier));
        }
    }

    private double clamp(double val) {
        return Math.max(MIN_MULTIPLIER, Math.min(MAX_MULTIPLIER, val));
    }
}
