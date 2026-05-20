package behavioral.strategy;

import behavioral.observer.GameEvent;
import behavioral.observer.GameEventListener;

public class ScoreManager implements GameEventListener {

    private ScoringStrategy strategy;   
    private int             currentScore;
    private int             comboCount;  

    public ScoreManager(ScoringStrategy strategy) {
        this.strategy     = strategy;
        this.currentScore = 0;
        this.comboCount   = 0;
    }

    public void setStrategy(ScoringStrategy newStrategy) {
        if (newStrategy == null) return;
        System.out.println("[ScoreManager] Strateji değişti: " +
                           strategy.getStrategyName() + " → " +
                           newStrategy.getStrategyName());
        this.strategy = newStrategy;
    }

    public ScoringStrategy getStrategy() { return strategy; }

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case LINE_CLEARED:
                int linesCleared = event.getData();
                comboCount++;

                int gained = strategy.calculateScore(linesCleared, 1, comboCount - 1);
                currentScore += gained;
                System.out.println("[ScoreManager] +" + gained +
                                   " puan (lines=" + linesCleared +
                                   ", combo=" + (comboCount - 1) +
                                   ", strateji=" + strategy.getStrategyName() + ")");
                break;
            case LEVEL_UP:
                System.out.println("[ScoreManager] Seviye atlandı → " + event.getData());
                break;
            case GAME_OVER:
                System.out.println("[ScoreManager] Oyun bitti. Toplam Skor: " + currentScore);
                break;
            case COMBO_ACHIEVED:
                System.out.println("[ScoreManager] COMBO x" + event.getData() + "!");
                break;
            default:
                break;
        }
    }

    public int getCurrentScore() { return currentScore; }
    public int getComboCount()   { return comboCount; }

    public void resetCombo() {
        comboCount = 0;
    }

    public void reset() {
        currentScore = 0;
        comboCount   = 0;
    }

    @Override
    public String toString() {
        return "ScoreManager [score=" + currentScore +
               ", combo=" + comboCount +
               ", strategy=" + strategy.getStrategyName() + "]";
    }
}
