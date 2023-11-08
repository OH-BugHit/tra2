package Viikko3;

import java.util.*;

public class TRAII_23_X3_hilkolli implements TRAII_23_X3 {

    /**
     * Itsearviointi:
     * Muuttuvasta syötekoosta johtuen muutan testauksessa suoritettavien testien määrää syötteen mukaan.
     * Näin saadaan tarkempi arvo jo pienillä syötteillä ja syötekoon kasvaessa testaukseen käytetty aika ei pitene kohtuuttomaksi (esim. kasva lineaarisesti).
     * Yksittäisten testien häiriötä on poistettu ottamalla useamman ajon keskiarvo.
     * Yksittäisen poikkeavan keskiarvon häiriötä on poistettu tekemällä keskiarvoajo useaan kertaan ja ottamalla näistä ajoista mediaani.
     *
     * Suurilla syötteillä otetaan vain mediaani, sillä ajot ovat pidempikestoisia ja yksittäiset häiriöt häviävät jo hyvin suureen operaatiomäärään.
     *
     * Testatessa tietokone (kannettava) on liitetty verkkovirtaan ja virranhallinta-asetukset on säädetty suorituskykyä ajatellen. Muut ohjelmat on sammutettu kokonaan.
     * Näyttöjä käytössä vain yksi.
     * Testasin testiohjelman toimintaa useita ajoja ja tulokset ovat johdonmukaisia ja yhteneviä.
     *
     * "Käynnissä" on intelliJ:n lisäksi vain windowsin tehtävienhallinta, josta tarkkailin kellotaajuutta testauksen aikana. Kellotaajuus säilyi kohtalaisen tasaisena 4.2-4.4GHz välillä, keskimäärin ~4.29GHz. Tässä kohdassa voitaisiin parantaa lukitsemalla kellotaajuus, joskin nytkin mennään suhtellisen tasaisesti.
     *
     * Testit ajettu i7-11800H prosessorilla @ 4.2-4.4GHz.
     *
     * Yhden alkion mittauksessa lisäys, lisäys+ poisto ja poisto tehdään vain kerran, joten otin for silmukat pois. Jos yhden alkion mittaus tehdään for-looppien kanssa, näkyy niiden suorittamiseen kuluva aika selkeästi mittaustuloksessa.
     *
     * Kahden ja neljän alkion mittauksessa on vielä nähtävissä selkeästi for-silmukkojen vaikutus ajanottoon.
     * Tämän mittausvirheen saisi poistettua poistamalla for-silmukat ja korvaamalla ne kirjoittamalla jokaisen operaation erikseen.
     *
     * Mittaustuloksista on nähtävillä ArrayDequen olevan nopein toteutus testattavalle operaatiojonolle n. 14ns/n ajalla
     * Seuraavaksi nopein on LinkedList toteutus. LinkedListillä nähdään sen vaativan hieman enemmän tilaa ja karkaavan suurella syötteellä pois välimuistista.
     * Säieturvalliset toteutukset ovat hitaampia. Näissäkin toteutuksissa nähdään Linkitetyn toteutuksen olevan hitaampi ja mahdollisesti vievän hieman enemmän tilaa.
     *
     *
     *
     *
     * Testaa jonon Q toiminnan nopeutta.
     * Mittaa ajan alkioiden mÃ¤Ã¤rÃ¤lle n = { min, min*2, min*4, min*8, ... <=max}.
     *
     * Kullekin alkiomÃ¤Ã¤rÃ¤lle n mitataan aika joka kuluu seuraavaan operaatiosarjaan:
     * 1. lisÃ¤tÃ¤Ã¤n jonoon n alkiota
     * 2. n kertaa vuorotellen lisÃ¤tÃ¤Ã¤n jonoon yksi alkio ja otetaan yksi alkio pois
     * 3. poistetaan jonosta n alkiota
     *
     * Viimeinen mitattava alkioiden mÃ¤Ã¤rÃ¤ on siis suurin min*2^k joka on pienempi tai yhtÃ¤ suuri kuin max.
     *
     * Tuloksena palautetaan kuvaus jossa on avaimena kukin testattu syÃ¶tteen koko ja kuvana kyseisen
     * syÃ¶tteen koon mittaustulos nanosekunteina.
     *
     * Tämän luokka käynnistää ensin lämmitysajon ja sitten varsinaisen testin josta tulokset otetaan
     *
     * @param Q testattava jono
     * @param min lisÃ¤ttÃ¤vien/poistettavien alkioiden minimimÃ¤Ã¤rÃ¤
     * @param max lisÃ¤ttÃ¤vien/poistettavien alkioiden mÃ¤Ã¤rÃ¤n ylÃ¤raja
     * @return jÃ¤rjestetty kuvaus jossa on kaikki testitulokset
     */
    @Override
    public SortedMap<Integer, Long> jononNopeus(Queue<Integer> Q, int min, int max) {
        SortedMap<Integer, Long> tulos = new TreeMap<>();
        //Lämmitys:
        long aloitusAika = System.currentTimeMillis();
        System.out.println("Lämmitys alkaa");
        while (System.currentTimeMillis() < aloitusAika + 3000) {
            testaaQ(Q, min, max, tulos);
        }
        System.out.println("Lämmitys päättyy");
        //Testi:
        return testaaQ(Q,min,max,tulos);
    }

