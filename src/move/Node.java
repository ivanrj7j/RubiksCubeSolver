package move;

public class Node {
    public Move move;
    public Node parent;

    public Node(Move move){
        this.move = move;
        this.parent = null;
    }

    public Node(Move move, Node parent){
        this.move = move;
        this.parent = parent;
    }
}
