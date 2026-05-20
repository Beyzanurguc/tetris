package bridge.gravity;

public class NormalGravity implements GravityImplementor {

    @Override
    public int getTicksPerDrop(int level) {
        if (level < 1) level = 1;
        return Math.max(1, 48 - (level - 1) * 5);
    }

    @Override
    public String getDescription() { return "Normal Yerçekimi"; }
}
