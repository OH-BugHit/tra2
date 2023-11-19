package Viikko5;

import fi.uef.cs.tra.Edge;
import fi.uef.cs.tra.Graph;
import fi.uef.cs.tra.Vertex;

import java.util.*;

public class TRAII_23_X5_hilkolli implements TRAII_23_X5 {

    /**
     * ITSEARVIOINTI TÃ„HÃ„N:
     *
     * Aluksi epäilytti saako tästä tehokasta, mutta tarkalla suunnitelmalla onnistui varsin kivuttomasti
     * Aikavaativuus: O(E)
     * Lahtösolmun kaarille suoritetaan korkeintaan (kaaret - lahtösolmun kaaret) * jotai vähemmän kun (kaaret - lahtösolmun kaaret) operaatioita.
     * Eli: Lähtösolmun kaaret * (kaaret-lähtösolmun kaaret) * x     | x < (kaaret-lähtösolmun kaaret)
     *
     * Eli: lähtösolmun kaaret * jotain vähemmän kun kaikki kaaret kun lähtösolmun kaaret > 0 * joku pienempi vakio kuin vasemmalla on ollut
     *
     * Lähtökaaret tai niiden looppi voi olla, kumpi vaan, suurempi tässä kertolaskussa, mutta korkeintaan E-1. Näin ollen. = O(E).
     * Lisäksi jos lähtösolmulla ei kaaria niin operaatio on O(1) ja jos kaikki kaaret niin O(E))
     *
     * Varmaankin paremmin sanottu että aikavaativuus on o(E^2)
     *
     * Tein paljon ajattelua ja analyysia ja mielestäni parantaa ei voi. Solmuhin ei kannata tallentaa tietoa edessä olevasta verkosta (tämä periaatteessa muuten voisi olla ainoa keino parantaa).
     **/
    /**
     * Kaikki erilaiset annetusta solmusta lahtoSolmu lÃ¤htevÃ¤t
     * korkeintaan maxPaino painoiset yksinkertaiset polut.
     * Polut palautetaan polkujen joukkona.
     * Polut palautetaan solmujen listana siten, ettÃ¤ polun
     * perÃ¤kkÃ¤isten solmujen vÃ¤lillÃ¤ on kaari syÃ¶teverkossa.
     * Polulla on vÃ¤hintÃ¤Ã¤n kaksi solmua (ja yksi kaari).
     * Polun paino on polun kaarten painojen summa.
     * Verkossa ei ole negatiivispainoisia kaaria.
     * Yksikertaisella polulla ei ole kehÃ¤Ã¤ (ts. siinÃ¤ ei ole mitÃ¤Ã¤n solmua kahdesti)
     * @param verkko syÃ¶teverkko
     * @param lahtoSolmu lÃ¤htÃ¶solmu
     * @param maxPaino polkujen maksimipaino
     * @return polkujen joukko
     */
    @Override
    public Set<List<Vertex>> kaikkiMaxPPolut(Graph verkko, Vertex lahtoSolmu, float maxPaino) {
        HashSet<List<Vertex>> tulos = new HashSet<>();
        boolean oliLahto = false;
        for (Edge e: lahtoSolmu.edges()) { // Tämmönen pieni kikka tänne et saadaan O(1) jos ei lähtösolmuja. Väritys muuten tekee O(E)
            oliLahto = true;
            break;
        }
        if (oliLahto) {
            for (Edge kaikki : verkko.edges()) { // Väritetään kaikki kaaret valkoiseksi // Tästä tulee nyt ainakin se O(E)
                kaikki.setColor(Graph.WHITE); // Tämän voisi jättää väliin jos oletettaisiin kaikkien kaarien olevan varmasti värittömiä. Niin ne taitaa ollakin, mut en pidä olettamisesta.
            }
            for (Edge kaari : lahtoSolmu.edges()) { // Lähdetään liikkeelle lähtösolmun kaaria pitkin
                if (kaari.getWeight() <= maxPaino) { // Mutta vain, mikäli lähtösolmun kaaren paino on max maxPaino
                    lahtoSolmu.setColor(Graph.BLACK); // Väritetään lähtösolmu käytetyksi, jottei sitä lisätä uudestaan. (Kehä)
                    List<Vertex> reitti = new LinkedList<>(); // Luodaan lista johon polku tallennetaan. Tämä sitten lisätään tulokseen
                    reitti.add(lahtoSolmu); // listaan lisätään tämä aina polun aloitussolmuksi
                    selevitaMaxPolut(kaari, reitti, maxPaino, tulos, lahtoSolmu); // Selvitetään rekursiivisesti polut. Polut lisätään joka rekursion tasolla suoraan tulokseen
                    lahtoSolmu.setColor(Graph.WHITE); // Tämä ei ole pakollinen sillä for-loopissa se muutetaan kuitenkin takaisin black. Halusin vaan jättää solmun valkoiseksi lopussa.
                }
            }
        }
        return tulos; // Palautetaan polkulistojen joukko
    }

