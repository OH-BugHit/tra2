package Viikko2;

import java.util.Map;

public interface TRAII_23_X2 {

    /**
     * Mittaa annetun kuvauksen containsKey -operaation aikavaativuuden nanosekunteina.
     * Mittaa ns. normaalin onnistuneen suorituksen. Ei siis minimiĆ¤ tai maksimia.
     * Ei muuta kuvausta (lisĆ¤Ć¤ tai poista alkioita).
     *
     * @param M testattava kuvaus
     * @return containsKey operaation normikesto nanosekunteina
     */
    public long containsKeyNopeus(Map<Double, Double> M);

}