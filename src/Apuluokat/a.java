package Apuluokat;

import Viikko3.TRAII_23_X3;

import java.util.*;

public class a implements TRAII_23_X3 {

    /**
     * Itsearviointi: Minimoin virhelähteet kun syötteitä on paljon, kone on laturissa, ja olen sulkenut muut ohjelmat taustalta.
     * Lasken keskiarvon ja toistan mittauksen monesti.
     * En keksi mitä olisin voinut tehdä paremmin.
     *
     * Testaa jonon Q toiminnan nopeutta.
     * Mittaa ajan alkioiden mÃ¤Ã¤rÃ¤lle n = { min, min*2, min*4, min*8, ... <=max}.
     * <p>
     * Kullekin alkiomÃ¤Ã¤rÃ¤lle n mitataan aika joka kuluu seuraavaan operaatiosarjaan:
     * 1. lisÃ¤tÃ¤Ã¤n jonoon n alkiota
     * 2. n kertaa vuorotellen lisÃ¤tÃ¤Ã¤n jonoon yksi alkio ja otetaan yksi alkio pois
     * 3. poistetaan jonosta n alkiota
     * <p>
     * Viimeinen mitattava alkioiden mÃ¤Ã¤rÃ¤ on siis suurin min*2^k joka on pienempi tai yhtÃ¤ suuri kuin max.
     * <p>
     * Tuloksena palautetaan kuvaus jossa on avaimena kukin testattu syÃ¶tteen koko ja kuvana kyseisen
     * syÃ¶tteen koon mittaustulos nanosekunteina.
     *
     * @param Q   testattava jono
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
        // lämmitysajo
        testiajo(Q, min, max, tulos);

        // Testi
        testiajo(Q, min, max, tulos);
        return tulos;
    }

    private static void testiajo(Queue<Integer> Q, int min, int max, SortedMap<Integer, Long> tulos) {
        for (int alkioMaara = min; alkioMaara <= max; alkioMaara *= 2) {

            // TODO tÃ¤hÃ¤n ainakin pitÃ¤Ã¤ tehdÃ¤ jotain

            //kun testi suoritetaan suurille alkiomäärille, se hidastuu huomattavasti
            //sen takia testiä toistetaan vähemmän suurilla alkiomäärillä

            int toistomaara = 200; //kun alkiomäärä on alle 32, niin toistetaan testi 200 kertaa
            int mediaanimaara = 7;

            if (alkioMaara > 32) { //kun alkiomäärä on enemmän kuin 32, mutta vähemmän kuin 500 000 niin toistetaan testi 300 kertaa
                toistomaara = 20;
                mediaanimaara = 3;
            }

            if (alkioMaara > 1000000) { //kun alkiomäärä on enemmän kuin 1 000 000, toistetaan testi 3 kertaa
                toistomaara = 1;
                mediaanimaara = 3;
            }


            long [] taulukko = new long[mediaanimaara]; //luodaan taulukko


            for (int i = 0; i < mediaanimaara; i++) {

                long start = System.nanoTime(); //otetaan aloitusaika talteen

                for (int j = 0; j < toistomaara; j++) {

                    for (int k = 0; k < alkioMaara; k++) { //tyhjään jonoon lisätään alkiot
                        Q.offer(i);
                    }

                    for (int k = 0; k < alkioMaara; k++) {
                        int x = Q.poll(); //tallennetaan ja haetaan ensimmäinen alkio ja poistetaan se sieltä
                        Q.offer(x); //lisätään uudestaan jonon perälle
                    }

                    for (int k = 0; k < alkioMaara; k++) { //poistetaan kaikki alkiot jonosta
                        Q.poll();
                    }
                }

                long stop = System.nanoTime(); //otetaan lopetusaika talteen
                taulukko [i] = ((stop-start)/toistomaara); //lisätään taulukkoon keskiarvoajat
            }
                // tulosten tallettaminen
                Arrays.sort(taulukko);
                tulos.put(alkioMaara, taulukko[mediaanimaara/2]); //otetaan mediaani taulukosta
        }
    }
}