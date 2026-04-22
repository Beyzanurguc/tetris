package pieces;

import core.Cell;

/**
 * CustomPiece — Kullanıcı tarafından tanımlanmış matrise sahip parça.
 *
 * CustomPieceLoader tarafından üretilir.
 * int[][] doğrudan verilir; renk COLOR_CUSTOM olarak atanır.
 */
public class CustomPiece extends AbstractPiece {

    private final String name;

    /**
     * @param matrix       kullanıcının tanımladığı int[][] form
     * @param maxRotations bu parça için izin verilen rotasyon sayısı
     * @param name         kullanıcının verdiği isim (örn. "Custom1")
     */
    public CustomPiece(int[][] matrix, int maxRotations, String name) {
        super(maxRotations);
        this.name = (name != null && !name.isEmpty()) ? name : "Custom";
        // AbstractPiece constructor'ı initialMatrix() çağırır,
        // ama biz super'den sonra matrix'i overwrite etmemiz gerekiyor.
        // Bu yüzden initialMatrix() boş matris döndürür, asıl matris burada atanır.
        if (matrix != null && matrix.length > 0) {
            this.matrix = new core.Matrix(matrix);
        }
    }

    @Override
    protected int[][] initialMatrix() {
        // CustomPiece ctor'ında matrix dışarıdan atanır;
        // bu metod sadece super() çağrısı için boş matris sağlar.
        return new int[][]{{0}};
    }

    @Override public int    getColorId()   { return Cell.COLOR_CUSTOM; }
    @Override public String getPieceName() { return name; }

    @Override
    public Piece clonePiece() {
        CustomPiece copy = new CustomPiece(null, maxRotations, name);
        copyStateTo(copy);
        return copy;
    }
}
