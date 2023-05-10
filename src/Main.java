public class Main {
    public static void main(String[] args) {
        var loader = new BatchLoader(args);

        loader.loadFromStdio();
    }
}