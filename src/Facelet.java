public class Facelet {

    public int[] facelets;

    public static final int U = 0;
    public static final int R = 1;
    public static final int F = 2;
    public static final int D = 3;
    public static final int L = 4;
    public static final int B = 5;

    /*
        Corner positions:

            0 URF
            1 UFL
            2 ULB
            3 UBR
            4 DFR
            5 DLF
            6 DBL
            7 DRB
    */

    public static final int[][] cornerFacelets = {
            {8, 9, 20},     // URF = U9 R1 F3
            {6, 18, 38},    // UFL = U7 F1 L3
            {0, 36, 47},    // ULB = U1 L1 B3
            {2, 45, 11},    // UBR = U3 B1 R3

            {29, 26, 15},   // DFR = D3 F9 R7
            {27, 44, 24},   // DLF = D1 L9 F7
            {33, 53, 42},   // DBL = D7 B9 L7
            {35, 17, 51}    // DRB = D9 R9 B7
    };

    /*
        Edge positions:

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

    public static final int[][] edgeFacelets = {
            {5, 10},     // UR = U6 R2
            {7, 19},     // UF = U8 F2
            {3, 37},     // UL = U4 L2
            {1, 46},     // UB = U2 B2

            {32, 16},    // DR = D6 R8
            {28, 25},    // DF = D2 F8
            {30, 43},    // DL = D4 L8
            {34, 52},    // DB = D8 B8

            {23, 12},    // FR = F6 R4
            {21, 41},    // FL = F4 L6
            {50, 39},    // BL = B6 L4
            {48, 14}     // BR = B4 R6
    };

    public static final int[][] cornerColors = {
            {U, R, F},    // URF
            {U, F, L},    // UFL
            {U, L, B},    // ULB
            {U, B, R},    // UBR
            {D, F, R},    // DFR
            {D, L, F},    // DLF
            {D, B, L},    // DBL
            {D, R, B}     // DRB
    };

    public static final int[][] edgeColors = {
            {U, R},       // UR
            {U, F},       // UF
            {U, L},       // UL
            {U, B},       // UB
            {D, R},       // DR
            {D, F},       // DF
            {D, L},       // DL
            {D, B},       // DB
            {F, R},       // FR
            {F, L},       // FL
            {B, L},       // BL
            {B, R}        // BR
    };

    public Facelet() {
        facelets = new int[54];

        for (int face = 0; face < 6; face++) {
            for (int i = 0; i < 9; i++) {
                facelets[face * 9 + i] = face;
            }
        }
    }

    public Facelet(Cubie state) {
        this();

        for (int position = 0; position < 8; position++) {
            int piece = state.cornerPermutation[position];
            int orientation = state.cornerOrientation[position];

            for (int i = 0; i < 3; i++) {
                facelets[
                        cornerFacelets[position][(i + orientation) % 3]
                        ] = cornerColors[piece][i];
            }
        }

        for (int position = 0; position < 12; position++) {
            int piece = state.edgePermutation[position];
            int orientation = state.edgeOrientation[position];

            for (int i = 0; i < 2; i++) {
                facelets[
                        edgeFacelets[position][(i + orientation) % 2]
                        ] = edgeColors[piece][i];
            }
        }
    }

    public Cubie toCubie() {
        Cubie result = new Cubie();

        for (int position = 0; position < 8; position++) {

            int orientation;

            for (orientation = 0; orientation < 3; orientation++) {
                int color = facelets[
                        cornerFacelets[position][orientation]
                        ];

                if (color == U || color == D) {
                    break;
                }
            }

            if (orientation == 3) {
                throw new IllegalStateException(
                        "Invalid corner orientation"
                );
            }

            int color1 = facelets[
                    cornerFacelets[position][(orientation + 1) % 3]
                    ];

            int color2 = facelets[
                    cornerFacelets[position][(orientation + 2) % 3]
                    ];

            boolean found = false;

            for (int piece = 0; piece < 8; piece++) {
                if (color1 == cornerColors[piece][1]
                        && color2 == cornerColors[piece][2]) {

                    result.cornerPermutation[position] = piece;
                    result.cornerOrientation[position] = orientation;

                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalStateException(
                        "Invalid corner at position " + position
                );
            }
        }

        for (int position = 0; position < 12; position++) {

            boolean found = false;

            for (int piece = 0; piece < 12; piece++) {

                if (facelets[edgeFacelets[position][0]]
                        == edgeColors[piece][0]
                        && facelets[edgeFacelets[position][1]]
                        == edgeColors[piece][1]) {

                    result.edgePermutation[position] = piece;
                    result.edgeOrientation[position] = 0;

                    found = true;
                    break;
                }

                if (facelets[edgeFacelets[position][0]]
                        == edgeColors[piece][1]
                        && facelets[edgeFacelets[position][1]]
                        == edgeColors[piece][0]) {

                    result.edgePermutation[position] = piece;
                    result.edgeOrientation[position] = 1;

                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalStateException(
                        "Invalid edge at position " + position
                );
            }
        }

        return result;
    }

    public String toString() {
        StringBuilder result = new StringBuilder(54);

        for (int color : facelets) {
            result.append(colorToChar(color));
        }

        return result.toString();
    }

    private static char colorToChar(int color) {
        return switch (color) {
            case U -> 'U';
            case R -> 'R';
            case F -> 'F';
            case D -> 'D';
            case L -> 'L';
            case B -> 'B';
            default -> '?';
        };
    }
}