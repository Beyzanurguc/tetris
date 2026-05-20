package behavioral.observer;

public class AudioManager implements GameEventListener {

    private boolean muted;   

    public AudioManager() {
        this.muted = false;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (muted) return;

        switch (event.getType()) {
            case LINE_CLEARED:
                System.out.println("[AudioManager] ♪ line-clear.wav (" +
                                   event.getData() + " satır)");
                break;
            case LEVEL_UP:
                System.out.println("[AudioManager] ♪ level-up.wav");
                break;
            case GAME_OVER:
                System.out.println("[AudioManager] ♪ game-over.wav");
                break;
            case PIECE_LANDED:
                System.out.println("[AudioManager] ♪ land.wav");
                break;
            case COMBO_ACHIEVED:
                System.out.println("[AudioManager] ♪ combo.wav x" + event.getData());
                break;
            default:
                break;
        }
    }

    public void setMuted(boolean muted) { this.muted = muted; }
    public boolean isMuted()            { return muted; }

    @Override
    public String toString() {
        return "AudioManager [muted=" + muted + "]";
    }
}
