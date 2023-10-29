package Viikko2;

import java.util.*;

public class TRAII_23_X2_hilkolli implements TRAII_23_X2 {
    Random rnd = new Random();
    /**
     * ITSEARVIOINTI TĆ„HĆ„N:
     *
     *
     **/
    /**
     * Mittaa annetun kuvauksen containsKey -operaation aikavaativuuden nanosekunteina.
     * Mittaa ns. normaalin onnistuneen suorituksen. Ei siis minimiĆ¤ tai maksimia.
     * Ei muuta kuvausta (lisĆ¤Ć¤ tai poista alkioita).
     *
     * @param M testattava kuvaus
     * @return containsKey operaation normikesto nanosekunteina
     */

    @Override
    public long containsKeyNopeus(Map<Double, Double> M) {
        double key = rnd.nextDouble();
        int testit = 25000;
        long start = System.nanoTime();
        for (int i = 0; i < testit; i++) {
            M.containsKey(key);
        }
        long end = System.nanoTime();
        long testi1 = (end-start)/testit;

        key = rnd.nextDouble();
        start = System.nanoTime();
        for (int i = 0; i < testit; i++) {
            M.containsKey(key);
        }
        end = System.nanoTime();
        long testi2 = (end-start)/testit;

        key = rnd.nextDouble();
        start = System.nanoTime();
        for (int i = 0; i < testit; i++) {
            M.containsKey(key);
        }
        end = System.nanoTime();
        long testi3 = (end-start)/testit;

        if (testi1 >= testi2 && testi1 <= testi3) {
            return testi1;
        } else if (testi2 > testi1 && testi2 < testi3) {
            return testi2;
        }
        return testi3;
    }
}