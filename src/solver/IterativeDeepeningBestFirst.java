package solver;

import heuristic.Heuristic;
import move.Move;
import move.MoveGenerator;
import move.Node;
import move.PriorityNode;
import state.CubeKey;
import state.Cubie;

import java.util.*;

public class IterativeDeepeningBestFirst implements CubeSolver {

    private final Heuristic heuristic;
    private final int maxDepth;

    public IterativeDeepeningBestFirst(Heuristic heuristic) {
        this(heuristic, 30);
    }

    public IterativeDeepeningBestFirst(Heuristic heuristic, int maxDepth) {
        this.heuristic = heuristic;
        this.maxDepth = maxDepth;
    }

    @Override
    public Solution solve(Cubie state) {

        int expandedNodes = 0;
        int visitedNodes = 0;

        if (state.isSolved()) {
            return new Solution(
                    new ArrayList<>(),
                    expandedNodes,
                    visitedNodes
            );
        }

        Node solution = null;

        for (int depthLimit = 1;
             depthLimit <= maxDepth && solution == null;
             depthLimit++) {

            long order = 0;

            PriorityQueue<PriorityNode> queue = new PriorityQueue<>(
                    Comparator
                            .comparingInt(PriorityNode::priority)
                            .thenComparingLong(PriorityNode::order)
            );

            HashMap<CubeKey, Integer> visited = new HashMap<>();

            Node root = new Node(
                    new Move(state, -1),
                    null,
                    0
            );

            int rootHeuristic = heuristic.heuristic(state);

            queue.offer(new PriorityNode(
                    root,
                    rootHeuristic,
                    order++
            ));

            visited.put(state.getKey(), 0);
            visitedNodes++;

            while (!queue.isEmpty() && solution == null) {

                PriorityNode priorityNode = queue.poll();
                Node current = priorityNode.node();

                expandedNodes++;

                MoveGenerator generator =
                        new MoveGenerator(current.move.state);

                for (Move move : generator.getMoves()) {

                    int newDepth = current.depth + 1;

                    if (newDepth > depthLimit) {
                        continue;
                    }

                    if (isRedundant(current.move.move, move.move)) {
                        continue;
                    }

                    CubeKey key = move.state.getKey();

                    Integer previousDepth = visited.get(key);

                    if (previousDepth != null &&
                            previousDepth <= newDepth) {
                        continue;
                    }

                    Node child = new Node(
                            move,
                            current,
                            newDepth
                    );

                    visited.put(key, newDepth);
                    visitedNodes++;

                    if (move.state.isSolved()) {
                        solution = child;
                        break;
                    }

                    int h = heuristic.heuristic(move.state);
                    int f = newDepth + h;

                    queue.offer(new PriorityNode(
                            child,
                            f,
                            order++
                    ));
                }
            }
        }

        ArrayList<Integer> moves = new ArrayList<>();

        if (solution != null) {
            Node current = solution;

            while (current != null && current.move.move != -1) {
                moves.add(current.move.move);
                current = current.parent;
            }

            Collections.reverse(moves);
        }

        return new Solution(
                moves,
                expandedNodes,
                visitedNodes
        );
    }

    private boolean isRedundant(int previousMove, int currentMove) {

        if (previousMove == -1) {
            return false;
        }

        if (isInverse(previousMove, currentMove)) {
            return true;
        }

        int previousFace = previousMove % 6;
        int currentFace = currentMove % 6;

        return previousFace == currentFace;
    }

    private boolean isInverse(int previousMove, int currentMove) {

        int previousFace = previousMove % 6;
        int currentFace = currentMove % 6;

        if (previousFace != currentFace) {
            return false;
        }

        int previousAmount = previousMove / 6 + 1;
        int currentAmount = currentMove / 6 + 1;

        return previousAmount + currentAmount == 4;
    }
}