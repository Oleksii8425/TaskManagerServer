package crdt.taskmanager;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final Server server;

    private ObjectOutputStream out;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            System.out.println("Client connected");
            out.writeObject(Server.sessionN); //session number
            int siteN = server.getClientsNumber() - 1;
            out.writeObject(siteN); //site number
            out.writeObject(Server.boards);
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            Operation operation;
            while ((operation = (Operation) in.readObject()) != null) {
                System.out.println("Received: " + ANSI_GREEN +  operation + ANSI_RESET);
                for (ClientHandler client : server.getClients()) {
                    client.broadcastOperation(operation);
                }
            }
            System.out.println("Client disconnected");
            server.removeClient(siteN);
            out.close();

            if (server.getClientsNumber() == 0) {
                Server.sessionN++;
                for (Board b : Server.boards.values()) {
                    for (Task t : b.getTasks()) {
                        t.getVectorClock().set(0, 0L);
                        t.getVectorClock().set(1, 0L);
                        t.getVectorClock().set(2, 0L);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcastOperation(Operation operation) {
        try {
            out.writeObject(operation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
