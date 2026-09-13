package solver;

import java.util.ArrayList;

public class Solution {
    public ArrayList<Integer> moves;
    public int searchedMoves;
    public int visitedMoves;

    public Solution(ArrayList<Integer> moves, int searchedMoves, int visitedMoves){
        this.moves = moves;
        this.searchedMoves = searchedMoves;
        this.visitedMoves = visitedMoves;
    }
}
