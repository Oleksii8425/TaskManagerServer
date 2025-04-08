package crdt.taskmanager;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    public static int N = 3; // maximum number of clients
    public static long sessionN = 0L;
    public static int maxSiteN = 0;

    private ServerSocket serverSocket;

    private static RGA<Board> boards = new RGA<>();
    private ArrayList<ClientHandler> clients = new ArrayList<>();

    public Server() {
        load();
    }

    public long getSessionN() {
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

    public void removeClient(int siteN) {
        for (int i = 0; i < clients.size(); i++) {
            if (i == siteN) {
                clients.remove(i);
                return;
            }
        }
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        try {
            while (true) {
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

    public void resetVectorClocks() {
        boards.setVectorClock(new long[] { 0L, 0L, 0L });

        for (Board b : boards) {
            b.getTasks().setVectorClock(new long[] { 0L, 0L, 0L });
            for (Task t : b.getTasks()) {
                t.getContent().setVectorClock(new long[] { 0L, 0L, 0L });
            }
        }
    }

    public static void save() {
        try (FileOutputStream fileOut = new FileOutputStream("data.dat");
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeLong(sessionN);
            for (Board board : boards) {
                for (Task task : board.getTasks()) {
                    System.out.println(task.getContent().toString());
                }
            }
            out.writeObject(boards);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void load() {
        try (FileInputStream fileIn = new FileInputStream("data.dat");
                ObjectInputStream in = new ObjectInputStream(fileIn)) {
            sessionN = in.readLong();
            boards = (RGA<Board>) in.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Data file not found.");
        } catch (IOException e) {
            System.err.println("Error reading file.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
