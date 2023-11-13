package Viikko4;

import Apuluokat.GraphMaker;
import fi.uef.cs.tra.AbstractGraph;
import fi.uef.cs.tra.Graph;
import fi.uef.cs.tra.Vertex;

import java.util.ArrayList;
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
        System.out.println(GraphMaker.toString(graph, 0));
        System.out.println("KehÃ¤: " + keha);

    }

    static List<Vertex> jokuKeha(Graph G) {
        varita(G, Graph.WHITE); // Väritetään verkko valkoiseksi
        List<Vertex> tulos = new LinkedList<>(); // Tuloslista

        for (Vertex v:G.vertices()) {
            v.setColor(Graph.GRAY); // väritetään aloitussolmu harmaaksi
            tulos.add(v); // Lisätään aloitussolmu tulokseen
            if (dfsColor(v, Graph.GREY, tulos, v)) { // lähdetään rekursiivisesti etsimään kehää ja mikäli sellainen löydetään, palautetaan se
                while (tulos.get(0) != tulos.get(tulos.size()-1)) { // Poistetaan ennen kehää talletetut solmut. Viimeinen lisätty solmu on kehän loppusolmu
                    tulos.remove(0);
                }
                return tulos; // tulos sisältää kehän siten, että siinä on ensimmäisenä ja viimeisenä viittaus samaan. Jomman kumman voisi poistaa vielä jos haluttaisiin vain kehän solmut. Varmaankin ehkä oli pitänytkin, mutta tuloste on selvempi näin. Eli vielä removeFirst kerran esim.
            }
            tulos.remove(tulos.size()-1); // Poistetaan aloitussolmu jos tästä aloituksesta ei löytynyt kehää
            v.setColor(Graph.WHITE); // Väritetään aloitussolmu valkoiseksi. Tämä ei tarpeellista sillä osaverkko on jo käyty läpi, mutta ei ole myöskään syytä jättää eriväriseksi.
        }
        return null;
    }


    // syvyyssuuntainen lÃ¤pikynti vÃ¤rittÃ¤en vÃ¤rillÃ¤ c
    static boolean dfsColor(Vertex v, int color, List<Vertex> tulos, Vertex edellinen) {
        for (Vertex w : v.neighbors()) {
            tulos.add(w);
            if (w != edellinen) {
                if (w.getColor() == color) {
                    return true;//valmis
                }
                w.setColor(color);
                if (dfsColor(w, color, tulos, v)) {
                    return true;
                }
            }
            tulos.remove(tulos.size()-1);
        }
        v.setColor(Graph.WHITE);
        return false;
    }

    // verkko annetun vÃ¤riseksi
    static void varita(AbstractGraph g, int c) {
        for (Vertex v : g.vertices())
            v.setColor(c);
    }
}