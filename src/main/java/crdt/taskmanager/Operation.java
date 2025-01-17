package crdt.taskmanager;

import java.io.Serializable;
import java.util.Vector;

public class Operation implements Serializable {
    String type;
    String character;
    Vector<Long> vectorClock;
    String board;
    String task;

    public Operation(String type, String character, Vector<Long> vectorClock, String board, String task) {
        this.type = type;
        this.character = character;
        this.vectorClock = vectorClock;
        this.board = board;
        this.task = task;
    }

    @Override
    public String toString() {
        return "Operation [type=" + type + ", character=" + character + ", vectorClock=" + vectorClock + ", board=" + board + ", task=" + task + "]";
    }
}
