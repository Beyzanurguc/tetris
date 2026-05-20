package decorators;

import core.Cell;

public class PortalDecorator extends CellDecorator {

    private int   portalTargetRow;
    private int   portalTargetCol;
    private boolean active;         
    private int   portalId;         

    private static int nextPortalId = 1;   

    public PortalDecorator(Cell cell, int targetRow, int targetCol) {
        super(cell);
        this.portalTargetRow = targetRow;
        this.portalTargetCol = targetCol;
        this.active          = true;
        this.portalId        = nextPortalId++;

        this.colorId         = Cell.COLOR_PORTAL;
    }

    @Override
    public int getColorId() { return Cell.COLOR_PORTAL; }

    @Override
    public boolean isFilled() { return true; }

    @Override
    public void activate() {
        if (!active) {
            System.out.println("[Portal #" + portalId + "] Devre dışı.");
            return;
        }
        System.out.println("[Portal #" + portalId + "] AKTIVE! " +
                           "Hedef: (" + portalTargetRow + ", " + portalTargetCol + ")");
        wrapped.activate();   

    }

    @Override
    public String render() {
        return active ? "[P]" : wrapped.render();
    }

    public int  getPortalTargetRow()     { return portalTargetRow; }
    public int  getPortalTargetCol()     { return portalTargetCol; }
    public int  getPortalId()            { return portalId;         }
    public boolean isActive()            { return active;           }

    public void setTarget(int row, int col) {
        this.portalTargetRow = row;
        this.portalTargetCol = col;
    }

    public void deactivate() {
        this.active = false;
        System.out.println("[Portal #" + portalId + "] Devre dışı bırakıldı.");
    }

    public static void resetIdCounter() {
        nextPortalId = 1;
    }

    @Override
    public String toString() {
        return "PortalDecorator{id=" + portalId +
               ", target=(" + portalTargetRow + "," + portalTargetCol + ")" +
               ", active=" + active + "}";
    }
}
