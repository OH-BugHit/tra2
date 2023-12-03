package Viikko7;

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

        int suurinVajaus = 0;
        List<List<Integer>> tulos = new ArrayList<>(L.size()/laatikonKoko*2);
        HashMap<Integer,LinkedList<Integer>> vajaus = new HashMap<>(L.size()/laatikonKoko*2); // Luodaan vähän reilu kuvaus vajauksien ylläpitoja varten

        if (L.isEmpty())    // tyhjällä syötteellä tyhjä tulos
            return tulos;

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
                ok = lisaaVanhaanLaatikkoon(luku,vajaus,tulos);
            }
            if (!ok) {
                // jollei mahdu avoimeen laatikkoon, tehdÃ¤Ã¤n uusi avoin laatikko
                if (avoimenTaytto + luku > laatikonKoko) {
                    otaVajausTalteen(vajaus, laatikonKoko - avoimenTaytto, laatikonIndeksi); // Jätetään talteen paljonko laatikkoon jäi tilaa
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

        // Tuloksen tiivistys

        return tulos;
    }

    private boolean lisaaVanhaanLaatikkoon(int luku, HashMap<Integer, LinkedList<Integer>> vajaus, List<List<Integer>> tulos) {
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