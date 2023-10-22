package Viikko1;

import fi.uef.cs.tra.BTree;
import fi.uef.cs.tra.BTreeNode;
import java.util.Set;

public interface TRAII_23_X1 {

    /**
     * Puun lehtisolmut.
     * Palauttaa joukkona kaikki ne puun T solmut joilla ei ole yhtÃ¤Ã¤n lasta.
     * @param T syÃ¶tepuu
     * @param <E> puun alkioiden tyyppi (ei kÃ¤ytetÃ¤ muuten kuin muuttujien parametrointiin)
     * @return lehtisolmujen joukko
     */
    public <E> Set<BTreeNode<E>> lehtiSolmut(BTree<E> T);

}