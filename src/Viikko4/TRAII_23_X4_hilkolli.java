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
     * Ratkaisu on yksinkertainen ja toimiva.
     * Mahdollisissa juurisolmuissa rajasin pois kaikki solmut joihin tulee kaaria.
     * Puiden lukumäärässä hain ongelmaan ratkaisua siitä, että puun solmulla voi olla vain yksi tulokaari. Ratkaisussa käytetään hyödyksi alun kaarten läpikäyntiä.
     * Puiden määrän ratkaisussa saadaan heti kiinni solmut joihin tulee kaksi kaarta ja kyseisen potentiaalisen juurisolmun haku lopetetaan heti.
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
     * @param kayttotapa Jos == 1 niin värjätään valkoisiksi mahdolliset juurisolmut, punaisiksi solmut joihin tulee enemmän kuin yksi kaari sekä tällaista solmua edeltävä solmu myös. Muut harmaiksi.
     */
    private static void mahdollisetJuurisolmut(DiGraph G, Set<Vertex> vSet, int kayttotapa) {
        for (Vertex v: G.vertices()) { // O(v) käydään kaikki solmut läpi
            if (kayttotapa == 1) v.setColor(DiGraph.WHITE); // O(1) asetetaan kaikki valkoiseksi
            vSet.add(v); //O(1) Alussa kaikki solmut on mahdollisia, kunnes muuta todistetaan
        }

        for (Edge e: G.edges()) { // O(e) käydään kaikki kaaret läpi
            Vertex v = e.getEndPoint(); //O(1)
            if (kayttotapa == 1) {
                if (v.getColor() == DiGraph.WHITE) {
                    v.setColor(DiGraph.GRAY); // Maalataan solmu ensimmäisellä kerralla tavatessa harmaaksi.
                } else {
                    v.setColor(DiGraph.RED); // Jos solmu löytyy uudelleen (ei ole valkoinen), tulee siihen vähintään kaksi kaarta eikä se voi olla puussa (lapsella tasan yksi vanhempi) tällöin maalataan punaiseksi.
                    e.getStartPoint().setColor(DiGraph.RED); // Maalataan myös tämä kaaren lähtösolmu niin saadaan mahdollisesti tehostettua seuraavaa vaihetta.
                };
            }
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
            if (!tarkistaPuu(v)) { // Jos ei löydy punaista solmua niin oli kelpo puu.
                tulos++;
            }
        }
        return tulos;
    }

    /**
     *
     * Puun rekursiivistä syvyyssuuntaista läpikäyntiä, jossa tarkistetaan löytyykö punaista solmua.
     * @param v Solmu josta hakua suoritetaan
     * @return palauttaa totuusarvon, löytyikö punaista solmua
     */
    private boolean tarkistaPuu(Vertex v) {
        if (v.getColor() == DiGraph.RED) { // Jos löytyy solmu johon tulee (vähintään) kaksi sisääntulevaa kaarta niin keskeytetään potentiaalisen puunjuuren läpikäynti.
            return true;
        }
        for (Vertex v2: v.neighbors()) { // Muussa tapauksessa jatketaan rekursiivisesti puun läpikäyntiä jossa siis tarkistetaan löytyykö solmua jolla on kaksi sisääntulevaa kaarta.
            if (tarkistaPuu(v2)) {
                return true;
            };
        }
        return false;
    }
}