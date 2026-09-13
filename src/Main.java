public class Main {
    public static void main(String[] args) {

        Cubie a = new Cubie();
        CubeMover mover = new CubeMover();
        Cubie b = mover.applyMoves(a, 1, 0, 2, 5);
        DFSSolver solver = new DFSSolver();
        Solution solution = solver.solve(b);

        System.out.printf("Total visited: %d Total searched: %d\n", solution.visitedMoves, solution.searchedMoves);
    }
}