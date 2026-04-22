package pieces;

/**
 * PieceFactory — Factory Method Deseni.
 *
 * Parça üretimini merkezileştirir; istemci (GameEngine) hangi somut
 * sınıfın örnekleneceğini bilmez.
 *
 * Kısıtlama: java.util.HashMap YASAK — tip → üretici eşlemesi
 * basit if/switch ile yapılmıştır.
 *
 * Genişletilebilirlik:
 *   Yeni parça eklemek için sadece yeni bir AbstractPiece alt sınıfı
 *   yazılır ve createPiece() içine bir case eklenir.
 *   Mevcut kod DEĞİŞMEZ → Açık/Kapalı Prensibine uygun.
 */
public class PieceFactory {

    // Desteklenen standart parça kodları (String sabit olarak)
    public static final String TYPE_I      = "I";
    public static final String TYPE_O      = "O";
    public static final String TYPE_T      = "T";
    public static final String TYPE_S      = "S";
    public static final String TYPE_Z      = "Z";
    public static final String TYPE_L      = "L";
    public static final String TYPE_J      = "J";

    /** Tüm standart tip kodlarını tutan sabit dizi (koleksiyon yasağı) */
    private static final String[] STANDARD_TYPES = {
        TYPE_I, TYPE_O, TYPE_T, TYPE_S, TYPE_Z, TYPE_L, TYPE_J
    };

    // ---------------------------------------------------------------- API

    /**
     * Tip koduna göre yeni bir Piece üret.
     *
     * @param type "I", "O", "T", "S", "Z", "L" veya "J"
     * @return somut Piece nesnesi
     * @throws IllegalArgumentException bilinmeyen tip
     */
    public static Piece createPiece(String type) {
        if (type == null) throw new IllegalArgumentException("Parça tipi null olamaz");

        switch (type.toUpperCase()) {
            case TYPE_I: return new IPiece();
            case TYPE_O: return new OPiece();
            case TYPE_T: return new TPiece();
            case TYPE_S: return new SPiece();
            case TYPE_Z: return new ZPiece();
            case TYPE_L: return new LPiece();
            case TYPE_J: return new JPiece();
            default:
                throw new IllegalArgumentException("Bilinmeyen parça tipi: " + type);
        }
    }

    /**
     * Rastgele standart bir parça üret.
     * Math.random() kullanılır — java.util.Random YASAK.
     */
    public static Piece createRandomPiece() {
        int idx = (int)(Math.random() * STANDARD_TYPES.length);
        return createPiece(STANDARD_TYPES[idx]);
    }

    /**
     * Kullanıcı tanımlı matris ile CustomPiece üret.
     *
     * @param matrix       parça formu
     * @param maxRotations izin verilen rotasyon sayısı
     * @param name         parça adı
     */
    public static Piece createCustomPiece(int[][] matrix, int maxRotations, String name) {
        return new CustomPiece(matrix, maxRotations, name);
    }

    /**
     * Tüm standart tiplerden birer tane üretip dizi olarak döndür.
     * Test ve debug amaçlıdır.
     */
    public static Piece[] createAllStandardPieces() {
        Piece[] pieces = new Piece[STANDARD_TYPES.length];
        for (int i = 0; i < STANDARD_TYPES.length; i++) {
            pieces[i] = createPiece(STANDARD_TYPES[i]);
        }
        return pieces;
    }

    /** Standart tip sayısını döndür */
    public static int getStandardTypeCount() {
        return STANDARD_TYPES.length;
    }
}
