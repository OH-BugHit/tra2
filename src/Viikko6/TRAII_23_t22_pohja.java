package Viikko6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class TRAII_23_t22_pohja {

    public static void main(String[] args) {

        // defaults
        int rahaSumma = 69;

        // rahasumma komentoriviltÃ¤
        if (args.length > 0)
            rahaSumma = Integer.parseInt(args[0]);

        ArrayList<Integer> Kolikot = new ArrayList<>();

        // kolikot komentoriviltÃ¤
        for (int i = 1; i < args.length; i++)
            Kolikot.add(Integer.valueOf(args[i]));

        if (Kolikot.size() == 0) {
            Kolikot.add(1);
            Kolikot.add(2);
            Kolikot.add(5);
            Kolikot.add(10);
            Kolikot.add(40);
            Kolikot.add(50);
        }

        System.out.println("Dynaaminen");
        LinkedList<Integer> kolikot = dynjako23(rahaSumma, Kolikot);

        System.out.println("Kolikoita tarvitaan " + kolikot.size() + " kpl");
        System.out.println("Kolikot: " + kolikot);

    }


    /**
     * Rahajako dynaamisen ohjelmoinnin keinoin
     *
     * @param rahaSumma kasattava rahamÃ¤Ã¤rÃ¤
     * @param Kolikot   kÃ¤ytÃ¶ssÃ¤ olevien kolikkojen arvot
     * @return tarvittavien kolikkojen lukumÃ¤Ã¤rÃ¤
     */
    static int[] dynjako(int rahaSumma, Collection<Integer> Kolikot) {
        int[] kolikkoMaara = new int[rahaSumma + 1];
        int[] kolikkoTaulukko = new int[rahaSumma +1];
        kolikkoMaara[0] = 0;
        // haetaan ja talletetaan kaikki osaratkaisut
        for (int rahaSummaIter = 1; rahaSummaIter <= rahaSumma; rahaSummaIter++) {
            int min = rahaSummaIter;
            int koli = rahaSummaIter;
            // kullakin kolikolla
            for (Integer kolikko : Kolikot) {
                if (kolikko <= rahaSummaIter) {
                    int kolikoita = kolikkoMaara[rahaSummaIter - kolikko] + 1;
                    if (kolikoita < min) {
                        koli = kolikko;
                        min = kolikoita;
                    }
                }
            }
            kolikkoTaulukko[rahaSummaIter] = koli;
            kolikkoMaara[rahaSummaIter] = min;
        }
        return kolikkoTaulukko;
    }

    /**
     * Rahajako dynaamisen ohjelmoinnin keinoin
     *
     * @param rahaSumma kasattava rahamÃ¤Ã¤rÃ¤
     * @param Kolikot   kÃ¤ytÃ¶ssÃ¤ olevien kolikkojen arvot
     * @return lista palautettavista kolikoista
     */
    static LinkedList<Integer> dynjako23(int rahaSumma, Collection<Integer> Kolikot) {
        LinkedList<Integer> tulos = new LinkedList<>();
        int[] kolikkoLista = dynjako(rahaSumma, Kolikot);
        while (rahaSumma > 0) {
            tulos.add(kolikkoLista[rahaSumma]);
            rahaSumma -= kolikkoLista[rahaSumma];
        }
        return tulos;
    }
}