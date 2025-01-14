package crdt.taskmanager;

import java.io.*;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class Server {
    private ServerSocket serverSocket;

    public static long sessionN = 0;
    public static Hashtable<String, Board> boards = new Hashtable<>();
    public static int clients = 0;

    public Server() {
        boards.put("board_1", new Board("board_1"));
        for (Board board : boards.values()) {
            for (Task task : board.getTasks()) {
                task.setVectorClock(new Vector<>(Arrays.asList(0L, 0L, 0L)));
            }
        }
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
