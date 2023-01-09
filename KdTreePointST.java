public class KdTreePointST<Value> implements PointST<Value> {
    private Node root;
    //RedBlackBST<Point2D> bst;
    private int N;
    private boolean lr;
    private double nearestDis;
    private Point2D near;
    
    // 2d-tree (generalization of a BST in 2d) representation.
    private class Node {
        private Point2D p;   // the point
        private Value val;   // the symbol table maps the point to this value
        private RectHV rect; // the axis-aligned rectangle corresponding to 
                             // this node
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top subtree

        // Construct a node given the point, the associated value, and the 
        // axis-aligned rectangle corresponding to the node.
        Node(Point2D p, Value val, RectHV rect) {
            this.p = p;
            this.val = val;
            this.rect = rect;
        }
    }

    // Construct an empty symbol table of points.
    public KdTreePointST() {
        //bst = new RedBlackBST<Point2D>();
        N = 0;
        root = new Node(null, null, null);
        lr = false;
        near = null;
        nearestDis = Double.MAX_VALUE;
    }

    // Is the symbol table empty?
    public boolean isEmpty() { 
        return (N == 0) ? true : false;
    }

    // Number of points in the symbol table.
    public int size() {
        return N;
    }

    // Associate the value val with point p.
    public void put(Point2D p, Value val) {
        lr = false;
        RectHV rect = null;
        if (N == 0) { 
            rect = new RectHV(Double.MIN_VALUE, Double.MIN_VALUE,
                 Double.MAX_VALUE, Double.MAX_VALUE);
           // StdOut.println(rect.toString());
            root = new Node(p, val, rect); 
        }    
        else { put(root, p, val, rect, !lr); }
        N++;
    }

    // Helper for put(Point2D p, Value val).
    private Node put(Node x, Point2D p, Value val, RectHV rect, boolean lr) {
        //StdOut.println(1);
        if (x == null) { 
            x = new Node(p, val, rect);
            //StdOut.println(;
        }
        else if (x.p.equals(p)) { x.val = val; }
        else {
            if (lr) {
                if (p.x() < x.p.x()) { 
                    if (x.lb == null) {
                        
                        RectHV xrect = new RectHV(x.rect.xmin(), x.rect.ymin(),
                        x.p.x(), x.rect.ymax());
                        x.lb = put(x.lb, p, val, xrect, !lr); 
                    }
                    else { put(x.lb, p, val, rect, !lr); }
                }
                else { 
                    if (x.rt == null) {
                        
                        RectHV xrect = new RectHV(x.p.x(), x.rect.ymin(), 
                                        x.rect.xmax(), x.rect.ymax());
                        x.rt = put(x.rt, p, val, xrect, !lr); 
                    }
                    else { put(x.rt, p, val, rect, !lr); }
                }
            }
            else {
                if (p.y() < x.p.y()) { 
                    if (x.lb == null) {
                         RectHV xrect = new RectHV(x.rect.xmin(), 
                         x.rect.ymin(), x.rect.xmax(), x.p.y());
                        x.lb = put(x.lb, p, val, xrect, !lr); 
                    }
                    else { put(x.lb, p, val, rect, !lr); }
                }
                else { 
                    if (x.rt == null) {
                        RectHV xrect = new RectHV(x.rect.xmin(), x.p.y(),
                         x.rect.xmax(), x.rect.ymax());
                        x.rt = put(x.rt, p, val, xrect, !lr);
                    }
                    else { put(x.rt, p, val, rect, !lr); }
                }
            }
        }
        return x;
    }

    // Value associated with point p.
    public Value get(Point2D p) {
        //StdOut.println(lr);
        lr = true;
        Value val = get(root, p, lr);
        //StdOut.println(val);
        return val;
    }

    // Helper for get(Point2D p).
    private Value get(Node x, Point2D p, boolean lr) {
       Value newVal = null; 
      // StdOut.println(1);
       if (x == null) { return null; }
       else if (x.p.x() == p.x() && x.p.y() == p.y()) { 
            //StdOut.println(x.p.toString());
            return x.val; 
            }
       else if (lr) {
            if (p.x() < x.p.x()) { return get(x.lb, p, !lr); }
       //StdOut.println(x.lb.p.toString()); 
        }
        else { 
            if (p.y() < x.p.y()) { return get(x.lb, p, !lr); }
        }
        //StdOut.println(x.p.toString());
        //return newVal;
        return get(x.rt, p, !lr); 
    }

    // Does the symbol table contain the point p?
    public boolean contains(Point2D p) {
        //boolean lr = true;
        return (get(p) == null) ? false : true;
    } 

    // All points in the symbol table, in level order.
    public Iterable<Point2D> points() {
        Queue<Node> q = new Queue<Node>();
        Queue<Point2D> result = new Queue<Point2D>();
        q.enqueue(root);
        //result.enqueue(root.p);
        Node hold;
        while (!q.isEmpty()) {
            hold = q.dequeue();
            if (hold.lb != null) { q.enqueue(hold.lb); }
            if (hold.rt != null) { q.enqueue(hold.rt); }
            result.enqueue(hold.p);
        }
        return result;
    }

    // All points in the symbol table that are inside the rectangle rect.
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, rect, q);
        return q;
    }

    // Helper for public range(RectHV rect).
    private void range(Node x, RectHV rect, Queue<Point2D> q) {
        if (x == null) { return; }
        if (rect.contains(x.p)) { 
            q.enqueue(x.p); 
        }
        if (rect.intersects(x.rect)) {
            range(x.lb, rect, q);
            range(x.rt, rect, q);
        }
       // range(null,rect,q);
    }

    // A nearest neighbor to point p; null if the symbol table is empty.
    public Point2D nearest(Point2D p) {
       double nearestDistance = Double.MAX_VALUE;
        Point2D nearest = null;
        lr = true;
        return nearest(root, p, nearest, nearestDistance, lr);
        
    }
    
    // Helper for public nearest(Point2D p).
    private Point2D nearest(Node x, Point2D p, Point2D nearest, 
                            double nearestDistance, boolean lr) {
         if (x == null) { return near; }
        else {
            if (!x.p.equals(p)) {
                    double cal = p.distanceSquaredTo(x.p);
                    if (cal < nearestDis) {
                        nearestDis = cal;
                        //StdOut.println(x.p);
                        near = new Point2D(x.p.x(), x.p.y());
                        //StdOut.println(nearestDis);
                        
                    }
            }           
        }
        //StdOut.println(x.p);
        if (lr) {
            if (p.x() < x.p.y()) {
                nearest(x.lb, p, nearest, nearestDistance, !lr);
                nearest(x.rt, p, nearest, nearestDistance, !lr);
            }
            else {
                nearest = nearest(x.rt, p, nearest, nearestDistance, !lr);
                nearest = nearest(x.lb, p, nearest, nearestDistance, !lr);
            
            }
            
        }
        else {
            if (p.y() < x.p.y()) {
                nearest = nearest(x.lb, p, nearest, nearestDistance, !lr);
                nearest = nearest(x.rt, p, nearest, nearestDistance, !lr);
            }
            else {
                nearest = nearest(x.rt, p, nearest, nearestDistance, !lr);
                nearest = nearest(x.lb, p, nearest, nearestDistance, !lr);
            
            }
            
        
        }
        return nearest(null, p, nearest, nearestDistance, !lr); 
    }

    // k points that are closest to point p.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        MaxPQ<Point2D> pq = new MaxPQ<Point2D>(p.distanceToOrder());
        //lr = true;
        nearest(root, p, k, pq, lr);
        return pq;
    }

    // Helper for public nearest(Point2D p, int k).
    private void nearest(Node x, Point2D p, int k, MaxPQ<Point2D> pq, 
                         boolean lr) {
        //if (x == null) { throw new NullPointerException(); }
        if (x == null || pq.size() > k) { return; }
        else if (!x.p.equals(p)) { pq.insert(x.p); }
        if (pq.size() > k) { pq.delMax(); }                     
        Point2D hold = pq.max();
        double distance = hold.distanceTo(p);
        if (distance > x.rect.distanceTo(p)) { 
            nearest(x.lb, p, k, pq, !lr);
            nearest(x.rt, p, k, pq, !lr);
        }
        
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        KdTreePointST<Integer> st = new KdTreePointST<Integer>();
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
            //StdOut.println(i);
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
        /*StdOut.println("Level Order :");
        for (Point2D p : st.points()) {
            StdOut.println(p);
        }*/
    }
}
