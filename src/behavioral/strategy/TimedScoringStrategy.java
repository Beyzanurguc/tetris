package behavioral.strategy;

public class TimedScoringStrategy implements ScoringStrategy {

    private static final int[] BASE_SCORES = {0, 100, 300, 500, 800};

    private final int timeBonus;

    private int elapsedTicks;

    public TimedScoringStrategy(int timeBonus) {
        this.timeBonus    = timeBonus;
        this.elapsedTicks = 0;
    }

    public void setElapsedTicks(int ticks) {
        this.elapsedTicks = ticks;
    }

    @Override
    public int calculateScore(int linesCleared, int level, int comboCount) {
        if (linesCleared <= 0) return 0;
        int idx   = Math.min(linesCleared, 4);
        int base  = BASE_SCORES[idx] * level;
        int bonus = Math.max(0, (timeBonus - elapsedTicks) * 2);
        int combo = comboCount * 30 * level;
        return base + bonus + combo;
    }

    @Override
    public String getStrategyName() { return "TimedScoring"; }

    @Override
    public String toString() {
        return "TimedScoringStrategy [timeBonus=" + timeBonus +
               ", elapsed=" + elapsedTicks + "]";
    }
}
