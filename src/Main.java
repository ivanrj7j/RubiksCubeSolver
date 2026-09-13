public class Main {
    public static void main(String[] args) {
        CubeMover mover = new CubeMover();

        Cubie a = new Cubie();
        Cubie b = mover.move(a, CubeMover.B);
        Cubie c = mover.move(b, CubeMover.B);
        Cubie d = mover.move(c, CubeMover.B);
        Cubie e = mover.move(d, CubeMover.B);

        Facelet first = new Facelet(a);
        Facelet last = new Facelet(e);

        System.out.println(a.getHash() == e.getHash());
        System.out.println(b.getHash());
    }
}