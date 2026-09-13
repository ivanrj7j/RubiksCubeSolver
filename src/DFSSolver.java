import move.Move;
import move.MoveGenerator;
import state.CubeKey;
import state.Cubie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class DFSSolver implements CubeSolver{
    @Override
    public Solution solve(Cubie state) {
        HashSet<CubeKey> visited = new HashSet<>();
        ArrayList<Integer> moves = new ArrayList<>();
        Stack<Cubie> stack = new Stack<>();

        stack.push(state);
        visited.add(state.getKey());

        int visitedMoves = 0;
        int searchedMoves = 0;

        boolean found = false;

        while (!stack.isEmpty() && !found){
            searchedMoves++;
            Cubie current = stack.pop();
            MoveGenerator generator = new MoveGenerator(current);

            for(Move move : generator.getMoves()){
                CubeKey key = move.state.getKey();
                if(!visited.contains(key)){
                    visitedMoves++;
                    visited.add(key);
                    stack.push(move.state);
                }
                if(move.state.isSolved()){
                    found = true;
                    break;
                }
            }

        }

        return new Solution(moves, searchedMoves, visitedMoves);
    }
}
