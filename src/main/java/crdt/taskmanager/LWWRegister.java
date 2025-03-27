package crdt.taskmanager;

import java.io.Serializable;

public class LWWRegister implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private S4Vector value;

    public LWWRegister(S4Vector initialValue) {
        value = initialValue;
    }

    public S4Vector getValue() {
        return value;
    }

    public boolean update(S4Vector newValue) {
        if (value.precedes(newValue)) {
            value = newValue;
            return true;
        }

        return false;
    }
}
