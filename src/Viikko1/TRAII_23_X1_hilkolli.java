package Viikko1;

import fi.uef.cs.tra.BTree;
import fi.uef.cs.tra.BTreeNode;

import java.util.HashSet;
import java.util.Set;

public class TRAII_23_X1_hilkolli implements TRAII_23_X1 {
    /**
     * ITSEARVIOINTI TÃ„HÃ„N:
     *
     *
     *
     **/
    /**
     * Puun lehtisolmut.
     * Palauttaa joukkona kaikki ne puun T solmut joilla ei ole yhtÃ¤Ã¤n lasta.
     * @param T syÃ¶tepuu
     * @param <E> puun alkioiden tyyppi (ei kÃ¤ytetÃ¤ muuten kuin muuttujien parametrointiin)
     * @return lehtisolmujen joukko
     */
     @Override
    public <E> Set<BTreeNode<E>> lehtiSolmut(BTree<E> T) {
        Set<BTreeNode<E>> lehdet = new HashSet<>();

        // TODO
         // saa ja kannattaa tehdÃ¤ joku toinenkin metodi avuksi

        return lehdet;
    }

}