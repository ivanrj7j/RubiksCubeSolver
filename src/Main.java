public class Main {
    public static void main(String[] args) {
        CubeMover mover = new CubeMover();

        Cubie a = new Cubie();
        Cubie b = mover.move(a, CubeMover.B);
        Cubie c = mover.move(b, CubeMover.B);
        Cubie d = mover.move(c, CubeMover.B);
        Cubie e = mover.move(d, CubeMover.B);

        System.out.println(Long.toHexString(a.getKey().high())+Long.toHexString(a.getKey().low()));
        System.out.println(Long.toHexString(e.getKey().high())+Long.toHexString(e.getKey().low()));
    }
}