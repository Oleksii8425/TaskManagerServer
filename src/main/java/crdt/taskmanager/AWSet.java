package crdt.taskmanager;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AWSet<T extends Titleable> implements Serializable, Iterable<T> {
    private static final long serialVersionUID = 1L;

    private Map<T, LWWRegister> elements;
    private Map<T, LWWRegister> removals;

    public AWSet() {
        this.elements = new HashMap<>();
        this.removals = new HashMap<>();
    }

    public List<T> getElements() {
        return elements.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().getValue()))
                .map(Map.Entry::getKey) // Extract values
                .toList();
    }

    @Override
    public Iterator<T> iterator() {
        return getElements().iterator();
    }
}

