package crdt.taskmanager;

import java.util.Iterator;

public class RGAIterator<T> implements Iterator<T> {

    RGANode<T> current;

    public RGAIterator(RGA<T> rga) {
        current = rga.getHead();
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public T next() {
        T data = current.value;
        current = current.link;
        return data;
    }
}
