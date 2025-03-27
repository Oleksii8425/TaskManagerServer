package crdt.taskmanager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class Board implements Serializable, Titleable {
    private static final long serialVersionUID = 1L;
    private Queue<Operation<String>> opQueue;
    private String title;
    private AWSet<Task> tasks;

    public Board() {
        this.opQueue = new LinkedList<>();
    }

    public Board(String title) {
        this.opQueue = new LinkedList<>();
        this.title = title;
        this.tasks = new AWSet<Task>();
    }

    public Board(String title, AWSet<Task> tasks) {
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

    public AWSet<Task> getTasks() {
        return tasks;
    }

    public void setTasks(AWSet<Task> tasks) {
        this.tasks = tasks;
    }
}
