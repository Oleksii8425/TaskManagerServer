package crdt.taskmanager;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final Server server;
    private final int siteN;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public ClientHandler(Socket socket, Server server, int siteN) {
        this.clientSocket = socket;
        this.server = server;
        this.siteN = siteN;
        try {
            this.in = new ObjectInputStream(clientSocket.getInputStream());
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            System.out.println("Client" + siteN + " connected");
            out.writeObject(server.getSessionN()); // session number
            out.writeObject(siteN); // site number
            out.writeObject(server.getBoards());

            while (true) {
                try {
                    S4Vector i = (S4Vector) in.readObject();
                    Operation operation = (Operation) in.readObject();

                    if (operation == null)
                        break;

                    System.out.println("Received: " + ANSI_GREEN + operation + " with i = " + i + ANSI_RESET);

                    for (ClientHandler client : server.getClients()) {
                        if (operation.siteN != client.siteN) {
                            client.broadcastOperation(i, operation);
                        }
                    }
                } catch (EOFException e) {
                    continue;
                }
            }

            System.out.println("Client disconnected");
            server.removeClient(siteN);
            out.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (server.getClients().size() == 0) { // if all clients are disconnected
            server.increaseSessionN();
            server.resetVectorClocks();
        }
    }

    public void broadcastOperation(S4Vector i, Operation op) {
        try {
            out.writeObject(i);
            out.writeObject(op);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
