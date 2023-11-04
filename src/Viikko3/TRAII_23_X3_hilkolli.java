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
     * @param Q testattava jono
     * @param min lisÃ¤ttÃ¤vien/poistettavien alkioiden minimimÃ¤Ã¤rÃ¤
     * @param max lisÃ¤ttÃ¤vien/poistettavien alkioiden mÃ¤Ã¤rÃ¤n ylÃ¤raja
     * @return jÃ¤rjestetty kuvaus jossa on kaikki testitulokset
     */
    @Override
    public SortedMap<Integer, Long> jononNopeus(Queue<Integer> Q, int min, int max) {
        SortedMap<Integer, Long> tulos = new TreeMap<>();


        // apumetodeja saa kÃ¤yttÃ¤Ã¤

        // tÃ¤mÃ¤ toisto tÃ¤ssÃ¤ esimerkkinÃ¤, mutta jokin tÃ¤llainen tarvitaan
        //Lämmitys:
        long testiaika = System.currentTimeMillis();
        System.out.println("Lämmitys alkaa");
        while (System.currentTimeMillis() < testiaika + 3000) {
            testaaQ(Q, min, max, tulos);
        }
        System.out.println("Lämmitys päättyy");

        return testaaQ(Q,min,max,tulos);
    }


    private SortedMap<Integer, Long> testaaQ(Queue<Integer> Q, int min, int max, SortedMap<Integer, Long> tulos) {
        for (int alkioMaara = min; alkioMaara <= max; alkioMaara *= 2) {
            if(alkioMaara < 3) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 10000,200));
            } else if (alkioMaara < 9) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 1000, 300));
            } else if (alkioMaara < 33) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 500, 50));
            } else if (alkioMaara < 1025) {
                tulos.put(alkioMaara, getResult(Q, alkioMaara, 200, 30));
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
     *
     * @param Q Jono testausta varten
     * @param alkioMaara aloittava testimäärä
     * @param operationCycles testin suorituskerrat keskiarvon laskentaan. Operaation keston ollessa arvioidusti nanokellon mittaustaajuuden ulkopuolella, tulee tähän asettaa riittävän suuri kerroin (esim. 100)
     * @param medianRepeat testin suorituskerrat mediaanin laskentaan.
     * @return Palauttaa testin tuloksen long muodossa.
     */
    private long getResult(Queue<Integer> Q, int alkioMaara, int operationCycles, int medianRepeat) {
        int tmp;
        long loppu;
        long alku;
        long[] mediaani = new long[medianRepeat];
        int j = 0;
        int l = 0;
        while (l < medianRepeat) {
            alku = System.nanoTime();
            while (j < operationCycles) {
                for (int operaatiot = 0; operaatiot <= alkioMaara; operaatiot++) {
                    Q.offer(operaatiot);
                }
                for (int operaatiot = 0; operaatiot <= alkioMaara; operaatiot++) {
                    tmp = Q.poll();
                    Q.offer(tmp);
                }
                for (int operaatiot = 0; operaatiot <= alkioMaara; operaatiot++) {
                    Q.poll();
                }
                j++;
            }
            loppu = System.nanoTime();
            j = 0;
            mediaani[l] = ((loppu - alku)/operationCycles);
            l++;
        }
        return mediaani[medianRepeat/2];
    }
}
