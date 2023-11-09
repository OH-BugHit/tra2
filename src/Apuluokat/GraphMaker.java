package Apuluokat;

import fi.uef.cs.tra.*;

import java.util.*;

/**
  * Helper class to create diffent {@link fi.uef.cs.tra.Graph}s and {@link fi.uef.cs.tra.DiGraph}s
  */
public class GraphMaker {


    /**
     * Internal Random.
     * Not used yet in all methods.
     */
    private static Random R = null;

    private static Random getR(int seed) {
        if (R == null)
            R = new Random(seed);
        else
            R.setSeed(seed);
        return R;
    }

    private static Random getR() {
        if (R == null)
            R = new Random(0);
        return R;
    }

    /**
     * Sets new seed for internal Random.
     * @param seed new seed
     */
    public static void setSeed(int seed) {
        getR(seed);
    }

    /**
      * Creates random {@link fi.uef.cs.tra.DiGraph}
      * with given number of vertices and edges
      * @param vertices number of vertices to generate
      * @param edges number of edges to generate
      * @return new random graph
      */
    public static DiGraph createDiGraph(int vertices, int edges) {
        return (DiGraph) createGraph(true, vertices, edges);
    }

    /**
      * Creates random {@link fi.uef.cs.tra.DiGraph}
      * with given number of vertices and edges, with seed
      * @param vertices number of vertices to generate
      * @param edges number of edges to generate
      * @param seed initialization of random number generator
      * @return new random graph
      */
    public static DiGraph createDiGraph(int vertices, int edges, int seed) {
        return (DiGraph) createGraph(true, vertices, edges, seed);
    }

    /**
      * Creates random {@link fi.uef.cs.tra.DiGraph}
      * with given number of vertices and edges, with seed
      * with each edge weight between 0 and maxW
      * @param vertices number of vertices to generate
      * @param edges number of edges to generate
      * @param seed initialization of random number generator
      * @param maxW maximum edge weigth
      * @return new random graph
      */
    public static DiGraph createDiGraph(int vertices, int edges, int seed, float maxW) {
        DiGraph G = (DiGraph) createGraph(true, vertices, edges, seed);
        setWeights(G, maxW, (float)0.1, seed);
        return G;
    }

    /**
      * Creates random {@link fi.uef.cs.tra.Graph}
      * with given number of vertices and edges, with seed
      * with each edge weight between 0 and maxW
      * @param vertices number of vertices to generate
      * @param edges number of edges to generate
      * @param seed initialization of random number generator
      * @param maxW maximum edge weigth
      * @return new random graph
      */
    public static Graph createGraph(int vertices, int edges, int seed, float maxW) {
        Graph G = (Graph) createGraph(false, vertices, edges, seed);
        setWeights(G, maxW, (float)0.1, seed);
        return G;
    }

    /**
      * Assigns a random weight for each {@link fi.uef.cs.tra.Edge}
      * of a Graph G
      * with each edge weight between step and maxW in steps of step
      */
    public static void setWeights(AbstractGraph G, float maxW, float step, int seed) {
        Random rnd = getR(seed);
        for (Edge e : G.edges()) {
            e.setWeight(randomWeight(rnd, maxW, step));
        }
    }

    private static float randomWeight(Random rnd, float maxW, float step) {
        if (step < (float)0.001)
            step = (float)0.001;
        float mult = (float)1.0/step;
        int multI = (int)(mult*maxW);
        if (multI < 0)
            multI = Integer.MAX_VALUE;
        return (rnd.nextInt(multI)+1)/mult;
    }

    /**
      * Creates random {@link fi.uef.cs.tra.Graph}
      * with given number of vertices and edges
      */
    public static Graph createGraph(int vertices, int edges) {
        return (Graph) createGraph(false, vertices, edges);
    }

    /**
      * Creates random {@link fi.uef.cs.tra.Graph}
      * with given number of vertices and edges with seed
      */
    public static Graph createGraph(int vertices, int edges, int seed) {
        return (Graph) createGraph(false, vertices, edges, seed);
    }

    /**
      * Creates random {@link fi.uef.cs.tra.Graph}
      * with given number of vertices and edges with a cycle
      */
    public static Graph createCyclicGraph(int vertices, int edges) {
        if (edges < vertices)
            edges = vertices;
        Graph G = (Graph)createGraph(false, vertices, edges-vertices);
        addRandomCycle(G, false);
        return G;
    }

    /**
      * Creates random {@link fi.uef.cs.tra.DiGraph}
      * with given number of vertices and edges, with a cycle
      */
    public static DiGraph createCyclicDiGraph(int vertices, int edges) {
        if (edges < vertices)
            edges = vertices;
        DiGraph G = (DiGraph)createGraph(true, vertices, edges-vertices);
        addRandomCycle(G, true);
        return G;
    }

