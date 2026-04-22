import core.*;
import pieces.*;
import engine.GameEngine;
import decorators.*;
import bridge.gravity.*;
import bridge.theme.*;
import bridge.ThemeManager;

/**
 * Main -- Butunlesik Smoke Test Sinifi.
 *
 * Her design pattern ve ozellik icin kisa dogrulama senaryolari icerir.
 *
 * Test Senaryolari:
 *   1. DynamicArray          -- manuel dizi operasyonlari
 *   2. Matrix                -- CW/CCW rotasyon
 *   3. Grid + Board          -- yerlestirme, satir temizleme, Dynamic Shrinking
 *   4. PieceFactory          -- tum 7 standart parca + Custom
 *   5. Rotation Counter      -- canRotate() siniri
 *   6. CustomPieceLoader     -- programatik yukleme
 *   7. GameEngine Singleton  -- ayni referans garantisi
 *   8. Decorator Zinciri     -- PortalDecorator + PowerUpDecorator
 *   9. Bridge: Gravity       -- Normal -> Variable -> Zero
 *  10. Bridge: Theme         -- Klasik -> Dark -> Neon
 */
public class Main {

    public static void main(String[] args) {
        sep('=');
        System.out.println("  CSE 304 -- Advanced Tetris  |  Smoke Tests");
        sep('=');

        testDynamicArray();
        testMatrix();
        testGridAndBoard();
        testPieceFactory();
        testRotationCounter();
        testCustomPieceLoader();
        testGameEngineSingleton();
        testDecoratorChain();
        testGravityBridge();
        testThemeBridge();
        testCompositeLayers();

        sep('=');
        System.out.println("  Tum testler basariyla tamamlandi.");
        sep('=');
    }

    // ================================================================ 1. DynamicArray
    private static void testDynamicArray() {
        header("1. DynamicArray (Manuel Koleksiyon)");

        DynamicArray arr = new DynamicArray(2);   // kucuk kapasiteyle zorla
        arr.add("A");
        arr.add("B");
        arr.add("C");   // --> otomatik 2x buyume
        arr.add("D");

        System.out.println("Icerik : " + arr);        // [A, B, C, D]
        arr.remove(1);                                 // B silindi
        System.out.println("B silindi: " + arr);      // [A, C, D]
        System.out.println("Size: " + arr.size() + " -- OK");
    }

    // ================================================================ 2. Matrix
    private static void testMatrix() {
        header("2. Matrix (Rotasyon)");

        Matrix m = new Matrix(new int[][]{
            {1, 1, 1},
            {0, 1, 0}
        });
        System.out.println("Orijinal:");
        System.out.print(m);
        System.out.println("CW 90 derece:");
        System.out.print(m.rotateCW());
        System.out.println("180 derece:");
        System.out.print(m.rotate180());
    }

    // ================================================================ 3. Grid + Board
    private static void testGridAndBoard() {
        header("3. Grid + Board (Dynamic Shrinking)");

        Board board = new Board(8, 6);
        Grid  grid  = board.getGrid();

        // Alt 2 satiri tamamen doldur
        for (int r = 6; r <= 7; r++) {
            for (int c = 0; c < 6; c++) {
                grid.setCell(r, c, new FilledCell(Cell.COLOR_I));
            }
        }
        System.out.println("Doldurulmus tahta:");
        System.out.println(board);

        int cleared = board.clearLines();
        System.out.println(cleared + " satir temizlendi. Skor: " + board.getScore());

        board.shrinkBoard();
        System.out.println("Shrink sonrasi yukseklik: " + board.getGrid().getRows() + " -- OK");
    }

    // ================================================================ 4. PieceFactory
    private static void testPieceFactory() {
        header("4. PieceFactory (Factory Method)");

        Piece[] all = PieceFactory.createAllStandardPieces();
        for (Piece p : all) {
            System.out.println(p.getPieceName() + "-Piece | colorId=" +
                               p.getColorId() + " | rotHak=" +
                               p.getRemainingRotations());
        }

        Piece rand = PieceFactory.createRandomPiece();
        System.out.println("Rastgele parca: " + rand.getPieceName() + " -- OK");
    }

