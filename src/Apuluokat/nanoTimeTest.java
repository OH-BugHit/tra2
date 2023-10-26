package Apuluokat;// nanoTimeTest.java SJ
// measuring time usage and resolution of nanoTime()
// results depend on OS, JVM, and processor

public class nanoTimeTest {

    public static void main(String [] args) {


        // try, e.g., 
        // 
        // java nanoTimeTest 100 2 20
        // java nanoTimeTest 100 10 0
        // java nanoTimeTest 10000 10 0
        // java nanoTimeTest 100000 10 0

        int N = 10000; // how many calls to nanoTime() in a test
        int K = 10; // how many tests
        int T = 5; // how many results to print

        if (args.length > 0)
            N = Integer.parseInt(args[0]);

        if (args.length > 1)
            K = Integer.parseInt(args[1]);

        if (args.length > 2)
            T = Integer.parseInt(args[2]);

        for (int k = 0; k < K; k++) 
            testNanoTime(N, T);

    } // main()


    static void testNanoTime(int n, int tul) {

        long[] times = new long[n];

        // call nanoTime() n times, put results to array
        for (int i = 0; i < n; i++)
            times[i] = System.nanoTime();

        // calculate difference of consecutive times
        long[] differences = new long[n-1];

        // also print if needed
        for (int i = 1; i < n; i++) {
            if (i < tul+1)
                System.out.println(times[i] + " " + (times[i]-times[i-1]));
            differences[i-1] = times[i]-times[i-1];
        }

        // average time
        long avg = (times[n-1]-times[0])/(n-1);

        // find min, median, and max
        java.util.Arrays.sort(differences);

        System.out.println("min=" + differences[0] + " avg=" + avg + " med=" + differences[n/2] + " max=" + differences[n-2]);


    }


}