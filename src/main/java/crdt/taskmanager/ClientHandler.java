package crdt.taskmanager;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final Server server;
    private final int siteN;

    private ObjectInputStream in;
    private ObjectOutputStream out;

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
            out.writeObject(server.getSessionN());
            out.writeObject(siteN);
            out.writeObject(server.getBoards());

            while (true) {
                try {
                    Operation<?> operation = (Operation<?>) in.readObject();

                    if (operation == null) {
                        System.out.println("Empty operation received");
                        break;
                    }

                    System.out.println("Received: " + operation);

                    for (ClientHandler client : server.getClients()) {
                        if (operation.siteN != client.siteN) {
                            client.broadcastOperation(operation);
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

        if (server.getClients().size() == 0) {// if all clients are disconnected
            server.increaseSessionN();
            server.resetVectorClocks();
        }
    }

    public void broadcastOperation(Operation<?> op) {
        try {
            out.writeObject(op);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
