import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.DirectedCycle;

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.LinkedList;
import edu.princeton.cs.algs4.Bag;


public class WordNet {
    // the array with synsets, i-th element is the synset (2nd field) of node i
    private String[] synsets;
    // given a word/noun, find the node ids (could be multiple)
    private TreeMap<String, Bag<Integer>> nounLookup;

    // Graph storing the relationship
    private Digraph graph;
    private SAP sap;

    /*
    // for testing
    public String toString() {
        StringBuilder s = new StringBuilder("");
        s.append("synsets:\n");
        for (int i = 0; i < synsets.length; i++)
            s.append(i + ": " + synsets[i] + "\n");
        s.append("directed graph:\n");
        for (int i = 0; i < graph.V(); i++) {
            s.append(i + "->");
            for (int neighbor: graph.adj(i))
                s.append(neighbor + " ");
            s.append("\n");
        }
        s.append("noun lookup:\n");
        for (String s1 : nounLookup.keySet()) {
            s.append(s1 + " :");
            for (int i : nounLookup.get(s1)) {
                s.append(i + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }
    public Digraph G() {
        return graph;
    }
    // testing over
    */

    // constructor takes the name of the two input files   
    public WordNet(String synsets, String hypernyms) {
        nounLookup = new TreeMap<String, Bag<Integer>>();
        // struct: (node, synset)
        class Synset {
            private int node;
            private String synset;
            Synset(int node, String synset) {
                this.node = node;
                this.synset = synset;
            }
            int node() {
                return this.node;
            }
            String synset() {
                return this.synset;
            }
        }
        ArrayList<Synset> a = new ArrayList<Synset>();
        int V = 0;
        // read in the synsets file
        In fileSynsets = new In(synsets);
        while (fileSynsets.hasNextLine()) {
            V++;
            String line = fileSynsets.readLine();
            String[] fields = line.split("\\,");
            int node = Integer.parseInt(fields[0]);
            a.add(new Synset(node, fields[1]));
            for (String noun : fields[1].split(" ")) {
                if (!nounLookup.containsKey(noun)) {
                    nounLookup.put(noun, new Bag<Integer>());
                }
                nounLookup.get(noun).add(node);
            }
        }
        fileSynsets.close();

        this.synsets = new String[a.size()];
        for (Synset syn : a) {
            this.synsets[syn.node()] = syn.synset();
        }
        graph = new Digraph(V);

        // read in the hypernyms file
        In fileHypernyms = new In(hypernyms);
        while (fileHypernyms.hasNextLine()) {
            String line = fileHypernyms.readLine();
            String[] fields = line.split("\\,");
            int fromNode = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int toNode = Integer.parseInt(fields[i]);
                graph.addEdge(fromNode, toNode);
            }
        }
        if (!isValid()) throw new java.lang.IllegalArgumentException();
        sap = new SAP(graph);
    }

    // helper function: find all nodes with zero out degree
    private LinkedList<Integer> findZeroOutdegree() {
        // count: number of nodes with 0 out degree
        LinkedList<Integer> result = new LinkedList<Integer>();
        for (int i = 0; i < graph.V(); i++) {
            if (graph.outdegree(i) == 0) result.add(i);
        }
        return result;
    }

    // helper function: isValid
    // check: 1. if there is only one node with zero outdgree (root)
    //        2. if there is any cycle
    private boolean isValid() {
        LinkedList<Integer> roots = findZeroOutdegree();
        if (roots.size() != 1) {
            System.out.println("more than one root");
            return false;
        }
        int root = roots.poll();
        // reverse the graph
        Digraph reverseGraph = new Digraph(graph.V());
        for (int i = 0; i < graph.V(); i++) {
            for (int j : graph.adj(i)) {
                reverseGraph.addEdge(j, i);
            }
        }
        // check if all nodes are marked
        DirectedDFS dfs = new DirectedDFS(reverseGraph, root);
        for (int i = 0; i < reverseGraph.V(); i++) {
            if (!dfs.marked(i)) return false;
        }
        // check if there is a cycle
        DirectedCycle cycle = new DirectedCycle(graph);
        if (cycle.hasCycle()) return false;
        return true;
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounLookup.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new java.lang.NullPointerException();
        return nounLookup.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new java.lang.NullPointerException();
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException();
        Bag<Integer> startNodeBag = nounLookup.get(nounA);
        Bag<Integer> endNodeBag = nounLookup.get(nounB);
        return sap.length(startNodeBag, endNodeBag);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new java.lang.NullPointerException();
        Bag<Integer> startNodeBag = nounLookup.get(nounA);
        Bag<Integer> endNodeBag = nounLookup.get(nounB);
        int commonAncestorNode = sap.ancestor(startNodeBag, endNodeBag);
        if (commonAncestorNode == -1) return null;
        return this.synsets[commonAncestorNode];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet a = new WordNet("wordnet/synsets100-subgraph.txt",
            "wordnet/hypernyms100-subgraph.txt");
        System.out.println(a.isValid());

        //System.out.println(a.distance("o", "e"));
        //System.out.println(a.sap("e", "o"));
    }
}
