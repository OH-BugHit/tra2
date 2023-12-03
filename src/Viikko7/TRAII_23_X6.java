package Viikko7;

import java.util.ArrayList;
import java.util.List;

public interface TRAII_23_X6 {

    /**
     * Jaottelee kokoelman C kokonaisluvut mahdollisimman vÃ¤hiin listoihin siten, ettÃ¤ kunkin listan alkioiden summa on
     * korkeintaan laatikonKoko.
     *
     * @param L            jaoteltavat alkiot
     * @param laatikonKoko kunkin tuloslistan maksimikapasiteeti
     * @param maxAika      suurin kÃ¤ytettÃ¤vissÃ¤ oleva aika (sekunteja)
     * @return tuloslistojen lista
     */
    public List<List<Integer>> laatikkoJako(final ArrayList<Integer> L, int laatikonKoko, int maxAika);

}