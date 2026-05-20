package behavioral.strategy;

public class ClassicScoringStrategy implements ScoringStrategy {

    private static final int[] BASE_SCORES = {0, 100, 300, 500, 800};

    @Override
    public int calculateScore(int linesCleared, int level, int comboCount) {
        if (linesCleared <= 0) return 0;
        int idx   = Math.min(linesCleared, 4);
        int base  = BASE_SCORES[idx] * level;
        int combo = comboCount * 50 * level;   
        return base + combo;
    }

    @Override
    public String getStrategyName() { return "ClassicScoring"; }

    @Override
    public String toString() {
        return "ClassicScoringStrategy [1L=100*lv, 2L=300*lv, 3L=500*lv, 4L=800*lv]";
    }
}
