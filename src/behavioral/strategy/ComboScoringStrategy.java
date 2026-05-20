package behavioral.strategy;

public class ComboScoringStrategy implements ScoringStrategy {

    private static final int[]    BASE_SCORES      = {0, 100, 300, 500, 800};

    private static final double[] COMBO_MULTIPLIERS = {1.0, 1.5, 2.0, 2.5, 3.0};

    @Override
    public int calculateScore(int linesCleared, int level, int comboCount) {
        if (linesCleared <= 0) return 0;
        int idx        = Math.min(linesCleared, 4);
        int base       = BASE_SCORES[idx] * level;
        int comboIdx   = Math.min(comboCount, COMBO_MULTIPLIERS.length - 1);
        double mult    = COMBO_MULTIPLIERS[comboIdx];
        return (int)(base * mult);
    }

    @Override
    public String getStrategyName() { return "ComboScoring"; }

    @Override
    public String toString() {
        return "ComboScoringStrategy [multipliers: x1.0 -> x1.5 -> x2.0 -> x2.5 -> x3.0]";
    }
}
