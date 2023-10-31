package Viikko2;

import java.util.*;

/**
 *
 * ARVIO!
 * Kertaluokka on O(n)
 * Muistin tasojen muutokset näkyvät pienemmillä syötteillä erittäin selvästi. Muuten kasvaa n kaksinkertaiseksi kun syöte kasvaa 2x
 */
public class TRAII_23_t7 {
    static Random rnd = new Random();

    public static void main(String[] args) {

        // komentoriviparametrina maksimikoko
        int N = 10000000; //0
        if (args.length > 0)
            N = Integer.parseInt(args[0]);

        // satunnaislukusiemen
        int siemen = 42;
        if (args.length > 1)
            siemen = Integer.parseInt(args[1]);
        Random rnd = new Random(siemen);

        // tulostusten mÃ¤Ã¤rÃ¤
        int print = 3;
        if (args.length > 2)
            print = Integer.parseInt(args[2]);


        // kutsutaan ensin ilman tulostuksia 3 sekunnin ajan
        lammita(N, rnd, 3);

        // sitten varsinainen testiajo
        testaaT7(N, rnd, print);


    } // main()


    /**
     * Testaa tehtÃ¤vÃ¤Ã¤ syÃ¶tekoolla 1..n
     * @param n maksimi syÃ¶tekoko
     * @param rnd satunnaislukugeneraattori
     * @param print tulostusten mÃ¤Ã¤rÃ¤
     */
    static void testaaT7(int n, Random rnd, int print) {
        int k = 1;
        PriorityQueue<Double> M = new PriorityQueue<>();
        while (M.size() < n) {
            while (M.size() < k)
                M.add(rnd.nextDouble());
            testaat7(M, print);
            k *= 2; //TÄHÄN TULEE KERROIN
        }
    }


    /**
     * Kutsuu testausta ilman tulostuksia kunnes annettu aika on kuulunut.
     * @param n maksimi syötekoko
     * @param rnd satunnaislukugeneraattori
     * @param sek suorituksen kesto
     */
    static void lammita(int n, Random rnd, int sek) {

        System.out.println("LÃ¤mmitys alkaa " + sek + "s");
        long loppu = System.nanoTime() + sek*1000L*1000*1000;
        while (System.nanoTime() < loppu)
            testaaT7(n, rnd, 0);
        System.out.println("LÃ¤mmitys loppuu");
    }


    /**
     * Testaa tehtävän
     * @param M PriorityQue jota testataan
     * @param print tulostusten määrä
     */
    static void testaat7(PriorityQueue<Double> M, int print) {

        if (print > 1)
            System.out.println("\nTesti " + M.getClass().toString() + " n = " + M.size());

        long alku = System.nanoTime();

        long tulos = containsKeyNopeus(M);

        long loppu = System.nanoTime();

        if (print > 0) {
            System.out.println("  tulos = " + tulos + " ns");
        }

        if (loppu-alku > 1000L*1000*3000)
            System.out.println("Varoitus: testi oli tarpeettoman hidas (yli 3s)");

        if (print > 3)  // sÃ¤Ã¤dÃ¤ tÃ¤stÃ¤ jos haluat nÃ¤hdÃ¤ paljonko testisi kesti
            System.out.println("Testi kesti " + ((loppu-alku)/1000000.0) + " ms");


    }


    private static long containsKeyNopeus(PriorityQueue<Double> M) {
        int operaatiot = 3; // operaation toistomäärä yhdellä testikierroksella
        int testit = 10; // testien lukumäärä yhdellä testimetodin ajolla

        double key; //jos haluttaisiin random key, nyt testataan pahinta mahdollista (eli ei löydy) kertaluokan selvittämiseksi
        long start;
        long end;
        long[] results = new long[testit];

        for (int i = 0; i < results.length; i++) {
            key = rnd.nextDouble();
            start = System.nanoTime();
            for (int j = 0; j < operaatiot; j++) {
                M.contains(-1);
            }
            end = System.nanoTime();
            results[i] =  (end - start) / operaatiot;
        }

        Arrays.sort(results); // järjestellään mediaanin ottamista varten.
        return results[results.length/2];
    }
}
