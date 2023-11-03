package Viikko3;

import fi.uef.cs.tra.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class DiGraphEsimerkki22 {

    public static void main(String[] args) {

        // defaults
        int vertices = 6;
        int edges = 9;

        if (args.length > 0)
            vertices = Integer.parseInt(args[0]);

        if (args.length > 1)
            edges = Integer.parseInt(args[1]);

        int seed = vertices + edges;

        if (args.length > 2)
            seed = Integer.parseInt(args[2]);


        DiGraphEsimerkki22 y = new DiGraphEsimerkki22();

        // Luodaan satunnainen verkko
        DiGraph graph = GraphMaker.createDiGraph(vertices, edges, seed);
        System.out.println("\nVerkko:");
        System.out.println(graph.toString());

        System.out.println("kehainen: " + y.kehainen(graph));
        System.out.println("vahvasti yht: " + y.vahvastiYhtenainen(graph));

        System.out.println("\nSeuraajat:");
        Vertex v1 = null;
        Vertex v2 = null;
        for (Vertex v : graph.vertices()) {
            System.out.println(v + " : " + y.seuraajat(v));
            if (v1 == null)
                v1 = v;
            else if (v2 == null)
                v2 = v;
        }

        System.out.println("\nKaariluokittelu:");
        y.kaariLuokittelu(graph);

        for (Vertex v : graph.vertices()) {
            System.out.print(v + " : ");
            for (Edge e : v.edges()) {
                Vertex w = e.getEndPoint();
                System.out.print("" + e.getColor() + w + " ");
            }
            System.out.println();
        }


        System.out.println("\nTulostus kaikki kaaret");
        for (Edge e : graph.edges())
            System.out.print(e + " ");
        System.out.println();

        System.out.println("\nTulostus kaaret solmuittain");
        for (Vertex v : graph.vertices()) {
            System.out.print(v + " : ");
            for (Edge e : v.edges())
                System.out.print(e + " ");
            System.out.println();
        }


        LinkedList<LinkedList<Vertex>> polut = y.kaikkiPolut(graph, v1, v2);
        System.out.println("\nKaikki polut");
        System.out.println(polut);

        LinkedList<LinkedList<Vertex>> comp = y.vahvastiYhtenaisetKomponentit(graph);
        System.out.println("\nVahvasti yhtenÃ¤iset komponentit");
        System.out.println(comp);

        GraphMaker.addRandomCycle(graph, false);
        System.out.println("\nKehÃ¤inen");
        for (Vertex v : graph.vertices()) {
            System.out.print(v + " : ");
            for (Vertex w : v.neighbors())
                System.out.print(w + " ");
            System.out.println();
        }
        System.out.println("kehainen: " + y.kehainen(graph));
        System.out.println("vahvasti yht: " + y.vahvastiYhtenainen(graph));

        if (vertices < 20) {
            polut = y.kaikkiPolut(graph, v1, v2);
            System.out.println("\nKaikki polut");
            System.out.println(polut);
        }

        comp = y.vahvastiYhtenaisetKomponentit(graph);
        System.out.println("\nVahvasti yhtenÃ¤iset komponentit");
        System.out.println(comp);

        graph = GraphMaker.createCompleteDiGraph(vertices);
        System.out.println("\nTÃ¤ydellinen");
        v1 = null;
        v2 = null;
        for (Vertex v : graph.vertices()) {
            if (v1 == null)
                v1 = v;
            else if (v2 == null)
                v2 = v;
            System.out.print(v + " : ");
            for (Vertex w : v.neighbors())
                System.out.print(w + " ");
            System.out.println();
        }
        if (vertices < 7) {
            polut = y.kaikkiPolut(graph, v1, v2);
            System.out.println("\nKaikki polut");
            System.out.println(polut);
        }


        graph = GraphMaker.createDAG(vertices, edges, seed);
        System.out.println("\nDAG");
        for (Vertex v : graph.vertices()) {
            System.out.print(v + " : ");
            for (Vertex w : v.neighbors())
                System.out.print(w + " ");
            System.out.println();
        }
        comp = y.vahvastiYhtenaisetKomponentit(graph);
        System.out.println("\nVahvasti yhtenÃ¤iset komponentit");
        System.out.println(comp);
        System.out.println("kehainen: " + y.kehainen(graph));
        System.out.println("topoSort: " + y.topoSort(graph));


        System.out.println();
        System.out.println(GraphMaker.toMatrixString(graph));

        System.out.println("\nPainomatriisi");
        float[][] M = GraphMaker.graphToMatrixWeight(graph);
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                System.out.print(M[i][j] + " ");
            }
            System.out.println();
        }

    }

    /**
     * KehÃ¤isyyden selvittÃ¤minen
     *
     * @param g suunnattu verkko
     * @return true jos verkossa on kehÃ¤, muuten false
     */
    public boolean kehainen(DiGraph g) {
        varita(g, DiGraph.WHITE);
        for (Vertex v : g.vertices())
            if (v.getColor() == DiGraph.WHITE)  // vielÃ¤ kÃ¤sittelemÃ¤ttÃ¶mille kaarille
                if (kehadfs(v))             // kutsutaan varsinaista hakua
                    return true;
        return false;
    }

    // suunnatun verkon komponentin kehÃ¤isyyden
    // selvittÃ¤minen syvyyssuuntaisella haulla
    // rekursiossa olevat solmut ovat harmaita, lopuksi mustia
    // jos vastaan tulee harmaa solmu, siihen pÃ¤Ã¤see kahta reittiÃ¤
    boolean kehadfs(Vertex start) {

        // lÃ¤pikÃ¤ynti tÃ¤stÃ¤ solmusta kesken == harmaa
        start.setColor(DiGraph.GRAY);
        for (Vertex vertex : start.neighbors()) {
            // harmaa naapurisolmu: kehÃ¤ muodostuu
            if (vertex.getColor() == DiGraph.GRAY)
                return true;
            else if (vertex.getColor() == DiGraph.WHITE)
                // rekursiokutsu lÃ¤pikÃ¤ymÃ¤ttÃ¶mÃ¤Ã¤n naapuriin
                if (kehadfs(vertex))
                    return true;

        }
        // lÃ¤pikÃ¤ynti tÃ¤stÃ¤ solmusta pÃ¤Ã¤ttynyt == musta
        start.setColor(Graph.BLACK);
        return false;
    }

    /**
     * Syvyyssuuntainen lÃ¤pikÃ¤ynti (tekemÃ¤ttÃ¤ verkolle mitÃ¤Ã¤n)
     * Siis runko.
     *
     * @param g lÃ¤pikÃ¤ytÃ¤vÃ¤ verkko
     */
    void dfsstart(DiGraph g) {
        for (Vertex v : g.vertices())                // kaikki solmut valkoisiksi
            v.setColor(DiGraph.WHITE);
        for (Vertex v : g.vertices())                // aloitus vielÃ¤ kÃ¤ymÃ¤ttÃ¶mistÃ¤ solmuista
            if (v.getColor() == DiGraph.WHITE)
                dfs(v);
    }

    /**
     * Syvyyssuuntainen lÃ¤pikÃ¤ynti solmusta start alkaen
     *
     * @param start aloitussolmu
     */
    void dfs(Vertex start) {
        // tÃ¤hÃ¤n toimenpide solmulle start (esijÃ¤rjestys)
        start.setColor(DiGraph.GRAY);
        for (Vertex v : start.neighbors())                // vielÃ¤ kÃ¤ymÃ¤ttÃ¶mÃ¤t
            if (v.getColor() == DiGraph.WHITE)            // naapurit
                dfs(v);
        // tÃ¤hÃ¤n toimenpide solmulle start (jÃ¤lkijÃ¤rjestys)
    }


    /**
     * Kaarien luokittelu.
     * VÃ¤rjÃ¤Ã¤ kaaret seuraavasti:
     * 1 = puukaari
     * 2 = paluukaari
     * 3 = etenemiskaari
     * 4 = ristikkÃ¤iskaari
     *
     * @param g syÃ¶teverkko
     */
    public void kaariLuokittelu(DiGraph g) {
        for (Vertex v : g.vertices()) {
            v.setColor(DiGraph.WHITE);
            v.setIndex(0);
        }
        AtomicInteger nro = new AtomicInteger(0);
        for (Vertex v : g.vertices())
            if (v.getColor() == DiGraph.WHITE)
                kaaridfs(v, nro);
    }


    /**
     * Kaariluokittelijan rekursio
     *
     * @param v tÃ¤mÃ¤nhetkinen solmu
     * @param nro laskuri
     */
    void kaaridfs(Vertex v, AtomicInteger nro) {

        v.setColor(DiGraph.GRAY);
        v.setIndex(nro.incrementAndGet());
        for (Edge e : v.edges()) {
            Vertex w = e.getEndPoint();
            if (w.getColor() == DiGraph.WHITE) {
                e.setColor(1); // puukaari
                kaaridfs(w, nro);
            } else if (w.getColor() == DiGraph.GRAY)
                e.setColor(2); // paluukaari
            else if (v.getIndex() < w.getIndex())
                e.setColor(3); // etenemiskaari
            else
                e.setColor(4);  // ristikkÃ¤iskaari
        }
        v.setColor(Graph.BLACK);
    }


    // syvyyssyynainen lpikynti vritten vrill c
    void dfsColor(Vertex vertex, int color) {
        vertex.setColor(color);
        for (Vertex neighborVertex : vertex.neighbors())
            if (neighborVertex.getColor() != color)
                dfsColor(neighborVertex, color);

    }

    // verkko (suunnattu tai suuntaamaton) annetun vriseksi
    void varita(AbstractGraph g, int c) {
        for (Vertex v : g.vertices())
            v.setColor(c);
    }

    // annetun solmun seuraajien joukko
    Set<Vertex> seuraajat(Vertex start) {
        Set<Vertex> s = new HashSet<>();
        // TODO demotehtÃ¤vÃ¤ poistettu
        return s;
    }


    // kaikki yksinkertaiset polut kahden solmun vÃ¤lillÃ¤
    // palautus solmulistojen listana
    LinkedList<LinkedList<Vertex>> kaikkiPolut(DiGraph g, Vertex start, Vertex end) {

        varita(g, DiGraph.WHITE);

        LinkedList<LinkedList<Vertex>> polut = new LinkedList<LinkedList<Vertex>>();
        LinkedList<Vertex> pino = new LinkedList<Vertex>();

        pino.add(start);

        if (start != end)
            start.setColor(Graph.BLACK);

        for (Vertex v : start.neighbors()) {
            kaikkiPolut_r(polut, pino, v, end);
        }

        start.setColor(Graph.WHITE);

        return polut;
    }

    void kaikkiPolut_r(LinkedList<LinkedList<Vertex>> polut, LinkedList<Vertex> pino,
                       Vertex start, Vertex end) {
        pino.add(start);

        if (start == end) {
            polut.add(new LinkedList<Vertex>(pino));
            pino.removeLast();
            return;
        }

        start.setColor(Graph.BLACK);

        for (Vertex v : start.neighbors()) {
            if (v.getColor() == Graph.WHITE)
                kaikkiPolut_r(polut, pino, v, end);
        }

        start.setColor(Graph.WHITE);
        pino.removeLast();
    }


    // vahvasti yhtenÃ¤inen, brute force
    boolean vahvastiYhtenainen(DiGraph g) {
        int n = g.size();

        for (Vertex v : g.vertices()) {
            Set<Vertex> seur = seuraajat(v);
            seur.add(v);
            if (seur.size() != n)
                return false;
        }

        return true;
    }


    // vahvasti yhtenÃ¤iset komponentit solmulistojen listana
    LinkedList<LinkedList<Vertex>> vahvastiYhtenaisetKomponentit(DiGraph g) {

        Vertex[] va = GraphMaker.getVertexArrayIndex(g);
        int n = va.length;

        // dfsnumber (post-order)
        int[] dfsnum = loppuNumerointiDfs(g);

        // Build transpose gt of g
        DiGraph gt = new DiGraph();
        Vertex[] vat = new Vertex[n];

        // vertices
        for (int i = 0; i < n; i++) {
            vat[i] = gt.addVertex("" + i);
            vat[i].setIndex(i);
        }

        // edges
        for (int i = 0; i < n; i++)
            for (Vertex w : va[i].neighbors())
                vat[w.getIndex()].addEdge(vat[i]);


        // dfs gt using decreasing order

        // inverse ("sorting" index) of dfsnum
        int[] dfsnum_inverse = new int[n];
        for (int i = 0; i < n; i++)
            dfsnum_inverse[dfsnum[i]] = i;

        // now dfsnum_inverse contains node indeces in order of incresing
        // dfsnumber

        varita(gt, DiGraph.WHITE);

        LinkedList<LinkedList<Vertex>> components =
                new LinkedList<LinkedList<Vertex>>();

        // start dfs in decreasing order of dfsnumber
        for (int i = n - 1; i >= 0; i--)
            if (vat[dfsnum_inverse[i]].getColor() == DiGraph.WHITE) {
                // new component found
                LinkedList<Vertex> comp = new LinkedList<Vertex>();
                puuDfs(vat[dfsnum_inverse[i]], va, comp);
                components.add(comp);
            }

        return components;
    }   // vahvastiYhtenaisetKomponentit()


    int[] loppuNumerointiDfs(DiGraph g) {
        int n = g.size();
        int[] f = new int[n];
        int i = 0;

        varita(g, DiGraph.WHITE);

        for (Vertex v : g.vertices())
            if (v.getColor() == DiGraph.WHITE)
                i = loppuNumerointiDfs_r(f, i, v);

        return f;
    }   // loppuNumerointiDfs()


    int loppuNumerointiDfs_r(int[] f, int i, Vertex v) {
        v.setColor(DiGraph.BLACK);
        for (Vertex w : v.neighbors())
            if (w.getColor() == DiGraph.WHITE)
                i = loppuNumerointiDfs_r(f, i, w);
        f[v.getIndex()] = i;
        return i + 1;
    }   // loppuNumerointiDfs_r()


    void puuDfs(Vertex v, Vertex[] va, LinkedList<Vertex> comp) {
        v.setColor(DiGraph.BLACK);
        comp.add(va[v.getIndex()]);
        for (Vertex w : v.neighbors())
            if (w.getColor() == DiGraph.WHITE)
                puuDfs(w, va, comp);
    }   // puuDfs()


    // \ vahvasti yhtenÃ¤iset komponentit


    // topologinen lajittelu DAG:n solmuille
    // ei tarkasta erikseen verkon kehÃ¤ttÃ¶myyttÃ¤
    // se toki voitaisiin tehdÃ¤ samalla vaivalla, mutta lisÃ¤Ã¤n sen
    // vasta demojen jÃ¤lkeen
    LinkedList<Vertex> topoSort(DiGraph G) {
        LinkedList<Vertex> L = new LinkedList<Vertex>();

        varita(G, DiGraph.WHITE);

        for (Vertex v : G.vertices())
            if (v.getColor() == DiGraph.WHITE)
                topoSort_r(v, L);

        return L;

    }

    void topoSort_r(Vertex v, LinkedList<Vertex> L) {
        v.setColor(DiGraph.BLACK);
        for (Vertex w : v.neighbors())
            if (w.getColor() == DiGraph.WHITE)
                topoSort_r(w, L);
        L.add(0, v);
    }

    // \ topologinen lajittelu

    // maksimaalinen virtaus
    // Ford-Fulkerson

    // returns only max flow
    float fordFulkerson(DiGraph G, Vertex s, Vertex d) {
        float[][] flow = new float[G.size()][G.size()];
        return fordFulkerson(G, s, d, flow);
    }

    // returns also usage of edges as a matrix
    // TODO: get vertex array, now this assumes indeces to be same
    // every time
    float fordFulkerson(DiGraph G, Vertex s, Vertex d, float[][] flow) {

        int n = G.size();
        float tcap = 0;

        int si = 0, di = 5;

        Vertex[] vA = GraphMaker.getVertexArrayIndex(G);
        for (int i = 0; i < n; i++) {
            if (vA[i] == s)
                si = i;
            if (vA[i] == d)
                di = i;
        }

        // init flow
        int ne = 0;
        for (Edge e : G.edges()) {
            int i = e.getStartPoint().getIndex();
            int j = e.getStartPoint().getIndex();
            flow[i][j] = 0;
            flow[j][i] = 0;
            ne++;
        }

        HashMap<Edge, Edge> pairs = new HashMap<Edge, Edge>(ne * 4);
        DiGraph R = makeRes(G, pairs);
        Vertex[] vAR = GraphMaker.getVertexArrayIndex(R);

        Vertex sr = vAR[si];
        Vertex dr = vAR[di];

        System.out.println("res");
        System.out.println(R);

        while (true) {

            System.out.println("haetaan polku");
            LinkedList<Edge> p = dfspath(R, sr, dr);
            System.out.println("polku:" + p);

            if (p == null)
                break;

            // find capacity of the found augmenting path
            float cap = p.getFirst().getWeight();
            for (Edge e : p)
                if (e.getWeight() < cap)
                    cap = e.getWeight();

            System.out.println("cap " + cap);


            for (Edge e : p) {
                Vertex v1 = e.getStartPoint();
                Vertex v2 = e.getEndPoint();
                flow[v1.getIndex()][v2.getIndex()] += cap;
                flow[v2.getIndex()][v1.getIndex()] =
                        -flow[v1.getIndex()][v2.getIndex()];
                e.setWeight(e.getWeight() - cap);
                Edge e2 = pairs.get(e);
                e2.setWeight(e2.getWeight() - cap);
            }
            tcap += cap;
        }
/*
        float fl = 0;
        for (int i = 0; i < n; i++)
            fl += flow[start.getIndex()][i];
        return fl;
        */

        return tcap;
    }

    LinkedList<Edge> dfspath(DiGraph res, Vertex start, Vertex sink) {

        System.out.println("dfsparh res");
        System.out.println(res);
        varita(res, DiGraph.WHITE);
        LinkedList<Edge> L = new LinkedList<Edge>();

        if (dfsPath_r(start, sink, L))
            return L;
        else
            return null;
    }

    boolean dfsPath_r(Vertex start, Vertex sink, LinkedList<Edge> L) {

        if (start == sink)
            return true;

        // System.out.println("dfsPath_r" + start);
        start.setColor(DiGraph.BLACK);
        for (Edge e : start.edges()) {
            if (e.getEndPoint() == start)
                continue; // bug in Vertex.edges()
            // System.out.println("dfsPath_r 2" + e + "  " + e.getEndPoint());
            // System.out.println("dfsPath_r 2" + e.getEndPoint().getColor());

            if (e.getEndPoint().getColor() == DiGraph.WHITE &&
                    e.getWeight() > 0) {
                // System.out.println("dfsPath_r 3" + e + "  " + e.getEndPoint());
                L.addLast(e);
                boolean found = dfsPath_r(e.getEndPoint(), sink, L);
                if (found)
                    return true;
                else
                    L.removeLast();
            }
        }

        return false;
    }


    DiGraph makeRes(DiGraph G, HashMap<Edge, Edge> pairs) {

        int n = G.size();
        float[][] M = GraphMaker.graphToMatrixWeight(G);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                System.out.print(" " + M[i][j]);
            System.out.println();
        }

        DiGraph C = new DiGraph();
        Vertex[] vA = new Vertex[n];

        for (int i = 0; i < n; i++)
            vA[i] = C.addVertex("r" + i, i);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                // TODO opposite edges?
                if (M[i][j] != -1.0) {
                    Edge e1 = vA[i].addEdge(vA[j], "" + i + j, 0, M[i][j]);
                    Edge e2 = vA[j].addEdge(vA[i], "" + j + i, 0, 0);
                    pairs.put(e1, e2);
                    pairs.put(e2, e1);
                }

        return C;
    }


    void floatExample() {

        final int n = 6;

        DiGraph G = new DiGraph();

        Vertex[] vA = new Vertex[n];

        vA[0] = G.addVertex("s");
        vA[1] = G.addVertex("1");
        vA[2] = G.addVertex("2");
        vA[3] = G.addVertex("3");
        vA[4] = G.addVertex("4");
        vA[5] = G.addVertex("d");

        vA[0].addEdge(vA[1], "16", 0, 16);
        vA[0].addEdge(vA[2], "13", 0, 13);
        vA[1].addEdge(vA[2], "10", 0, 10);
        vA[1].addEdge(vA[3], "12", 0, 12);
        vA[2].addEdge(vA[4], "14", 0, 14);
        //vA[2].addEdge(vA[1], " 4",  0, 4);
        vA[3].addEdge(vA[2], " 9", 0, 9);
        vA[3].addEdge(vA[5], "20", 0, 20);
        vA[4].addEdge(vA[3], " 7", 0, 7);
        vA[4].addEdge(vA[5], " 4", 0, 4);

        System.out.println("Maksimivirta");
        System.out.println(G);

        float[][] flow = new float[n][n];

        float cap = fordFulkerson(G, vA[0], vA[5], flow);

        System.out.println(cap);
        System.out.println(flow);

    }


}