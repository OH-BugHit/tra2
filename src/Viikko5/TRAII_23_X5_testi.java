package Viikko5;// TRAII_23_X5_testi.java SJ

import Apuluokat.GraphMaker;
import fi.uef.cs.tra.Graph;
import fi.uef.cs.tra.Edge;
import fi.uef.cs.tra.Vertex;

import java.util.*;

public class TRAII_23_X5_testi {

    static TRAII_23_X5 testattava = new TRAII_23_X5_pohja();
    //                                              ^^^^ oma tunnus tÃ¤hÃ¤n

    static Random rnd = new Random();

    public static void main(String[] args) {

        // komentoriviparametrina saatu solmujen mÃ¤Ã¤rÃ¤ koko
        int N = 10;
        if (args.length > 0)
            N = Integer.parseInt(args[0]);

        int E = 10;
        if (args.length > 1)
            E = Integer.parseInt(args[1]);

        // satunnaislukusiemen
        int siemen = 42;
        if (args.length > 2)
            siemen = Integer.parseInt(args[2]);
        rnd.setSeed(siemen);

        // tulostusten mÃ¤Ã¤rÃ¤
        int print = 8;
        if (args.length > 3)
            print = Integer.parseInt(args[3]);


        boolean ok = true;

        boolean oke = testaaKaikkiPPolutEsim(print);
        if (oke)
            System.out.println("\nEsimerkkiverkolla kustakin testistÃ¤ tuli oikea mÃ¤Ã¤rÃ¤ kelvollisia polkuja.");


        ok &= testaaKaikkiPPolut(2, 1, 5f, 10f, siemen, print);
        ok &= testaaKaikkiPPolut(2, 1, 50f, 10f, siemen, print);
        ok &= testaaKaikkiPPolut(3, 3, 5f, 10f, siemen, print);
        ok &= testaaKaikkiPPolut(4, 3, 5f, 10f, siemen, print);
        ok &= testaaKaikkiPPolut(5, 5, 5f, 10f, siemen, print);
        ok &= testaaKaikkiPPolut(N, E, 5f, 10.0f, siemen, print);


        rnd.setSeed(System.currentTimeMillis());
        int nTest = 50;
        int k;
        int virheet = 0;
        for (k = 0; k < nTest; k++) {
            if (!testaaKaikkiPPolut(rnd.nextInt(k/3+1)+1, rnd.nextInt(k/2+1)+2,
                    rnd.nextFloat()*10+1, rnd.nextFloat()*20+11, (int)(System.currentTimeMillis()), 0))
                virheet++;
            if (virheet > 10)
                break;
        }
        if (virheet > 0)
            ok = false;
        System.out.println("\n" + k + " satunnaisesta polkusolmutestistÃ¤ " + (k - virheet) + " ei antanut virheellisiÃ¤ polkuja.");

        ok = ok && oke;

        if (ok)
            System.out.println("\nKaikki tehdyt polkutestit antoivat kelvollisia polkuja.");
        else
            System.out.println("\nJoissain poluissa virheitÃ¤ tai puutteita.");



        System.out.println("Ohjelma EI testaa sitÃ¤ tulivatko kaikki mahdolliset polut tulokseen.");

    } // main()


    /**
     * Testaa kaikkien polkujen hakua etukÃ¤teen lasketuille tuloksille
     * @param print tulostuksen mÃ¤Ã¤rÃ¤
     * @return true jos kaikki polut ovat kelvollisia, muuten false
     */
    static boolean testaaKaikkiPPolutEsim(int print) {
        if (print > 0)
            System.out.println("\nPolkutestiEsim");

        // haetaan esimerkkiverkko ja saadaan testit ja tulokset
        List<testiTulos> tl = new ArrayList<>();
        Graph G = teeEsimSyote(tl, print);

        boolean ok = true;

        for (testiTulos tt : tl) {
            if (print > 1)  System.out.println("\n  Testataan solmusta " + tt.alku + " alkavat max " + tt.maxPituus + " polut");
            Set<List<Vertex>> tulos = testattava.kaikkiMaxPPolut(G, tt.alku, tt.maxPituus);
            if (print > 1) System.out.println("  Saatiin " + tulos.size() +  " polkua, odotus oli " + tt.polkuja);
            if (tulos.size() != tt.polkuja) {
                ok = false;
            }
            ok &= onkoPPolkuja(tulos, tt.alku, tt.maxPituus, print);
        }

        if (print > 0)
            System.out.println("Polkutesti tulos " + ok);

        return ok;

    }



