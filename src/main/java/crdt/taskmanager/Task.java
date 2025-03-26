package crdt.taskmanager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class Task implements Serializable, Titleable {
    private static final long serialVersionUID = 1L;
    private Queue<Operation<String>> opQueue;
    private String title;
    private RGA<String> content;

    public Task(String title) {
        opQueue = new LinkedList<>();
        this.title = title;
        this.content = new RGA<>();
    }

    public void enqueueOperation(Operation<String> op) {
        opQueue.add(op);
    }

    public void dequeueOperation(Operation<String> op) {
        opQueue.remove(op);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RGA<String> getContent() {
        return content;
    }
}