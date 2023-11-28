package Viikko6;

import java.util.Arrays;
import java.util.Comparator;

public class TRAII_23_t24_pohja {

    public static void main(String[] args) {

        // esimerkkiteksti josta muodostetaan loppuosataulukko
        String teksti = "abcccaabba";

        System.out.println("Merkkijono:" + teksti);

        // muodostetaan ja tulostetaan
        Integer[] lot = luoLoppuOsaTaulukko(teksti);
        System.out.println("Loppuosataulukko:");
        tulostaLoppuosaTaulukko(lot, teksti);

        System.out.println("\nEtsitÃ¤Ã¤n:");
        // kokeillaan paria etsimistÃ¤
        System.out.println("aaa: " + etsiLoppuosaTaulukosta("aaa", teksti, lot));    // ei lÃ¶ydy
        System.out.println("abc: " + etsiLoppuosaTaulukosta("abc", teksti, lot));   // lÃ¶ytyy alusta
        System.out.println("abba: " + etsiLoppuosaTaulukosta("abba", teksti, lot)); // lÃ¶ytyy lopusta
        System.out.println("ccc: " + etsiLoppuosaTaulukosta("ccc", teksti, lot)); // lÃ¶ytyy keskeltÃ¤
        System.out.println("abca: " + etsiLoppuosaTaulukosta("abca", teksti, lot)); // ei lÃ¶ydy
        System.out.println("ddd: " + etsiLoppuosaTaulukosta("ddd", teksti, lot)); // ei lÃ¶ydy

    }


    /**
     * Etsi avaimen esiintymisindeksi tekstistÃ¤ kÃ¤yttÃ¤en loppuosataulukkoa
     * @param avain etsittÃ¤vÃ¤ avain
     * @param teksti alkuperÃ¤inen teksti
     * @param loppuosaTaulukko  loppuosataulukko
     * @return avaimen esiintymiskohta, tai -1 jollei avainta lÃ¶ydy tekstistÃ¤ AA
     */
    static int etsiLoppuosaTaulukosta(String avain, String teksti, Integer[] loppuosaTaulukko) {
        int alku = 0;
        int loppu = loppuosaTaulukko.length -1;
        int indeksi;
        while (alku <= loppu) {
            indeksi = alku + (loppu - alku) / 2;
            if (avain.length() <= teksti.substring(loppuosaTaulukko[indeksi]).length()) { // Tarkistetaan riittääkö tekstin pituus
                int alkukohta =  loppuosaTaulukko[indeksi];
                int loppukohta =  loppuosaTaulukko[indeksi]+ avain.length();
                int cmp = teksti.substring(alkukohta, loppukohta).compareTo(avain); // Verrataan avainta indeksin kohdan tekstiin
                if (cmp == 0) { // Jos löytyi niin palautetaan indeksi josta löytyi
                    return loppuosaTaulukko[indeksi];
                }
                if (cmp < 0) { // Jos teksti on arvoltaan pienempi kuin avain (teksti tulee aakkosissa ennen avainta) niin etsitään loppuosatalukosta myöhemmästä vaiheesta
                    alku = indeksi +1; // +1 ja -1 koska indeksi oli äsken tutkittavana
                } else {
                    loppu = indeksi -1; // Jos teksti on arvoltaa suurempi kuin avain niin etsitään aiemmasta vaiheesta loppuosataulua
                }
            } else {
                if (teksti.substring(loppuosaTaulukko[indeksi]).compareTo(avain) < 0) {
                    alku = indeksi +1;
                } else {
                    loppu = indeksi -1;
                }
            }
        }
        return -1;  // jollei lÃ¶ytynyt
    }

    /**
     * Tulostaa loppuosataulukon ja alkuperÃ¤isen merkkijonon loppuosat
     * @param lot loppuosataulukko
     * @param teksti alkuperÃ¤inen teksti
     */
    static void tulostaLoppuosaTaulukko(Integer[] lot, String teksti) {
        for (int i = 0; i < teksti.length(); i++) {
            System.out.format("%3d: %3d %s%n", i,
                    lot[i], teksti.substring(lot[i]));
        }
    }

    /**
     * Luo loppuosataulukon annetusta tekstistÃ¤ (helpoimmalla tavalla).
     * Tehokkaampiakin tapoja on olemassa.
     * @param teksti merkkijono josta taulukko muodostetaan
     * @return tekstin loppuosataulukko
     */
    static Integer[] luoLoppuOsaTaulukko(String teksti) {
        int n = teksti.length();
        Integer[] loppuosataulukko = new Integer[n];
        for (int i = 0; i < n; i++)
            loppuosataulukko[i] = i;

        Arrays.sort(loppuosataulukko, new LoppuosaVertailija(loppuosataulukko, teksti));
        return loppuosataulukko;
    }


    /**
     * Loppuosataulukoiden indeksien vertailija jÃ¤rjestÃ¤mistÃ¤ varten.
     * TÃ¤mÃ¤ jÃ¤rjestÃ¤Ã¤ indeksti 0..n-1 sisÃ¤ltÃ¤vÃ¤n loppuosataulukon (Integer[]) merkkijonon
     * loppuosien mukaiseen aakkosjÃ¤rjestykseen.
     */
    static class LoppuosaVertailija implements Comparator<Integer> {

        Integer[] loppuosataulukko; // jÃ¤rjestettÃ¤vÃ¤ loppuosataulukko
        String teksti; // alkuperÃ¤inen merkkijono

        public LoppuosaVertailija(Integer[] loppuosataulukko, String teksti) {
            this.loppuosataulukko = loppuosataulukko;
            this.teksti = teksti;
        }

        /**
         *
         * Vertailee indekseja ind1 ja ind2 sen mukaan minkÃ¤lainen merkkijono niiden kohdilta lÃ¶ytyy.
         *
         * @param ind1 the first object to be compared.
         * @param ind2 the second object to be compared.
         * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than
         * the second.
         * @throws NullPointerException if an argument is null and this comparator does not permit null arguments
         * @throws ClassCastException   if the arguments' types prevent them from being compared by this comparator.
         */
        @Override
        public int compare(Integer ind1, Integer ind2) {
            return teksti.substring(ind1).compareTo(teksti.substring(ind2));
        }
    }

}