    /**
     * Testaa kaikkien polkujen hakua
     * @param solmuja solmujen mÃ¤Ã¤rÃ¤
     * @param kaaria kaarten mÃ¤Ã¤rÃ¤
     * @param maxKaariPaino suurin kaarten paino
     * @param maxPolkuPaino testattava polkujen paino
     * @param siemen kÃ¤ytettÃ¤vÃ¤ satunnaislukusiemen
     * @param print tulostuksen mÃ¤Ã¤rÃ¤
     * @return true jos kaikki polut ovat kelvollisia, muuten false
     */
    static boolean testaaKaikkiPPolut(int solmuja, int kaaria, float maxKaariPaino, float maxPolkuPaino, int siemen, int print) {
        if (print > 0)
            System.out.println("\nPolkutesti, solmuja=" + solmuja + " kaaria="+kaaria + " max paino=" + maxPolkuPaino);
        Graph G = teeSyote(solmuja, kaaria, maxKaariPaino, siemen, print);

        boolean ok = true;

        for (Vertex v : G.vertices()) {
            if (print > 1)  System.out.println("  Testataan solmusta " + v + " alkavat max " + maxPolkuPaino + " polut");
            Set<List<Vertex>> tulos = testattava.kaikkiMaxPPolut(G, v, maxPolkuPaino);
            ok &= onkoPPolkuja(tulos, v, maxPolkuPaino, print);
        }

        if (print > 0)
            System.out.println("Polkutesti tulos " + ok);

        return ok;

    }



    /**
     * Tarkastaa onko annettu polku kelvollinen.
     * @param polku polku
     * @param eka odotettu polun ensimmÃ¤inen solmu
     * @param maxPaino maksimipaino
     * @param print tulostuksen mÃ¤Ã¤rÃ¤
     * @return true jos polku on kelvollinen, muuten false
     */
    static boolean onkoPPolku(List<Vertex> polku, Vertex eka, float maxPaino, int print) {
        float paino = 0;
        boolean ok =  true;
        if (print > 4) System.out.println("    Tarkastetaan polkua: " + polku);
        if (polku.size() < 2) {
            if (print > 2) System.out.println("        Liian lyhyt polku: " + polku);
            return false;
        }
        ListIterator<Vertex> li = polku.listIterator();
        Vertex edell = li.next();
        if (edell != eka) {
            if (print > 2) System.out.println("        VÃ¤Ã¤rÃ¤ eka solmu: " + edell + ", piti olla: " + eka);
            ok = false;
        }

        if (polku.size() != new HashSet<>(polku).size()) {
            if (print > 2) System.out.println("        polulla joku solmu kahdesti: " + polku);
            ok = false;
        }

        while (li.hasNext()) {
            Vertex seur = li.next();
            Edge e = edell.getEdge(seur);
            if (e == null) {
                if (print > 2) System.out.println("        Kaarta: " + edell + "-" + seur + " ei ole!");
                ok =  false;
            } else
                paino += e.getWeight();
            edell = seur;
        }
        if (print > 2 && paino > maxPaino+0.01f) System.out.println("       Liian painava (" + paino + " max=" +maxPaino+ ") polku: " + polku);
        return (ok && paino <= maxPaino+0.01f); // sallitaan pyÃ¶ristysvirheet
    }

