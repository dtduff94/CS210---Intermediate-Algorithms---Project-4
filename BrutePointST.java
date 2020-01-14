import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BrutePointST<Value> implements PointST<Value> {
    private RedBlackBST<Point2D, Value> BST;
    
    // Construct an empty symbol table of points.
    public BrutePointST() {
        BST = new RedBlackBST<Point2D, Value> ();
    }

    // Is the symbol table empty?
    public boolean isEmpty() { 
        return BST.size() == 0;
    }

    // Number of points in the symbol table.
    public int size() {
        return BST.size();
    }

    // Associate the value val with point p.
    public void put(Point2D p, Value val) {
        if (p == null) {
	    throw new IllegalArgumentException();
	}
	BST.put(p, val);
    }

    // Value associated with point p.
    public Value get(Point2D p) {
        if (p == null) {
	    throw new IllegalArgumentException();
	}
	return BST.get(p);
    }

    // Does the symbol table contain the point p?
    public boolean contains(Point2D p) {
        if (p == null) {
	    throw new IllegalArgumentException();
	}
	return BST.contains(p);
    }

    // All points in the symbol table.
    public Iterable<Point2D> points() {
        return BST.keys();
    }

    // All points in the symbol table that are inside the rectangle rect.
    public Iterable<Point2D> range(RectHV rect) {
	Iterable<Point2D> points = BST.keys();
	Queue<Point2D> in_rectangle = new Queue<Point2D>();
	for (Point2D key : points) {
	    if (rect.xmin() <= key.x() && key.x() <= rect.xmax() && rect.ymin() <= key.y() && key.y() <= rect.ymax()) {
		in_rectangle.enqueue(key);
	    }
	}
	return in_rectangle;
    }

    // A nearest neighbor to point p; null if the symbol table is empty.
    public Point2D nearest(Point2D p) {
        if (BST.isEmpty()) {
	    return null;
	}
	Point2D nearest = BST.min();
	double distance = p.distanceSquaredTo(nearest);
	Iterable<Point2D> points = BST.keys();
	for (Point2D key : points) {
	    if(p.distanceSquaredTo(key) < distance && p.compareTo(key) != 0) {
		nearest = key;
		distance = p.distanceSquaredTo(key);
	    }
	}
	return nearest;
    }

    // k points that are closest to point p.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        if (isEmpty()) {
	    throw new IllegalArgumentException();
	}
	MinPQ<Point2D> minPQ = new MinPQ<Point2D> (size(), p.distanceToOrder());
	for (Point2D i : BST.keys()) {
	    if (i.equals(p)) {
		continue;
	    }
	    minPQ.insert(i);
	}
	Queue<Point2D> closest = new Queue<Point2D>();
	for (int j = 0; j < k; j++) {
	    Point2D temp = minPQ.delMin();
	    closest.enqueue(temp);
	}
	return closest;
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
