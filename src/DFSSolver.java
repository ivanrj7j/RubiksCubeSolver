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

        int visitedMoves = 0;
        int searchedMoves = 0;

        boolean found = false;

        while (!stack.isEmpty() && !found){
            searchedMoves++;
            Cubie current = stack.pop();
            MoveGenerator generator = new MoveGenerator(current);

            for(Cubie c : generator.getMoves()){
                CubeKey key = c.getKey();
                if(!visited.contains(key)){
                    visitedMoves++;
                    visited.add(key);
                    stack.push(c);
                }
                if(c.isSolved()){
                    found = true;
                    break;
                }
            }

        }

        return new Solution(moves, searchedMoves, visitedMoves);
    }
}