    /**
     * Varsinainen testirunko
     * @param Q Testattava jono
     * @param min lisättävien/poistettavien alkioiden minimimäärä
     * @param max lisättävien/poistettavien alkioiden määrän yläraja
     * @param tulos järjestetty kuvaus, jossa on kaikki testitulokset
     * @return Palauttaa järjestetyn kuvauksen, jossa on kaikki testitulokset
     */
    private SortedMap<Integer, Long> testaaQ(Queue<Integer> Q, int min, int max, SortedMap<Integer, Long> tulos) {
        for (int alkioMaara = min; alkioMaara <= max; alkioMaara *= 2) { //toistetaan alkiomäärää tuplaten kunnes maximimäärä ylitetty
            if(alkioMaara < 3) { //Tehty eri toisomäärillä olevia testejä parantamaan tarkkuutta pienemmillä syötteillä. Suuremmilla syötteillä lasketaan testimääriä, jotta testauksen vaatima aika ei kasva liikaa.
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 400,30));
            } else if (alkioMaara < 9) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 100, 16));
            } else if (alkioMaara < 33) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 30, 16));
            } else if (alkioMaara < 1025) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 10, 16));
            } else if (alkioMaara < 500000) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 2, 5));
            } else if (alkioMaara < 1000000) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 2, 3));
            }
            else {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 1, 3));
            }
        }
        return tulos;
    }

    /**
     * Aliohjelma, jossa testaus suoritetaan annettujen parametrien mukaisesti toistoja tehden
     * @param Q Jono testausta varten
     * @param alkioMaara aloittava testimäärä
     * @param operationCycles testin suorituskerrat keskiarvon laskentaan. Operaation keston ollessa arvioidusti nanokellon mittaustaajuuden ulkopuolella, tulee tähän asettaa riittävän suuri kerroin (esim. 100)
     * @param medianRepeat testin suorituskerrat mediaanin laskentaan.
     * @return Palauttaa testin tuloksen long muodossa.
     */
    private long getResult(Queue<Integer> Q, int alkioMaara, int operationCycles, int medianRepeat) {
        long alku; // Mittauksen alkuaika
        long loppu; // Mittauksen loppuaika
        long[] mediaani = new long[medianRepeat];
        int l = 0; // Mediaanitoistojen for-loopin apumuuttuja
        int j = 0; // Mitattavan operaatiosetin for-loopin apumuuttuja
        while (l < medianRepeat) { // Toistetaan medianRepeat muuttujan arvon verran kierroksia
            if (alkioMaara == 1) {
                alku = System.nanoTime(); // Aloitetaan mittaus
                while (j < operationCycles) { // Toistetaan operationCycles muutujan arvon verran kierroksia
                    Q.offer(j); // Lisätään n alkiota
                    Q.offer(j);
                    Q.poll();
                    Q.poll();
                    j++;
                }
                loppu = System.nanoTime(); // Lopetetaan mittaus
            } else {
                alku = System.nanoTime(); // Aloitetaan mittaus
                while (j < operationCycles) { // Toistetaan operationCycles muutujan arvon verran kierroksia
                    for (int operaatiot = 0; operaatiot <= alkioMaara; operaatiot++) {
                        Q.offer(operaatiot); // Lisätään n alkiota
                    }
                    for (int operaatiot = 0; operaatiot <= alkioMaara; operaatiot++) { // n alkion verran lisätään alkio ja poistetaan.
                        Q.offer(operaatiot);
                        Q.poll();
                    }
                    for (int operaatiot = 0; operaatiot <= alkioMaara; operaatiot++) {
                        Q.poll();
                    }
                    j++;
                }
                loppu = System.nanoTime(); // Lopetetaan mittaus
            }
            j = 0;
            mediaani[l] = ((loppu - alku)/operationCycles); // Lisätään taulukkoon mediaanin laskemista varten
            l++;
        }
        Arrays.sort(mediaani); // Järjestetään taulukko mediaanin ottamista varten
        return mediaani[medianRepeat/2];
    }
}
