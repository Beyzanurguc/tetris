package pieces;

import core.Cell;
import core.Matrix;

/**
 * AbstractPiece — Tüm Tetris parçalarının ortak davranışını barındıran
 * soyut temel sınıf.
 *
 * Alt sınıfların sadece şunları sağlaması yeterlidir:
 *   1. initialMatrix() — başlangıç int[][] yap
 *   2. getColorId()    — renk sabiti
 *   3. getPieceName()  — sembolik ad
 *   4. clonePiece()    — Prototype kopyası
 *
 * Rotation Counter:
 *   maxRotations = 4 (tüm parçalar dört yönde dönebilir).
 *   Her rotate() çağrısı usedRotations'ı bir artırır.
 *   canRotate() → usedRotations < maxRotations kontrolünü yapar.
 *
 *   NOT: Bazı parçalar (O-parçası) maxRotations = 1 ile override edebilir.
 */
public abstract class AbstractPiece implements Piece {

    // ------------------------------------------------------------ rotasyon
    protected static final int DEFAULT_MAX_ROTATIONS = 4;

    protected Matrix matrix;          // güncel rotasyondaki form
    protected int    maxRotations;    // bu parça için izin verilen toplam dönüş
    protected int    usedRotations;   // şimdiye kadar yapılan dönüş

    // ------------------------------------------------------------ konum
    protected int row;
    protected int col;

    // ------------------------------------------------------------ ctor
    protected AbstractPiece(int maxRotations) {
        this.maxRotations  = maxRotations;
        this.usedRotations = 0;
        this.matrix        = new Matrix(initialMatrix());
        this.row           = 0;
        this.col           = 0;
    }

    protected AbstractPiece() {
        this(DEFAULT_MAX_ROTATIONS);
    }

    // ------------------------------------------------------------ soyut
    /** Alt sınıf başlangıç formunu tanımlar */
    protected abstract int[][] initialMatrix();

    // ------------------------------------------------------------ Piece impl

    @Override
    public int[][] getMatrix() {
        // int[][] olarak ham kopya döndür (bağımlılığı koparmak için)
        int[][] result = new int[matrix.getRows()][matrix.getCols()];
        for (int r = 0; r < matrix.getRows(); r++) {
            for (int c = 0; c < matrix.getCols(); c++) {
                result[r][c] = matrix.get(r, c);
            }
        }
        return result;
    }

    @Override
    public Piece rotate() {
        if (!canRotate()) {
            System.out.println("[" + getPieceName() + "] Rotasyon hakkı kalmadı!");
            return this;
        }
        matrix = matrix.rotateCW();
        usedRotations++;
        System.out.println("[" + getPieceName() + "] Döndürüldü. Kalan hak: " +
                           getRemainingRotations());
        return this;
    }

    @Override
    public boolean canRotate() {
        return usedRotations < maxRotations;
    }

    @Override
    public int getRemainingRotations() {
        return Math.max(0, maxRotations - usedRotations);
    }

    @Override
    public int getUsedRotations() {
        return usedRotations;
    }

    // ------------------------------------------------------------ konum
    @Override public int  getRow()       { return row; }
    @Override public int  getCol()       { return col; }
    @Override public void setRow(int r)  { this.row = r; }
    @Override public void setCol(int c)  { this.col = c; }

    // ------------------------------------------------------------ yardımcı

    /** Alt sınıfların clonePiece() içinde kullanabileceği ortak kopyalama */
    protected void copyStateTo(AbstractPiece target) {
        target.matrix        = this.matrix.clone();
        target.usedRotations = this.usedRotations;
        target.maxRotations  = this.maxRotations;
        target.row           = this.row;
        target.col           = this.col;
    }

    @Override
    public String toString() {
        return getPieceName() + "-Piece (" + row + "," + col + ") " +
               "rot:" + usedRotations + "/" + maxRotations + "\n" + matrix;
    }
}
