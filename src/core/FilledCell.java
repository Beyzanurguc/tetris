package core;

public class FilledCell extends Cell {

    public FilledCell(int colorId) {
        super(true, colorId);
    }

    @Override
    public boolean isFilled()  { return true; }

    @Override
    public int getColorId()    { return colorId; }

    @Override
    public void activate() {

        System.out.println("[FilledCell] activate() — renk: " + colorId);
    }

    @Override
    public String render() {

        return "[" + colorId + "]";
    }
}
