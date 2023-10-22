package Apuluokat;

/**
 * Apuluokka ajan mittaamiseen.
 */
public class Ajastin2  implements Comparable<Ajastin2> {

	long start, end = 0L;
    String name = "";

    String fform = "%.3f";


    /**
     * Luo ja kÃ¤ynnistÃ¤Ã¤ uuden kellon.
     */
    public Ajastin2() {
        start = System.nanoTime();
    }

    /**
     * Luo ja kÃ¤ynnistÃ¤Ã¤ uuden kellon.
     * @param nimi kellon nimi
     */
    public Ajastin2(String nimi) {
        this();
        name = nimi;
    }

    /**
     * PysÃ¤yttÃ¤Ã¤ kellon
     * @return kulunut aika nanosekunteina
     */
    public long stop() {
        end = System.nanoTime();
        return (end - start);
    }

    /**
     * Kellon uusi kÃ¤ynnistys nollasta.
     */
    public void start() {
        start = System.nanoTime();
    }

    /**
     * Kellon uusi kÃ¤ynnistys siitÃ¤ mihin se edellisellÃ¤ stop:lla jÃ¤i.
     */
    public void restart() {
        if (end != 0L)
            start = System.nanoTime() - (end - start);
    }

    // kulunut aika nanosekunteina

    /**
     * KÃ¤ynnistyksestÃ¤ pysÃ¤ytykseen (tai nykyhetkeen jollei pysÃ¤ytysttÃ¤ ole tehty) kulunut aika.
     * @return aika nanosekunteina
     */
    public long time() {
        long t = end - start;
        if (end == 0L)
            t += System.nanoTime();
        return t;
    }

    /**
     * KÃ¤ynnistyksestÃ¤ pysÃ¤ytykseen (tai nykyhetkeen jollei pysÃ¤ytysttÃ¤ ole tehty) kulunut aika.
     * @return aika nanosekunteina
     */
    public long nanos() {
        long t = end - start;
        if (end == 0L)
            t += System.nanoTime();
        return t;
    }

    /**
     * KÃ¤ynnistyksestÃ¤ pysÃ¤ytykseen (tai nykyhetkeen jollei pysÃ¤ytysttÃ¤ ole tehty) kulunut aika.
     * @return aika mikrosekunteina
     */
    public double micros() {
        long t = end - start;
        if (end == 0L)
            t += System.nanoTime();
        return t/1000.0;
    }

    /**
     * KÃ¤ynnistyksestÃ¤ pysÃ¤ytykseen (tai nykyhetkeen jollei pysÃ¤ytysttÃ¤ ole tehty) kulunut aika.
     * @return aika millisekunteina
     */
    public double millis() {
        long t = end - start;
        if (end == 0L)
            t += System.nanoTime();
        return t/(1000.0*1000.0);
    }

    /**
     * KÃ¤ynnistyksestÃ¤ pysÃ¤ytykseen (tai nykyhetkeen jollei pysÃ¤ytysttÃ¤ ole tehty) kulunut aika.
     * @return aika sekunteina
     */
    public double sek() {
        long t = end - start;
        if (end == 0L)
            t += System.nanoTime();
        return t/(1000.0*1000.0*1000.0);
    }

    /**
     * compare two times, null is largest
     * @param b other timer
     * @return -1, 0, 1
     */
    public int compareTo(Ajastin2 b) {
        if (b == null)
            return -1;
        if (this.time() < b.time())
            return -1;
        else if (this.time() > b.time())
            return 1;
        else
            return 0;
    }

    /**
     * equality of values
     * @param b
     * @return
     */
    public boolean equals(Ajastin2 b) {
        if (b == null)
            return false;
        if (this.time() == b.time())
            return true;
        else
            return false;
    }


    /**
     * Ajan merkkijonoesitys, automaattinen yksikkÃ¶.
     * @return
     */
    public String toString() {
        long t = end - start;

        if (end == 0L)
            t += System.nanoTime();

        String ts = null;
        if (t < 1000)
            ts = t + " ns";

        else if (t < 100*1000)
            ts = String.format(fform, t/1000.0) + " us";

        else if (t < 100*1000*1000)
            ts = String.format(fform, t/(1000.0*1000.0)) + " ms";

        else
            ts = String.format(fform, t/(1000.0*1000.0*1000.0)) + " s";


        return name + " " + ts;
    }

}
			
