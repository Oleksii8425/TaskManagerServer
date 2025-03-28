package crdt.taskmanager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class Board implements Serializable, Titleable {
    private static final long serialVersionUID = 1L;
    private Queue<Operation<String>> opQueue;
    private String title;
    private RGA<Task> tasks;

    public Board() {
        this.opQueue = new LinkedList<>();
    }

    public Board(String title) {
        this.opQueue = new LinkedList<>();
        this.title = title;
        this.tasks = new RGA<Task>();
    }

    public Board(String title, RGA<Task> tasks) {
        this.opQueue = new LinkedList<>();
        this.title = title;
        this.tasks = tasks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RGA<Task> getTasks() {
        return tasks;
    }

    public void setTasks(RGA<Task> tasks) {
        this.tasks = tasks;
    }
}
