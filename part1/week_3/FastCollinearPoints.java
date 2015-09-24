import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segmentList;
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // handle two corner cases: null points or any point is null
        if (points == null) throw new java.lang.NullPointerException("points null");
        for (Point p : points)
            if (p == null) throw new java.lang.NullPointerException("some null");
        // copy the points
        Point[] copyPoints = points.clone();
        Arrays.sort(copyPoints);
        // initialize the segmentList (has no elements)
        segmentList = new ArrayList<LineSegment>();
        // sort the points array first
        Arrays.sort(copyPoints);
        //System.out.println(Arrays.deepToString(points));
        // check if there is any repeated point
        for (int i = 0; i < copyPoints.length - 1; i++)
            if (copyPoints[i].slopeTo(copyPoints[i+1]) == Double.NEGATIVE_INFINITY)
                throw new java.lang.IllegalArgumentException("repeat points");
        if (copyPoints.length < 4) return;

        Point[] otherPoints = new Point[copyPoints.length - 1];        
        // loop through all points
        for (int i = 0; i < copyPoints.length; i++) {
            // copy everything but the target point
            // keep the order (important!)
            for (int j = 0; j < i; j++) otherPoints[j] = copyPoints[j];
            for (int j = i + 1; j < copyPoints.length; j++) otherPoints[j - 1] = copyPoints[j];

            Arrays.sort(otherPoints, copyPoints[i].slopeOrder());

            int startPos = 0;
            int endPos = 0;
            double currentSlope = copyPoints[i].slopeTo(otherPoints[startPos]);
            //System.out.println(currentSlope);
            while (endPos < otherPoints.length) {
                // if same slope
                if (copyPoints[i].slopeTo(otherPoints[endPos]) == currentSlope) {
                    endPos++;
                    // handle the boundary
                    if (endPos == otherPoints.length && endPos >= (startPos + 3)) {
                        addSegment(copyPoints[i], otherPoints[startPos], otherPoints[endPos - 1]);
                    }
                }
                // if slope changes
                else {
                    if (endPos >= startPos + 3)
                        // add a new segment
                        addSegment(copyPoints[i], otherPoints[startPos], otherPoints[endPos - 1]);
                    startPos = endPos;
                    currentSlope = copyPoints[i].slopeTo(otherPoints[startPos]);
                }
            }
        }

    }
    // helper function: add a line segment based on three points
    private void addSegment(Point p0, Point p1, Point p2) {
        if (p1.compareTo(p0) < 0) return;
        //System.out.println(p0.toString() + "--" + p1.toString() + "--" +p2.toString());
        segmentList.add(new LineSegment(p0, p2));
    }
    // the number of line segments
    public int numberOfSegments() {
        return segmentList.size();
    }
    // the line segments
    public LineSegment[] segments() {
        LineSegment [] s = new LineSegment[numberOfSegments()];
        Object [] a = segmentList.toArray();
        for (int i = 0; i < s.length; i++) s[i] = (LineSegment) a[i];
        return s;
    }
    public static void main(String[] args) {

        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points

        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}