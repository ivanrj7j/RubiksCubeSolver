import java.util.ArrayList;

public class MoveGenerator {
    Cubie state;
    CubeMover mover;

    MoveGenerator(Cubie state){
        this.state = state;
        this.mover = new CubeMover();
    }

    ArrayList<Cubie> getMoves(){
        ArrayList<Cubie> nextStates = new ArrayList<>();
        for(int i = 1; i <= 3; i++){
            for(int j = 0; j < 6; j++){
                nextStates.add(mover.move(state, j, i));
            }
        }

        return nextStates;
    }
}
