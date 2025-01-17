package crdt.taskmanager;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class Server {
    private ServerSocket serverSocket;

    public static long sessionN = 0;
    public static Hashtable<String, Board> boards = new Hashtable<>();
    private ArrayList<ClientHandler> clients = new ArrayList<>();

    public Server() {
        boards.put("board_1", new Board("board_1"));
        for (Board board : boards.values()) {
            for (Task task : board.getTasks()) {
                task.setVectorClock(new Vector<>(Arrays.asList(0L, 0L, 0L)));
            }
        }
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }

    public int getClientsNumber() {
        return clients.size();
    }

    public void removeClient(int index) {
        clients.remove(index);
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        try {
            while (clients.size() <= 3) {
                ClientHandler client = new ClientHandler(serverSocket.accept(), this);
                client.start();
                clients.add(client);
            }
        } catch (IOException e) {
            stop();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}
