package behavioral.strategy;

public interface ScoringStrategy {

    int calculateScore(int linesCleared, int level, int comboCount);

    String getStrategyName();
}
