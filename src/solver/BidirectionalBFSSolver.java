package solver;

import move.CubeMover;
import move.Move;
import move.MoveGenerator;
import move.Node;
import state.CubeKey;
import state.Cubie;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BidirectionalBFSSolver implements CubeSolver {

    @Override
    public Solution solve(Cubie state) {

        ArrayDeque<Node> forwardQueue = new ArrayDeque<>();
        ArrayDeque<Node> backwardQueue = new ArrayDeque<>();

        Map<CubeKey, Node> forwardVisited = new HashMap<>();
        Map<CubeKey, Node> backwardVisited = new HashMap<>();

        Node forwardRoot = new Node(
                new Move(state, -1),
                null
        );

        Node backwardRoot = new Node(
                new Move(new Cubie(), -1),
                null
        );

        forwardQueue.offer(forwardRoot);
        backwardQueue.offer(backwardRoot);

        forwardVisited.put(state.getKey(), forwardRoot);
        backwardVisited.put(
                backwardRoot.move.state.getKey(),
                backwardRoot
        );

        Node forwardMeetingNode = null;
        Node backwardMeetingNode = null;

        int searchedMoves = 0;
        int visitedMoves = 0;

        while(!forwardQueue.isEmpty() && !backwardQueue.isEmpty()) {

            // Expand the smaller frontier
            if(forwardQueue.size() <= backwardQueue.size()) {

                Node current = forwardQueue.poll();
                searchedMoves++;

                MoveGenerator generator =
                        new MoveGenerator(current.move.state);

                for(Move move : generator.getMoves()) {

                    if(CubeMover.shouldPrune(
                            current.move.move,
                            move.move
                    )) {
                        continue;
                    }

                    CubeKey key = move.state.getKey();

                    if(forwardVisited.containsKey(key)) {
                        continue;
                    }

                    Node inspected = new Node(
                            move,
                            current,
                            current.depth + 1
                    );

                    forwardVisited.put(key, inspected);
                    forwardQueue.offer(inspected);
                    visitedMoves++;

                    if(backwardVisited.containsKey(key)) {
                        forwardMeetingNode = inspected;
                        backwardMeetingNode = backwardVisited.get(key);
                        return buildSolution(
                                forwardMeetingNode,
                                backwardMeetingNode,
                                searchedMoves,
                                visitedMoves
                        );
                    }
                }

            } else {

                Node current = backwardQueue.poll();
                searchedMoves++;

                MoveGenerator generator =
                        new MoveGenerator(current.move.state);

                for(Move move : generator.getMoves()) {

                    if(CubeMover.shouldPrune(
                            current.move.move,
                            move.move
                    )) {
                        continue;
                    }

                    CubeKey key = move.state.getKey();

                    if(backwardVisited.containsKey(key)) {
                        continue;
                    }

                    Node inspected = new Node(
                            move,
                            current,
                            current.depth + 1
                    );

                    backwardVisited.put(key, inspected);
                    backwardQueue.offer(inspected);
                    visitedMoves++;

                    if(forwardVisited.containsKey(key)) {
                        forwardMeetingNode = forwardVisited.get(key);
                        backwardMeetingNode = inspected;

                        return buildSolution(
                                forwardMeetingNode,
                                backwardMeetingNode,
                                searchedMoves,
                                visitedMoves
                        );
                    }
                }
            }
        }

        return new Solution(
                new ArrayList<>(),
                searchedMoves,
                visitedMoves
        );
    }

    private Solution buildSolution(
            Node forwardNode,
            Node backwardNode,
            int searchedMoves,
            int visitedMoves
    ) {

        ArrayList<Integer> moves = new ArrayList<>();

        // Start → meeting point
        Node current = forwardNode;

        while(current.parent != null) {
            moves.add(current.move.move);
            current = current.parent;
        }

        Collections.reverse(moves);

        // Meeting point → solved
        current = backwardNode;

        while(current.parent != null) {
            moves.add(inverseMove(current.move.move));
            current = current.parent;
        }

        return new Solution(
                moves,
                searchedMoves,
                visitedMoves
        );
    }

    private int inverseMove(int move) {

        if(move < 0) {
            return -1;
        }

        // U, R, F, D, L, B
        // Their inverse is the corresponding ' move.
        if(move < 6) {
            return move + 12;
        }

        // U2, R2, F2, D2, L2, B2
        if(move < 12) {
            return move;
        }

        // U', R', F', D', L', B'
        return move - 12;
    }
}