    private static AbstractGraph createGraph(boolean isDigraph,
                                             int vertices, int edges) {
        return createGraph(isDigraph, vertices, edges,
                           (int)(Math.random()*(vertices*edges + edges)));
    }

    private static AbstractGraph createGraph(boolean isDigraph,
                                             int vertices, int edges, int seed) {

        AbstractGraph graph = (isDigraph ? new DiGraph() : new Graph());
        Vertex[] vertexArray = new Vertex[vertices];

        for (int i = 0; i < vertices; i++)
            vertexArray[i] = graph.addVertex("" + i);

        int idx1, idx2;

        Random rnd = new Random(seed);

        int cutoff = edges*edges*10;
        while (edges > 0 && cutoff-- > 0) {
            idx1 = rnd.nextInt(vertices);
            idx2 = rnd.nextInt(vertices);
            if ((idx1 != idx2) && !vertexArray[idx1].isAdjacent(vertexArray[idx2])) {
                vertexArray[idx1].addEdge(vertexArray[idx2], // "");
                                          new String(new char[] {(char) (65 + edges)}));
                edges--;
            }
        }
        return graph;
    }


    /**
      * Creates complete {@link fi.uef.cs.tra.DiGraph}
      * with given number of vertices
      */
    public static DiGraph createCompleteDiGraph(int vertices) {
        return (DiGraph) createCompleteGraph(true, vertices);
    }

    /**
      * Creates complete {@link fi.uef.cs.tra.Graph}
      * with given number of vertices
      */
    public static Graph createCompleteGraph(int vertices) {
        return (Graph) createCompleteGraph(false, vertices);
    }


    private static AbstractGraph createCompleteGraph(boolean isDigraph,
                                                     int vertices) {
        AbstractGraph graph = (isDigraph ? new DiGraph() : new Graph());
        Vertex[] vertexArray = new Vertex[vertices];

        for (int i = 0; i < vertices; i++)
            vertexArray[i] = graph.addVertex("" + i);

        for (int i = 0; i < vertices; i++)
            for (int j = isDigraph ? 0 : i+1; j < vertices; j++)
                if (i != j)
                    vertexArray[i].addEdge(vertexArray[j]);
        return graph;
    }


    /**
      * Creates random Directed Acyclic Graph (DAG) ({@link fi.uef.cs.tra.DiGraph})
      * with given number of vertices and edges, no cycle, seed
      */
    public static DiGraph createDAG(int vertices, int edges, int seed) {

        DiGraph G = new DiGraph();
        Vertex[] vA = new Vertex[vertices];

        for (int i = 0; i < vertices; i++)
            vA[i] = G.addVertex("" + i);

        int idx1, idx2;

        Random rnd = new Random(seed);
        int[] p = randomPermutation(vertices, seed*3);

        int cutoff = edges*edges*10;
        while (edges > 0 && cutoff-- > 0) {
            idx1 = rnd.nextInt(vertices-1);
            idx2 = rnd.nextInt(vertices-idx1-1) + idx1 + 1;
            if (!vA[p[idx1]].isAdjacent(vA[p[idx2]])) {
                vA[p[idx1]].addEdge(vA[p[idx2]],
                                    new String("" + p[idx1] + "-" + p[idx2]));
                edges--;
            }

        }
        return G;
    }


    /**
      * Creates random forest (one or more trees)
      * with given number of vertices and edges, no cycle, seed
      * weighted edges, maxweight
      * no weights if maxweight == 0
      */
    public static DiGraph createForest(int vertices, int edges, int seed, int maxweight) {

        DiGraph G = new DiGraph();
        Vertex[] vA = new Vertex[vertices];
        boolean[] isChild = new boolean[vertices];

        if (edges >= vertices)
            edges = vertices-1;

        for (int i = 0; i < vertices; i++) {
            vA[i] = G.addVertex("" + i);
            isChild[i] = false;
        }

        int idx1, idx2;

        Random rnd = new Random(seed);
        int[] p = randomPermutation(vertices, seed*3);

        int cutoff = edges*edges*10;
        while (edges > 0 && cutoff-- > 0) {
            idx1 = rnd.nextInt(vertices-1);
            idx2 = rnd.nextInt(vertices-idx1-1) + idx1 + 1;
            if (idx1 != idx2 && !isChild[p[idx2]]) {
                if (maxweight == 0) 
                    vA[p[idx1]].addEdge(vA[p[idx2]],
                                        new String("" + p[idx1] + "-" + p[idx2]));
                else
                    vA[p[idx1]].addEdge(vA[p[idx2]],
                                        new String("" + p[idx1] + "-" + p[idx2]),
                                        0,
                                        rnd.nextInt(maxweight)+1);
                edges--;
                isChild[p[idx2]] = true;
            }

        }
        return G;

    }