    /**
     * Metodi lisää tulosjoukkoon kaikki polut jonne asti päästään käytettävissä olevalla painolla
     * @param kaari Kaari, jota pitkin tullaan solmuun
     * @param reitti Polku, jota pitkin ollaan päädytty tähän rekursiotasoon
     * @param painoaJaljella Paljonko on käytettävissä painoa tässä rekursiotasossa
     * @param tulos Tulosjoukko, johon kelvolliset polut lisätään
     * @param edellinenSolmu Viittaus edelliseen solmuun. Tarvitaan, jotta kaaresta saadaan vastakkainen pää
     */
    private void selevitaMaxPolut(Edge kaari, List<Vertex> reitti, float painoaJaljella, HashSet<List<Vertex>> tulos, Vertex edellinenSolmu) { //  <---------------------
        if (kaari.getWeight() <= painoaJaljella && kaari.getColor() == Graph.WHITE) { // Tarkistetaan onko riittävästi painoa käytettävissä ja onko kaari käyttämätön    |
            kaari.setColor(Graph.RED); // Merkitään kaari käytetyksi                                                                                                     |
            painoaJaljella -= kaari.getWeight(); // Vähennetään painoa, _jota välitetään eteenpäin_                                                                      |
            reitti.add(kaari.getEndPoint(edellinenSolmu)); // Lisätään polkuun, jota pitkin on päästy tänne, solmu johon pääsiin                                         |
            kaari.getEndPoint(edellinenSolmu).setColor(Graph.BLACK); // Väritetään käytetty solmu mustaksi                                                               |
            List<Vertex> lisays = new LinkedList<>(reitti); // Lisätään uusin solmu polkuun                                                                              |
            tulos.add(lisays); // Lisätään uusi reitti (joka on nyt yhden solmun pidempi), tulokseen                                                                     |
            for (Edge kaariSeuraavaTaso: kaari.getEndPoint(edellinenSolmu).edges()) { // Aletaan käymään päästystä solmusta eteenpäin kaaria.                            |
                if (kaariSeuraavaTaso.getEndPoint(kaari.getEndPoint(edellinenSolmu)).getColor() != Graph.BLACK) {// Tarkistetaan ettei kaaren päässä ole käytetty solmu! |
                    selevitaMaxPolut(kaariSeuraavaTaso, reitti, painoaJaljella, tulos, kaari.getEndPoint(edellinenSolmu)); // Homma jatkuu rekursiossa ------------------
                }
            } // Kun käyty tämä reitti loppuun niin
            reitti.remove(reitti.size()-1); // Poistetaan solmu polulta, sillä kun palataan rekursiossa ylöspäin, niin tämä solmu ei kuulu enää mahdolliseen seuraavaan uuteen polkuun.
        }
        kaari.setColor(Graph.WHITE); // Väritellään kaari taas käytettäväksi
        kaari.getEndPoint(edellinenSolmu).setColor(Graph.WHITE); // Väritetään solmu taas käytettäväksi
    }
}