package Viikko4;

import Apuluokat.GraphMaker;
import fi.uef.cs.tra.AbstractGraph;
import fi.uef.cs.tra.Graph;
import fi.uef.cs.tra.Vertex;

import java.util.LinkedList;
import java.util.List;


public class TRAII_23_t16_pohja {

    public static void main(String[] args) {

        // defaults
        int vertices = 8;
        int edges = 5;

        if (args.length > 0)
            vertices = Integer.parseInt(args[0]);

        if (args.length > 1)
            edges = Integer.parseInt(args[1]);

        int seed = vertices+edges;

        if (args.length > 2)
            seed = Integer.parseInt(args[2]);

        Graph graph;

        System.out.println("\nVerkko (kaksi puuta, ei kehÃ¤Ã¤): ");
        // graph = GraphMaker.createGraph(vertices, edges, seed);
        graph = GraphMaker.createFlora(0, 0, 2, 0, vertices);
        System.out.println(GraphMaker.toString(graph, 0));
        List<Vertex> keha = jokuKeha(graph);
        System.out.println("KehÃ¤: " + keha);

        System.out.println("\nLisÃ¤tÃ¤Ã¤n kehÃ¤: ");
        GraphMaker.addRandomCycle(graph, 3, false);
        keha = jokuKeha(graph);
        System.out.println("KehÃ¤: " + keha);

    }

    static List<Vertex> jokuKeha(Graph G) {
        varita(G, Graph.WHITE);

        // TODO
        // vihje: hae kehÃ¤ syvyyssuuntaisella haulla

        return null;
    }




    // syvyyssuuntainen lÃ¤pikynti vÃ¤rittÃ¤en vÃ¤rillÃ¤ c
    static void dfsColor(Vertex v, int color) {
        v.setColor(color);

        for (Vertex w : v.neighbors())
            if (w.getColor() != color)
                dfsColor(w, color);
    }

    // verkko annetun vÃ¤riseksi
    static void varita(AbstractGraph g, int c) {
        for (Vertex v : g.vertices())
            v.setColor(c);
    }



}