    /**
      * Creates random forest (one or more trees)
      * with given number of vertices and edges, no cycle, seed
      * no weigths
      */
    public static DiGraph createForest(int vertices, int edges, int seed) {
        return createForest(vertices, edges, seed, 0);
    }

    


    /**
      * Creates example graph for traII_2014_X2
      * graph with 
      */
    public static Graph createFlora(int seeds, int stems, int trees, int mutants, int rseed) {

        if (seeds < 0) seeds = 0;
        if (stems < 0) stems = 0;
        if (trees < 0) trees = 0; 
        if (mutants < 0) mutants = 0; 

        int c = seeds + stems + trees + mutants;
        Random rnd = new Random(rseed + c);

        Graph G = new Graph();
        Vertex[] vA = new Vertex[c];

        int[] p = randomPermutation(c, rseed+1);

        for (int i = 0; i < c; i++)
            vA[i] = G.addVertex("" + i);

        for (int i = seeds; i < seeds + stems; i++)
            growStem(G, vA[p[i]], rnd);

        if (trees > 0)
            growSmallTree(G, vA[p[seeds + stems]], rnd);

        for (int i = seeds + stems + 1; i < seeds + stems + trees; i++)
            growTree(G, vA[p[i]], rnd);

        for (int i = seeds + stems + trees; i < c; i++)
            makeMutant(G, vA[p[i]], rnd);

        return G;
    }

    private static void growStem(Graph G, Vertex v, Random r) {
        int b = r.nextInt(7)+1;
        for (int i = 0; i < b; i++) {
            Vertex br = G.addVertex(v.getLabel() + i);
            br.addEdge(v, "E" + br.getLabel() + v.getLabel());
        }
    }

    private static void growSmallTree(Graph G, Vertex v, Random r) {
        Vertex n = G.addVertex(v.getLabel() + "A");
        v.addEdge(n, "E" + v.getLabel() + n.getLabel());
        n = G.addVertex(v.getLabel() + "B");
        v.addEdge(n, "E" + v.getLabel() + n.getLabel());
        v = n;
        n = G.addVertex(v.getLabel() + "C");
        v.addEdge(n, "E" + v.getLabel() + n.getLabel());
    }

    private static void growTree(AbstractGraph G, Vertex v, Random r) {
        growTree(G, v, r, r.nextInt(10) + 5);
    }

    /**
     * Add chidren for vertex v.
     * @param G graph to be modified
     * @param v root vertex
     * @param r random source
     * @param b number of vertices to add
     */
    public static void growTree(AbstractGraph G, Vertex v, Random r, int b) {
        Vertex prev = v;
        for (int i = 0; i < b; i++) {
            Vertex br = G.addVertex(v.getLabel() + i);
            prev.addEdge(br, "E" + prev.getLabel() + br.getLabel());
            if ((i % 2) == 0 || r.nextBoolean())
                prev = br;
        }
    }

    private static void makeMutant(AbstractGraph G, Vertex v, Random r) {
        makeMutant(G, v, r, r.nextInt(10)+5);
    }


    private static void makeMutant(AbstractGraph G, Vertex v, Random r, int b) {
        Vertex prev = v;
        Vertex mut = null;
        boolean mutok = false;
        for (int i = 0; i < b; i++) {
            Vertex br = G.addVertex(v.getLabel() + i);
            br.addEdge(prev, "E" + br.getLabel() + prev.getLabel());
            if ((i % 2) == 0 || r.nextBoolean())
                prev = br;
            if (r.nextInt(3) == 0) {
                if (mut == null)
                    mut = br;
                else if (! br.isAdjacent(mut)) {
                    br.addEdge(mut, "E" + br.getLabel() + mut.getLabel());
                    mutok = true;
                }
            }
        }
        if (! mutok)
            addCycle(prev);
    }

