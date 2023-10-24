package Viikko1;

import fi.uef.cs.tra.BTree;
import fi.uef.cs.tra.BTreeNode;

import java.util.HashSet;
import java.util.Set;

public class TRAII_23_X1_hilkolli implements TRAII_23_X1 {
    /**
     * ITSEARVIOINTI TÃ„HÃ„N:
     * Aikavaativuus O(n), jossa n on puun alkioiden lukumäärä. Alkiot käydään yksitellen jokainen läpi. Tulokseen lisätään lehtisolmut.
     * Ratkaisu on selkeä ja toimiva
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
         if (T.getRoot() != null) {
             BTreeNode<E> root = T.getRoot();
             lisaaLehtisolmut(root, lehdet);
         }

        return lehdet;
    }

    private <E> void lisaaLehtisolmut(BTreeNode<E> root, Set<BTreeNode<E>> lehdet) {
         if (root.getLeftChild() != null) {
             lisaaLehtisolmut(root.getLeftChild(), lehdet);
         }
         if (root.getRightChild() != null) {
             lisaaLehtisolmut(root.getRightChild(), lehdet);
         }
         if (root.getLeftChild() == null && root.getRightChild() == null) {
             lehdet.add(root);
         }
    }

}