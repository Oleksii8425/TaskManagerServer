package crdt.taskmanager;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String title;
    private final List<Task> tasks = new ArrayList<>();

    public Board(String title) {
        this.title = title;
        tasks.addLast(new Task("test task", new RGA()));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }
}