    /**
     * Grow component of sole vertex v to a vn+1 -vertex component that is not a valid tree.
     * @param G graph
     * @param v initial vertex
     * @param r random number generator
     * @param vn number of additional vertices (at least 2)
     * @param en approximate number of additional edges
     * @param keepRoot disallow edges to original root
     * @param zeroInDegreeNodes set of vertices with indegree 0.
     */
    public static void makeNonTree(DiGraph G, Vertex v, Random r, int vn,
                                   int en, boolean keepRoot, Set<Vertex> zeroInDegreeNodes) {
        if (vn < 2)
            vn = 2;

        if (en < vn)
            en = vn+1;

        if (zeroInDegreeNodes == null)
            zeroInDegreeNodes = new HashSet<>();

            boolean mok = false;
        int r2 = r.nextInt(vn-1)+1;
        Vertex[] vA = new Vertex[vn+1];
        vA[0] = v;
        for (int i = 1; i <= vn; i++) {
            vA[i] = G.addVertex(v.getLabel() + i);
            if (i == r2) {
                zeroInDegreeNodes.add(vA[i]);
            } else if (i == r2+1) {
                vA[i-2].addEdge(vA[i], "E" + vA[i-2].getLabel() + "-" + vA[i].getLabel());
                vA[i-1].addEdge(vA[i], "E" + vA[i-1].getLabel() + "-" + vA[i].getLabel());
                mok = true;
            } else {
                vA[i-1].addEdge(vA[i], "E" + vA[i-1].getLabel() + "-" + vA[i].getLabel());
            }

        }
        int e = vn-1;
        int k = 0;

        while (k < (vn+en)*2 && e < en) {
            k++;
            int v1 = r.nextInt(vn+1);
            int v2 = keepRoot ? r.nextInt(vn)+1 : r.nextInt(vn+1);

            if (v1 != v2 && !vA[v1].isAdjacent(vA[v2])) {
                vA[v1].addEdge(vA[v2], "E" + vA[v1].getLabel() + "-" + vA[v2].getLabel());
                e++;
                if (!zeroInDegreeNodes.remove(vA[v2]))
                    mok = true;
            }
        }

        if (! mok) {
            if (!vA[vn-1].isAdjacent(vA[vn]))
                vA[vn-1].addEdge(vA[vn], "E" + vA[vn-1].getLabel() + "-" + vA[vn].getLabel());
            if (!vA[vn].isAdjacent(vA[vn - 1]))
                vA[vn].addEdge(vA[vn - 1], "E" + vA[vn].getLabel() + "-" + vA[vn-1].getLabel());
        }

    } // makeNonTree()





    // adds length 2 cycle
    private static void addCycle(Vertex v) {
        for (Vertex n1 : v.neighbors()) {
            for (Vertex n2 : n1.neighbors()) {
                if (n2 != v && ! v.isAdjacent(n2)) {
                    v.addEdge(n2, "E" + v.getLabel() + n2.getLabel());
                    return;
                }
            }
        }
    }





    /**
      * Creates bi-partie Graph with vertex groups sizes v1 and v2, edges, seed
      * if seed is 0, do not randomize nodes
      */
    public static Graph createBiPartie(int v1, int v2, int edges, int seed) {

        Graph G = new Graph();
        Vertex[] vA = new Vertex[v1+v2];

        for (int i = 0; i < v1+v2; i++)
            vA[i] = G.addVertex("" + i);

        int idx1, idx2;

        Random rnd = new Random(seed);
        int[] p = randomPermutation(v1 + v2, seed*3);

        int cutoff = edges*edges*10;
        while (edges > 0 && cutoff-- > 0) {
            idx1 = rnd.nextInt(v1);
            idx2 = rnd.nextInt(v2) + v1;
            if (!vA[p[idx1]].isAdjacent(vA[p[idx2]])) {
                vA[p[idx1]].addEdge(vA[p[idx2]],
                                    new String("" + p[idx1] + "-" + p[idx2]));
                edges--;
            }

        }
        return G;
    }


    /**
      * Returns an array of vertices of the graph
      */
    public static Vertex[] getVertexArray(AbstractGraph G) {
        return getVertexArray(G, false);
    }

    /**
      * Returns an array of vertices of the graph
      * Sets Index of each vertex accordingly
      */
    public static Vertex[] getVertexArrayIndex(AbstractGraph G) {
        return getVertexArray(G, true);
    }

    private static Vertex[] getVertexArray(AbstractGraph G, boolean index) {
        int n = G.size();
        Vertex[] vertexArray = new Vertex[n];
        int i = 0;
        for (Vertex v : G.vertices()) {
            vertexArray[i] = v;
            if (index)
                v.setIndex(i);
            i++;
        }
        return vertexArray;
    }

    /**
     * Return a random vertex.
     * @param G input graph
     * @return random vertex
     */
    public static Vertex randomVertex(AbstractGraph G) {
        if (G == null) throw new NullPointerException("randomVertex: graph is null");
        if (G.size() == 0) throw new NoSuchElementException("randomVertex: graph is empty");

        Vertex[] vA = getVertexArray(G);

        return vA[getR().nextInt(G.size())];
    }

