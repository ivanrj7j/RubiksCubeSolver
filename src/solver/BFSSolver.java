package solver;

import move.Move;
import move.MoveGenerator;
import move.Node;
import state.CubeKey;
import state.Cubie;

import java.util.*;

public class BFSSolver implements CubeSolver {
    @Override
    public Solution solve(Cubie state) {
        HashSet<CubeKey> visited = new HashSet<>();
        ArrayList<Integer> moves = new ArrayList<>();
        ArrayDeque<Node> queue = new ArrayDeque<>();

        queue.offer(new Node(
                new Move(state, -1),
                null
        ));

        visited.add(state.getKey());

        int searchedMoves = 0;
        int visitedMoves = 0;

        boolean found = false;
        Node leaf = null;
        while(!queue.isEmpty() && !found){
            searchedMoves++;
            Node current = queue.poll();

            MoveGenerator generator = new MoveGenerator(current.move.state);
            for(Move move : generator.getMoves()){
                CubeKey key = move.state.getKey();
                Node inspected = new Node(move, current);

                if(!visited.contains(key)){
                    visitedMoves++;
                    visited.add(key);
                    queue.offer(inspected);
                }

                if(move.state.isSolved()){
                    found = true;
                    leaf = inspected;
                    break;
                }
            }
        }

        while (leaf != null){
            moves.add(leaf.move.move);
            leaf = leaf.parent;
        }

        Collections.reverse(moves);

        return new Solution(moves, searchedMoves, visitedMoves);
    }
}
