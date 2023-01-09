public class ArrayST<Key, Value> {
    private static final int INIT_CAPACITY = 2;
    private Key[] keys;
    private Value[] values;
    private int N;

    // Create a symbol table with INIT_CAPACITY.
    public ArrayST() {
        keys = (Key[]) new Object[INIT_CAPACITY];
        values = (Value[]) new Object[INIT_CAPACITY];
        N = 0;
    }

    // Create a symbol table with given capacity.
    public ArrayST(int capacity) {
        keys = (Key[]) new Object[capacity];
        values = (Value[]) new Object[capacity];
        N = 0;
    }

    // Return the number of key-value pairs in the table.
    public int size() {
        return N;
    }

    // Return true if the table is empty and false otherwise.
    public boolean isEmpty() {
        return (N == 0) ? true : false;
    }

    // Return true if the table contains key and false otherwise.
    public boolean contains(Key key) {
        return (get(key) == null) ? false : true;
    }

    // Return the value associated with key, or null.
    public Value get(Key key) {
        Value val = null;
        for (int a = 0; a < size(); a++) {
            if (keys[a].equals(key)) {
                val = values[a];
            }
        }
        return val;
    }

    // Put the key-value pair into the table; remove key from table 
    // if value is null.
    public void put(Key key, Value value) {
        if (N == keys.length) {
            resize(N*2);
            //resize(values[]);
        }
        if (value != null) { 
            keys[N] = key;
            values[N] = value;
            N++;
        }
        else { delete(key); }
    }

    // Remove key (and its value) from table.
    public void delete(Key key) {
        int hold = 0;
        for (int a = 0; a < N; a++) {
            if (keys[a].equals(key)) { hold = a; }
        }
        for (int b = hold; b < N; b++) { 
            keys[b] = keys[b+1];
            values[b] = values[b+1];
        }
        resize(N);
        N--;
    }

    // Return all the keys in the table.
    public Iterable<Key> keys()  {
        Queue<Key> q = new Queue<Key>();
        for (int a = 0; a < size(); a++) {
            q.enqueue(keys[a]);
        }
        return q;
    }

    // Resize the internal arrays to capacity.
    private void resize(int capacity) {
        Key[] tempk = (Key[]) new Object[capacity];
        Value[] tempv = (Value[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            tempk[i] = keys[i];
            tempv[i] = values[i];
        }
        values = tempv;
        keys = tempk;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        ArrayST<String, Integer> st = new ArrayST<String, Integer>();
        int count = 0;
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            st.put(s, ++count);
        }
        for (String s : args) {
            st.delete(s);
        }
        for (String s : st.keys()) {
            StdOut.println(s + " " + st.get(s));
        }
    }
}