    /**
     * Return a random vertex (except no n)
     * @param G input vertex
     * @param n excluding vertex
     * @return random vertex
     */
    public static Vertex randomVertex(AbstractGraph G, Vertex n) {
        if (G == null) throw new NullPointerException("randomVertex: graph is null");
        if (G.size() == 0) throw new NoSuchElementException("randomVertex: graph is empty");
        if (G.size() == 1) throw new NoSuchElementException("randomVertex: only one vertex");

        Vertex[] vA = getVertexArray(G);

        while (true) {
            int i = getR().nextInt(G.size());
            if (vA[i] != n)
                return vA[i];
        }
    }

    /**
      * Sets color of every vertex in graph
      **/
    public static void varita(AbstractGraph g, int c) {
        for (Vertex v : g.vertices())
            v.setColor(c);

    }
    /**
      * Sets color of every vertex and edge in graph
      **/
    public static void varitaSolmutKaaret(AbstractGraph g, int c) {
        for (Vertex v : g.vertices())
            v.setColor(c);
        for (Edge e : g.edges())
            e.setColor(c);
    }



    /**
      * Sets color of every vertex in graph
      **/
    public static void color(AbstractGraph g, int c) {
        for (Vertex v : g.vertices())
            v.setColor(c);
    }


    /**
     * Adds a random cycle to a graph.
     * Does not add duplicate edges if multiGraph is false of not DiGraph
     * @param G Graph to modify.
     * @param multiGraph whether add new edge in parallel of an existing one.
     * @return one Vertex along the cycle
     */
    public static Vertex addRandomCycle(AbstractGraph G, boolean multiGraph) {
        Vertex[] vertexArray = getVertexArray(G);
        int n = vertexArray.length;
        int[] p = randomPermutation(n);
        for (int i = 0; i < n; i++) {
            if (multiGraph || !vertexArray[p[i]].isAdjacent(vertexArray[p[(i + 1) % n]]))
                vertexArray[p[i]].addEdge(vertexArray[p[(i + 1) % n]],
                        new String(new char[]{'c', (char) (65 + i)}));
        }
        return vertexArray[getR().nextInt(vertexArray.length)];
    } // addRandomCycle()

    /**
     * Adds a random cycle to a graph.
     * Does not add duplicate edges if multiGraph is false of not DiGraph
     * @param G Graph to modify.
     * @param length length of the cycle (# of edges), minumum 2, max G.size()
     * @param multiGraph whether add new edge in parallel of an existing one.
     * @return one Vertex along the cycle
     */
    public static Vertex addRandomCycle(AbstractGraph G, int length, boolean multiGraph) {
        Vertex[] vertexArray = getVertexArray(G);
        int n = vertexArray.length;
        int[] p = randomPermutation(n);
        int len = Math.min(n, Math.max(length, 2));
        for (int i = 0; i < len; i++) {
            if (multiGraph || !vertexArray[p[i]].isAdjacent(vertexArray[p[(i + 1) % len]]))
                vertexArray[p[i]].addEdge(vertexArray[p[(i + 1) % len]],
                        new String(new char[]{'c', (char) (65 + i)}));
        }
        return vertexArray[p[getR().nextInt(len)]];
    } // addRandomCycle()



    /**
      * Adds a random edges to a graph
      * Does not add duplicate edges if multiGraph is false or not DiGraph
      */
    public static void addRandomEdges(AbstractGraph G, int count, boolean multiGraph) {
        addRandomEdges(G, count, multiGraph, (float)0.0);
    } // addRandomEdges()

    /**
      * Adds a random edges to a graph
      * Does not add duplicate edges if multiGraph is false or not DiGraph
      * Max weight maxW
      */
    public static void addRandomEdges(AbstractGraph G, int count, boolean multiGraph, float maxW) {
        Vertex[] vertexArray = getVertexArray(G);
        int n = vertexArray.length;
        Random rnd = new Random(n+count);
        int i = n*n*5;
        while (count > 0 && i > 0) {
            i--;
            int i1 = rnd.nextInt(n);
            int i2 = rnd.nextInt(n);
            if (i1 == i2 || (vertexArray[i1].isAdjacent(vertexArray[i2]) && !multiGraph))
                continue;
            Edge e = vertexArray[i1].addEdge(vertexArray[i2],
                                        new String(new char[] {'e', (char) (65 + count)}));
            if (maxW != (float)0.0) 
                e.setWeight(randomWeight(rnd, maxW, (float)0.1));


            count--;
        }

    } // addRandomEdges()


    /**
      * Adds a set of cliques (by adding edges)
      * clique sizes are given in array
      * Does not add duplicate edges if multiGraph is false of not DiGraph
      */
    public static boolean addCliques(AbstractGraph G, boolean multiGraph, int[] sizes) {
        return addCliques(G, multiGraph, sizes, G.size());
    }

