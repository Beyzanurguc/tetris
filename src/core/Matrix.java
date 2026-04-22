package core;

/**
 * Manuel 2D Matris — java.util koleksiyonları kullanılmadan,
 * tek boyutlu int[] dizi üzerinden row-major order ile gerçekleştirilmiştir.
 *
 * İndex dönüşümü: (row, col) → row * cols + col
 *
 * Tasarım Kararları:
 *  - Dahili depo: int[] (primitive, nesne yükü yok)
 *  - Kopya (clone) metodu sağlanır → Prototype deseni için kullanılır
 *  - Yerinde CW rotasyonu O(rows×cols)
 */
public class Matrix {

    // ------------------------------------------------------------------ fields
    protected int[] data;  // satır-majör sıralı düz dizi
    protected int   rows;
    protected int   cols;

    // --------------------------------------------------------------- ctor/init

    /** Tüm hücreleri 0 ile doldurarak matris yarat */
    public Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0)
            throw new IllegalArgumentException("rows ve cols pozitif olmalı");
        this.rows = rows;
        this.cols = cols;
        this.data = new int[rows * cols];
    }

    /** Var olan 2D diziden matris yarat (kopyalama ile) */
    public Matrix(int[][] source) {
        if (source == null || source.length == 0)
            throw new IllegalArgumentException("Kaynak dizi boş olamaz");
        this.rows = source.length;
        this.cols = source[0].length;
        this.data = new int[rows * cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                data[r * cols + c] = source[r][c];
            }
        }
    }

    // -------------------------------------------------------------- public API

    /** Hücre değeri döndür — O(1) */
    public int get(int row, int col) {
        checkBounds(row, col);
        return data[row * cols + col];
    }

    /** Hücre değeri ata — O(1) */
    public void set(int row, int col, int value) {
        checkBounds(row, col);
        data[row * cols + col] = value;
    }

    /** Matrisin derin kopyasını döndür (Prototype için) */
    public Matrix clone() {
        Matrix copy = new Matrix(rows, cols);
        for (int i = 0; i < data.length; i++) {
            copy.data[i] = this.data[i];
        }
        return copy;
    }

    /**
     * Saat yönünde 90° rotasyon — yeni Matrix nesnesi döndürür.
     * Orijinal matris değişmez (immutable rotation).
     *
     * Formül: newMatrix[col][rows-1-row] = this[row][col]
     */
    public Matrix rotateCW() {
        Matrix rotated = new Matrix(cols, rows);   // boyutlar yer değiştiriyor
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int value = get(r, c);
                rotated.set(c, rows - 1 - r, value);
            }
        }
        return rotated;
    }

    /**
     * Saat yönünün tersinde 90° rotasyon — yeni Matrix nesnesi döndürür.
     *
     * Formül: newMatrix[cols-1-col][row] = this[row][col]
     */
    public Matrix rotateCCW() {
        Matrix rotated = new Matrix(cols, rows);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int value = get(r, c);
                rotated.set(cols - 1 - c, r, value);
            }
        }
        return rotated;
    }

    /** 180° rotasyon */
    public Matrix rotate180() {
        Matrix rotated = new Matrix(rows, cols);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rotated.set(rows - 1 - r, cols - 1 - c, get(r, c));
            }
        }
        return rotated;
    }

    /** Tüm hücreleri bir değer ile doldur */
    public void fill(int value) {
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }
    }

    /** Matrisin boyutlarını değiştir — veri kaybolabilir (kullanımda dikkatli ol) */
    public void resize(int newRows, int newCols) {
        int[] newData = new int[newRows * newCols];
        int copyRows = Math.min(rows, newRows);
        int copyCols = Math.min(cols, newCols);
        for (int r = 0; r < copyRows; r++) {
            for (int c = 0; c < copyCols; c++) {
                newData[r * newCols + c] = get(r, c);
            }
        }
        // eski diziyi temizle
        for (int i = 0; i < data.length; i++) data[i] = 0;
        this.data = newData;
        this.rows = newRows;
        this.cols = newCols;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    // --------------------------------------------------------- private helpers

    private void checkBounds(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols)
            throw new IndexOutOfBoundsException(
                "Matrix sınırları dışı: (" + row + ", " + col + ")");
    }

    // ------------------------------------------------------------- debug print
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            sb.append("| ");
            for (int c = 0; c < cols; c++) {
                sb.append(String.format("%2d ", get(r, c)));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
}
