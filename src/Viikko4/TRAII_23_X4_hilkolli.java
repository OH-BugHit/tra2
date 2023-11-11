package Viikko4;

import fi.uef.cs.tra.DiGraph;
import fi.uef.cs.tra.Edge;
import fi.uef.cs.tra.Vertex;

import java.util.HashSet;
import java.util.Set;


public class TRAII_23_X4_hilkolli implements TRAII_23_X4 {

    /**
     * ITSEARVIOINTI TÃ„HÃ„N:
     * Juurisolmutestin aikavaativuus O(v+e) jossa v = solmut ja e = kaaret
     * Puiden määrän aikavaativuus O(v+e) jossa v = solmut ja e = kaaret
     *
     * Ratkaisu on yksinkertainen ja toimiva. Puiden lukumäärän ratkaisussa käytetään hyödyksi alun kaarten läpikäyntiä.
     * Puiden määrän ratkaisussa saadaan heti kiinni solmut joihin tulee kaksi kaarta ja kyseisen potentiaalisen juurisolmun haku lopetetaan heti.
     * Tässä käytetään hyväkseen try-catch rakennetta jolloin rekursiosta karataan heittämällä poikkeus.
     * Kysyinkin eräällä luennolla mitä mieltä olet tavasta ja jäin siihen vaikutelmaan, että se on hyväksyttävää (joskin ei ehkä kaunista).
     * Ratkaisua ei voida mielestäni tehostaa.
     * Kaikki kaaret ja solmut käydään läpi aina vähintään kertaalleen mahdollisia juurisolmuja etsiessä.
     *
     **/
    
    /** Palauttaa joukkona kaikki ne suunnatun verkon solmut joihin ei johda yhtÃ¤Ã¤n kaarta.
     *
     * @param G syÃ¶teverkko
     * @return juurisolmujen joukko
     */
    @Override
    public Set<Vertex> juuriSolmut(DiGraph G) {
        Set<Vertex> tulos = new HashSet<>();
        mahdollisetJuurisolmut(G, tulos,0);
        return tulos;
    }

    /**
     * Lisää annettuun solmujen joukkoon potentiaaliset juurisolmut
     * @param G Tutkittava verkko
     * @param vSet Setti johon juurisolmut lisätään
     * @param kayttotapa Jos == 1 niin värjätään valkoisiksi mahdolliset juurisolmut, punaisiksi solmut joihin tulee enemmän kuin yksi kaari ja muut solmut harmaiksi.
     */
    private static void mahdollisetJuurisolmut(DiGraph G, Set<Vertex> vSet, int kayttotapa) {
        for (Vertex v: G.vertices()) { // O(v) käydään kaikki solmut läpi
            if (kayttotapa == 1) v.setColor(DiGraph.WHITE); // O(1) asetetaan kaikki valkoiseksi
            vSet.add(v); //O(1) Alussa kaikki solmut on mahdollisia, kunnes muuta todistetaan
        }

        for (Edge e: G.edges()) { // O(e) käydään kaikki kaaret läpi
            Vertex v = e.getEndPoint(); //O(1)
            if (kayttotapa == 1) v.setColor(v.getColor() == DiGraph.WHITE ? DiGraph.GRAY : DiGraph.RED); // Maalataan ensimmäisellä kerralla harmaaksi. Jos solmu löytyy uudelleen (ei ole valkoinen), tulee siihen vähintään kaksi kaarta eikä se voi olla puussa (lapsella tasan yksi vanhempi) tällöin maalataan punaiseksi.
            vSet.remove(v); // Poistetaan potentiaalisista puunjuurista kaikki johon tulee kaaria O(1)
        }
    }

    /**
     * Palauttaa moniko suunnatun verkon G komponentti on puu  (eli
     * sellainen komponentti jossa ei ole paluu-, ristikkÃ¤is- tai etenemiskaaria.
     * @param G syÃ¶teverkko
     * @return ehjien puiden lukumÃ¤Ã¤rÃ¤
     */
    @Override
    public int puidenLukumaara(DiGraph G) {
        Set<Vertex> potentiaalisetPuunJuuret = new HashSet<>();
        int tulos = 0;
        mahdollisetJuurisolmut(G, potentiaalisetPuunJuuret,1);

        for (Vertex v: potentiaalisetPuunJuuret) { // O(v) käydään läpi max v määrä solmuja ja max O(e) määrä kaaria (vain kehättömät mukana ). Täten O(v+e)
            try {
                tarkistaPuu(v);
                tulos++;
            } catch (Exception ignored) { //Hypättiin tulos++ yli, mikäli löytyi punainen solmu. Siirrytään seuraavaan potentiaaliseen puunjuureen O(1)
            }
        }
        return tulos;
    }

    /**
     * Puun rekursiivistä syvyyssuuntaista läpikäyntiä, jossa tarkistetaan löytyykö punaista solmua.
     * @param v Solmu josta hakua suoritetaan
     * @throws Exception Jos löytyy, niin heittää poikkeuksen
     */
    private void tarkistaPuu(Vertex v) throws Exception {
        if (v.getColor() == DiGraph.RED) { // Jos löytyy solmu johon tulee (vähintään) kaksi sisääntulevaa kaarta niin keskeytetään potentiaalisen puunjuuren läpikäynti.
            throw new Exception("Ei ole puu");
        }
        for (Vertex v2: v.neighbors()) { // Muussa tapauksessa jatketaan rekursiivisesti puun läpikäyntiä jossa siis tarkistetaan löytyykö solmua jolla on kaksi sisääntulevaa kaarta.
            tarkistaPuu(v2);
        }
    }
}