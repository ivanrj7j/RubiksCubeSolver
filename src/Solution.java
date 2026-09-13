import java.util.ArrayList;

public class Solution {
    ArrayList<Integer> moves;
    int searchedMoves;
    int visitedMoves;

    Solution(ArrayList<Integer> moves, int searchedMoves, int visitedMoves){
        this.moves = moves;
        this.searchedMoves = searchedMoves;
        this.visitedMoves = visitedMoves;
    }
}
