package decorators;

import core.Cell;

/**
 * PortalDecorator — Portal (Teleport) Özelliği Kazandıran Dekoratör.
 *
 * Bir hücreyi Portal haline getirir:
 *   - activate() çağrıldığında portalın hedef koordinatına
 *     (portalTargetRow, portalTargetCol) "sinyal" gönderir.
 *   - Gerçek teleport lojiki GameEngine tarafından işlenir
 *     (bu dekoratör sadece meta-veriyi saklar).
 *   - render() Portal sembolü gösterir → konsol çıktısında fark edilir.
 *   - isFilled() → true; colorId → COLOR_PORTAL.
 *
 * Kullanım:
 *   Cell base = new FilledCell(Cell.COLOR_I);
 *   Cell portal = new PortalDecorator(base, 15, 3);
 *   // (15, 3) koordinatına ışınlar
 */
public class PortalDecorator extends CellDecorator {

    private int   portalTargetRow;
    private int   portalTargetCol;
    private boolean active;         // portal aktif mi? (tek kullanımlık olabilir)
    private int   portalId;         // birden fazla portal çiftini eşleştirmek için

    private static int nextPortalId = 1;   // sınıf geneli sayaç (koleksiyon yok)

    /**
     * @param cell           sarmalanacak hücre
     * @param targetRow      ışınlanma hedef satırı
     * @param targetCol      ışınlanma hedef sütunu
     */
    public PortalDecorator(Cell cell, int targetRow, int targetCol) {
        super(cell);
        this.portalTargetRow = targetRow;
        this.portalTargetCol = targetCol;
        this.active          = true;
        this.portalId        = nextPortalId++;
        // Rengi Portal rengine taşı
        this.colorId         = Cell.COLOR_PORTAL;
    }

    // ---------------------------------------------------------------- override

    @Override
    public int getColorId() { return Cell.COLOR_PORTAL; }

    @Override
    public boolean isFilled() { return true; }

    /**
     * Portal aktivasyonu.
     * GameEngine bu metodu çağırdığında teleport koordinatları okunur.
     */
    @Override
    public void activate() {
        if (!active) {
            System.out.println("[Portal #" + portalId + "] Devre dışı.");
            return;
        }
        System.out.println("[Portal #" + portalId + "] AKTIVE! " +
                           "Hedef: (" + portalTargetRow + ", " + portalTargetCol + ")");
        wrapped.activate();   // zincirdeki diğer dekoratörleri de çalıştır
        // Tek kullanımlık davranış istenirse aşağıdaki satır açılır:
        // active = false;
    }

    @Override
    public String render() {
        return active ? "[P]" : wrapped.render();
    }

    // ---------------------------------------------------------------- portal API

    public int  getPortalTargetRow()     { return portalTargetRow; }
    public int  getPortalTargetCol()     { return portalTargetCol; }
    public int  getPortalId()            { return portalId;         }
    public boolean isActive()            { return active;           }

    /** Portalın hedefini güncelle (dinamik yeniden konumlandırma) */
    public void setTarget(int row, int col) {
        this.portalTargetRow = row;
        this.portalTargetCol = col;
    }

    /** Portalı devre dışı bırak */
    public void deactivate() {
        this.active = false;
        System.out.println("[Portal #" + portalId + "] Devre dışı bırakıldı.");
    }

    /** Portal sayacını sıfırla (yeni oyun başlangıcında) */
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
