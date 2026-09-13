package move;

import state.Cubie;

public class CubeMover {
    public static final int U = 0;
    public static final int R = 1;
    public static final int F = 2;
    public static final int D = 3;
    public static final int L = 4;
    public static final int B = 5;

    /*
        cornerPermutationMove[move][newPosition] = oldPosition

        Corners:
            0 URF
            1 UFL
            2 ULB
            3 UBR
            4 DFR
            5 DLF
            6 DBL
            7 DRB
    */
    private static final int[][] cornerPermutationMove = {
            {3, 0, 1, 2, 4, 5, 6, 7}, // U
            {4, 1, 2, 0, 7, 5, 6, 3}, // R
            {1, 5, 2, 3, 0, 4, 7, 6}, // F
            {0, 1, 2, 3, 5, 6, 7, 4}, // D
            {0, 2, 6, 3, 4, 1, 5, 7}, // L
            {0, 1, 3, 7, 4, 5, 2, 6}  // B
    };

    /*
        cornerOrientationMove[move][newPosition]

        0 = correctly oriented
        1 = clockwise twist
        2 = counter-clockwise twist
    */
    private static final int[][] cornerOrientationMove = {
            {0, 0, 0, 0, 0, 0, 0, 0}, // U
            {2, 0, 0, 1, 1, 0, 0, 2}, // R
            {1, 2, 0, 0, 2, 1, 0, 0}, // F
            {0, 0, 0, 0, 0, 0, 0, 0}, // D
            {0, 1, 2, 0, 0, 2, 1, 0}, // L
            {0, 0, 1, 2, 0, 0, 2, 1}  // B
    };

    /*
        edgePermutationMove[move][newPosition] = oldPosition

        Edges:
            0 UR
            1 UF
            2 UL
            3 UB
            4 DR
            5 DF
            6 DL
            7 DB
            8 FR
            9 FL
            10 BL
            11 BR
    */
    private static final int[][] edgePermutationMove = {
            {3, 0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11}, // U
            {8, 1, 2, 3, 11, 5, 6, 7, 4, 9, 10, 0}, // R
            {0, 9, 2, 3, 4, 8, 6, 7, 1, 5, 10, 11}, // F
            {0, 1, 2, 3, 5, 6, 7, 4, 8, 9, 10, 11}, // D
            {0, 1, 10, 3, 4, 5, 9, 7, 8, 2, 6, 11}, // L
            {0, 1, 2, 10, 4, 5, 6, 11, 8, 9, 7, 3}  // B
    };

    /*
        edgeOrientationMove[move][newPosition]

        0 = correctly oriented
        1 = flipped
    */
    private static final int[][] edgeOrientationMove = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // U
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // R
            {0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0}, // F
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // D
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // L
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1}  // B
    };

    public Cubie move(Cubie state, int move) {
        if (move < U || move > B) {
            throw new IllegalArgumentException("Invalid move: " + move);
        }

        int[] edgePermutation = new int[12];
        int[] edgeOrientation = new int[12];
        int[] cornerPermutation = new int[8];
        int[] cornerOrientation = new int[8];

        for (int position = 0; position < 8; position++) {
            int oldPosition = cornerPermutationMove[move][position];
            cornerPermutation[position] = state.cornerPermutation[oldPosition];
            cornerOrientation[position] = (state.cornerOrientation[oldPosition] + cornerOrientationMove[move][position]) % 3;
        }

        for (int position = 0; position < 12; position++) {
            int oldPosition = edgePermutationMove[move][position];
            edgePermutation[position] = state.edgePermutation[oldPosition];
            edgeOrientation[position] = (state.edgeOrientation[oldPosition] + edgeOrientationMove[move][position]) % 2;
        }

        return new Cubie(edgePermutation, edgeOrientation, cornerPermutation, cornerOrientation);
    }

    public Cubie move(Cubie state, int move, int amount) {
        if (amount < 1 || amount > 3) {
            throw new IllegalArgumentException("Amount must be 1, 2 or 3");
        }

        Cubie result = state.copy();
        for (int i = 0; i < amount; i++) {
            result = move(result, move);
        }

        return result;
    }

    public Cubie inverse(Cubie state, int move) {
        return move(state, move, 3);
    }

    public Cubie applyMoves(Cubie state, int... moves) {
        Cubie result = state.copy();
        for (int move : moves) {
            result = move(result, move);
        }

        return result;
    }

    public static String stringifyMove(int move){
        if(move < 0 || move > 17){
            throw new RuntimeException("move.Move number should be in [0, 17]");
        }
        String[] prefixes = {"U", "R", "F", "D", "L", "B"};
        int prefix = move % 6;
        int postfix = move / 6;

        if(postfix == 0) return prefixes[prefix];

        String[] postfixes = {"2", "'"};
        return  prefixes[prefix] + postfixes[postfix-1];
    }
}