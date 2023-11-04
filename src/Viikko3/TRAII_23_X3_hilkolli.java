package Viikko3;

import java.util.*;

public class TRAII_23_X3_hilkolli implements TRAII_23_X3 {

    /**
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
        long testiaika = System.currentTimeMillis();
        System.out.println("Lämmitys alkaa");
        while (System.currentTimeMillis() < testiaika + 3000) {
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
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 10000,200));
            } else if (alkioMaara < 9) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 1000, 200));
            } else if (alkioMaara < 33) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 250, 60));
            } else if (alkioMaara < 1025) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 200, 40));
            } else if (alkioMaara < 500000) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 3, 5));
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
            j = 0;
            mediaani[l] = ((loppu - alku)/operationCycles); // Lisätään taulukkoon mediaanin laskemista varten
            l++;
        }
        Arrays.sort(mediaani); // Järjestetään taulukko mediaanin ottamista varten
        return mediaani[medianRepeat/2];
    }
}
