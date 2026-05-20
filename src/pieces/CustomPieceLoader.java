package pieces;

import java.util.Scanner;

public class CustomPieceLoader {

    public static final int MAX_CUSTOM_PIECES = 3;
    public static final int MAX_MATRIX_DIM    = 6;   

    private Piece[] loadedPieces;
    private int     loadedCount;

    public CustomPieceLoader() {
        loadedPieces = new Piece[MAX_CUSTOM_PIECES];
        loadedCount  = 0;
    }

    public void loadFromConsole() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Kullanıcı Tanımlı Parça Yükleyici ===");
        System.out.print("Kaç parça tanımlamak istersiniz? (1-" +
                          MAX_CUSTOM_PIECES + "): ");

        int count = readIntInRange(scanner, 1, MAX_CUSTOM_PIECES);

        for (int i = 0; i < count; i++) {
            System.out.println("\n--- Parça " + (i + 1) + " ---");
            Piece p = loadSinglePiece(scanner, i + 1);
            if (p != null) {
                loadedPieces[loadedCount++] = p;
                System.out.println("✓ Parça '" + p.getPieceName() + "' yüklendi.\n" + p);
            }
        }

        System.out.println("\nToplam yüklenen parça: " + loadedCount);
    }

    public boolean loadProgrammatically(int[][] matrix, int maxRotations, String name) {
        if (loadedCount >= MAX_CUSTOM_PIECES) {
            System.out.println("[CustomPieceLoader] Kapasite dolu (max " +
                               MAX_CUSTOM_PIECES + ")");
            return false;
        }
        validateMatrix(matrix);
        Piece p = PieceFactory.createCustomPiece(matrix, maxRotations, name);
        loadedPieces[loadedCount++] = p;
        System.out.println("[CustomPieceLoader] Parça eklendi: " + name);
        return true;
    }

    public Piece[] getLoadedPieces() {
        Piece[] result = new Piece[loadedCount];
        for (int i = 0; i < loadedCount; i++) {
            result[i] = loadedPieces[i].clonePiece();
        }
        return result;
    }

    public Piece getPieceAt(int index) {
        if (index < 0 || index >= loadedCount)
            throw new IndexOutOfBoundsException("Geçersiz parça index: " + index);
        return loadedPieces[index].clonePiece();
    }

    public int getLoadedCount() { return loadedCount; }

    public void clearAll() {
        for (int i = 0; i < loadedCount; i++) {
            loadedPieces[i] = null;   
        }
        loadedCount = 0;
    }

    private Piece loadSinglePiece(Scanner scanner, int slotNo) {

        System.out.print("Parça adı: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Custom" + slotNo;

        System.out.print("Satır sayısı (1-" + MAX_MATRIX_DIM + "): ");
        int rows = readIntInRange(scanner, 1, MAX_MATRIX_DIM);

        System.out.print("Sütun sayısı (1-" + MAX_MATRIX_DIM + "): ");
        int cols = readIntInRange(scanner, 1, MAX_MATRIX_DIM);

        int[][] matrix = new int[rows][cols];
        System.out.println("Matrisi girin (satır satır, değerler arası boşluk; 0=boş 1=dolu):");
        for (int r = 0; r < rows; r++) {
            System.out.print("  Satır " + (r + 1) + ": ");
            for (int c = 0; c < cols; c++) {
                matrix[r][c] = readIntInRange(scanner, 0, 1);
            }
        }

        System.out.print("İzin verilen rotasyon sayısı (1-4): ");
        int maxRot = readIntInRange(scanner, 1, 4);

        return PieceFactory.createCustomPiece(matrix, maxRot, name);
    }

    private int readIntInRange(Scanner scanner, int min, int max) {
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                int val = Integer.parseInt(line.split("\\s+")[0]);
                if (val >= min && val <= max) return val;
                System.out.print("  [" + min + "-" + max + "] aralığında girin: ");
            } catch (NumberFormatException e) {
                System.out.print("  Geçersiz giriş, tekrar deneyin: ");
            }
        }
    }

    private void validateMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0)
            throw new IllegalArgumentException("Matris boş olamaz");
        if (matrix.length > MAX_MATRIX_DIM || matrix[0].length > MAX_MATRIX_DIM)
            throw new IllegalArgumentException("Matris max " + MAX_MATRIX_DIM + "×" +
                                               MAX_MATRIX_DIM + " olabilir");

        for (int[] row : matrix)
            for (int cell : row)
                if (cell != 0) return;
        throw new IllegalArgumentException("Matrisin en az bir dolu hücresi olmalı");
    }
}
