public class BrutePointST<Value> implements PointST<Value> {
    private RedBlackBST<Point2D, Value> bst;
    private int N;

    // Construct an empty symbol table of points.
    public BrutePointST() {
        bst = new RedBlackBST<Point2D, Value>();
    }

    // Is the symbol table empty?
    public boolean isEmpty() { 
        return bst.isEmpty();
    }

    // Number of points in the symbol table.
    public int size() {
        return bst.size();
    }

    // Associate the value val with point p.
    public void put(Point2D p, Value val) {
        bst.put(p, val);
    }

    // Value associated with point p.
    public Value get(Point2D p) {
        return bst.get(p);
    }

    // Does the symbol table contain the point p?
    public boolean contains(Point2D p) {
        return bst.contains(p);
    }

    // All points in the symbol table.
    public Iterable<Point2D> points() {
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D point : bst.keys()) {
            queue.enqueue(point);
        }
        return queue;
    }

    // All points in the symbol table that are inside the rectangle rect.
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D point : bst.keys()) {
            if (rect.contains(point)) {
                queue.enqueue(point);
            }
        }
        return queue;
    }

    // A nearest neighbor to point p; null if the symbol table is empty.
    public Point2D nearest(Point2D p) {
        double min = Double.MAX_VALUE;
        double cal;
        Point2D minP = p;
        for (Point2D point : bst.keys()) {
            if (point.compareTo(p) != 0) {
                cal = point.distanceTo(p); 
                if (cal < min) {
                    minP = point;
                    min = cal;
                }
            }
        }
        return minP;
    }

    // k points that are closest to point p.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        Point2D[] points2D = new Point2D[size()];
        Queue<Point2D> queue = new Queue<Point2D>();
        int a = 0;
        double exchHold;
        //Comparator<Point2D> c = new DistanceToOrder();
        double[] hold = new double[size()];
        //double[] tempHold = new double[size()];
        //int[] temp = new int[size()];
        Point2D exch;
        Point2D[] pt = new Point2D[size()];
        for (Point2D point : bst.keys()) {
            if (point.distanceTo(p) != 0) {
                pt[a] = point;
                hold[a] = point.distanceTo(p);
                a++; 
            }
        }
        //Arrays.sort(hold,new Point2D.DistanceToOrder());
        for (int b = 0; b < size()-1; b++) {
            for (int c = b; c < size()-1; c++) {
                if (hold[c] < hold[b]) {
                    exch = pt[b];
                    pt[b] = pt[c];
                    pt[c] = exch;
                    exchHold = hold[b];
                    hold[b] = hold[c];
                    hold[c] = exchHold;    
                } 
            }
        }
        for (int i = 0; i < k; i++) {
            queue.enqueue(pt[i]);
        }
        return queue;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        BrutePointST<Integer> st = new BrutePointST<Integer>();
        double qx = Double.parseDouble(args[0]);
        double qy = Double.parseDouble(args[1]);
        double rx1 = Double.parseDouble(args[2]);
        double rx2 = Double.parseDouble(args[3]);
        double ry1 = Double.parseDouble(args[4]);
        double ry2 = Double.parseDouble(args[5]);
        int k = Integer.parseInt(args[6]);
        Point2D query = new Point2D(qx, qy);
        RectHV rect = new RectHV(rx1, ry1, rx2, ry2);
        int i = 0;
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            st.put(p, i++);
        }
        StdOut.println("st.empty()? " + st.isEmpty());
        StdOut.println("st.size() = " + st.size());
        StdOut.println("First " + k + " values:");
        i = 0;
        for (Point2D p : st.points()) {
            StdOut.println("  " + st.get(p));
            if (i++ == k) {
                break;
            }
        }
        StdOut.println("st.contains(" + query + ")? " + st.contains(query));
        StdOut.println("st.range(" + rect + "):");
        for (Point2D p : st.range(rect)) {
            StdOut.println("  " + p);
        }
        StdOut.println("st.nearest(" + query + ") = " + st.nearest(query));
        StdOut.println("st.nearest(" + query + ", " + k + "):");
        for (Point2D p : st.nearest(query, k)) {
            StdOut.println("  " + p);
        }
    }
}
