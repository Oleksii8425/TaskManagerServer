import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start(6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}