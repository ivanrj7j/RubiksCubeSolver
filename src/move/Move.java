package move;

import state.Cubie;

public class Move {
    public Cubie state;
    public int move;

    public Move(Cubie state, int move){
        this.state = state;
        this.move = move;
    }
}
