package Viikko4;

import Apuluokat.GraphMaker;
import fi.uef.cs.tra.AbstractGraph;
import fi.uef.cs.tra.Graph;
import fi.uef.cs.tra.Vertex;

import java.util.ArrayList;
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
        List<Vertex> tulos = new ArrayList<>();

        for (Vertex v:G.vertices()) {
            v.setColor(Graph.GRAY);
            tulos.add(v);
            if (dfsColor(v, Graph.GREY, tulos)) {
                return tulos;
            }
            tulos.remove(tulos.size()-1);
            v.setColor(Graph.WHITE);
        }

        return null;
    }




    // syvyyssuuntainen lÃ¤pikynti vÃ¤rittÃ¤en vÃ¤rillÃ¤ c
    static boolean dfsColor(Vertex v, int color, List<Vertex> tulos) {
        for (Vertex w : v.neighbors()) {
            tulos.add(w);
            if (w.getColor() == color) {
                return true;//valmis
            }
            w.setColor(color);
            if (dfsColor(w, color, tulos)) {
                return true;
            }
        }
        v.setColor(Graph.WHITE);
        tulos.remove(tulos.size()-1);
        return false;
    }

    // verkko annetun vÃ¤riseksi
    static void varita(AbstractGraph g, int c) {
        for (Vertex v : g.vertices())
            v.setColor(c);
    }



}