package move;

public class Node {
    public Move move;
    public Node parent;
    public int depth;

    public Node(Move move){
        this.move = move;
        this.parent = null;
        this.depth = 0;
    }

    public Node(Move move, Node parent){
        this.move = move;
        this.parent = parent;
        this.depth = 0;
    }
    public Node(Move move, Node parent, int depth){
        this.move = move;
        this.parent = parent;
        this.depth = depth;
    }

}
