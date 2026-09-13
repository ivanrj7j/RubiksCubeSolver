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

        for(int i = 0; i < 9*6; i++){
            System.out.printf("(%d, %d)\n", first.facelets[i], last.facelets[i]);
            if(first.facelets[i] != last.facelets[i]){
                System.out.println("Not equal");
                return;
            }
        }
        System.out.println("Equal");
    }
}