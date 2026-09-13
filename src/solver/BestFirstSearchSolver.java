package solver;

import helpers.DecreaseKeyPriorityQueue;
import heuristic.Heuristic;
import move.Move;
import move.MoveGenerator;
import move.Node;
import move.PriorityNode;
import state.CubeKey;
import state.Cubie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class BestFirstSearchSolver implements CubeSolver{
    private final Heuristic heuristic;

    public BestFirstSearchSolver(Heuristic heuristic){
        this.heuristic = heuristic;
    }

    @Override
    public Solution solve(Cubie state) {
        PriorityQueue<PriorityNode> queue = new PriorityQueue<>(Comparator.comparingInt(PriorityNode::priority).thenComparingLong(PriorityNode::order));
        ArrayList<Integer> moves = new ArrayList<>();
        HashSet<CubeKey> visited = new HashSet<>();

        queue.offer(new PriorityNode(
                new Node(
                        new Move(state, -1),
                        null
                ),
                heuristic.heuristic(state),
                0
        ));
        visited.add(state.getKey());

        int visitedMoves = 0;
        int searchedMoves = 0;

        boolean found = false;
        Node leaf = null;
        long order = 1;

        while(!queue.isEmpty() && !found){
            searchedMoves++;
            PriorityNode _current = queue.poll();
            Node current = _current.node();

            MoveGenerator generator = new MoveGenerator(current.move.state);
            for(Move move : generator.getMoves()){
                CubeKey key = move.state.getKey();
                Node inspected = new Node(move, current, current.depth + 1);

                if(!visited.contains(key)){
                    visitedMoves++;;
                    visited.add(key);

                    queue.offer(new PriorityNode(inspected, heuristic.heuristic(move.state), order++));

                    if(move.state.isSolved()){
                        found = true;
                        leaf = inspected;
                        break;
                    }
                }
            }
        }

        while (leaf != null){
            moves.add(leaf.move.move);
            leaf = leaf.parent;
        }

        Collections.reverse(moves);

        return new Solution(moves,searchedMoves, visitedMoves);
    }
}