    /**
      * Adds a set of cliques (by adding edges) with seed
      * clique sizes are given in array
      * Does not add duplicate edges if multiGraph is false of not DiGraph
      */
    public static boolean addCliques(AbstractGraph G, boolean multiGraph, int[] sizes, int seed) {
        Vertex[] vertexArray = getVertexArray(G);
        int nv = vertexArray.length;
        int[] p = randomPermutation(nv, seed);

        int nc = sizes.length;

        int i = 0;
        for (int c = 0; c < sizes.length; c++) {
            if (i + sizes[c] > nv)
                return false;	// not enough vertices left

            for (int j = i; j < i + sizes[c]; j++) {
                for (int k = i; k < i + sizes[c]; k++) {
                    if (!(j == k) && ( multiGraph ||
                                       !vertexArray[p[j]].isAdjacent(vertexArray[p[k]])))
                        vertexArray[p[j]].addEdge(vertexArray[p[k]],
                                                  new String(new char[] {'c', (char)(48 + c), (char) (65 + j), (char) (65 + k)}));
                }
            }

            i += sizes[c];
        }

        return true;
    } // addCliques()


    /**
     * Create a weighted free tree and add that to G
     * @param G graph into which tree will be added
     * @param vertices number of vertices in the tree
     * @param maxW max weigth of each edge
     * @return total weigth of all edges
     */
    public static float addTree(Graph G, int vertices, float maxW) {
        if (vertices < 1)
            return 0f;
        getR();
        float sum = 0.0f;
        Vertex[] vA = new Vertex[vertices];
        for (int i = 0; i < vertices; i++)
            vA[i] = G.addVertex("R");
        if (vertices < 2)
            return 0f;

        float w = randomWeight(R, maxW, 0.1f);
        sum += w;
        vA[1].addEdge(vA[0], w);
        for (int i = 2; i < vertices; i++) {
            w = randomWeight(R, maxW, 0.1f);
            sum += w;
            int j = i;
            while (j == i)
                j = R.nextInt(i);
            vA[i].addEdge(vA[j], w);
        }

        return sum;
    }

    /**
     * Create a weighted component that has a cycle (if n >= 3) and add that to G
     * @param G graph into which component will be added
     * @param vertices number of vertices in the tree
     * @param maxW max weigth of each edge
     * @return total weigth of all edges
     */
    public static float addCycledComponent(Graph G, int vertices, float maxW) {
        if (vertices < 3)
            throw new RuntimeException("GraphMaker.addCycledComponent:  Internal error: cannot create cycled component with less than 3 vertices!");
        getR();

        // create a free tree, then add some edges
        float sum = 0.0f;
        Vertex[] vA = new Vertex[vertices];
        for (int i = 0; i < vertices; i++)
            vA[i] = G.addVertex("R");

        float w = randomWeight(R, maxW, 0.1f);
        sum += w;
        vA[1].addEdge(vA[0], w);
        for (int i = 2; i < vertices; i++) {
            w = randomWeight(R, maxW, 0.1f);
            sum += w;
            int j = i;
            while (j == i)
                j = R.nextInt(i);
            vA[i].addEdge(vA[j], w);
        }

        int add = R.nextInt(vertices) + 1;
        int added = 0;
        while (add > 0 || added == 0) {
            int v1 = R.nextInt(vertices);
            int v2 = v1;
            while (v2 == v1)
                v2 = R.nextInt(vertices);
            if (!vA[v1].isAdjacent(vA[v2])) {
                w = randomWeight(R, maxW, 0.1f);
                sum += w;
                vA[v1].addEdge(vA[v2], w);
                added++;
            }

            add--;
        }

        return sum;
    }

    /**
     * Give all vertex a new name
     * @param G graph
     * @param prefix prefix of name
     */
    public static void nameVertices(AbstractGraph G, String prefix) {
        int i = 0;
        for (Vertex v : G.vertices())
            v.setLabel(prefix + i++);
    }

    /**
     * Create copy of G with order of vertices randomly permuted.
     * Copies vertex labels and edge weigths.
     * @param G input graph
     * @return copy of graph
     */
    public static Graph shuffleCopy(Graph G) {
        getR();
        int n = G.size();
        Vertex[] vA = getVertexArrayIndex(G);
        int[] perm = randomPermutation(n, R.nextInt());
        // System.out.println("perm=" + Arrays.toString(perm));

        Graph GC = new Graph();
        Vertex[] vAC = new Vertex[n];

        // create vertices, keep names, color, weigth
        // TODO: Vertex.addVertex does not accept NaN color
        for (int i = 0; i < n; i++) {
            // vAC[perm[i]] = GC.addVertex(vA[i].getLabel(), vA[i].getColor(), vA[i].getWeight(), 0);
            vAC[i] = GC.addVertex(vA[perm[i]].getLabel());
        }

        // copy edges
        for (Edge e : G.edges())  {
            Vertex v1 = e.getStartPoint();
            Vertex v2 = e.getEndPoint();
            // vAC[perm[v1.getIndex()]].addEdge(vAC[perm[v2.getIndex()]], e.getLabel(), e.getColor(), e.getWeight());
            vAC[perm[v1.getIndex()]].addEdge(vAC[perm[v2.getIndex()]], e.getWeight());

        }

        return GC;
    }

