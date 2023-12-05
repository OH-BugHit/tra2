package Viikko7;

import javax.xml.crypto.Data;
import java.util.*;

public class TRAII_23_X6_hilkolli implements TRAII_23_X6 {

    /**
     * Itsearviointi tÃ¤hÃ¤n:
     * Heurestiikalla ilman satunnaisuutta päästään testiohjelman tarjoamalla syötteellä aina 20 testeissä keskimäärin 1,00 eroon. Tämä tulos on aina mukana, joten ainakin testiohjelman syötteelle keskiarvo on aina enintään 1,00
     * Satunnaisuus tehtiin satunnaistuttamalla syötteen alkupään rakentumista. Tämä on minulle mieleentulevista vaihtoehdoista soveltuvin tapa lisätä satunnaisuutta tähän suunnittelemaani algoritmiin.
     * Satunnaistan sen, kuinka pitkältä alalta syöte on sekaisin alkupäässä.
     * Algoritmini heurestiikka perustuu syötteen loppupäällä vajaiden laatikoiden täydentämiseen ja alkuosan rakenne vaikuttaa siis jonkin verran siihen miten hyvin loppuosa voi istua laatikoihin.
     *
     * Algoritmia voisi parantaa kenties "huonontomalla" heurestiikkaa ja korostamalla satunnaisuutta, tai sekoittamalla alkuosan ja melkein loppuosan syötettä tilastollisesti järkevämmin.
     * Myöskin liittämällä tämän heurestiikan tarjoama vakio 1,00 ratkaisu, joka löytyy yhdellä ajolla toiseen algoritmiin, joka hyödyntäisi enemmän satunnaisuutta yksinkertaisemmalla heurestiikalla, voisi olla hyvä ja tehokas keino.
     * Aikavaativuudeksi näyttäisi tulevan O(n*x+nlogn),jossa n = syötteen alkioiden määrä ja x = laatikoiden määrä ratkaisussa. (nlogn mainittu sillä n*x voi olla pienempi).
     * Aikavaativuutta ennen ajoa voisi arvioida esim.  x ~ "sum(L)/laatikonKoko" +1
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
        int maxIteraatiot = 10; // Suurella syötteellä optimin löytyminen on hyvin epätodennäköistä, joten iteraatioiden määrä voisi olla yhtä hyvin 1 sillä jo ensimmäinen löytää tämän ainakin annetun testiohjelman syötteillä...
        double syotteenJarjestyksenPituus;
        if (L.size() < 35) { // Ero keskimäärin 0,00-0,60
            maxIteraatiot = 100000;
        }
        else if (L.size() < 301) { // Satunnaisesti löytyy erolla 0.
            maxIteraatiot = 30000;
        }
        else if (L.size() < 3001) { // Erittäin harvoin löytyy erolla 0.
            maxIteraatiot = 1000;
        }

        for (int iteraatiot = 0; iteraatiot < maxIteraatiot; iteraatiot++) { // Toistetaan iteraatioiden verran
            List<List<Integer>> tulos = new ArrayList<>();
            if (L.isEmpty()) {    // tyhjällä syötteellä tyhjä tulos
                return tulos;
            }

            // Arvotaan alussa sekoisin tulevan syötteen pituus
            Random r = new Random(System.currentTimeMillis());
            syotteenJarjestyksenPituus = r.nextDouble(1); // 1 = 100%

            ArrayList<Integer> syote = new ArrayList<>(L); // En ollut varma saako syötettä järjestää kun sitä ei saanut muokata niin otin kopion
            syote.sort(Collections.reverseOrder()); // Järjestetään syöte käänteiseen järjestykseen. Tilkitsee hyvin vajaukset kun ainakin loppupäässä on järjestyksessä tavaraa.

            if (iteraatiot != 0) { // Randomisaatio, ensimmäinen iteraatio antaa hyvän heurestiikan tuloksen niin tehdään kerran ilman.
                ArrayList<Integer> sekoitettuSyote = new ArrayList<>();
                for (int i = 0; i < syote.size() *syotteenJarjestyksenPituus; i++) { // Satunnainen määrä isoa päätä otetaan
                    sekoitettuSyote.add(syote.get(i));
                }
                Collections.shuffle(sekoitettuSyote, new Random(System.currentTimeMillis())); // Sekoitetaan iso pää syötteestä
                for (int i = sekoitettuSyote.size(); i < syote.size(); i++) {
                    sekoitettuSyote.add(i,syote.get(i)); // Lisätään pienemmät järjestyksessä
                }
                syote = sekoitettuSyote;
            }

            HashMap<Integer, LinkedList<Integer>> vajaus = new HashMap<>(); // Luodaan kuvaus, joka sisältää avaimena tuloksesta löytyvän laatikon vajausmäärän ja arvona listan indeksistä (tai useammasta) josta laatikko tuloslistasta löytyy

            int suurinVajaus = 0; // Ylläpidetään suurinta vajausta nopeaan vertailuun. Tällä taklataan hankalien syötteiden aiheuttamaa aikavaativuuden kasvua.
            LinkedList<Integer> vajausArvot = new LinkedList<>(); // Luodaan linkitetty lista (poistoja keskeltä voi tulla niin siksi linkedList), jossa pidetään yllä tulokseen laitettujen laatikkojen vajauksia suuruusjärjestyksessä. Voidaan hakea sopiva vajaus luvulle.
            List<Integer> avoinLaatikko = new LinkedList<>(); // epätyhjälle syötteelle luodaan uusi avoin laatikko

            tulos.add(avoinLaatikko); // Lisätään ensimmäinen laatikko listaan.
            int laatikonIndeksi = 0; // Aloitetaan laatikoiden indeksointi nollasta
            int avoimenTaytto = 0; // Ja avoimen täyttö on 0
            boolean ok = false; // Tätä käytetään, kun lisätään vanhaan laatikkoon, jos onnistui niin mennään seuraavaan lukuun.

            // Syötteen läpikäynti
            for (int luku : syote) {
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
            } else if (tulos.size() < parasTulos.size()) { // Otetaan talteen paras tulos iteraatioista.
                parasTulos = tulos;
            }
        }
        return parasTulos;
    }

    private boolean lisaaVanhaanLaatikkoon(int luku, HashMap<Integer, LinkedList<Integer>> vajaus, List<List<Integer>> tulos, LinkedList<Integer> vajausArvot) {
        // Triviaali eli jos sattuisi sopivalla vajauksella olemaan..
        if (vajaus.containsKey(luku)) { // jos löytyy juuri sopiva
            LinkedList<Integer> loytynyt = vajaus.get(luku); // haetaan lista kaikista tulokseen lisätyistä laatikoista joissa on luvun kokoinen vaje
            tulos.get(loytynyt.get(0)).add(luku); // vajauksesta löytyy siis lista viittauksista tuloksen indeksiin/indekseihin joista löytyy tämä nimenomainen vajaus. Tuloksessa olevaan, listan ensimmäisen arvon mukaiseen indeksiin lisätään siis laatikkoon sinne mahtuva luku.
            loytynyt.remove(0); // tästä laatikosta ei enää puutu mitään niin poistetaan se lukua vastaavasta listasta
            if (loytynyt.isEmpty()) {
                vajaus.remove(luku); // Ja mikäli tätä lukua ei vastaa enää minkään laatikon vaje, poistetaan avain kuvauksesta kokonaan
            } else {
                vajaus.put(luku,loytynyt); // Muussa tapauksessa asetetaan alusta yhdellä lyhennetty lista kuvaukseen samalle avaimelle.
            }
            Iterator<Integer> iterator = vajausArvot.listIterator(); // poistetaan vajausarvoista vajaus, jota ei enää ole
            while(iterator.hasNext()) {
                int i = iterator.next();
                if (i == luku) {
                    iterator.remove();
                    break;
                }
            }
            return true; // Palautetaan true, sillä luku on nyt käytetty
        } // Muussa tapauksessa halutaan kuitenkin lisätä luku johonkin sopivaan avoimeen laatikkoon
        int vajaInd = 0; // Ylläpidetään indeksiä
        Iterator<Integer> iterator = vajausArvot.listIterator(); // Vajausarvoissa on lista kaikkien laatikoiden vajausmääristä järjestyksessä
        int mistaVajauksestaPoistetaan = 0;
        int a;
        while (iterator.hasNext()) { // Etsitään sopiva vajaus johon mahtuu, tällä voidaan sitten hakea hashMapista keynä se oikea laatikko. Näin vältetään hashmapista hidas etsiskely.
            a = iterator.next();
            if (luku <= a) {
                mistaVajauksestaPoistetaan = a;
                break;
            }
            vajaInd ++;
        }
        if (mistaVajauksestaPoistetaan != 0) {
            vajausArvot.set(vajaInd,vajausArvot.get(vajaInd)-luku); // Päivitetään vajausArvoa vastaamaan uutta vajausta kun luku lisätään laatikkoon
            LinkedList<Integer> lista = vajaus.get(mistaVajauksestaPoistetaan); // Haetaan kuvauksesta lista tulokseen lisätyistä laatioista joista puuttuu vajausArvo-listasta havaittu määrä
            int tulosIndeksi = lista.remove(0);; // Otetaan indeksinumero listan alusta (poistetaam se samalla listasta)
            if (lista.isEmpty()) { // Jos lista tälle vajaukselle jää tyhjäksi
                vajaus.remove(mistaVajauksestaPoistetaan); // Poistetaan se kuvauksesta
            } else {
                vajaus.put(mistaVajauksestaPoistetaan, lista); // Muussa tapauksessa asetetaan päivitetty lista kuvaukseen
            }
            otaVajausTalteen(vajaus,mistaVajauksestaPoistetaan-luku,tulosIndeksi); // Talletetaan uusi vajaus
            tulos.get(tulosIndeksi).add(luku); // Lisätään tuloksessa olevaan oikeaan laatikkoon luku
            return true; // Palautetaan true, sillä luku on nyt käytetty
        }
        return false; // Ei löytynyt sopivaa
    }

    private static void otaVajausTalteen(HashMap<Integer, LinkedList<Integer>> vajaus, int tilaaJai, int laatikonIndeksi) {
        if (vajaus.containsKey(tilaaJai)) { // Jos on jo samalla vajauksella laatikko niin lisätään listaksi toinen indeksikohta
            vajaus.get(tilaaJai).add(laatikonIndeksi);
        } else {
            LinkedList<Integer> indeksiLista = new LinkedList<>(); // Muussa tapauksessa luodaan uusi lista
            indeksiLista.add(laatikonIndeksi); // Laitetaan listaan laatikon indeksi
            vajaus.put(tilaaJai,indeksiLista); // Ja laitetaan vajausmappiin, avaimena siis minkä verran laatikko on vajaa
        }
    }
}