package pieces;

import core.Cell;

public class CustomPiece extends AbstractPiece {

    private final String name;

    public CustomPiece(int[][] matrix, int maxRotations, String name) {
        super(maxRotations);
        this.name = (name != null && !name.isEmpty()) ? name : "Custom";

        if (matrix != null && matrix.length > 0) {
            this.matrix = new core.Matrix(matrix);
        }
    }

    @Override
    protected int[][] initialMatrix() {

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
