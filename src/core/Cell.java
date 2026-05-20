package core;

public abstract class Cell {

    public static final int COLOR_EMPTY   = 0;
    public static final int COLOR_I       = 1;   
    public static final int COLOR_O       = 2;   
    public static final int COLOR_T       = 3;   
    public static final int COLOR_S       = 4;   
    public static final int COLOR_Z       = 5;   
    public static final int COLOR_L       = 6;   
    public static final int COLOR_J       = 7;   
    public static final int COLOR_CUSTOM  = 8;   
    public static final int COLOR_PORTAL  = 9;   
    public static final int COLOR_POWERUP = 10;  

    protected boolean filled;   
    protected int     colorId;  

    protected Cell(boolean filled, int colorId) {
        this.filled  = filled;
        this.colorId = colorId;
    }

    public abstract boolean isFilled();

    public abstract int getColorId();

    public abstract void activate();

    public abstract String render();

    public void setFilled(boolean filled) { this.filled  = filled;  }
    public void setColorId(int colorId)   { this.colorId = colorId; }
}
