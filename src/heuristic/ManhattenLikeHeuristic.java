package heuristic;

import move.Move;
import move.MoveGenerator;
import move.Node;
import state.Cubie;

import java.util.ArrayDeque;
import java.util.HashSet;

public class ManhattenLikeHeuristic implements Heuristic {

    private static int[][][] cornerDistance;
    private static int[][][] edgeDistance;

    private static void preComputeTables() {
        if (cornerDistance != null && edgeDistance != null) {
            return;
        }

        cornerDistance = new int[8][8][3];
        edgeDistance = new int[12][12][2];

        initialize(cornerDistance);
        initialize(edgeDistance);

        preComputeCorners();
        preComputeEdges();
    }

    private static void initialize(int[][][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                for (int k = 0; k < table[i][j].length; k++) {
                    table[i][j][k] = -1;
                }
            }
        }
    }

    private static void preComputeCorners() {

        for (int cubie = 0; cubie < 8; cubie++) {

            Cubie solvedState = new Cubie();

            ArrayDeque<Node> queue = new ArrayDeque<>();
            HashSet<Integer> visited = new HashSet<>();

            int startCode = encodeCorner(cubie, cubie, 0);

            cornerDistance[cubie][cubie][0] = 0;
            visited.add(startCode);

            queue.offer(new Node(
                    new Move(solvedState, -1),
                    null,
                    0
            ));

            while (!queue.isEmpty()) {

                Node current = queue.poll();

                for (Move move : new MoveGenerator(current.move.state).getMoves()) {

                    Cubie nextState = move.state;

                    int position = findCorner(nextState, cubie);
                    int orientation = nextState.cornerOrientation[position];

                    int code = encodeCorner(
                            cubie,
                            position,
                            orientation
                    );

                    if (!visited.contains(code)) {

                        visited.add(code);

                        cornerDistance[cubie][position][orientation] =
                                current.depth + 1;

                        queue.offer(new Node(
                                move,
                                current,
                                current.depth + 1
                        ));
                    }
                }
            }
        }
    }

    private static void preComputeEdges() {

        for (int cubie = 0; cubie < 12; cubie++) {

            Cubie solvedState = new Cubie();

            ArrayDeque<Node> queue = new ArrayDeque<>();
            HashSet<Integer> visited = new HashSet<>();

            int startCode = encodeEdge(cubie, cubie, 0);

            edgeDistance[cubie][cubie][0] = 0;
            visited.add(startCode);

            queue.offer(new Node(
                    new Move(solvedState, -1),
                    null,
                    0
            ));

            while (!queue.isEmpty()) {

                Node current = queue.poll();

                for (Move move : new MoveGenerator(current.move.state).getMoves()) {

                    Cubie nextState = move.state;

                    int position = findEdge(nextState, cubie);
                    int orientation = nextState.edgeOrientation[position];

                    int code = encodeEdge(
                            cubie,
                            position,
                            orientation
                    );

                    if (!visited.contains(code)) {

                        visited.add(code);

                        edgeDistance[cubie][position][orientation] =
                                current.depth + 1;

                        queue.offer(new Node(
                                move,
                                current,
                                current.depth + 1
                        ));
                    }
                }
            }
        }
    }

    private static int findCorner(Cubie state, int cubie) {

        for (int position = 0; position < 8; position++) {
            if (state.cornerPermutation[position] == cubie) {
                return position;
            }
        }

        throw new IllegalStateException("Corner cubie not found");
    }

    private static int findEdge(Cubie state, int cubie) {

        for (int position = 0; position < 12; position++) {
            if (state.edgePermutation[position] == cubie) {
                return position;
            }
        }

        throw new IllegalStateException("Edge cubie not found");
    }

    private static int encodeCorner(
            int cubie,
            int position,
            int orientation
    ) {
        return cubie * 24 + position * 3 + orientation;
    }

    private static int encodeEdge(
            int cubie,
            int position,
            int orientation
    ) {
        return cubie * 24 + position * 2 + orientation;
    }

    public ManhattenLikeHeuristic() {
        preComputeTables();
    }

    @Override
    public int heuristic(Cubie state) {

        int maxCornerDistance = 0;

        for (int position = 0; position < 8; position++) {

            int cubie = state.cornerPermutation[position];
            int orientation = state.cornerOrientation[position];

            maxCornerDistance = Math.max(
                    maxCornerDistance,
                    cornerDistance[cubie][position][orientation]
            );
        }

        int maxEdgeDistance = 0;

        for (int position = 0; position < 12; position++) {

            int cubie = state.edgePermutation[position];
            int orientation = state.edgeOrientation[position];

            maxEdgeDistance = Math.max(
                    maxEdgeDistance,
                    edgeDistance[cubie][position][orientation]
            );
        }

        return Math.max(maxCornerDistance, maxEdgeDistance);
    }
}