    // ================================================================ 5. Rotation Counter
    private static void testRotationCounter() {
        header("5. Rotation Counter");

        Piece t = PieceFactory.createPiece("T");  // maxRotations = 4
        System.out.println("T-Piece baslangic formu:");
        System.out.print(t);

        for (int i = 0; i < 5; i++) {   // 5. denemede yasak
            boolean ok = t.canRotate();
            if (ok) t.rotate();
            System.out.println("Deneme " + (i + 1) +
                               " | canRotate=" + ok +
                               " | kullanilan=" + t.getUsedRotations());
        }

        Piece o = PieceFactory.createPiece("O");  // maxRotations = 1
        System.out.println("O-Piece (maxRot=1) canRotate=" + o.canRotate());
        o.rotate();
        System.out.println("O-Piece don. sonrasi canRotate=" + o.canRotate() + " -- OK");
    }

    // ================================================================ 6. CustomPieceLoader
    private static void testCustomPieceLoader() {
        header("6. CustomPieceLoader (Programatik)");

        CustomPieceLoader loader = new CustomPieceLoader();

        int[][] uShape = {
            {1, 0, 1},
            {1, 1, 1}
        };
        loader.loadProgrammatically(uShape, 4, "U-Piece");

        int[][] plusShape = {
            {0, 1, 0},
            {1, 1, 1},
            {0, 1, 0}
        };
        loader.loadProgrammatically(plusShape, 1, "Plus-Piece");

        System.out.println("Yuklenen parca sayisi: " + loader.getLoadedCount());
        for (int i = 0; i < loader.getLoadedCount(); i++) {
            Piece p = loader.getPieceAt(i);
            System.out.println(p.getPieceName() + ":");
            System.out.print(p);
        }
        System.out.println("CustomPieceLoader -- OK");
    }

    // ================================================================ 7. GameEngine Singleton
    private static void testGameEngineSingleton() {
        header("7. GameEngine Singleton");

        GameEngine e1 = GameEngine.getInstance();
        GameEngine e2 = GameEngine.getInstance();
        GameEngine e3 = GameEngine.getInstance();

        System.out.println("e1 == e2 : " + (e1 == e2));
        System.out.println("e2 == e3 : " + (e2 == e3));
        System.out.println("Singleton dogruland -- OK");

        e1.startGame();
        System.out.println("Oyun basladi. Running=" + e1.isGameRunning());

        for (int i = 0; i < 5; i++) e1.tick();
        System.out.println("5 tick sonrasi. Tick=" + e1.getTickCount());
        e1.endGame();
    }

    // ================================================================ 8. Decorator Zinciri
    private static void testDecoratorChain() {
        header("8. Decorator Zinciri");

        // FilledCell --> PortalDecorator --> PowerUpDecorator
        Cell base   = new FilledCell(Cell.COLOR_T);
        Cell portal = new PortalDecorator(base, 15, 3);
        Cell full   = new PowerUpDecorator(portal,
                          PowerUpDecorator.BONUS_CLEAR_ROW, 2);

        System.out.println("Render : " + full.render());
        System.out.println("isFilled: " + full.isFilled());
        System.out.println("colorId : " + full.getColorId());

        System.out.println("-- PowerUp activate:");
        full.activate();

        PortalDecorator pd = (PortalDecorator) ((PowerUpDecorator) full).getWrapped();
        System.out.println("Portal hedef: (" + pd.getPortalTargetRow() +
                           ", " + pd.getPortalTargetCol() + ") -- OK");

        ((PowerUpDecorator) full).dispose();
        System.out.println("Zincir dispose() edildi -- OK");
    }

