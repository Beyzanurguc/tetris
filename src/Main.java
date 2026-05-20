import core.*;
import pieces.*;
import engine.GameEngine;
import decorators.*;
import bridge.gravity.*;
import bridge.theme.*;
import bridge.ThemeManager;
import behavioral.observer.*;
import behavioral.strategy.*;
import behavioral.command.*;
import behavioral.state.*;
import behavioral.iterator.*;
import behavioral.chain.*;

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

        testStatePattern();
        testCommandPattern();
        testObserverPattern();
        testStrategyPattern();
        testIteratorPattern();
        testChainOfResponsibility();
        testTemplateMethod();

        sep('=');
        System.out.println("  Tum testler basariyla tamamlandi.");
        sep('=');
    }

    private static void testDynamicArray() {
        header("1. DynamicArray (Manuel Koleksiyon)");

        DynamicArray arr = new DynamicArray(2);   
        arr.add("A");
        arr.add("B");
        arr.add("C");   
        arr.add("D");

        System.out.println("Icerik : " + arr);        
        arr.remove(1);                                 
        System.out.println("B silindi: " + arr);      
        System.out.println("Size: " + arr.size() + " -- OK");
    }

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

    private static void testGridAndBoard() {
        header("3. Grid + Board (Dynamic Shrinking)");

        Board board = new Board(8, 6);
        Grid  grid  = board.getGrid();

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

    private static void testRotationCounter() {
        header("5. Rotation Counter");

        Piece t = PieceFactory.createPiece("T");  
        System.out.println("T-Piece baslangic formu:");
        System.out.print(t);

        for (int i = 0; i < 5; i++) {   
            boolean ok = t.canRotate();
            if (ok) t.rotate();
            System.out.println("Deneme " + (i + 1) +
                               " | canRotate=" + ok +
                               " | kullanilan=" + t.getUsedRotations());
        }

        Piece o = PieceFactory.createPiece("O");  
        System.out.println("O-Piece (maxRot=1) canRotate=" + o.canRotate());
        o.rotate();
        System.out.println("O-Piece don. sonrasi canRotate=" + o.canRotate() + " -- OK");
    }

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

    private static void testDecoratorChain() {
        header("8. Decorator Zinciri");

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

    private static void testCompositeLayers() {
        header("11. Composite -- BoardLayer (Katman Yonetimi)");

        Board board = new Board(10, 6);
        CompositeLayer root = board.getLayerRoot();

        System.out.println("Katman yapisi:");
        System.out.println(root.describe());

        SimpleLayer base = board.getBaseLayer();
        int[][] iPiece = {{1, 1, 1, 1}};
        base.paint(iPiece, 9, 1, Cell.COLOR_I);
        System.out.println("Base[9][1-4] boyandi. Renk: " +
                           root.getCellColorAt(9, 2) +
                           " (beklenen: " + Cell.COLOR_I + ")");

        SimpleLayer ghost = board.getGhostLayer();
        ghost.paint(iPiece, 6, 1, Cell.COLOR_J);
        System.out.println("Ghost[6][1-4] boyandi. Renk: " +
                           root.getCellColorAt(6, 2) +
                           " (beklenen: " + Cell.COLOR_J + ")");

        SimpleLayer fx = board.getFXLayer();
        fx.setColor(9, 2, Cell.COLOR_PORTAL);
        System.out.println("FX[9][2] portal efekti. Painter sonucu: " +
                           root.getCellColorAt(9, 2) +
                           " (beklenen: " + Cell.COLOR_PORTAL + " -- FX uste cikiyor)");

        ghost.hide();
        System.out.println("Ghost gizlendi. [6][2] = " +
                           root.getCellColorAt(6, 2) +
                           " (beklenen: 0 = EMPTY)");

        ghost.show();
        System.out.println("Ghost tekrar gorunur. [6][2] = " +
                           root.getCellColorAt(6, 2) +
                           " (beklenen: " + Cell.COLOR_J + ")");

        System.out.println("Composite desen -- OK");
    }

    private static void testStatePattern() {
        header("12. State Pattern (Durum Deseni)");

        GameEngine engine = GameEngine.getInstance();

        engine.changeState(new MenuState());
        System.out.println("Mevcut durum: " + engine.getCurrentState().getClass().getSimpleName());
        assert engine.getCurrentState() instanceof MenuState : "MenuState bekleniyor!";

        engine.startGame();
        engine.changeState(new PlayingState());
        System.out.println("Mevcut durum: " + engine.getCurrentState().getClass().getSimpleName());
        assert engine.getCurrentState() instanceof PlayingState : "PlayingState bekleniyor!";

        engine.changeState(new PausedState());
        System.out.println("Mevcut durum: " + engine.getCurrentState().getClass().getSimpleName());
        assert engine.getCurrentState() instanceof PausedState : "PausedState bekleniyor!";

        engine.changeState(new GameOverState());
        System.out.println("Mevcut durum: " + engine.getCurrentState().getClass().getSimpleName());

        System.out.println("State Pattern -- OK");
    }

    private static void testCommandPattern() {
        header("13. Command Pattern (Komut Deseni)");

        GameEngine engine = GameEngine.getInstance();
        engine.startGame();

        Piece piece = engine.getCurrentPiece();
        Board board = engine.getBoard();
        CommandHistory history = engine.getCommandHistory();

        System.out.println("Baslangic col: " + piece.getCol());

        Command leftCmd = new MoveLeftCommand(piece, board);
        leftCmd.execute();
        history.push(leftCmd);
        System.out.println("MoveLeft sonrasi col: " + piece.getCol());

        Command rightCmd = new MoveRightCommand(piece, board);
        rightCmd.execute();
        history.push(rightCmd);
        System.out.println("MoveRight sonrasi col: " + piece.getCol());

        Command rotCmd = new RotateCommand(piece);
        rotCmd.execute();

        history.undo();
        System.out.println("Undo sonrasi col: " + piece.getCol());

        System.out.println("CommandHistory boyutu: " + history.size());
        System.out.println("Command Pattern -- OK");
    }

    private static void testObserverPattern() {
        header("14. Observer Pattern (Gozlemci Deseni)");

        GameEngine engine = GameEngine.getInstance();

        ScoreManager sm = new ScoreManager(new ClassicScoringStrategy());
        AudioManager  am = new AudioManager();

        engine.subscribe(sm);
        engine.subscribe(am);
        System.out.println("Abone sayisi: " + engine.getListenerCount());

        engine.notifyListeners(new GameEvent(GameEvent.EventType.LINE_CLEARED, 2));
        System.out.println("Mevcut skor (ScoreManager): " + sm.getCurrentScore());

        engine.notifyListeners(new GameEvent(GameEvent.EventType.LEVEL_UP, 2));

        engine.unsubscribe(am);
        System.out.println("AudioManager cikarildiktan sonra abone sayisi: " +
                           engine.getListenerCount());

        System.out.println("Observer Pattern -- OK");
    }

    private static void testStrategyPattern() {
        header("15. Strategy Pattern (Strateji Deseni)");

        ScoreManager sm = new ScoreManager(new ClassicScoringStrategy());
        System.out.println("Aktif strateji: " + sm.getStrategy().getStrategyName());

        int s1 = sm.getStrategy().calculateScore(2, 1, 0);
        System.out.println("ClassicScoring(2 satir, lv1, combo=0) = " + s1 + " (beklenen: 300)");

        sm.setStrategy(new TimedScoringStrategy(60));
        System.out.println("Yeni strateji: " + sm.getStrategy().getStrategyName());
        int s2 = sm.getStrategy().calculateScore(1, 1, 0);
        System.out.println("TimedScoring(1 satir, lv1, 0 tick) = " + s2);

        sm.setStrategy(new ComboScoringStrategy());
        System.out.println("Yeni strateji: " + sm.getStrategy().getStrategyName());
        int s3 = sm.getStrategy().calculateScore(1, 1, 3);   
        System.out.println("ComboScoring(1 satir, lv1, combo=3) = " + s3 + " (beklenen: 250)");

        System.out.println("Strategy Pattern -- OK");
    }

    private static void testIteratorPattern() {
        header("16. Iterator Pattern (Yineleyici Deseni)");

        Board board = new Board(4, 4);
        GameIterator<Cell> cellIt = board.iterator();
        int cellCount = 0;
        while (cellIt.hasNext()) {
            cellIt.next();   
            cellCount++;
        }
        System.out.println("Board(4x4) toplam hucre: " + cellCount + " (beklenen: 16)");
        assert cellCount == 16 : "Hucre sayisi hatali!";

        cellIt.reset();
        System.out.println("reset() sonrasi hasNext: " + cellIt.hasNext());

        Piece t = PieceFactory.createPiece("T");
        Matrix m = new Matrix(t.getMatrix());
        MatrixRowIterator rowIt = new MatrixRowIterator(m);
        int rowCount = 0;
        while (rowIt.hasNext()) {
            int[] row = rowIt.next();
            System.out.print("Satir " + rowCount + ": [");
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i]);
                if (i < row.length - 1) System.out.print(",");
            }
            System.out.println("]");
            rowCount++;
        }
        System.out.println("MatrixRowIterator: " + rowCount + " satir islendi.");

        System.out.println("Iterator Pattern -- OK");
    }

    private static void testChainOfResponsibility() {
        header("17. Chain of Responsibility (Sorumluluk Zinciri)");

        GameEngine engine = GameEngine.getInstance();
        EffectManager em = new EffectManager(engine);

        System.out.println("-- LEVEL_UP olayi:");
        em.process(new GameEvent(GameEvent.EventType.LEVEL_UP, 2));

        System.out.println("\n-- LINE_CLEARED olayi:");
        em.process(new GameEvent(GameEvent.EventType.LINE_CLEARED, 4));

        System.out.println("\n-- PIECE_LANDED olayi:");
        em.process(new GameEvent(GameEvent.EventType.PIECE_LANDED, 0));

        engine.unsubscribe(em);
        System.out.println("Chain of Responsibility -- OK");
    }

    private static void testTemplateMethod() {
        header("18. Template Method Pattern (Sablon Metot Deseni)");

        Board board = new Board(4, 4);

        System.out.println("-- IPiece.update() (Template Method):");
        IPiece ip = new IPiece();
        ip.setRow(0);
        ip.setCol(0);

        ip.update(board);   
        System.out.println("Test 18a -- IPiece Template Method: PASS");

        System.out.println("\n-- OPiece.update() (Template Method, bos hook):");
        Board board2 = new Board(4, 4);
        OPiece op = new OPiece();
        op.setRow(0);
        op.setCol(0);
        op.update(board2);   
        System.out.println("Test 18b -- OPiece Template Method (bos hook): PASS");

        System.out.println("\n-- TPiece.update() (Template Method, T-spin hook):");
        Board board3 = new Board(5, 5);
        TPiece tp = new TPiece();
        tp.setRow(0);
        tp.setCol(1);
        tp.update(board3);   
        System.out.println("Test 18c -- TPiece Template Method (T-spin hook): PASS");

        System.out.println("Template Method Pattern -- OK");
    }

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
