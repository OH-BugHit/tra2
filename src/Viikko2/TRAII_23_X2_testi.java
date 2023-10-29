package Viikko2;// TRAII_23_X2_testi.java SJ
// Testiluokka TRAII 2023 tehtÃ¤viin X2 ja 5-7


import java.util.*;

public class TRAII_23_X2_testi {

    static TRAII_23_X2 testattava = new TRAII_23_X2_hilkolli();
    //                                              ^^^^ oma tunnus tÃ¤hÃ¤n


    public static void main(String[] args) {

        // komentoriviparametrina maksimikoko kuvaukselle
        int N = 1000000;
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
        testaaX2(N, rnd, print);


    } // main()


    /**
     * Testaa tehtÃ¤vÃ¤Ã¤ syÃ¶tekoolla 1..n
     * @param n maksimi syÃ¶tekoko
     * @param rnd satunnaislukugeneraattori
     * @param print tulostusten mÃ¤Ã¤rÃ¤
     */
    static void testaaX2(int n, Random rnd, int print) {
        int k = 1;
        //Map<Double, Double> M = new HashMap<>();
        Map<Double, Double> M = new TreeMap<>();
        while (M.size() < n) {
            while (M.size() < k)
                M.put(rnd.nextDouble(), rnd.nextDouble());
            testaaX2(M, print);
            k *= 2; //TÄHÄN TULEE KERROIN
        }
    }


    /**
     * Kutsuu testausta ilman tulostuksia kunnes annettu aika on kuulunut.
     * @param n maksimi syÃ¶tekoko
     * @param rnd satunnaislukugeneraattori
     * @param sek suorituksen kesto
     */
    static void lammita(int n, Random rnd, int sek) {

        System.out.println("LÃ¤mmitys alkaa " + sek + "s");
        long loppu = System.nanoTime() + sek*1000L*1000*1000;
        while (System.nanoTime() < loppu)
            testaaX2(n, rnd, 0);
        System.out.println("LÃ¤mmitys loppuu");
    }


    /**
     * Testaa tehtÃ¤vÃ¤Ã¤ X2 kuvauksella M
     * @param M kuvaus jota testataan
     * @param print tulostusten mÃ¤Ã¤rÃ¤
     */
    static void testaaX2(Map<Double, Double> M, int print) {

        if (print > 1)
            System.out.println("\nTesti, Map = " + M.getClass().toString() + " n = " + M.size());

        long alku = System.nanoTime();

        long tulos = testattava.containsKeyNopeus(M);

        long loppu = System.nanoTime();

        if (print > 0) System.out.println("  tulos = " + tulos + " ns");

        if (loppu-alku > 1000L*1000*1000)
            System.out.println("Varoitus: testi oli tarpeettoman hidas (yli 1s)");

        if (print > 3)  // sÃ¤Ã¤dÃ¤ tÃ¤stÃ¤ jos haluat nÃ¤hdÃ¤ paljonko testisi kesti
            System.out.println("Testi kesti " + ((loppu-alku)/1000000.0) + " ms");


    }


}