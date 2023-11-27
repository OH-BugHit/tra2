package Viikko5;

import Apuluokat.GraphMaker;
import fi.uef.cs.tra.AbstractGraph;
import fi.uef.cs.tra.Edge;
import fi.uef.cs.tra.Graph;
import fi.uef.cs.tra.Vertex;
import org.w3c.dom.html.HTMLParagraphElement;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class TRAII_23_t19_20_pohja {

    public static void main(String[] args) {

        // defaults
        int vertices = 70;
        int edges = 80;

        if (args.length > 0)
            vertices = Integer.parseInt(args[0]);

        if (args.length > 1)
            edges = Integer.parseInt(args[1]);

        int seed = vertices+edges;

        if (args.length > 2)
            seed = Integer.parseInt(args[2]);

        Graph graph;


        System.out.println("\nVerkko1: ");
        graph = GraphMaker.createGraph(vertices, edges, seed);
        System.out.print(GraphMaker.toString(graph, 0));

        System.out.print("YhtenÃ¤inen: ");
        boolean yhten = onkoYhtenainen(graph);
        System.out.println(yhten + "\n");
        if (! yhten) {   // kutsutaan tehtÃ¤vÃ¤Ã¤ 19
            System.out.println("TÃ¤ydennys:");
            taydennaYhtenaiseksi(graph);
            System.out.print(GraphMaker.toString(graph, 0));
        }

        yhten = onkoYhtenainen(graph);
        System.out.println("YhtenÃ¤inen nyt: " + yhten);
        
        if (yhten) {
            System.out.print("\nLeikkaussolmut:   ");
            List<Vertex> ls = leikkausSolmut(graph);
            System.out.println(ls);
            if (! ls.isEmpty()) {
                System.out.println("TÃ¤ydennys:");
                taydenna2yhtenaiseksi(graph);
            }
            System.out.print("Leikkaussolmut nyt:   ");
            System.out.println(leikkausSolmut(graph));
            System.out.println(GraphMaker.toString(graph, 0));
        }


    }

    /**
     * Onko annettu verkko yhtenÃ¤inen vai ei.
     * @param G syoteverkko
     * @return true jos verkko on yhtenÃ¤inen, muuten false
     */
    static boolean onkoYhtenainen(Graph G) {
        // kaikki valkoisiksi
        varita(G, Graph.WHITE);

        // syvyyssyyntainen lÃ¤pikynti jostain solmusta
        Vertex w = G.firstVertex();
        dfsColor(w, Graph.BLACK);

        // onko vielÃ¤ valkoiseksi jÃ¤Ã¤neitÃ¤ solmuja
        for (Vertex v : G.vertices())
            if (v.getColor() == Graph.WHITE)
                return false;
        return true;
    }

    /**
     * tehtävä 19
     * Aikavaativuus = O(V). Käydään syvyyshaulla verkko läpi. Käydään jokainen solmu vain kerran läpi.
     * TÃ¤ydennÃ¤ epÃ¤yhtenÃ¤inne verkko yhtenÃ¤iseksi lisÃ¤Ã¤mÃ¤llÃ¤ kaaria komponenttien vÃ¤lille.
     * Jos verkko on jo yhtenÃ¤inen, ei tarvitse tehdÃ¤ mitÃ¤Ã¤n.
     * @param G syÃ¶teverkko
     */
    static void taydennaYhtenaiseksi(Graph G) {
        int indeksi = 1; // Indeksi jolla merkitään eri komponenttien solmut keskenään samaksi
        LinkedList<Vertex> eriVerkot = new LinkedList<>();
        for (Vertex v: G.vertices()) {
            v.setIndex(0); // Aloitetaan merkkaamalla verkko kokonaan 0
        }
        for (Vertex v: G.vertices()) {
            if (v.getIndex() == 0) { // Jos indeksi on nolla (eli ei vielä muokkailtu
                eriVerkot.add(v); // lisätään aloituspiste listaan, joka lopuksi yhdistetään toisiinsa
                merkkaaVerkko(v,indeksi); // merkataa verkko joka lähtee aloitusverkosta
                indeksi++; // kasvatetaan indeksiä seuraavan komponentin merkkaamiseksi eri indeksillä
            }
        }
        while (eriVerkot.size() > 1) {
            eriVerkot.removeLast().addEdge(eriVerkot.getFirst()); // Lisätään kaikista komponenteista kaari ensimmäisenä läpikäytyyn solmuun. Solmusta 0 (eli ensimmäisen komponentin ensimmäisestä solmusta) tulee tosin aika houketteleva leikkaussolmu.
        }
    }   // taydennaYhtenaiseksi()

    private static void merkkaaVerkko(Vertex v, int indeksi) {
        if (v.getIndex() == 0) { // Kehien poisto
            v.setIndex(indeksi); // Merkitään komponentin mukaisella indeksinumerolla
            for (Vertex v2: v.neighbors()) { // Ja käydään koko verkko läpi...
                    merkkaaVerkko(v2, indeksi);
            }
        }
    }


    /**
     * syvyyssuuntainen lÃ¤pikynti vÃ¤ritten vÃ¤rillÃ¤ c
     * @param v aloitussolmu
     * @param color kÃ¤ytettÃ¤vÃ¤ vÃ¤ri
     */
    static void dfsColor(Vertex v, int color) {
        v.setColor(color);

        for (Vertex w : v.neighbors())
            if (w.getColor() != color)
                dfsColor(w, color);
    }

    /**
     * VÃ¤ritÃ¤ verkon kaikki solmut annetun vÃ¤risiksi.
     * @param G vÃ¤ritettÃ¤vÃ¤ verkko
     * @param c kÃ¤ytettÃ¤vÃ¤ vÃ¤ri
     */
    static void varita(AbstractGraph G, int c) {
        for (Vertex v : G.vertices())
            v.setColor(c);
    }


    /**
     * Tehtävä 20.
     * Voi tehdä ylimääräisen kaaren sattumalta jos kahden eri komponentin leikkaussolmut ovat naapureita keskenään ja ne kaksi kaarta eivät ole naapureita keskenään.
     * TÃ¤ydennÃ¤ verkko 2-yhtenÃ¤iseksi.
     * LisÃ¤Ã¤ kaaria siten, ettei verkossa enÃ¤Ã¤ ole leikkaussolmuja eikÃ¤ siltoja.
     * @param G syÃ¶teverkko
     */
    static void taydenna2yhtenaiseksi(Graph G) {
        int testataanLyheniko = -1;
        while (true) { // Tehdään niin monta kertaa kuin leikkaussolmuja vielä löytyy
            for (Vertex v: G.vertices() // Asetetaan kaikkien solmujen indeksinumeroksi -1, leikkaussolmut() käyttää ainakin solmujen indeksejä
                 ) {
                v.setIndex(-1);
            }
            LinkedList<Vertex> L = (leikkausSolmut(G)); // Luodaan lista leikkaussolmuista
            if (L.isEmpty()) { // Mikäli lista on tyhjä, while(true)-looppi katkeaa (Ei enää leikkaussolmuja)
                break;
            }
            if (testataanLyheniko == L.size()) { // Jos lista ei lyhentynyt edellisestä iteraatiosta niin kyseessä on ketju. Algoritmini ei tunnista ketjuja suoraan, mutta tällä tavalla ne saadaan kiinni ja algotimi jatkaa siitä.
                boolean done = false; // Ketjussa erilliset komponentit ovat usein naapureita
                for (Vertex v: G.vertices()) { // Otetaan ketjusta sattumalta solmu // Vain kolmen ketjussa saatetaan tarvita toinen "arvonta"
                    for (Vertex v2: G.vertices()) { // Otetaan ketjusta sattumalla toinen solmu ja toistetaan tätä, mikäli seuraavat ehdot eivät täyty:
                        if (v != v2 && !v.isAdjacent(v2)) { // solmut eivät saa olla samat ja eivät saa olla naapureita keskenään.
                            v.addEdge(v2); // Lisätään kaari näiden välille
                            done = true; // merkataan true, että saadaan break myös ulommassa loopissa
                            break;
                        }
                    }
                    if (done) break;
                }
            }
            testataanLyheniko = L.size(); // Tässä otetaan talteen leikkaussolmujen koko. Jos seuraavalla while(true) kierroksella edelleen sama niin jää kiinni.
            int indeksi = 0; // Indeksinumerot alkaa 0+1
            for (Vertex alustaSolmu : G.vertices()) {
                alustaSolmu.setIndex(-1); // Asetetaan kaikkien solmujen indeksinumeroksi -1, leikkaussolmut() käyttää ainakin solmujen indeksejä
            }

            HashMap<Integer, List<Vertex>> leikkaussolmunNaapurit = new HashMap<>(); // Kuvaukseen talletetaan <Komponentin solmujen indeksinumero, komponentin solmut listana>

            for (Vertex leikkausSolmu : L) { // Käydään leikkaussolmut läpi eri komponenttien merkitsemiseksi ja leikkaussolmujen merkitsemiseksi
                indeksi++; // indeksinumero leikkaussolmun naapureille, alkaa numerosta 1
                leikkausSolmu.setIndex(0); // leikkaussolmut indeksoidaan 0
                for (Edge naapuriin : leikkausSolmu.edges()) { // etsitään leikkaussolmun naapurit indeksointia varten
                    if (naapuriin.getEndPoint(leikkausSolmu).getIndex() != 0) { // Jos leikkaussolmun naapuri on leikkaussolmu (ei toki haluta yhdistää kahta leikkaussolmua toisiinsa) nin ei muokata sitä sitten
                        naapuriin.getEndPoint(leikkausSolmu).setIndex(indeksi); // Ja jos ei ollut niin annetaan sille oman 2-yhteisen komponentin indeksi
                        List<Vertex> tmp = new LinkedList<>(); // luodaan lista näistä naapureista
                        if (leikkaussolmunNaapurit.containsKey(indeksi)) {
                            tmp = leikkaussolmunNaapurit.get(indeksi); // ja jos sellainen oli jo niin otetaan ne mukaan??
                        }
                        tmp.add(naapuriin.getEndPoint(leikkausSolmu));
                        leikkaussolmunNaapurit.put(indeksi, tmp); // No nyt on sitten kuvauksessa (tai kun kaikki for-loopissa käyty läpi niin sitten on) <indeksi, komponentin solmut listana>
                    }
                }
            }

            // leikkaussolmun naapureille on nyt sitten annettu indeksinumerot ja ne on lisätty kuvaukseen: key = indeksinumero, value = leikkaussolmun naapurit, seuraavaksi liitetään eri keyn omaavat komponentit toisiinsa...
            if (leikkaussolmunNaapurit.size() == 1) { // erikoistapaus, että leikkaussolmuja vain yksi! Tällöin tyydytään yhdistämään naapurit joilla ei ole kaaria keskenään. (Vois olla vaikka tähtiverkko)
                for (Vertex naapuri : leikkaussolmunNaapurit.get(indeksi)) { // Valitaan yksi tästä komponenttijoukosta (tulee break aina)
                    for (Vertex naapuri2 : leikkaussolmunNaapurit.get(indeksi)) { // käydään läpi se jäljellä oleva komponentti
                        if (naapuri != naapuri2) { // Ja kunhan ei lisätä itseensä
                            if (!naapuri.isAdjacent(naapuri2)) { // Eikä valmiiksi ole jo naapuri
                                naapuri.addEdge(naapuri2); // Niin lisätään kaari solmujen välille
                            }
                        }
                    }
                    break;
                }
            }

            if (leikkaussolmunNaapurit.size() > 1) { // Ja sitten se pyörittely kun on enemmän kuin yksi jäljellä
                Vertex ydinsolmu = leikkaussolmunNaapurit.get(indeksi).get(0); // Valitaan ns. "ydinsolmu" josta tulee kyllä tosi hyvä leikkauskohde kans... otetaan viimeisen joukon ensimmäinen solmu
                indeksi--; // Samaa indeksiä käytetään kahteen asiaan. Tällä vähennyksellä asetetaan leikkaussolmunNaapurit.getin hakunumero oikein ja vaihdetaan listas
                for (int j = indeksi; j > 0; j--) { //
                 List<Vertex> joukko = leikkaussolmunNaapurit.get(indeksi);
                    for (Vertex solmu : joukko) {
                        if (!ydinsolmu.isAdjacent(solmu) && ydinsolmu != solmu) {
                            ydinsolmu.addEdge(solmu);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Leikkaussolmut yhtenÃ¤isestÃ¤ verkosta.
     * @param g syÃ¶teverkko
     * @return leikkaussolmujen lista (tyhjÃ¤ lista jollei ole leikkaussolmuja)
     */
    static LinkedList<Vertex> leikkausSolmut(Graph g) {

        // numeroidaan solmut, kunkin solmun index-kenttÃ¤Ã¤n
        GraphMaker.getVertexArrayIndex(g);
        int n = g.size();

        // solmujen ja kaarten pohjavÃ¤ri
        varita(g, Graph.WHITE);
        for (Edge e : g.edges())
            e.setColor(Graph.WHITE);

        // taulukot
        int[] dfsnumber = new int[n];
        int[] low = new int[n];
        int i = 0;
        LinkedList<Vertex> L = new LinkedList<Vertex>();
        // komponentti kerrallaan
        for (Vertex v : g.vertices())
            if (v.getColor() == Graph.WHITE)
                i = numberdfs(v, dfsnumber, low, i, L, null);

        return L;
    }

    // fdsnumerointi taulukkoon, samalla luokittelee kaaret
    // puukaaret mustiksi, paluukaaret harmaiksi
    // isa on solmu josta tÃ¤tÃ¤ kutsutaan
    static int numberdfs(Vertex v, int[] dfsnumber, int[] low, int i,
                  LinkedList<Vertex> L, Vertex isa) {
        v.setColor(Graph.BLACK);
        dfsnumber[v.getIndex()] = i++;

        // dfs rekursio ja kaarien luokittelu
        // naapurien lÃ¤pikÃ¤ynnin kaarien avulla
        for (Edge e : v.edges()) {

            // kaari on jo kÃ¤sitelty toiseen suuntaan (isÃ¤-kaari,
            // tai jÃ¤lkelÃ¤isen paluukaari 
            if (e.getColor() != Graph.WHITE)
                continue;

            // naapurisolmu
            Vertex w = e.getEndPoint(v);

            if (w.getColor() == Graph.WHITE) {
                e.setColor(Graph.BLACK);    // puukaari
                // rekursiokutsu
                i = numberdfs(w, dfsnumber, low, i, L, v);
            } else if (w.getColor() == Graph.BLACK)
                e.setColor(Graph.GRAY); // paluukaari
        } // for(v.edges)

        // kaikkien rekursiokutsujen jÃ¤lkeen (jÃ¤lkijÃ¤rjestys)
        // low-arvon laskenta
        int min = dfsnumber[v.getIndex()];
        for (Edge e : v.edges()) {
            Vertex w = e.getEndPoint(v);
            if (w == isa)   // isÃ¤Ã¤ ei lasketa
                continue;

            // lasten low-luvut
            if (e.getColor() == Graph.BLACK) {
                if (low[w.getIndex()] < min)
                    min = low[w.getIndex()];

            // esi-isien (joihin paluukaari) dfsnumerot    
            } else if (e.getColor() == Graph.GRAY) {
                if (dfsnumber[w.getIndex()] < min)
                    min = dfsnumber[w.getIndex()];
            }

        }   // for(v.edges)

        low[v.getIndex()] = min;

        // leikkaussolmujen tunnistus
        if (v.getIndex() == 0) { // juurisolmu (vain yksi)
            int poikia = 0;
            // lasten lkm dfs-puussa
            for (Edge e : v.edges())
                if (e.getColor() == Graph.BLACK)
                    poikia++;
            if (poikia > 1)
                L.add(v);

        // muut solmut
        } else {
            // poika w, jolle low[w] >= dfnumber[v]
            for (Edge e : v.edges())
                if (e.getColor() == Graph.BLACK) {
                    Vertex w = e.getEndPoint(v);
                    if (low[w.getIndex()] >= dfsnumber[v.getIndex()]) {
                        L.add(v);
                        break;
                    }
                } // if BLACK
        } // else

        return i;   // palautetaan numeroitujen solmujen mÃ¤Ã¤rÃ¤
    }   // numberdfs() 

    // \leikkaussolmut


}