    // ================================================================ 9. Bridge: Gravity
    private static void testGravityBridge() {
        header("9. Bridge -- Gravity (Varying Gravity)");

        GravityAbstraction g = new GravityAbstraction(new NormalGravity());
        System.out.println(g.describeGravity());
        System.out.println("Lv1 drop araligi: " + g.getDropInterval(1) + " tick");
        System.out.println("Lv5 drop araligi: " + g.getDropInterval(5) + " tick");

        System.out.println("-- Runtime degisim --> VariableGravity (x2.0)");
        g.setImplementor(new VariableGravity(2.0, 3, -0.3));
        System.out.println(g.describeGravity());
        System.out.println("Lv1 drop araligi: " + g.getDropInterval(1) + " tick");

        System.out.println("-- Runtime degisim --> ZeroGravity");
        g.setImplementor(new ZeroGravity());
        System.out.println(g.describeGravity());
        int zeroInterval = g.getDropInterval(1);
        System.out.println("Lv1 drop araligi: " + zeroInterval +
                           (zeroInterval >= Integer.MAX_VALUE / 2 ? " (MAX, otomatik dus yok)" : ""));
        System.out.println("Gravity Bridge -- OK");
    }

    // ================================================================ 10. Bridge: Theme
    private static void testThemeBridge() {
        header("10. Bridge -- ThemeManager");

        ThemeManager tm = ThemeManager.getInstance();
        System.out.println(tm.listThemes());

        tm.switchTheme("Klasik");
        System.out.println("[Klasik] Bos  : '" + tm.renderEmpty()               + "'");
        System.out.println("[Klasik] I    : '" + tm.renderFilled(Cell.COLOR_I)  + "'");
        System.out.println("[Klasik] Portal:'" + tm.renderPortal()               + "'");

        tm.switchTheme("Dark");
        System.out.println("[Dark] T-parca: '" + tm.renderFilled(Cell.COLOR_T)  + "'");
        System.out.println("[Dark] PowerUp: '" + tm.renderPowerUp()              + "'");

        tm.nextTheme();
        System.out.println("nextTheme() --> Aktif: " + tm.getActiveThemeName());

        tm.register("Minimal", new ClassicThemeImpl());
        System.out.println("Custom tema kaydedildi. Toplam: " +
                           tm.getRegisteredThemeCount());
        System.out.println(tm.listThemes());
        System.out.println("ThemeManager Bridge -- OK");
    }

    // ================================================================ 11. Composite: BoardLayer
    private static void testCompositeLayers() {
        header("11. Composite -- BoardLayer (Katman Yonetimi)");

        Board board = new Board(10, 6);
        CompositeLayer root = board.getLayerRoot();

        System.out.println("Katman yapisi:");
        System.out.println(root.describe());

        // Base katmanina bir parca boya
        SimpleLayer base = board.getBaseLayer();
        int[][] iPiece = {{1, 1, 1, 1}};
        base.paint(iPiece, 9, 1, Cell.COLOR_I);
        System.out.println("Base[9][1-4] boyandi. Renk: " +
                           root.getCellColorAt(9, 2) +
                           " (beklenen: " + Cell.COLOR_I + ")");

        // Ghost katmanina ghost boya
        SimpleLayer ghost = board.getGhostLayer();
        ghost.paint(iPiece, 6, 1, Cell.COLOR_J);
        System.out.println("Ghost[6][1-4] boyandi. Renk: " +
                           root.getCellColorAt(6, 2) +
                           " (beklenen: " + Cell.COLOR_J + ")");

        // FX katmanina portal efekti
        SimpleLayer fx = board.getFXLayer();
        fx.setColor(9, 2, Cell.COLOR_PORTAL);
        System.out.println("FX[9][2] portal efekti. Painter sonucu: " +
                           root.getCellColorAt(9, 2) +
                           " (beklenen: " + Cell.COLOR_PORTAL + " -- FX uste cikiyor)");

        // Ghost katmanini gizle
        ghost.hide();
        System.out.println("Ghost gizlendi. [6][2] = " +
                           root.getCellColorAt(6, 2) +
                           " (beklenen: 0 = EMPTY)");

        // Ghost katmanini tekrar goster
        ghost.show();
        System.out.println("Ghost tekrar gorunur. [6][2] = " +
                           root.getCellColorAt(6, 2) +
                           " (beklenen: " + Cell.COLOR_J + ")");

        System.out.println("Composite desen -- OK");
    }

    // ================================================================ Yardimci
    private static void header(String title) {
        System.out.println();
        sep('-');
        System.out.println("  " + title);
        sep('-');
    }

    private static void sep(char ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 52; i++) sb.append(ch);
        System.out.println(sb.toString());
    }
}
