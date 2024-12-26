import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Hashtable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Server {
    private ServerSocket serverSocket;

    private Hashtable<String, Board> boards = new Hashtable<>();

    public Server() {
        try {
            readBoards();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        try {
            while (true)
                new ClientHandler(serverSocket.accept()).start();
        } catch (IOException e) {
            stop();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private void readBoards() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        boards = mapper.readValue(new File("boards.json"), new TypeReference<>() {});
    }

    private void writeBoards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        Board b1 = new Board("test1");
        b1.addTask(new Task("test task", new RGA()));
        Board b2 = new Board("test2");
        boards.put("test1", b1);
        boards.put("test2", b2);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .writerWithDefaultPrettyPrinter()
                .writeValue(new File("boards.json"), boards);
    }
}
