package Viikko7;

import javax.xml.crypto.Data;
import java.util.*;

public class TRAII_23_X6_pohja implements TRAII_23_X6 {

    /**
     * Itsearviointi tÃ¤hÃ¤n:
     * 
     * 
     * 
     */

    /**
     * Jaottelee kokoelman C kokonaisluvut mahdollisimman vÃ¤hiin listoihin siten, ettÃ¤ kunkin listan alkioiden summa on
     * korkeintaan laatikonKoko.
     *
     * @param L            jaoteltavat alkiot
     * @param laatikonKoko kunkin tuloslistan maksimikapasiteetti
     * @param maxAika      suurin kÃ¤ytettÃ¤vissÃ¤ oleva aika (sekunteja)
     * @return tuloslistojen lista
     */
    @Override
    public List<List<Integer>> laatikkoJako(final ArrayList<Integer> L, int laatikonKoko, int maxAika) {
        List<List<Integer>> parasTulos = new ArrayList<>();

        for (int iteraatiot = 0; iteraatiot < 20; iteraatiot++) {
            Random r = new Random(System.currentTimeMillis());
            List<List<Integer>> tulos = new ArrayList<>();
            if (L.isEmpty()) {    // tyhjällä syötteellä tyhjä tulos
                return tulos;
            }
            L.sort(Collections.reverseOrder());

            HashMap<Integer, LinkedList<Integer>> vajaus = new HashMap<>(L.size() / laatikonKoko * 2); // Luodaan vähän reilu kuvaus vajauksien ylläpitoja varten

            int suurinVajaus = 0; // Ylläpidetään suurinta vajausta nopeaan vertailuun, jotta vältytään turhalta vajausArvot get lastilta joka nyt ei olis kyllä paha sekään...
            LinkedList<Integer> vajausArvot = new LinkedList<>();
            // epätyhjälle syötteelle luodaan uusi avoin laatikko
            List<Integer> avoinLaatikko = new LinkedList<>();

            tulos.add(avoinLaatikko);
            int laatikonIndeksi = 0;
            int avoimenTaytto = 0;
            boolean ok = false;

            // Syötteen läpikäynti
            for (int luku : L) {
                if (luku > laatikonKoko || luku < 1) // tÃ¤mÃ¤ olisi virhe syÃ¶tteessÃ¤, ei pitÃ¤isi esiintyÃ¤
                    throw new RuntimeException("Liian iso/pieni alkio: " + luku + " (max = " + laatikonKoko + ")");
                if (luku <= suurinVajaus) {
                    ok = lisaaVanhaanLaatikkoon(luku, vajaus, tulos, vajausArvot);
                }
                if (!ok) {
                    // jollei mahdu avoimeen laatikkoon, tehdÃ¤Ã¤n uusi avoin laatikko
                    if (avoimenTaytto + luku > laatikonKoko) {
                        otaVajausTalteen(vajaus, laatikonKoko - avoimenTaytto, laatikonIndeksi); // Jätetään talteen paljonko laatikkoon jäi tilaa
                        Iterator<Integer> i = vajausArvot.listIterator(0);
                        int indeksi = 0;
                        while (i.hasNext()) {
                            int a = i.next();
                            if (a > laatikonKoko - avoimenTaytto) {
                                break;
                            }
                            indeksi++;
                        }
                        vajausArvot.add(indeksi, laatikonKoko - avoimenTaytto);
                        if (suurinVajaus < laatikonKoko - avoimenTaytto) {
                            suurinVajaus = laatikonKoko - avoimenTaytto;
                        }
                        avoinLaatikko = new LinkedList<>();
                        laatikonIndeksi++; // vaihdetaan tuloksessa seuraavaan indeksiin
                        tulos.add(avoinLaatikko);
                        avoimenTaytto = 0;
                    }
                    // lisÃ¤tÃ¤Ã¤n avoimeen laatikkoon
                    avoinLaatikko.add(luku);
                    avoimenTaytto += luku;
                }
                ok = false;
            }
            if (iteraatiot == 0) {
                parasTulos = tulos;
            } else if (tulos.size() < parasTulos.size()) {
                parasTulos = tulos;
            }
        }

        return parasTulos;
    }

    private boolean lisaaVanhaanLaatikkoon(int luku, HashMap<Integer, LinkedList<Integer>> vajaus, List<List<Integer>> tulos, LinkedList<Integer> vajausArvot) {
        // Triviaali eli jos sattuisi sopivalla vajauksella olemaan..
        if (vajaus.containsKey(luku)) {
            LinkedList<Integer> loytynyt = vajaus.get(luku);
            tulos.get(loytynyt.get(0)).add(luku); // vajauksesta löytyy siis lista viittauksista tuloksen indeksiin/indekseihin joista löytyy tämä nimenomainen vajaus
            loytynyt.remove(0);
            if (loytynyt.isEmpty()) {
                vajaus.remove(luku);
            } else {
                vajaus.put(luku,loytynyt);
            }
            Iterator<Integer> iterator = vajausArvot.listIterator();
            while(iterator.hasNext()) {
                int i = iterator.next();
                if (i == luku) {
                    iterator.remove();
                    break;
                }
            }
            return true;
        }
        int vajaInd = 0;
        Iterator<Integer> iterator = vajausArvot.listIterator();
        int mistaVajauksestaPoistetaan = 0;
        int a;
        while (iterator.hasNext()) {
            a = iterator.next();
            if (luku <= a) {
                mistaVajauksestaPoistetaan = a;
                break;
            }
            vajaInd ++;
        }
        if (mistaVajauksestaPoistetaan != 0) {
            vajausArvot.set(vajaInd,vajausArvot.get(vajaInd)-luku);
            LinkedList<Integer> lista = vajaus.get(mistaVajauksestaPoistetaan);
            int tulosIndeksi = lista.get(0);
            lista.remove(0);
            if (lista.isEmpty()) {
                vajaus.remove(mistaVajauksestaPoistetaan);
            } else {
                vajaus.put(mistaVajauksestaPoistetaan, lista);
            }
            otaVajausTalteen(vajaus,mistaVajauksestaPoistetaan-luku,tulosIndeksi); // TÄÄLLÄ EI EHKÄ LISÄTÄ UUTTA VAJAUSTA!
            tulos.get(tulosIndeksi).add(luku);
            return true;
        }
        return false; // Ei löytynyt sopivaa
    }

    private static void otaVajausTalteen(HashMap<Integer, LinkedList<Integer>> vajaus, int tilaaJai, int laatikonIndeksi) { // Tällä hetkellä korjaa jos sattuu puuttumaan sama, tarvitaan vielä joku taulukko jossa on kaikki vajaukset tjs.
        if (vajaus.containsKey(tilaaJai)) { // Jos on jo samalla vajauksella laatikko niin lisätään listaksi toinen indeksikohta
            vajaus.get(tilaaJai).add(laatikonIndeksi);
        } else {
            LinkedList<Integer> indeksiLista = new LinkedList<>(); // Muussa tapauksessa luodaan uusi lista
            indeksiLista.add(laatikonIndeksi); // Laitetaan listaan laatikon indeksi
            vajaus.put(tilaaJai,indeksiLista); // Ja laitetaan vajausmappiin, avaimena siis minkä verran laatikko on vajaa
        }
    }
}