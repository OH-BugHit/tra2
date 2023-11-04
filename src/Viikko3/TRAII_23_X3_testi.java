package Viikko3;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Testiluokka tehtÃ¤vÃ¤Ã¤n X3
 */
public class TRAII_23_X3_testi {

    public static void main(String[] args) {

        // nÃ¤itÃ¤ voi toki vaihdella
        int N1 = 1;
        if (args.length > 0)
            N1 = Integer.parseInt(args[0]);

        int N2 = 10000000;
        if (args.length > 0)
            N2 = Integer.parseInt(args[1]);

        TRAII_23_X3 testattava = new TRAII_23_X3_hilkolli();

        // testataan parilla eRi jonototeutuksella
        testaa(testattava, new ArrayDeque<>(), N1, N2);

        testaa(testattava, new LinkedList<>(), N1, N2);

        testaa(testattava, new LinkedBlockingQueue<>(), N1, N2);

        testaa(testattava, new ArrayBlockingQueue<>(N2+1), N1, N2);


    }


    /**
     * Testaa tehtÃ¤vÃ¤ X3 yhdellÃ¤ jonolla.
     * @param testattava testattava mittausluokka
     * @param J testattava jono
     * @param min alkiomÃ¤Ã¤rÃ¤n alaraja
     * @param max alkiomÃ¤Ã¤rÃ¤n ylÃ¤raja
     */
    static void testaa(TRAII_23_X3 testattava, Queue<Integer> J, int min, int max) {
        System.out.println("\nTestataan " + J.getClass().getName() + " " + min + ".." + max);

        // kutsutaan mittausta
        long alku = System.nanoTime();
        SortedMap<Integer, Long> tulos = testattava.jononNopeus(J, min, max);
        long aika = System.nanoTime() - alku;

        // tarkastetaan onko kuvauksen avaimet oikein, tulostusta voi toki lisÃ¤tÃ¤ jos haluaa
        Set<Integer> odotetutAvaimet = odotetutAlkioMaarat(min, max);
        System.out.println("Oikeat alkiomÃ¤Ã¤rÃ¤t: " + odotetutAvaimet.equals(tulos.keySet()));

        // tulostetaan mittaukseen mennyt aika, max 2s/alkiomÃ¤Ã¤rÃ¤ oli tarkoitus kÃ¤yttÃ¤Ã¤
        System.out.println("Mittaus kesti " + (1.0*aika / (1000.0*1000*1000*odotetutAvaimet.size())) + " s/alkiomÃ¤Ã¤rÃ¤.");

        // tulostetaan kuvauksen sisÃ¤ltÃ¶
        System.out.println("n   ns    ns/n");
        for (Map.Entry<Integer, Long> e : tulos.entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue() + " " +
                    e.getValue()/e.getKey());
        }
    }

    /**
     * Generoi joukon jossa on min, min*2, min*4, min*2^2 siten, ettÃ¤ arvot ovat <= max
     * @param min alaraja
     * @param max ylÃ¤raja
     * @return joukko
     */
    static Set<Integer> odotetutAlkioMaarat(int min, int max) {
        Set<Integer> avaimet = new TreeSet<>();
        int k = min;
        while (k <= max) {
            avaimet.add(k);
            k *= 2;
        }
        return avaimet;
    }



}