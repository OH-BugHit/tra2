package Viikko1;// TRAII_23_X1_testi.java SJ


 // Testiluokka TRAII 2023 tehtÃ¤vÃ¤Ã¤n X1


import fi.uef.cs.tra.*;
import java.util.*;

public class TRAII_23_X1_testi {

  static TRAII_23_X1 testattava = new TRAII_23_X1_hilkolli();

    //                                              ^^^^ oma tunnus tÃ¤hÃ¤n

    public static void main(String[] args) {

        // komentoriviparametrina saatu puun koko
        int N = 10;
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


        boolean ok = true;

        System.out.println("\n Ajetaan " + N + " pientÃ¤ testiÃ¤:");

        // N testiÃ¤ eri kokoisilla syÃ¶tteillÃ¤
        for (int i = 0; i < N; i++)
            ok &= testaaX1(i, rnd, print);

        System.out.println("\n Ajetaan max 1000 satunnaista testiÃ¤:");
        // max 1000 satunnaista testiÃ¤
        rnd.setSeed(System.currentTimeMillis());
        int nTest = 1000;
        int k;
        int virheet = 0;
        for (k = 0; k < nTest; k++) {
            if (!testaaX1(rnd.nextInt(k+1), rnd, 0))
                virheet++;
            if (virheet > 30)
                break;
        }
        if (virheet > 0)
            ok = false;
        System.out.println("\n" + k + " testistÃ¤ " + (k - virheet) + " oikein.");

        if (ok)
            System.out.println("\nKaikki tehdyt testit antoivat oikean tuloksen.");
        else
            System.out.println("\nJoissain testeissÃ¤ virheitÃ¤.");


    } // main()

    /**
     * Testaa X1:n n kokoisella puulla.
     * @param n puusolmujen mÃ¤Ã¤rÃ¤
     * @param rnd satunnaislukugeneraattori
     * @param print tulostusten mÃ¤Ã¤rÃ¤
     * @return true jos testi onnistui, muuten false
     */
    static boolean testaaX1(int n, Random rnd, int print) {
        return testaaX1(satunnainenLista(n, rnd), print);
    }

    /**
     * Testaa X1:n puulla joka kasataan listata
     * @param L puuhun laitettavat alkiot
     * @param print tulostusten mÃ¤Ã¤rÃ¤
     * @return true jos testi onnistui, muuten false
     */
    static <E extends Comparable<? super E>> boolean testaaX1(List<E> L, int print) {

        if (print > 0)
            System.out.println("\nTesti, n = " + L.size());

        BTree<E> puu = new BTree<>();
        Set<BTreeNode<E>> LSvrt = teePuu(L, puu);

        if (print > 1) {
            System.out.println("Alkiot syÃ¶tepuuhun:" + L);
            System.out.println("Lehtisolmut (verrokki): " + LSvrt);
        }
        Set<BTreeNode<E>> HS;
        try {
            HS = testattava.lehtiSolmut(puu);
        } catch (Exception e) {
            if (print > 0) System.out.println("TestistÃ¤ tuli poikkeus: " + e);
            return false;
        }

        if (print > 1) {
            System.out.println("Lehtisolmut (tulos):    " + HS);
        }

        if (LSvrt.equals(HS)) {
            if (print > 0) System.out.println("Tulos oikein");
            return true;
        } else {
            if (print > 0) System.out.println("Tulos ei ole oikein");
            return false;
        }

    }


    /**
     * Generoi puun listasta alkiot.
     * Palauttaa puun haarautumissolmut.
     * @param alkiot puuhun lisÃ¤ttÃ¤vÃ¤t alkiot
     * @param puu puu johon alkiot lisÃ¤tÃ¤Ã¤n, pitÃ¤isi olla tyhjÃ¤  (muuten haarautumissolmujen joukko ei ole oikea)
     * @param <E> solmujen alkiotyyppi
     * @return luodun puun haaratutumissolmut
     */
    public static <E extends Comparable<? super E>> Set<BTreeNode<E>> teePuu(List<E> alkiot, BTree<E> puu) {
        Set<BTreeNode<E>> lSolmut = new HashSet<>();
        if (puu.getRoot() != null) System.out.println("VAROITUS: PUU EI OLE TYHJÃ„");
        for (E x : alkiot) {
            lisaaPuuhun(puu, x, lSolmut);
        }
        return lSolmut;
    }

    /**
     * Luo satunnaisen uniikkien alkioiden listan
     * @param n alkiomÃ¤Ã¤rÃ¤
     * @param rnd satunnaislukugeneraattori
     * @return satunnainen lista
     */
    public static ArrayList<Integer> satunnainenLista(int n, Random rnd) {
        HashSet<Integer> HS = new HashSet<>(n*2);
        ArrayList<Integer> AL = new ArrayList<>(n);
        int i = 0;
        while (i < n) {
            int x = rnd.nextInt(n*2+1);
            if (HS.add(x)) {
                AL.add(x);
                i++;
            }
        }
        return AL;
    }


    /**
     * BinÃ¤Ã¤ripuuhun lisÃ¤ys suunnilleen sisÃ¤jÃ¤rjestykseen.
     * LisÃ¤Ã¤ alkion puuhun vaikka se olisi jo puussa.
     * LisÃ¤Ã¤ uudet lehtisolmut joukkoon lehdet
     * Poistaa uuden lehtisolmun vanhemman joukosta lehdet
     *
     * @param T   syÃ¶tepuu
     * @param x   lisÃ¤ttÃ¤vÃ¤ alkio
     * @param lehdet yllÃ¤pidettÃ¤vÃ¤ lehtisolmujen joukko
     * @param <E> alkioiden tyyppi
     */
    public static <E extends Comparable<? super E>> void lisaaPuuhun(BTree<E> T, E x, Set<BTreeNode<E>> lehdet) {
        BTreeNode<E> n = T.getRoot();
        if (n == null) {
            BTreeNode<E> uusi = new BTreeNode<>(x);
            lehdet.add(uusi);
            T.setRoot(uusi);
            return;
        }

        while (true) {
            if (x.compareTo(n.getElement()) < 0) {
                if (n.getLeftChild() == null) {
                    BTreeNode<E> uusi = new BTreeNode<>(x);
                    lehdet.add(uusi);
                    n.setLeftChild(uusi);
                    lehdet.remove(n);
                    return;
                } else
                    n = n.getLeftChild();
            } else {
                if (n.getRightChild() == null) {
                    BTreeNode<E> uusi = new BTreeNode<>(x);
                    lehdet.add(uusi);
                    n.setRightChild(uusi);
                    lehdet.remove(n);
                    return;
                } else
                    n = n.getRightChild();
            }
        } // while

    } // lisaaPuuhun()

}