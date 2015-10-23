import java.util.LinkedList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private class Node {
        private Point2D point;
        private RectHV rect;  // the retangle that point is in
        private boolean vertical;
        private Node left;
        private Node right;
        private int count;

        Node(Point2D p, RectHV hv, boolean v, Node l, Node r, int c) {
            point = p;
            rect = hv;
            vertical = v;
            left = l;
            right = r;
            count = c;
        }

        Node(Point2D p, RectHV hv, boolean v) {
            this(p, hv, v, null, null, 1);
        }
        // default for the root node
        Node(Point2D p) {
            this(p, new RectHV(0.0, 0.0, 1.0, 1.0), true, null, null, 1);
        }
        public RectHV getRect() {
            return rect;
        }
        // left or bottom rectangle, depending on orientation
        public RectHV getLeftRect() {
            if (vertical) return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            else return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
        }
        public RectHV getRightRect() {
            if (vertical) return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            else return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
        }
        public int compareTo(Point2D p) {
            // -1 if p is on the left/bottom side of node (depending on orientation)
            if (p.equals(point)) return 0;
            if (vertical) {
                if (p.x() < point.x()) return -1;
                else return 1;
            }
            else {
                if (p.y() < point.y()) return -1;
                else return 1;
            }
        }
    }

    private Node root;
    // construct an empty set / tree of points
    public KdTree() {
        root = null;
    }
    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }
    // number of points in node x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.count;
    }
    // number of points in the set
    public int size() {
        if (root == null) return 0;
        return root.count;
    }
    // add the point to Node x, which is in rectangle hv
    // with orientation v (true if vertical), and return the node
    private void insert(Point2D p, Node x) {
        // root (null) for empty tree
        if (x == null) {
            Node newNode = new Node(p);
            root = newNode;
            return;
        }
        int compare = x.compareTo(p);
        // if the point exists, do nothing
        if (compare == 0) return;
        // if goes to the left
        if (compare == -1) {
            if (x.left == null) {
                Node newNode = new Node(p, x.getLeftRect(), !x.vertical, null, null, 1);
                x.left = newNode;
            }
            else insert(p, x.left);
        }
        // goes to the right
        else {
            if (x.right == null) {
                Node newNode = new Node(p, x.getRightRect(), !x.vertical, null, null, 1);
                x.right = newNode;
            }
            else insert(p, x.right);
        }
        x.count = size(x.left) + size(x.right) + 1;
        return;
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException("null point");
        insert(p, root);
    }
    // does the subtree from x contains p?
    private boolean contains(Point2D p, Node x) {
        if (x == null) return false;
        if (p.equals(x.point)) return true;
        if (x.vertical && p.x() < x.point.x()) return contains(p, x.left);
        if (!x.vertical && p.y() < x.point.y()) return contains(p, x.left);
        return contains(p, x.right);
    }
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException("null point");
        return contains(p, root);
    }
    // draw all points of subtree x in retangle rect
    private void draw(Node x, RectHV rect) {
        if (x == null) return;
        if (x.vertical) {
            // draw left
            draw(x.left, new RectHV(rect.xmin(), rect.ymin(), x.point.x(), rect.ymax()));
            // draw point and line
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            x.point.draw();
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(x.point.x(), rect.ymin(), x.point.x(), rect.ymax());
            // draw right
            draw(x.right, new RectHV(x.point.x(), rect.ymin(), rect.xmax(), rect.ymax()));
        }
        else {
            // draw left
            draw(x.left, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.point.y()));
            // draw point and line
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            x.point.draw();
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(rect.xmin(), x.point.y(), rect.xmax(), x.point.y());
            // draw right
            draw(x.right, new RectHV(rect.xmin(), x.point.y(), rect.xmax(), rect.ymax()));
        }
    }
    // draw all points to standard draw 
    public void draw() {
        draw(root, new RectHV(0.0, 0.0, 1.0, 1.0));
    }
    // help function for range, insert all nodes in a range to a linked list
    private void insertRange(RectHV rect, Node x, LinkedList<Point2D> list) {
        if (x == null) return;
        if (rect.contains(x.point)) list.add(x.point);
        boolean checkLeft = false;
        boolean checkRight = false;
        if (x.vertical && rect.xmin() <= x.point.x()) checkLeft = true;
        if (x.vertical && rect.xmax() >= x.point.x()) checkRight = true;
        if (!x.vertical && rect.ymin() <= x.point.y()) checkLeft = true;
        if (!x.vertical && rect.ymax() >= x.point.y()) checkRight = true;
        if (checkLeft) insertRange(rect, x.left, list);
        if (checkRight) insertRange(rect, x.right, list);
    }
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.NullPointerException("null rect");
        LinkedList<Point2D> result = new LinkedList<Point2D>();
        insertRange(rect, root, result);
        return result;
    }
    // a nearest neighbor in the subtree x (in rect) to point p; null if the set is empty
    private Node nearest(Point2D p, Node x) {
        if (x == null) return null;
        if (x.left == null && x.right == null) return x;
        int compare = x.compareTo(p);

        if (compare == 0) return x;
        double distSquare = p.distanceSquaredTo(x.point);
        Node result = x;

        Node firstSearch = null;
        Node secondSearch = null;

        if (compare == -1) {
            firstSearch = x.left;
            secondSearch = x.right;
        }
        else {
            firstSearch = x.right;
            secondSearch = x.left;
        }
 
        if (firstSearch != null) {
            Node firstNearest = nearest(p, firstSearch);
            double distFirstSquare = p.distanceSquaredTo(firstNearest.point);
            if (distFirstSquare < distSquare) {
                result = firstNearest;
                distSquare = distFirstSquare;
            }
        }

        if (secondSearch != null) {
            if (distSquare > secondSearch.rect.distanceSquaredTo(p)) {
                Node secondNearest = nearest(p, secondSearch);
                double distSecondSquare = p.distanceSquaredTo(secondNearest.point);
                if (distSecondSquare < distSquare) {
                    result = secondNearest;
                    // no need to store distSquare anymore
                    // distSquare = distSecondSquare;
                }
            }
        }
        return result;
    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException("null point");
        Node nearestNode = nearest(p, root);
        if (nearestNode == null) return null;
        else return nearestNode.point;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree a = new KdTree();
        a.insert(new Point2D(0.7, 0.2));
        a.insert(new Point2D(0.5, 0.4));
        a.insert(new Point2D(0.2, 0.3));
        a.insert(new Point2D(0.4, 0.7));
        a.insert(new Point2D(0.9, 0.6));
        a.draw();
    }
}