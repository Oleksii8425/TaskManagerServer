package crdt.taskmanager;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;

    private static Long sessionN = 0L;
    private static RGA<Board> boards = new RGA<>();
    private ArrayList<ClientHandler> clients = new ArrayList<>();

    public Server() {
    }

    public Long getSessionN() {
        return sessionN;
    }

    public void increaseSessionN() {
        sessionN++;
    }

    public RGA<Board> getBoards() {
        return boards;
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }

    public void removeClient(int index) {
        clients.remove(index);
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        try {
            while (clients.size() <= 3) {
                ClientHandler client = new ClientHandler(serverSocket.accept(), this, clients.size());
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

    public void resetVectorClocks() {
        for (Board b : boards) {
            for (Task t : b.getTasks()) {
                t.getContent().updateVectorClock(0, 0L);
                t.getContent().updateVectorClock(1, 0L);
                t.getContent().updateVectorClock(2, 0L);
            }
        }
    }
}
