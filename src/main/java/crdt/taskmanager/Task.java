package crdt.taskmanager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private Queue<Operation<String>> opQueue;
    private String title;
    private RGA<String> content;

    public Task(String title) {
        opQueue = new LinkedList<>();
        this.title = title;
        this.content = new RGA<>();
    }

    @JsonCreator
    public Task(@JsonProperty("title") String title,
            @JsonProperty("content") RGA<String> content) {
        opQueue = new LinkedList<>();
        this.title = title;
        this.content = content;
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

    @Override
    public String toString() {
        return "Task " + title;
    }
}