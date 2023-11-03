package Viikko3;

import java.util.*;

public class TRAII_23_X3_pohja implements TRAII_23_X3 {

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

        // TODO tÃ¤hÃ¤n ja muuallekin saa tehdÃ¤ muutoksia kunhan ei muuta otsikkoa
        // apumetodeja saa kÃ¤yttÃ¤Ã¤

        // tÃ¤mÃ¤ toisto tÃ¤ssÃ¤ esimerkkinÃ¤, mutta jokin tÃ¤llainen tarvitaan
        for (int alkioMaara = min; alkioMaara <= max; alkioMaara *= 2) {

            // TODO tÃ¤hÃ¤n ainakin pitÃ¤Ã¤ tehdÃ¤ jotain

            // tulosten tallettaminen
            tulos.put(alkioMaara, 1L /* TODO tÃ¤hÃ¤n se mitattu tulos nanosekunteina */ );

        }

        return tulos;
    }

}