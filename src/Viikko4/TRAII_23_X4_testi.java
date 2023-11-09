package Viikko4;// TRAII_23_X4_testi.java SJ

import Apuluokat.GraphMaker;
import fi.uef.cs.tra.DiGraph;
import fi.uef.cs.tra.Vertex;

import java.util.*;

public class TRAII_23_X4_testi {

    static TRAII_23_X4 testattava = new TRAII_23_X4_hilkolli();
    //                                              ^^^^ oma tunnus tÃ¤hÃ¤n

    static Random rnd = new Random();

    public static void main(String[] args) {

        // komentoriviparametrina saatu puun koko
        int N = 10;
        if (args.length > 0)
            N = Integer.parseInt(args[0]);

        // satunnaislukusiemen
        int siemen = 42;
        if (args.length > 1)
            siemen = Integer.parseInt(args[1]);
        rnd.setSeed(siemen);

        // tulostusten mÃ¤Ã¤rÃ¤
        int print = 5;
        if (args.length > 2)
            print = Integer.parseInt(args[2]);


        boolean ok1 = true;

        ok1 &= testaaJuuriSolmut(0, 0, print);
        ok1 &= testaaJuuriSolmut(0, 1, print);
        ok1 &= testaaJuuriSolmut(1, 2, print);
        ok1 &= testaaJuuriSolmut(2, 0, print);
        ok1 &= testaaJuuriSolmut(0, 2, print);
        ok1 &= testaaJuuriSolmut(N, N, print);

        rnd.setSeed(System.currentTimeMillis());
        int nTest = 200;
        int k;
        int virheet = 0;
        for (k = 0; k < nTest; k++) {
            if (!testaaJuuriSolmut(rnd.nextInt(k+1), rnd.nextInt(k+1), 0))
                virheet++;
            if (virheet > 30)
                break;
        }
        if (virheet > 0)
            ok1 = false;
        System.out.println("\n" + k + " satunnaisesta juurisolmutestistÃ¤ " + (k - virheet) + " oikein.");

        if (ok1)
            System.out.println("\nKaikki tehdyt juurisolmutestit antoivat oikean tuloksen.");
        else
            System.out.println("\nJoissain juurisolmutesteissÃ¤ virheitÃ¤.");

        rnd.setSeed(siemen);
        boolean ok2 = true;

        ok2 &= testaaPuidenMaara(0, 0, print);
        ok2 &= testaaPuidenMaara(0, 1, print);
        ok2 &= testaaPuidenMaara(1, 2, print);
        ok2 &= testaaPuidenMaara(2, 0, print);
        ok2 &= testaaPuidenMaara(0, 2, print);
        ok2 &= testaaPuidenMaara(3, 3, print);
        ok2 &= testaaPuidenMaara(5, 5, print);
        ok2 &= testaaPuidenMaara(N, N, print);



        rnd.setSeed(System.currentTimeMillis());
        nTest = 200;
        k = 0;
        virheet = 0;
        while (k < nTest) {
            k++;
            if (!testaaPuidenMaara(rnd.nextInt(k+1), rnd.nextInt(k+1), 0))
                virheet++;
            if (virheet > 30)
                break;
        }
        if (virheet > 0)
            ok2 = false;
        System.out.println("\n" + k + " satunnaisesta puumÃ¤Ã¤rÃ¤testistÃ¤ " + (k - virheet) + " oikein.");

        if (ok2)
            System.out.println("\nKaikki tehdyt puumÃ¤Ã¤rÃ¤testit antoivat oikean tuloksen.");
        else
            System.out.println("\nJoissain puumÃ¤Ã¤rÃ¤testeissÃ¤ virheitÃ¤.");


        if (ok1 && ok2)
            System.out.println("\nKaikki tehdyt testit antoivat oikean tuloksen.");
        else
            System.out.println("\nJoissain testeissÃ¤ virheitÃ¤.");


    } // main()


    static DiGraph teeSyote(int puita, int muita, Set<Vertex> juuriSolmut) {
        DiGraph G = new DiGraph();

        // tÃ¤ssÃ¤ solmujen nimet on annettu siten, ettÃ¤ puut ja muut on helppo erottaa
        // Ã„LÃ„ KUITENKAAN RYHDY TUNNISTAMAAN PUITA SOLMUN NIMEN TAI JÃ„RJESTYKSEN PERUSTEELLA
        for (int i = 0; i < puita; i++) {
            Vertex juuri = G.addVertex("P" + i + ".");
            juuriSolmut.add(juuri);
            GraphMaker.growTree(G, juuri, rnd, rnd.nextInt(puita));
        }

        for (int i = 0; i < muita; i++) {
            Vertex solmu = G.addVertex("M" + i + ".");
            juuriSolmut.add(solmu);
            GraphMaker.makeNonTree(G, solmu, rnd, rnd.nextInt(muita)+2, rnd.nextInt(muita)+5, rnd.nextBoolean(), juuriSolmut);
        }

        return G;
    }

    static boolean testaaJuuriSolmut(int puita, int muita, int print) {
        if (print > 0)
            System.out.println("\nJuurisolmutesti, puita=" + puita + " muita="+muita);
        Set<Vertex> verrokki = new HashSet<>();
        DiGraph G = teeSyote(puita, muita, verrokki);
        if (print > 4 && G.size() < 30) {
            System.out.println("SyÃ¶te:");
            System.out.print(GraphMaker.toString(G, 1));
        }
        if (print > 3 && verrokki.size() < 30) {
            System.out.println("Odotetut juurisolmut: " + verrokki);
        }

        Set<Vertex> juuret = testattava.juuriSolmut(G);

        if (print > 2 && verrokki.size() < 30) {
            System.out.println("Saadut juurisolmut: " + juuret);
        }

        if (verrokki.equals(juuret)) {
            if (print > 0)
                System.out.println("Juurisolmutesti ok");
            return true;
        } else {
            System.out.println("Juurisolmujen joukko ei tÃ¤smÃ¤Ã¤ odotettuun");
            return false;
        }
    }

    static boolean testaaPuidenMaara(int puita, int muita, int print) {
        if (print > 0)
            System.out.println("\nPuiden mÃ¤Ã¤rÃ¤testi, puita=" + puita + " muita="+muita);
        Set<Vertex> verrokki = new HashSet<>();
        DiGraph G = teeSyote(puita, muita, verrokki);
        if (print > 4 && G.size() < 20) {
            System.out.println("SyÃ¶te:");
            System.out.print(GraphMaker.toString(G, 1));
        }

        int tulos = testattava.puidenLukumaara(G);

        if (print > 1) {
            System.out.println("Saatu tulos: " + tulos + ", odotettu tulos: " + puita);
        }

        if (tulos == puita) {
            if (print > 0)
                System.out.println("PuumÃ¤Ã¤rÃ¤testi ok");
            return true;
        } else {
            System.out.println("Puiden mÃ¤Ã¤rÃ¤ ei tÃ¤smÃ¤Ã¤ odotettuun : Saatu tulos: " + tulos + ", odotettu tulos: " + puita);
            if (print > 2 && G.size() < 50) {
                System.out.println("SyÃ¶te oli:");
                System.out.print(GraphMaker.toString(G, 1));
            }

            return false;
        }
    }






}