package pieces;

public class PieceFactory {

    public static final String TYPE_I      = "I";
    public static final String TYPE_O      = "O";
    public static final String TYPE_T      = "T";
    public static final String TYPE_S      = "S";
    public static final String TYPE_Z      = "Z";
    public static final String TYPE_L      = "L";
    public static final String TYPE_J      = "J";

    private static final String[] STANDARD_TYPES = {
        TYPE_I, TYPE_O, TYPE_T, TYPE_S, TYPE_Z, TYPE_L, TYPE_J
    };

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

    public static Piece createRandomPiece() {
        int idx = (int)(Math.random() * STANDARD_TYPES.length);
        return createPiece(STANDARD_TYPES[idx]);
    }

    public static Piece createCustomPiece(int[][] matrix, int maxRotations, String name) {
        return new CustomPiece(matrix, maxRotations, name);
    }

    public static Piece[] createAllStandardPieces() {
        Piece[] pieces = new Piece[STANDARD_TYPES.length];
        for (int i = 0; i < STANDARD_TYPES.length; i++) {
            pieces[i] = createPiece(STANDARD_TYPES[i]);
        }
        return pieces;
    }

    public static int getStandardTypeCount() {
        return STANDARD_TYPES.length;
    }
}
