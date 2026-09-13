package move;

import state.Cubie;

import java.util.ArrayList;

public class MoveGenerator {
    private final Cubie state;
    private final CubeMover mover;

    public MoveGenerator(Cubie state){
        this.state = state;
        this.mover = new CubeMover();
    }

    public ArrayList<Move> getMoves(){
        ArrayList<Move> nextStates = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 6; j++){
                nextStates.add(new Move(mover.move(state, j, i+1), (i*6) + j));
            }
        }

        return nextStates;
    }
}
