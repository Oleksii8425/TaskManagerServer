package crdt.taskmanager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Server started!");
            Server server = new Server();
            server.start(6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}