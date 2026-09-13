public class Cubie {
    public int[] edgePermutation;
    public int[] edgeOrientation;
    public int[] cornerPermutation;
    public int[] cornerOrientation;

    /*
        Corner positions / pieces:

            0 URF
            1 UFL
            2 ULB
            3 UBR
            4 DFR
            5 DLF
            6 DBL
            7 DRB

        Edge positions / pieces:

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

        Corner orientation:

            0 = correctly oriented
            1 = clockwise twist
            2 = counter-clockwise twist

        Edge orientation:

            0 = correctly oriented
            1 = flipped
    */

    public Cubie() {
        edgePermutation = new int[12];
        edgeOrientation = new int[12];

        cornerPermutation = new int[8];
        cornerOrientation = new int[8];

        for (int i = 0; i < 8; i++) {
            cornerPermutation[i] = i;
            cornerOrientation[i] = 0;
        }

        for (int i = 0; i < 12; i++) {
            edgePermutation[i] = i;
            edgeOrientation[i] = 0;
        }
    }

    public Cubie(
            int[] edgePermutation,
            int[] edgeOrientation,
            int[] cornerPermutation,
            int[] cornerOrientation
    ) {
        if (edgePermutation.length != 12 ||
                edgeOrientation.length != 12 ||
                cornerPermutation.length != 8 ||
                cornerOrientation.length != 8) {
            throw new IllegalArgumentException("Invalid cubie array length");
        }

        this.edgePermutation = edgePermutation.clone();
        this.edgeOrientation = edgeOrientation.clone();
        this.cornerPermutation = cornerPermutation.clone();
        this.cornerOrientation = cornerOrientation.clone();
    }

    public Cubie copy() {
        return new Cubie(
                edgePermutation,
                edgeOrientation,
                cornerPermutation,
                cornerOrientation
        );
    }

    public boolean isSolved() {
        for (int i = 0; i < 12; i++) {
            if (edgePermutation[i] != i || edgeOrientation[i] != 0) {
                return false;
            }
        }

        for (int i = 0; i < 8; i++) {
            if (cornerPermutation[i] != i || cornerOrientation[i] != 0) {
                return false;
            }
        }

        return true;
    }

    public boolean isValid() {
        int edgeOrientationSum = 0;
        int cornerOrientationSum = 0;

        for (int i = 0; i < 12; i++) {
            edgeOrientationSum += edgeOrientation[i];
        }

        for (int i = 0; i < 8; i++) {
            cornerOrientationSum += cornerOrientation[i];
        }

        if (edgeOrientationSum % 2 != 0) {
            return false;
        }

        if (cornerOrientationSum % 3 != 0) {
            return false;
        }

        if (permutationParity(edgePermutation)
                != permutationParity(cornerPermutation)) {
            return false;
        }

        return isValidPermutation(edgePermutation)
                && isValidPermutation(cornerPermutation);
    }

    private static boolean isValidPermutation(int[] permutation) {
        boolean[] seen = new boolean[permutation.length];

        for (int value : permutation) {
            if (value < 0 || value >= permutation.length || seen[value]) {
                return false;
            }

            seen[value] = true;
        }

        return true;
    }

    private static int permutationParity(int[] permutation) {
        int inversions = 0;

        for (int i = 0; i < permutation.length; i++) {
            for (int j = i + 1; j < permutation.length; j++) {
                if (permutation[i] > permutation[j]) {
                    inversions++;
                }
            }
        }

        return inversions % 2;
    }

    public long getHash() {
        long hash = 0x9E3779B97F4A7C15L;

        for (int i = 0; i < 8; i++) {
            hash ^= cornerPermutation[i] + 0x9E3779B9L + (hash << 6) + (hash >> 2);
            hash ^= cornerOrientation[i] + 0x9E3779B9L + (hash << 6) + (hash >> 2);
        }

        for (int i = 0; i < 12; i++) {
            hash ^= edgePermutation[i] + 0x9E3779B9L + (hash << 6) + (hash >> 2);
            hash ^= edgeOrientation[i] + 0x9E3779B9L + (hash << 6) + (hash >> 2);
        }

        return hash;
    }
}