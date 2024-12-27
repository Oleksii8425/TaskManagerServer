package crdt.taskmanager;

import java.io.*;
import java.net.ServerSocket;
import java.util.Hashtable;

public class Server {
    private ServerSocket serverSocket;

    public static Hashtable<String, Board> boards = new Hashtable<>();
    public static int clients = 0;

    public Server() {
        boards.put("board_1", new Board("board_1"));
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        try {
            while (clients <= 4) {
                new ClientHandler(serverSocket.accept()).start();
                clients++;
            }
        } catch (IOException e) {
            stop();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }


}
