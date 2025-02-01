package crdt.taskmanager;

import java.util.Hashtable;
import java.util.Map;

public class CustomHashtable<K, V> extends Hashtable<K, V> {
    private static final long serialVersionUID = 1L;
    
    @Override
    public V get(Object key) {
        for (Map.Entry<K, V> entry : this.entrySet()) {
            if (entry.getKey().equals(key)) {  // Ensures field-based equality
                return entry.getValue();
            }
        }

        return null;
    }
}
