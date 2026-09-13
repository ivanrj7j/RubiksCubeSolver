package heuristic;

import state.Cubie;

public class SimpleMisplaceHeuristic implements Heuristic{
    @Override
    public int heuristic(Cubie state) {
        int misplacedCorners = 0;
        int misplacedEdges = 0;

        for(int i = 0; i < 12; i++){
            if(i < 8){
                if(state.cornerPermutation[i] != i || state.cornerOrientation[i] != 0) misplacedCorners++;
            }
            if(state.edgePermutation[i] != i || state.edgeOrientation[i] != 0) misplacedEdges++;
        }

        return Math.max((misplacedCorners+3)/4, (misplacedEdges+3)/4);
    }
}
