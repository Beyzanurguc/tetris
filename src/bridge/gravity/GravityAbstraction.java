package bridge.gravity;

public class GravityAbstraction {

    private GravityImplementor implementor;

    public GravityAbstraction(GravityImplementor implementor) {
        if (implementor == null)
            throw new IllegalArgumentException("GravityImplementor null olamaz");
        this.implementor = implementor;
    }

    public int getDropInterval(int level) {
        int ticks = implementor.getTicksPerDrop(level);
        return Math.max(1, ticks);   
    }

    public String describeGravity() {
        return "Gravity Modu: " + implementor.getDescription() +
               " | Tick/Drop (Lv1): " + implementor.getTicksPerDrop(1);
    }

    public void setImplementor(GravityImplementor newImpl) {
        if (newImpl == null)
            throw new IllegalArgumentException("Yeni implementor null olamaz");
        System.out.println("[GravityAbstraction] Implementor değişiyor: " +
                           implementor.getDescription() +
                           " → " + newImpl.getDescription());
        this.implementor = newImpl;
    }

    public GravityImplementor getImplementor() { return implementor; }
}
