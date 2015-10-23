/*
Brute-force implementation. Write a mutable data type PointSET.java that 
represents a set of points in the unit square. Implement the following API 
by using a red-black BST (using either SET from algs4.jar or java.util.TreeSet).
*/

import java.util.TreeSet;
import java.util.LinkedList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private TreeSet<Point2D> points;
    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<Point2D>();
    }
    // is the set empty?
    public boolean isEmpty() {
        return points.size() == 0;
    }
    // number of points in the set
    public int size() {
        return points.size();
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        points.add(p);
    }
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        return points.contains(p);
    }
    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points)
            p.draw();
    }
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        LinkedList<Point2D> pointsInRect = new LinkedList<Point2D>();
        for (Point2D p : points)
            if (rect.contains(p)) pointsInRect.add(p);
        return pointsInRect;
    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (points.size() == 0) return null;
        double maxDist = Double.MAX_VALUE;
        Point2D result = null;
        for (Point2D a : points)
            if (p.distanceSquaredTo(a) < maxDist) {
                result = a;
                maxDist = p.distanceSquaredTo(a);
            }
        return result;
    }
    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}