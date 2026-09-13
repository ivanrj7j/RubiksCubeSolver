import java.util.ArrayList;

public class Node {
    Cubie state;
    ArrayList<Node> children;
    Node parent;

    Node(Cubie state){
        this.state = state;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    Node(Cubie state, Node parent){
        this.state = state;
        this.children = new ArrayList<>();
        this.parent = parent;
    }

    void addChild(Node n){
        n.parent = this;
        this.children.add(n);
    }

    void addChild(Cubie c){
        this.children.add(new Node(c, this));
    }
}