    /**
     * Create example for TRAII 2019 X5.
     * Creates a graph with several components. One of the components will have sum
     * of edge weights maxCompWeight. The edges of that component will be stored to
     * maxComponent.
     * @param vertices number of vertices
     * @param maxCompWeight weigth of heaviest component
     * @param maxComponent vertices of the heaviest component
     * @return the generated graph
     */
    public static Graph createWeightedComponents(int vertices, float maxCompWeight, Collection<Vertex> maxComponent) {

        // trival case, 0 weigth -> no edges
        if (maxCompWeight == 0.0f) {
            Graph G = createGraph(vertices, 0, 0, 0.0f);
            if (G.size() > 0)
                maxComponent.add(G.firstVertex());
            return G;
        }

        if (vertices < 2 && maxCompWeight > 0.0f)
            throw new RuntimeException("GraphMaker.createWeightedComponents:  Internal error: cannot create positive weight component with less than 2 vertices!");

        Graph G = new Graph();
        Vertex[] vA = new Vertex[vertices];

        int[] perm = randomPermutation(vertices);

        for (int i = 0; i < vertices; i++)
            vA[i] = G.addVertex("" + i);

        int n = vertices;
        int i = 0;
        float w = maxCompWeight;
        int comp = n / 3 + 1;
        if (comp < 2)
            comp = 2;
        boolean first = true;

        // create one component at a time
        while (n > 0) {
            createComponent(comp, i, vA, perm, w, first, maxComponent,
                    (first ? true : (comp < 3 ? false : R.nextBoolean())) );

            // reduce size and weight
            i += comp;
            n -= comp;
            comp /= 1.5f;
            if (comp < 1)
                comp = 1;
            if (comp > n)
                comp = n;
            w /= 1.5f;
            first = false;
        }
        return G;
    }
    /**
     * Add a component to graph.
     * @param n number of vertices in component
     * @param firstIndex first index of component
     * @param vA array of vertices
     * @param perm permutation to use in indexing vA
     * @param compW component weight
     * @param isFirst whether to add vertices to maxComponent
     * @param maxComponent collection of vertices
     * @param cycle whether to include a cycle to this new component
     */
    private static void createComponent(int n, int firstIndex, Vertex[] vA, int[] perm, float compW, boolean isFirst, Collection<Vertex> maxComponent, boolean cycle) {
        if (firstIndex + n > vA.length)
            throw new RuntimeException("GraphMaker.createComponent:  Internal error: Illegal indexes");
        if (isFirst && n < 2 && compW > 0.0f)
            throw new RuntimeException("GraphMaker.createComponent:  Internal error: cannot create positive weight component with less than 2 vertices!");
        if (cycle && n < 3)
            throw new RuntimeException("GraphMaker.createComponent:  Internal error: cannot create cycle with less than 3 vertices!");

        if (n < 2)
            return;
        if (n == 2) {   // trivial case
            vA[perm[firstIndex]].addEdge(vA[perm[firstIndex + 1]], compW);
            if (isFirst) {
                maxComponent.add(vA[perm[firstIndex]]);
                maxComponent.add(vA[perm[firstIndex+1]]);
            }

            return;
        }
        int e = n - 1;

        if (cycle)
            e += R.nextInt(n) + 1;

        if (isFirst) {  // first component must be connected, make a path first
            for(int i = 0; i < n-1; i++) {
                float nw = randomWeight(R, compW / e, 0.1f);
                if (e == 1)
                    nw = compW;
                vA[perm[firstIndex+i]].addEdge(vA[perm[firstIndex+i+1]], nw);
                maxComponent.add(vA[perm[firstIndex+i]]);
                e--;
                compW -= nw;
            }
            maxComponent.add(vA[perm[firstIndex+n-1]]);

        }

        // add the rest of edges
        int count = 1000 * 1000;
        while (compW > 0.0f && e > 0) {
            float nw = randomWeight(R, compW / e, 0.1f);
            if (e == 1)
                nw = compW;
            int idx1 = R.nextInt(n);
            int idx2 = R.nextInt(n);
            if ((idx1 != idx2) && !vA[perm[firstIndex + idx1]].isAdjacent(vA[perm[firstIndex + idx2]])) {
                vA[perm[firstIndex + idx1]].addEdge(vA[perm[firstIndex + idx2]], nw);
                if (isFirst) {
                    maxComponent.add(vA[perm[firstIndex+idx1]]);
                    maxComponent.add(vA[perm[firstIndex+idx2]]);
                }
                e--;
                compW -= nw;
            }
            if (count-- < 0)
                throw new RuntimeException("GraphMaker.createComponent: Internal error: ifinite loop");
        }
    }


