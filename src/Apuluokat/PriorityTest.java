package Apuluokat;

import java.util.Random;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.PriorityQueue;

public class PriorityTest {

    static int increase = 20;   // /10
    static int elements = 200000;

    public static void main(String[] args) {

        if (args.length > 0)
            elements = Integer.valueOf(args[0]);

        int mittauksia = 10;

        if (args.length > 1)
            mittauksia = Integer.valueOf(args[1]);

        int[] t;
        int c1;

        Ajastin2 a;

        for (int kokoMittaus = 0; kokoMittaus < 3; kokoMittaus++) {


            System.out.println("N TreeSet PriorityQ PriorityQ(n) PQn/TS");

            for (int n = 1; n < elements; n = (n * increase) / 10) {


                long[] ajat = new long[3];
                long aika;
                for (int i = 0; i < 3; i++)
                    ajat[i] = Long.MAX_VALUE;
                int c2 = 0;


                for (int mittaus = 0; mittaus < mittauksia; mittaus++) {

                    t = randomarray(n, true);

                    a = new Ajastin2();
                    TreeSet<Integer> ts = new TreeSet<Integer>();

                    for (int mm = 0; mm < n; mm++) {
                        ts.add(t[mm]);
                    }

                    long s = 0;
                    for (int mm = 0; mm < n; mm++) {
                        s += ts.pollFirst();
                    }

                    aika = a.stop();

                    if (aika < ajat[0])
                        ajat[0] = aika;

                    // -------

                    a = new Ajastin2();
                    PriorityQueue<Integer> pq = new PriorityQueue<Integer>();

                    for (int mm = 0; mm < n; mm++) {
                        pq.offer(t[mm]);
                    }
                    s = 0;
                    for (int mm = 0; mm < n; mm++) {
                        s += pq.poll();
                    }


                    aika = a.stop();

                    if (aika < ajat[1])
                        ajat[1] = aika;

                    // -------

                    a = new Ajastin2();

                    pq = new PriorityQueue<Integer>((int) (n));

                    for (int mm = 0; mm < n; mm++) {
                        pq.offer(t[mm]);
                    }
                    s = 0;
                    for (int mm = 0; mm < n; mm++) {
                        s += pq.poll();
                    }

                    aika = a.stop();

                    if (aika < ajat[2])
                        ajat[2] = aika;

                    // -------

                    // -------

                } // for mittauksia

                System.out.print(n);
                for (int i = 0; i < 3; i++) {
                    // System.out.print(" " + ajat[i]); // ns
                    System.out.print(" " + ajat[i] / n); // ns/elem
                    // System.out.print(" " + (ajat[i] != 0 ? (long)4*n/(ajat[i]/1000) : 0)); // MB/s
                }
                System.out.print(" " + (double) ajat[2] / ajat[0]);
                System.out.println();

            } // for n

        }

    }


    static int[] randomarray(int n, boolean rand) {
        int[] t = new int[n];

        if (rand) {

            HashSet<Integer> alkiot = new HashSet<Integer>((int)(n*1.5));
            Random r = new Random(n);
            int i = 0;
            while (i < n) {
                int x = r.nextInt(n*2);
                if (alkiot.contains(x))
                    continue;
                t[i++] = x;
                alkiot.add(x);
            }

        }
        else
            for (int i = 0; i < n; i++)
                t[i] = 1;
        return t;
    }



}