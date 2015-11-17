import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.LinkedList;
import java.util.Arrays;

public class SAP {
    private Digraph digraph;
    private BFS search1;
    private BFS search2;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new java.lang.NullPointerException();
        digraph = new Digraph(G.V());
        for (int i = 0; i < digraph.V(); i++)
            for (int j : G.adj(i))
                digraph.addEdge(i, j);
        search1 = new BFS(digraph.V());
        search2 = new BFS(digraph.V());
    }

    private class BFS {
        private boolean[] marked;
        private int[] edgeTo;
        private int[] dist;
        // record the nodes that touched by last bfs so we
        // do not have to reset everything
        private LinkedList<Integer> lastVisitNodes;
        public BFS(int V) {
            marked = new boolean[V];
            edgeTo = new int[V];
            dist = new int[V];
            lastVisitNodes = new LinkedList<Integer>();
            resetAll();
        }

        // reset based on last search (using lastVisitNodes)
        private void resetLastVisit() {          
            while (!lastVisitNodes.isEmpty()) {
                int i = lastVisitNodes.poll();
                marked[i] = false;
                edgeTo[i] = -1;
                dist[i] = -1;
            }
        }
        // full reset
        private void resetAll() {
            for (int i = 0; i < marked.length; i++) {
                marked[i] = false;
                edgeTo[i] = -1;
                dist[i] = -1;
            }
        }

        public String toString() {
            return "marked: " + Arrays.toString(marked) + 
                   "\nedgeTo: " + Arrays.toString(edgeTo) + 
                   "\ndist: " + Arrays.toString(dist) + "\n";
        }
        // bread first search with one start node for a directed graph
        // this is a special case of group of start node
        /* commented as not used
        private void bfs(Digraph G, int start) {
            LinkedList<Integer> startGroup = new LinkedList<Integer>();
            startGroup.add(start);
            bfs(G, startGroup);
        }
        */

        // bread first search with a group of start nodes for a directed graph
        private void bfs(Digraph G, Iterable<Integer> start) {
            resetLastVisit();
            LinkedList<Integer> q = new LinkedList<Integer>();
            for (int i : start) {
                q.add(i);
                marked[i] = true;
                dist[i] = 0;
                lastVisitNodes.add(i);
            }
            while (!q.isEmpty()) {
                int v = q.poll();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        q.add(w);
                        marked[w] = true;
                        edgeTo[w] = v;
                        dist[w] = dist[v] + 1;
                        lastVisitNodes.add(w);
                    }
                }
            }
        }

        // breadth first search with start and group of end node for a directed graph
        // return the end note that got visited first
        // if cannot be reached return -1
        // this is a special case of start and end with a group of nodes
        /*
        private int bfs(Digraph G, int start, int end) {
            LinkedList<Integer> startGroup = new LinkedList<Integer>();
            startGroup.add(start);
            LinkedList<Integer> endGroup = new LinkedList<Integer>();
            endGroup.add(end);
            return bfs(G, startGroup, endGroup);
        }
        // breadth first search with group of start and end nodes for a directed graph
        // return the first node in end if reachable, otherwise return -1
        private int bfs(Digraph G, Iterable<Integer> start, Iterable<Integer> end) {
            resetLastVisit();
            LinkedList<Integer> q = new LinkedList<Integer>();
            // put end node in a hash table for fast check
            HashSet<Integer> endSet = new HashSet<Integer>();
            for (int i : end) endSet.add(i);
            
            // initialize
            for (int i : start) {
                q.add(i);
                marked[i] = true;
                dist[i] = 0;
                lastVisitNodes.add(i);
                if (endSet.contains(i)) return i;
            }

            while (!q.isEmpty()) {
                int v = q.poll();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        q.add(w);
                        marked[w] = true;
                        edgeTo[w] = v;
                        lastVisitNodes.add(w);
                        if (endSet.contains(w)) return w;
                    }
                }
            }
            return -1;
        }
        */
    }
    // length of shortest ancestral path between v and w; -1 if no such path
    // a special case of group of start and end nodes
    public int length(int v, int w) {
        LinkedList<Integer> startGroup = new LinkedList<Integer>();
        startGroup.add(v);
        LinkedList<Integer> endGroup = new LinkedList<Integer>();
        endGroup.add(w);
        return length(startGroup, endGroup);
    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    // this is a special case of group of start/end nodes
    public int ancestor(int v, int w) {
        LinkedList<Integer> startGroup = new LinkedList<Integer>();
        startGroup.add(v);
        LinkedList<Integer> endGroup = new LinkedList<Integer>();
        endGroup.add(w);
        return ancestor(startGroup, endGroup);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int commonAncestor = ancestor(v, w);
        if (commonAncestor == -1) return -1;
        int node = commonAncestor;
        int result = search1.dist[commonAncestor] + search2.dist[commonAncestor];
        /*
        while (search1.edgeTo[node] != -1) {
            node = search1.edgeTo[node];
            result++;
        }
        node = commonAncestor;
        while (search2.edgeTo[node] != -1) {
            node = search2.edgeTo[node];
            result++;
        }
        */
        return result;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // throw exceptions
        for (int i : v)
            if (i < 0 || i >= digraph.V()) throw new java.lang.IndexOutOfBoundsException();
        for (int i : w)
            if (i < 0 || i >= digraph.V()) throw new java.lang.IndexOutOfBoundsException();

        // first get all reachable nodes from start node
        search1.bfs(digraph, v);
        search2.bfs(digraph, w);
        int result = -1;
        int tmpDist = Integer.MAX_VALUE;

        for (int i : search1.lastVisitNodes) {
            if (search2.marked[i]) {
                if (search1.dist[i] + search2.dist[i] < tmpDist) {
                    tmpDist = search1.dist[i] + search2.dist[i];
                    result = i;
                }
            }
        }
        return result;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            StdOut.printf("v = %d, w = %d\n", v, w);
            int length   = sap.length(v, w);
            //int length = 1000;
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}