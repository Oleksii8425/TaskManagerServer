package crdt.taskmanager;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            System.out.println("Client connected");
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(Server.sessionN); //session number
            out.writeObject(Server.clients - 1); //site number
            out.writeObject(Server.boards);
            InputStream input = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Received: " + message);
            }
            System.out.println("Client disconnected");
            out.close();

            if (Server.clients == 0) {
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
        }
    }
}
