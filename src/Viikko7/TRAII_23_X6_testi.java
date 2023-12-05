package Viikko7;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TRAII_23_X6_testi {

  static TRAII_23_X6 testattava = new TRAII_23_X6_hilkolli();

  static Random rnd = new Random();

  public static void main(String[] args) {

    // komentoriviparametrina
    // ensimmÃ¤isen testin alkiomÃ¤Ã¤rÃ¤
    int N = 30;
    if (args.length > 0) N = Integer.parseInt(args[0]);

    // ensimmÃ¤isen testin lukujen suuruusluokka
    int M = N;
    if (args.length > 1) M = Integer.parseInt(args[1]);

    // ensimmÃ¤isen testin optimi
    int KM = 4;
    if (args.length > 2) KM = Integer.parseInt(args[2]);

    // satunnaislukusiemen
    int siemen = (int) System.currentTimeMillis();
    if (args.length > 3) siemen = Integer.parseInt(args[3]);
    rnd.setSeed(siemen);

    // tulostusten mÃ¤Ã¤rÃ¤
    int print = 5;
    if (args.length > 4) print = Integer.parseInt(args[4]);

    // kutsutaan N kokoisilla syÃ¶tteillÃ¤, pienillÃ¤ luvuilla
    int nTest = 5;
    int kaikkiErotYht = 0;
    int kaikkiTestit = 0;
    long alku = System.currentTimeMillis();
    int laatikonKoko = N * M / KM;

    boolean ok = true;

    // N-kokoisella syÃ¶tteellÃ¤
    int ero = testaaX6(N, laatikonKoko, KM, 0, nTest, 1, print);
    if (ero < 0) ok = false;
    else {
      kaikkiErotYht += ero;
      kaikkiTestit += nTest;
    }

    // isommilla syÃ¶tteillÃ¤
    ero = testaaX6(N * 10, laatikonKoko * 100, KM * 2, 0, nTest, 2, print);
    if (ero < 0) ok = false;
    else {
      kaikkiErotYht += ero;
      kaikkiTestit += nTest;
    }

    // vielÃ¤ isommilla syÃ¶tteillÃ¤
    ero = testaaX6(N * 100, laatikonKoko * 200, KM * 30, 0, nTest, 3, print);
    if (ero < 0) ok = false;
    else {
      kaikkiErotYht += ero;
      kaikkiTestit += nTest;
    }

    // vielÃ¤ isommilla syÃ¶tteillÃ¤
    ero = testaaX6(N * 1000, laatikonKoko * 1000, KM * 500, 0, nTest, 4, print);
    if (ero < 0) ok = false;
    else {
      kaikkiErotYht += ero;
      kaikkiTestit += nTest;
    }

    if (ok)
      System.out.format(
          "%nEro kaikissa %d testeissÃ¤ keskimÃ¤Ã¤rin %.2f%n",
          kaikkiTestit, 1.0 * kaikkiErotYht / kaikkiTestit);
    else System.out.println("\nJossain testissÃ¤ virheellinen tulos");

    long aika = System.currentTimeMillis() - alku;
    System.out.format(
        "Aikaa kului %.2f sek, %.2f sek/testi (sisÃ¤ltÃ¤en syÃ¶tteen generoinnin ja tarkastukset).",
        aika / 1000.0, aika / (1000.0 * kaikkiTestit));
  }

  static int testaaX6(
      int N, int laatikonKoko, int tavoiteOptimi, int vaje, int toistoja, int maxAika, int print) {
    boolean ok = true;
    int eroYht = 0;
    for (int i = 0; i < toistoja; i++) {
      int ero = testaaX6(N, laatikonKoko, tavoiteOptimi, vaje, maxAika, print);
      if (ero < 0) ok = false;
      else eroYht += ero;
    }
    if (ok) System.out.format("%nN = %d: ero keskimÃ¤Ã¤rin %.2f%n", N, 1.0 * eroYht / toistoja);
    else System.out.println("\nN = " + N + ": Jossain testissÃ¤ virheellinen tulos");

    return ok ? eroYht : -1;
  }

  static int testaaX6(
      int n, int laatikonKoko, int tavoiteOptimi, int vaje, int maxAika, int print) {
    AtomicInteger todellinenOptimi = new AtomicInteger(tavoiteOptimi);
    ArrayList<Integer> syote = teeKaukaloSyote(n, laatikonKoko, todellinenOptimi, vaje, rnd);

    return testaaX6(syote, laatikonKoko, todellinenOptimi.get(), print, maxAika);
  }

  static int testaaX6(
      ArrayList<Integer> syote, int laatikonKoko, int optimi, int print, int maxAika) {

    int ero = 0;
    long syoteSumEnnen = sum(syote);

    if (print > 0)
      System.out.format(
          "%nTesti, n=%d sum=%d laatikonKoko=%d lukujenkeskikoko=%.2f optimi=%d maxAika=%d%n",
          syote.size(),
          syoteSumEnnen,
          laatikonKoko,
          (syoteSumEnnen * 1.0 / syote.size()),
          optimi,
          maxAika);

    if (print > 3 && syote.size() <= 30)
        System.out.println("SyÃ¶te: " + syote);

    Iterator<Integer> li = syote.iterator();

    // mitataan myÃ¶s aika ja huomautetaan jos yliaikaa
    long alku = System.currentTimeMillis();

    List<List<Integer>> tulos = testattava.laatikkoJako(syote, laatikonKoko, maxAika);

    long aika = System.currentTimeMillis() - alku;
    if (aika > maxAika * 1300L) // 30% pelivaraa
        System.out.println("Turhan pitkÃ¤ suoritus: " + (aika / 1000.0) + "s (max=" + maxAika + "s)");

    if (print > 0)
      System.out.println("tuloskaukaloita=" + tulos.size() + "  ero=" + (tulos.size() - optimi));

    ero = tulos.size() - optimi;

    if (print > 3 && syote.size() <= 50) tulostaLaatikot(tulos);

    long syoteSumJalkeen = sum(syote);

    if (syoteSumEnnen != syoteSumJalkeen) {
      System.out.println("  SyÃ¶tettÃ¤ on muutettu!");
      ero = -1;
    }

    try {
      boolean hN = li.hasNext();
    } catch (ConcurrentModificationException e) {
      System.out.println("  SyÃ¶tettÃ¤ on muutettu!");
      ero = -1;
    }

    long kaukaloideSumma = tarkastaLaatikot(tulos, laatikonKoko);
    if (kaukaloideSumma < 0) ero = -1;
    if (kaukaloideSumma != syoteSumEnnen) {
      System.out.println("Tuloksen kaukaloiden sisÃ¤ltÃ¶jen summa ei tÃ¤smÃ¤Ã¤ syÃ¶tteeseen!");
      ero = -1;
    }

    boolean samat = onkoSamat(syote, tulos, print);
    if (!samat) {
      System.out.println("Tuloksen kaukaloiden sisÃ¤llÃ¶t eivÃ¤t tÃ¤smÃ¤Ã¤ syÃ¶tteeseen!");
      ero = -1;
    }

    return ero;
  }

  static void tulostaLaatikot(List<List<Integer>> tulos) {
    for (List<Integer> k : tulos) {
      int sum = k.stream().reduce(0, Integer::sum);
      System.out.print(k);
      System.out.println(" (sum = " + sum + ")");
    }
  }

  static long tarkastaLaatikot(List<List<Integer>> tulos, int laatikonKoko) {
    long yht = 0;
    for (List<Integer> k : tulos) {
      long sum = sum(k);
      if (sum > laatikonKoko) {
        System.out.println("Liian iso laatikon sisÃ¤ltÃ¶: " + sum + " (max = " + laatikonKoko + ")");
        return -1;
      }
      yht += sum;
    }
    return yht;
  }

  /**
   * Kokoelman alkioiden summa
   *
   * @param L syÃ¶tekokoelma
   * @return alkioiden summa
   */
  static long sum(Collection<Integer> L) {
    long s = 0;
    for (int x : L)
        s += x;
    return s;
  }

  /**
   * Generoi kokonaislukulistan jonka alkiot voidaan jakaa optimi kappaleeseen kaukaloita joiden
   * koko on laatikonKoko.
   *
   * @param n AlkiomÃ¤Ã¤rÃ¤tavoite
   * @param laatikonKoko kunkin laatikon maksimisumma
   * @param optimi kutsuttaessa tavoite optimikaukalomÃ¤Ã¤rÃ¤ksi, palauttaa todellisen optimin
   * @param vaje paljonko tyhjÃ¤Ã¤ jÃ¤tetÃ¤Ã¤n kokonaissummaan
   * @param rnd satunnaislukugeneraattori
   * @return kokonaislukulista
   */
  static ArrayList<Integer> teeKaukaloSyote(
      int n, int laatikonKoko, AtomicInteger optimi, int vaje, Random rnd) {
    ArrayList<Integer> syote = new ArrayList<>(n);
    int optimiTavoite = optimi.get();
    optimi.set(0);
    int avgCount = n / optimiTavoite;
    int k = n; // alkioita jÃ¤ljellÃ¤
    while (k > 0) {
      optimi.incrementAndGet(); // uusi kaukalo
      int kaukalooon = Math.min(k, Math.max(2, avgCount / 4 + rnd.nextInt(3 * avgCount / 2)));
      if (kaukalooon == k - 1) kaukalooon = k;
      int kaukaloAvg = 1 + laatikonKoko / kaukalooon;
      int kaukaloSumma = laatikonKoko;
      while (kaukalooon > 0 && kaukaloSumma > 0) {
        int luku =
            kaukalooon == 1
                ? kaukaloSumma
                : Math.min(
                    kaukaloSumma,
                    Math.max(1, kaukaloAvg / 10 + (rnd.nextInt(16 * kaukaloAvg / 10))));
        syote.add(luku);
        k--;
        kaukaloSumma -= luku;
        kaukalooon--;
      }
    }

    Collections.shuffle(syote, rnd);

    // vajutetaan hieman jos niin pyydettiin
    while (vaje > 0) {
      int pos = rnd.nextInt(n);
      int vah = Math.max(rnd.nextInt(Math.max(1, vaje / 2)), 1);
      if (syote.get(pos) > vah) {
        syote.set(pos, syote.get(pos) - vah);
        vaje -= vah;
      }
    }

    return syote;
  }

  /**
   * Onko kaikki = L1 + L2
   *
   * @param kaikki kaikki alkiot
   * @return true jos samat alkiot, muuten false
   */
  static boolean onkoSamat(List<Integer> kaikki, List<List<Integer>> kaukalot, int print) {
    HashMap<Integer, Integer> MS = new HashMap<>(kaikki.size() * 2);
    HashMap<Integer, Integer> MT = new HashMap<>(kaikki.size() * 2);
    for (Integer x : kaikki) MS.compute(x, (k, v) -> v == null ? 1 : v + 1);
    for (List<Integer> C : kaukalot)
      for (Integer x : C) MT.compute(x, (k, v) -> v == null ? 1 : v + 1);
    return MS.equals(MT);
  }
}