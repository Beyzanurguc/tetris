package core;

public abstract class BoardLayer {

    protected final String name;     
    protected boolean visible;       

    protected BoardLayer(String name) {
        this.name    = name;
        this.visible = true;
    }

    public abstract int getCellColorAt(int row, int col);

    public abstract boolean isFilledAt(int row, int col);

    public abstract void clear();

    public abstract String describe();

    public String  getName()    { return name; }
    public boolean isVisible()  { return visible; }

    public void show()          { visible = true; }
    public void hide()          { visible = false; }

    @Override
    public String toString() { return "BoardLayer[" + name + ", visible=" + visible + "]"; }
}
