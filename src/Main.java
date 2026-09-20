import heuristic.ManhattenLikeHeuristic;
import heuristic.SimpleMisplaceHeuristic;
import move.CubeMover;
import solver.*;
import state.Cubie;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        Cubie a = new Cubie();
        CubeMover mover = new CubeMover();
        Cubie b = a;
        Random r = new Random();
        for(int i = 0; i < 6; i++){
            int move = r.nextInt(6);
            System.out.println(move);
            b = mover.move(b, move);
        }
        CubeSolver solver1 = new BFSSolver();
//        CubeSolver solver2 = new IterativeDeepeningBestFirst(new ManhattenLikeHeuristic(), 15);

        Solution solution1 = solver1.solve(b);
//        Solution solution2 = solver2.solve(b);

        System.out.printf("Greedy : Moves needed: %d Total visited: %d Total searched: %d\n", solution1.moves.size()-1, solution1.visitedMoves, solution1.searchedMoves);

//        System.out.printf("BFS: Moves needed: %d Total visited: %d Total searched: %d\n", solution2.moves.size()-1, solution2.visitedMoves, solution2.searchedMoves);

//        System.out.printf("Best first search found solution with %.3f%% of BFS", 100.0 * solution1.visitedMoves/solution2.visitedMoves);
    }
}