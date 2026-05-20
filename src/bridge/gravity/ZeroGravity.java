package bridge.gravity;

public class ZeroGravity implements GravityImplementor {

    private static final int NEVER_DROP = Integer.MAX_VALUE;

    @Override
    public int getTicksPerDrop(int level) {
        return NEVER_DROP;   
    }

    @Override
    public String getDescription() { return "Sıfır Yerçekimi (Manuel Mod)"; }
}
