import move.CubeMover;
import state.Cubie;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        Cubie a = new Cubie();
        CubeMover mover = new CubeMover();
        Cubie b = a;
        Random r = new Random();
        for(int i = 0; i < 7; i++){
            int move = r.nextInt(6);
            System.out.println(move);
            b = mover.move(b, move);
        }
        CubeSolver solver = new BFSSolver();
        Solution solution = solver.solve(b);

        System.out.printf("Moves needed: %d Total visited: %d Total searched: %d\n", solution.moves.size()-1, solution.visitedMoves, solution.searchedMoves);

        System.out.println("Moves needed:");
        for(int move : solution.moves){
            if(move >= 0) System.out.printf("%s ", CubeMover.stringifyMove(move));
        }
    }
}