package Viikko2;

import java.util.*;

/**
 *
 * ARVIO!
 * Kertaluokka on O(n)
 * Ainakin jos osaan käyttää tota subsettiä oikein.. Syötteen kaksinkertaistuessa, käytetty aika kaksinkertaistuu.
 * Tuloksessa näkyy selvästi välimuistin vaikutus
 */
public class TRAII_23_t8 {
    static Random rnd = new Random();

    public static void main(String[] args) {

        // komentoriviparametrina maksimikoko
        int N = 1000000; //
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
        testaaT8(N, rnd, print);


    } // main()


    /**
     * Testaa tehtÃ¤vÃ¤Ã¤ syÃ¶tekoolla 1..n
     * @param n maksimi syÃ¶tekoko
     * @param rnd satunnaislukugeneraattori
     * @param print tulostusten mÃ¤Ã¤rÃ¤
     */
    static void testaaT8(int n, Random rnd, int print) {
        int k = 1;
        TreeSet<Double> M = new TreeSet<>();
        M.add(0.0); //aloitus
        while (M.size() < n) {
            while (M.size() < k)
                M.add(rnd.nextDouble());
            testaat8(M, print);
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
            testaaT8(n, rnd, 0);
        System.out.println("LÃ¤mmitys loppuu");
    }


    /**
     * Testaa tehtävän
     * @param M PriorityQue jota testataan
     * @param print tulostusten määrä
     */
    static void testaat8(TreeSet<Double> M, int print) {

        if (print > 1)
            System.out.println("\nTesti " + M.getClass().toString() + " n = " + M.size());

        long alku = System.nanoTime();

        long tulos = subSetNopeus(M);

        long loppu = System.nanoTime();

        if (print > 0) {
            System.out.println("  tulos = " + tulos + " ns");
        }

        if (loppu-alku > 1000L*1000*5000)
            System.out.println("Varoitus: testi oli tarpeettoman hidas (yli 5s)");

        if (print > 3)  // sÃ¤Ã¤dÃ¤ tÃ¤stÃ¤ jos haluat nÃ¤hdÃ¤ paljonko testisi kesti
            System.out.println("Testi kesti " + ((loppu-alku)/1000000.0) + " ms");


    }


    private static long subSetNopeus(TreeSet<Double> M) {
        int operaatiot = 50; // operaation toistomäärä yhdellä testikierroksella
        int testit = 100; // testien lukumäärä yhdellä testimetodin ajolla

        if (M.size() <= 8192) { // Parannetaan tarkkuutta kun syöte on pieni!
            operaatiot = 300;
            testit = 50;
        }
        double key = -1.0;
        M.add(key);

        double startKey = 0.0; //aloitus
        long start;
        long end;
        long[] results = new long[testit];

        for (int i = 0; i < results.length; i++) {
            key = M.size()-1.0;
            start = System.nanoTime();
            for (int j = 0; j < operaatiot; j++) {
                /*TreeSet<Double> a = new TreeSet<>(*/M.subSet(startKey,key);//); // jos ei muodosteta uutta.
            }
            end = System.nanoTime();
            results[i] =  (end - start) / operaatiot;
        }

        Arrays.sort(results); // järjestellään mediaanin ottamista varten.
        return results[results.length/2];
    }
}
