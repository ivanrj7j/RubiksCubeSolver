import java.util.ArrayList;

public class MoveGenerator {
    Cubie state;
    CubeMover mover;

    MoveGenerator(Cubie state){
        this.state = state;
        this.mover = new CubeMover();
    }

    ArrayList<Move> getMoves(){
        ArrayList<Move> nextStates = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 6; j++){
                nextStates.add(new Move(mover.move(state, j, i+1), (i*6) + j));
            }
        }

        return nextStates;
    }
}
