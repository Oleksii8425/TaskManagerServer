package crdt.taskmanager;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final Server server;
    private Integer siteN;

    private ObjectInputStream in;
    private ObjectOutputStream out;

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

    public Integer getSiteN() {
        return this.siteN;
    }

    public void run() {
        try {
            siteN = (Integer) in.readObject();

            // if the site is not initialised and the number of existing clients
            // is less than the max number of clients
            if (siteN == null && Server.maxSiteN < Server.N) {
                siteN = Server.maxSiteN;
                Server.maxSiteN++;
            }

            System.out.println("Client" + siteN + " connected");
            System.out.println("Total clients connected: " + server.getClients().size());

            // read offline operations from the site if any is present
            while (true) {
                int res = readOperation();
                if (res == 1)
                    break;
            }

            out.writeLong(server.getSessionN());
            out.writeInt(siteN);
            out.writeObject(server.getBoards());

            while (true) {
                try {
                    int res = readOperation();
                    if (res == 1)
                        break;
                } catch (EOFException e) {
                    continue;
                }
            }

            server.removeClient(siteN);
            System.out.println("Client " + siteN + " disconnected");
            System.out.println("Total clients connected: " + server.getClients().size());

            // increase session number if all clients are disconnected
            // if (server.getClients().size() == 0) {
            //     Server.sessionN++;
            //     System.out.println("session: " + Server.sessionN);
            //     server.resetVectorClocks();
            // }

            out.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private int readOperation() throws ClassNotFoundException, IOException {
        Operation<?> operation = (Operation<?>) in.readObject();

        if (operation != null) {
            System.out.println("Received: " + operation);

            switch (operation.getTarget()) {
                case BOARD -> {
                    handleBoardOperation((Operation<Board>) operation);
                }
                case TASK -> {
                    handleTaskOperation((Operation<Task>) operation);
                }
                case TEXT -> {
                    handleTextOperation((Operation<Character>) operation);
                }
            }

            for (ClientHandler client : server.getClients()) {
                if (operation.siteN != client.siteN) {
                    System.out.println("Broadcasting operation to client " + client.siteN);
                    client.broadcastOperation(operation);
                }
            }

            return 0;
        }

        return 1;
    }

    private void handleBoardOperation(Operation<Board> operation) {
        try {
            server.getBoards()
                    .enqueueOperation(operation);

            Thread boardOperationThread = new Thread(
                    new BoardOperationHandler(server.getBoards()));

            boardOperationThread.start();
            boardOperationThread.join();
        } catch (InterruptedException e) {
            System.err.println("BoardOperationHandler was interrupted!");
        }
    }

    private void handleTaskOperation(Operation<Task> operation) {
        try {
            RGA<Task> tasks = server.getBoards()
                    .get(operation.boardTitle)
                    .getTasks();

            tasks.enqueueOperation(operation);

            Thread taskOperationThread = new Thread(
                    new TaskOperationHandler(tasks));

            taskOperationThread.start();
            taskOperationThread.join();
        } catch (InterruptedException e) {
            System.err.println("TaskOperationHandler was interrupted!");
        }
    }

    private void handleTextOperation(Operation<Character> operation) {
        try {
            RGA<Character> taskContent = server.getBoards()
                    .get(operation.boardTitle).getTasks()
                    .get(operation.taskTitle)
                    .getContent();

            taskContent.enqueueOperation(operation);

            Thread textOperationThread = new Thread(
                    new TextOperationHandler(taskContent));

            textOperationThread.start();
            textOperationThread.join();
        } catch (InterruptedException e) {
            System.err.println("TextOperationHandler was interrupted!");
        }
    }

    public void broadcastOperation(Operation<?> op) {
        try {
            out.writeObject(op);
        } catch (IOException e) {
            System.err.println("Error: Client unavailable to broadcast an operation.");
        }
    }
}
