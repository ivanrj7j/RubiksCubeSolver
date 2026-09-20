package state;

public class Cubie {
    public byte[] edgePermutation;
    public byte[] edgeOrientation;
    public byte[] cornerPermutation;
    public byte[] cornerOrientation;

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
        edgePermutation = new byte[12];
        edgeOrientation = new byte[12];

        cornerPermutation = new byte[8];
        cornerOrientation = new byte[8];

        for (byte i = 0; i < 8; i++) {
            cornerPermutation[i] = i;
            cornerOrientation[i] = 0;
        }

        for (byte i = 0; i < 12; i++) {
            edgePermutation[i] = i;
            edgeOrientation[i] = 0;
        }
    }

    public Cubie(
            byte[] edgePermutation,
            byte[] edgeOrientation,
            byte[] cornerPermutation,
            byte[] cornerOrientation
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

    public Cubie(CubeKey key) {
        edgePermutation = new byte[12];
        edgeOrientation = new byte[12];
        cornerPermutation = new byte[8];
        cornerOrientation = new byte[8];

        long low = key.low();
        long high = key.high();
        int shift = 0;

        for (int i = 0; i < 8; i++) {
            cornerPermutation[i] = (byte) ((low >>> shift) & 0xF);
            shift += 4;
        }

        for (int i = 0; i < 8; i++) {
            cornerOrientation[i] = (byte) ((low >>> shift) & 0x3);
            shift += 2;
        }

        for (int i = 0; i < 12; i++) {
            if (shift < 64) {
                edgePermutation[i] = (byte) ((low >>> shift) & 0xF);
            } else {
                edgePermutation[i] = (byte) ((high >>> (shift - 64)) & 0xF);
            }
            shift += 4;
        }

        for (int i = 0; i < 12; i++) {
            if (shift < 64) {
                edgeOrientation[i] = (byte) ((low >>> shift) & 0x1);
            } else {
                edgeOrientation[i] = (byte) ((high >>> (shift - 64)) & 0x1);
            }
            shift++;
        }
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

    private static boolean isValidPermutation(byte[] permutation) {
        boolean[] seen = new boolean[permutation.length];

        for (int value : permutation) {
            if (value < 0 || value >= permutation.length || seen[value]) {
                return false;
            }

            seen[value] = true;
        }

        return true;
    }

    private static int permutationParity(byte[] permutation) {
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

    public CubeKey getKey() {
        long low = 0;
        long high = 0;
        int shift = 0;

        for (int i = 0; i < 8; i++) {
            low |= ((long) cornerPermutation[i]) << shift;
            shift += 4;
        }

        for (int i = 0; i < 8; i++) {
            low |= ((long) cornerOrientation[i]) << shift;
            shift += 2;
        }

        for (int i = 0; i < 12; i++) {
            int value = edgePermutation[i];

            if (shift < 64) {
                low |= (long) value << shift;
            } else {
                high |= (long) value << (shift - 64);
            }

            shift += 4;
        }

        for (int i = 0; i < 12; i++) {
            int value = edgeOrientation[i];

            if (shift < 64) {
                low |= (long) value << shift;
            } else {
                high |= (long) value << (shift - 64);
            }

            shift++;
        }

        return new CubeKey(high, low);
    }

    @Override
    public String toString() {
        CubeKey key = getKey();
        return String.format("%016x", key.high()) + String.format("%016x", key.low());
    }
}