package Viikko2;

import java.util.*;

public class TRAII_23_X2_hilkolli implements TRAII_23_X2 {
    Random rnd = new Random();
    /**
     * ITSEARVIOINTI TĆ„HĆ„N:
     * Yksittäisenä operaationa contains on hyvin nopea ja vaati huomattavan määrän toistoja. Toistan contains operaation 200 000 kertaa ja otan keskimääräisen suoritusajan talteen näistä.
     * Jokainen operaatio tehdään eri avaimella, jotta avaimen vaikutuksestakin saadaan keskiarvo.
     * Käytän tässä vaiheessa keskiarvoa, sillä mediaanin laskemiseen menee huomattavasti pidempi aika ja täysin poikkeukselliset arvot jätetään jokatapauksessa huomiotta seuraavassa vaiheessa.
     *
     * Toistan sitten tämän 200 000 keskiarvoajon 100 kertaa ja otan niistä sadasta ajosta mediaanin.
     * Mediaanin käyttö tässä poistaa mahdolliset hyvin poikkeuksellisen keskiarvon saaneet ajot.
     *
     *
     * Testatessa tietokone (kannettava) on liitetty verkkovirtaan ja virranhallinta-asetukset on säädetty suorituskykyä ajatellen. Muut ohjelmat on sammutettu kokonaan.
     * Näyttöjä käytössä vain yksi.
     * Testasin testiohjelman toimintaa useita ajoja ja tulokset ovat johdonmukaisia ja yhteneviä. Käytännössä ne olivat lähes täsmälleen samat aina. 1-2ns eroja syntyi todennäköisesti vain pyöristyksistä. Tätä voisi parantaa lisäämällä tarkkuutta sisältämään desimaalit.
     *
     * Testasin testiohjelman toimintaa siten, että se suoritti kaksi containsKey-operaatiota peräkkäin yhden sijaan. Käytetty aika kaksinkertaistui.
     * Testasin testiohjelman toimintaa siten, että se suoritti kolme containsKey-operaatiota peräkkäin yhden sijaan. Käytetty aika kolminkertaistui.
     * Tästä voidaan päätellä, että mittaus tapahtuu containsKey-operaatiolle oikein ja itse metodi toimii oikein.
     *
     * "Käynnissä" on intelliJ:n lisäksi vain windowsin tehtävienhallinta, josta tarkkailin kellotaajuutta testauksen aikana. Kellotaajuus säilyi kohtalaisen tasaisena 4.2-4.4GHz välillä, keskimäärin ~4.29GHz. Tässä kohdassa voitaisiin parantaa lukitsemalla kellotaajuus, joskin nytkin mennään suhtellisen tasaisesti.
     *
     * Testit ajettu i7-11800H prosessorilla @ 4.2-4.4GHz.
     * Testi "TreeMap n = 1048576" kesti ~640ms. Jos testi kestää liian kauan mahdollisesti hitaammalla koneella, säädä operaatoiden toistoa (int operaatiot) tai ajettavien testien lukumäärää (int testit) vähemmäksi.
     *
     * Testattaessa nähdään selvästi HashMapin contains-operaation vakioaikaisuus. Testiajon sisällä kaikki testit ovat max 1ns päässä toisistaan tämä ero tullee mahdollisesti lähinnä pyöristyksessä lähimpään nanosekuntiin. Yleensä kuitenkin kaikissa testeissä samat (1ns tai 2ns)
     * Testattaessa nähdään selvästi TreeMapin contains-operaation aikavaativuuden logaritmisyys. Yhden kokoisen syötteen contains operaation kestona saadaan 1ns (kesto on 1ns-2ns ja ajon n = 1048576 kesto on 30ns (suora logn tästä olisi 20). Muutkin arvot sattuvat hyvin logaritmisesti.
     **/
    /**
     * Mittaa annetun kuvauksen containsKey -operaation aikavaativuuden nanosekunteina.
     * Mittaa ns. normaalin onnistuneen suorituksen. Ei siis minimiĆ¤ tai maksimia.
     * Ei muuta kuvausta (lisĆ¤Ć¤ tai poista alkioita).
     *
     * @param M testattava kuvaus
     * @return containsKey operaation normikesto nanosekunteina
     */

    @Override
    public long containsKeyNopeus(Map<Double, Double> M) {
        int operaatiot = 200000; // operaation toistomäärä yhdellä testikierroksella
        int testit = 100; // testien lukumäärä yhdellä testimetodin ajolla

        double key;
        long start;
        long end;
        long[] results = new long[testit];

        for (int i = 0; i < results.length; i++) {
            key = rnd.nextDouble();
            start = System.nanoTime();
            for (int j = 0; j < operaatiot; j++) {
                M.containsKey(key);
            }
            end = System.nanoTime();
            results[i] =  (end - start) / operaatiot;
        }

        Arrays.sort(results); // järjestellään mediaanin ottamista varten.
        return results[results.length/2];
    }
}