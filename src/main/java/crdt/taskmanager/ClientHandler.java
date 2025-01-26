package crdt.taskmanager;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final Server server;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
        try {
            this.in = new ObjectInputStream(clientSocket.getInputStream());
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
            Operation operation;
            while (true) {
                try {
                    operation = (Operation) in.readObject();
                    if (operation == null) break;
                } catch (EOFException e) {
                    continue;
                }
                
                System.out.println("Received: " + ANSI_GREEN +  operation + ANSI_RESET);
                for (ClientHandler client : server.getClients()) {
                    client.broadcastOperation(operation);
                }
            }

            System.out.println("Client disconnected");
            server.removeClient(siteN);
            out.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

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
    }

    private void broadcastOperation(Operation operation) {
        try {
            out.writeObject(operation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