    /**
     * Testaa onko annetussa polkujen joukkossa kelvollisia polkuja.
     * @param S polkujen joukko
     * @param eka odotettu kunkin polun eka solmu
     * @param maxPaino polun maksimipaino
     * @param print tulostuksen mÃ¤Ã¤rÃ¤
     * @return true jos kaikki polut ovat kelvollisia, muuten false
     */
    static boolean onkoPPolkuja(Set<List<Vertex>> S, Vertex eka, float maxPaino, int print) {
        boolean ok = true;
        if (print > 3) System.out.println("  Tarkastetaan polkulistaa, polkuja on " + S.size() + " kpl");
        for (List<Vertex> L : S) {
            ok &= onkoPPolku(L, eka, maxPaino, print);
        }
        if (print > 3) System.out.println("  Polkujen kelvollisuuden tarkastuksen tulos: " + ok);
        return ok;
    }

    /**
     * Generoi syÃ¶teverkon
     * @param solmuja solmujen mÃ¤Ã¤rÃ¤
     * @param kaaria kaarten mÃ¤Ã¤rÃ¤
     * @param maxKaariPaino suurin kaarten paino
     * @param siemen kÃ¤ytettÃ¤vÃ¤ satunnaislukusiemen
     * @param print tulostuksen mÃ¤Ã¤rÃ¤
     * @return uusi verkko
     */
    static Graph teeSyote(int solmuja, int kaaria, float maxKaariPaino, int siemen, int print) {
        Graph G = GraphMaker.createGraph(solmuja, kaaria, siemen, maxKaariPaino);
        if (print > 2) {
            System.out.println("  Verkko: \n" + GraphMaker.toString(G, 1));
        }

        return G;
    }


    /**
     * Rakentaa esimerkkiverkon ja luo nipun kÃ¤sin laskettuja polkumÃ¤Ã¤riÃ¤
     * @param tuloksia lista johon kÃ¤sin laskettuja tuloksia tallennetaan
     * @return syÃ¶teverkko
     */
    static Graph teeEsimSyote(List<testiTulos> tuloksia, int print) {
        Graph G = new Graph();
        Vertex[] V = new Vertex[6];
        V[0] = G.addVertex("a");
        V[1] = G.addVertex("b");
        V[2] = G.addVertex("c");
        V[3] = G.addVertex("d");
        V[4] = G.addVertex("e");
        V[5] = G.addVertex("f");

        V[0].addEdge(V[1], 6f);
        V[0].addEdge(V[2], 3f);
        V[1].addEdge(V[3], 2f);
        V[2].addEdge(V[3], 1f);
        V[2].addEdge(V[4], 8f);
        V[2].addEdge(V[5], 4f);
        V[3].addEdge(V[4], 9f);
        V[3].addEdge(V[5], 7f);
        V[4].addEdge(V[5], 5f);

        if (print > 1) {
            System.out.println(GraphMaker.toString(G, 1));
            System.out.println("a---3---c---4---f");
            System.out.println("|       |\\     /|");
            System.out.println("|       | 8   7 |");
            System.out.println("|       |  \\ /  |");
            System.out.println("6       1   X   5");
            System.out.println("|       |  / \\  |");
            System.out.println("|       | /   \\ |");
            System.out.println("|       |/     \\|");
            System.out.println("b---2---d---9---e");
        }

        tuloksia.add(new testiTulos(V[0], 2, 0));
        tuloksia.add(new testiTulos(V[0], 3, 1));
        tuloksia.add(new testiTulos(V[0], 7, 5));
        tuloksia.add(new testiTulos(V[0], 8, 6));
        tuloksia.add(new testiTulos(V[2], 15, 16));
        tuloksia.add(new testiTulos(V[2], 20, 25));
        tuloksia.add(new testiTulos(V[2], 8, 6));
        tuloksia.add(new testiTulos(V[3], 8, 6));
        tuloksia.add(new testiTulos(V[3], 100, 32));

        return G;
    }


    /**
     * apuluokka tallentamaan testitapauksia
     * Ã¤lÃ¤ kÃ¤ytÃ¤ tÃ¤tÃ¤ algoritmissasi
     */
    static class testiTulos {
        Vertex alku;
        float maxPituus;
        int polkuja;

        public testiTulos(Vertex alku, float maxPituus, int polkuja) {
            this.alku = alku;
            this.maxPituus = maxPituus;
            this.polkuja = polkuja;
        }
    }







}