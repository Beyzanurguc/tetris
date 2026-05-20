package bridge.gravity;

public class VariableGravity implements GravityImplementor {

    private double multiplier;          
    private int    variationPeriod;     
    private int    internalTick;        
    private double variationDelta;      

    private static final double MIN_MULTIPLIER = 0.1;
    private static final double MAX_MULTIPLIER = 5.0;

    public VariableGravity(double initialMultiplier, int variationPeriod, double variationDelta) {
        this.multiplier      = clamp(initialMultiplier);
        this.variationPeriod = variationPeriod;
        this.variationDelta  = variationDelta;
        this.internalTick    = 0;
    }

    public VariableGravity(double multiplier) {
        this(multiplier, 0, 0.0);
    }

    @Override
    public int getTicksPerDrop(int level) {
        if (level < 1) level = 1;

        int base = Math.max(1, 48 - (level - 1) * 5);

        int result = (int) Math.round(base * multiplier);

        applyVariation();
        return Math.max(1, result);
    }

    @Override
    public String getDescription() {
        return String.format("Değişken Yerçekimi (x%.2f)", multiplier);
    }

    public void setMultiplier(double m) {
        this.multiplier = clamp(m);
        System.out.println("[VariableGravity] Multiplier → " +
                           String.format("%.2f", this.multiplier));
    }

    public double getMultiplier() { return multiplier; }

    public void applyVariation() {
        if (variationPeriod <= 0) return;
        internalTick++;
        if (internalTick % variationPeriod == 0) {
            double newVal = multiplier + variationDelta;

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
