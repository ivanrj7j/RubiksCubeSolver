import java.util.ArrayList;

public class Node {
    Move move;
    Node parent;

    Node(Move move){
        this.move = move;
        this.parent = null;
    }

    Node(Move move, Node parent){
        this.move = move;
        this.parent = parent;
    }
}
