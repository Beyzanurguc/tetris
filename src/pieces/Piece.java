package pieces;

/**
 * Piece — Tetris parçası arayüzü.
 *
 * Design Patterns:
 *  - Factory Method: PieceFactory bu interface'e göre üretim yapar
 *  - Prototype     : clone() metodu ile parça kopyalanabilir (Ghost Piece)
 *
 * Kısıtlamalar:
 *  - Rotation Counter: Her parçanın sınırlı rotasyon hakkı vardır.
 *    canRotate() metodu ile kontrol edilir.
 *  - Koleksiyon yasağı: getMatrix() int[][] döndürür (ham dizi).
 */
public interface Piece {

    // -------------------------------------------------------- form & kimlik

    /**
     * Parçanın mevcut rotasyonundaki bloklarını int[][] formatında döndür.
     * 0 = boş, 1+ = dolu (renk kimliği olarak kullanılabilir).
     */
    int[][] getMatrix();

    /** Parçanın renk kimliği (Cell sabitlerinden biri) */
    int getColorId();

    /** Parçanın sembolik adı ("I", "T", "S" …) */
    String getPieceName();

    // -------------------------------------------------------- rotasyon

    /**
     * Parçayı saat yönünde döndür (mutasyonel — iç matris değişir).
     * Dönme gerçekleşmeden önce canRotate() kontrol edilmelidir.
     *
     * @return this (method chaining için)
     */
    Piece rotate();

    /**
     * Parçanın şu an dönebilir mi?
     * rotationCounter > 0 ve maxRotations'ı aşmadıysa true.
     */
    boolean canRotate();

    /** Kalan rotasyon hakkını döndür */
    int getRemainingRotations();

    /** Toplam kullanılan rotasyon sayısı */
    int getUsedRotations();

    // -------------------------------------------------------- prototype

    /**
     * Parçanın derin kopyasını döndür.
     * Ghost Piece ve "next piece preview" için kullanılır.
     * java.util.Cloneable KULLANILMAZ — manuel kopyalama.
     */
    Piece clonePiece();

    // -------------------------------------------------------- konum (Board koordinatları)

    int getRow();
    int getCol();
    void setRow(int row);
    void setCol(int col);
}