    private static int[] randomPermutation(int n) {
        return randomPermutation(n, n);
    }

    private static int[] randomPermutation(int n, int seed) {
        int[] p = new int[n];

        if (seed == 0) {
            for (int i = 0 ; i < n; i++)
                p[i] = i;
            return p;
        }


        Random rnd = new Random(seed);

        // mark all positions unused
        for (int i = 0 ; i < n; i++)
            p[i] = -1;

        // for each i make random location
        for (int i = 0 ; i < n; i++) {
            // find next free from random start
            int r = rnd.nextInt(n);
            while (true) {	// dangerous style..
                if (p[r] == -1) {
                    p[r] = i;
                    break;
                }
                r = (r+1) % n;
            }
        }

        return p;

    } // randomPermutation()


    /**
      * Simpler toString, 0 = really bare, 1 slightly more, 2 even more
      */
    public static String toString(AbstractGraph G, int detail) {

        StringBuilder s = new StringBuilder();

        int i = 0;
        for (Vertex v : G.vertices()) {

            if (v.getLabel() == null || v.getLabel() == "")
                s.append(i);
            else
                s.append(v.getLabel());

            if (detail > 0 && v.getColor() >= 0) {
                s.append(" (");
                s.append(v.getColor());
                s.append(")");
            }

            s.append(" | ");

            for (Edge e : v.edges()) {
                Vertex w;

                if (G.isDiGraph())
                    w = e.getEndPoint();
                else
                    w = e.getEndPoint(v);

                if (w.getLabel() == null || w.getLabel() == "")
                    s.append(i);
                else
                    s.append(w.getLabel());

                float weight = e.getWeight();

                if (detail > 0 && weight >= 0) {
                    s.append("{");
                    s.append(weight);
                    s.append("}");
                    // s.append("-");
                }

                s.append(" ");
            }

            s.append("\n");
        }

        return s.toString();
    }




                

            


    /**
      * Returns weight matrix of the graph
      * Sets vertex indexes accordingly
      */
    public static float[][] graphToMatrixWeight(AbstractGraph G) {
        
        Vertex[] vertexArray = getVertexArrayIndex(G);
        int n = vertexArray.length;

        float[][] M = new float[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                M[i][j] = (float)-1.0;

        for (int i = 0; i < n; i++) {
            Vertex v = vertexArray[i];
            for (Edge e : v.edges()) {
                Vertex w = e.getEndPoint();
                // avoid bug in Vertex.edges():
                if (w == v)
                    continue;
                float weight = e.getWeight();
                if (weight == DiGraph.NaN)
                    weight = 1;

                M[i][w.getIndex()] = weight;
            }
        }

        return M;

    }   // graphToMatrix()


    /**
      * Returns truth value matrix of the graph
      * Sets vertex indexes accordingly
      */
    public static boolean[][] graphToMatrix(AbstractGraph G) {
        
        Vertex[] vertexArray = getVertexArrayIndex(G);
        int n = vertexArray.length;

        boolean[][] M = new boolean[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                M[i][j] = false;

        for (int i = 0; i < n; i++) {
            Vertex v = vertexArray[i];
            for (Edge e : v.edges()) {
                Vertex w = e.getEndPoint();
                // avoid bug in Vertex.edges():
                if (w == v)
                    continue;

                M[i][w.getIndex()] = true;
            }
        }

        return M;

    }   // graphToMatrix()


    /**
      * Returns matrix in string of the graph
      * TODO nicer formatting for n > 9
      */
    public static String toMatrixString(AbstractGraph G) {
        boolean[][] M = graphToMatrix(G);
        Vertex[] vertexArray = getVertexArray(G);
        int n = vertexArray.length;

        StringBuilder s = new StringBuilder("      ");
        for (int i = 0; i < n; i++) {
            s.append(i);
            s.append(" ");
        }
        s.append("\n");

        for (int i = 0; i < n; i++) {
            s.append(vertexArray[i]);
            s.append(" ");
            s.append(i);
            s.append("|");
            for (int j = 0; j < n; j++) {
                s.append(M[i][j] ? "1 " : "0 ");
            }
            s.append("\n");
        }

        return s.toString();
    }


} // class GraphMaker

