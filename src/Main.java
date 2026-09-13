public class Main {
    public static void main(String[] args) {
        CubeMover mover = new CubeMover();

        Cubie a = new Cubie();
        MoveGenerator generator = new MoveGenerator(a);
        for(Cubie state : generator.getMoves()){
            System.out.println(state);
        }
    }
}