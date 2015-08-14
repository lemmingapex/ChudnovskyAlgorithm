package sw.chudnovskyalgorithm;

import org.apfloat.Apfloat;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;

/*
 * @author scott
 */
public class ChudnovskyAlgorithmThreadedTest {


    public static final long precision = 25000l;

    @Test
    public void testSingleThreaded() {
        System.out.println("PI: " + ChudnovskyAlgorithm.calculatePi(precision) + "\n");
    }

    @Test
    public void testMultiThreaded() {
        List<Range> ranges = ChudnovskyAlgorithm.calculateTermRanges(4, precision);
        System.out.println("number of ranges: " + ranges.size());

        ExecutorService executor = Executors.newFixedThreadPool(ranges.size());
        List<Future<List<Apfloat>>> futures = new ArrayList<Future<List<Apfloat>>>(ranges.size());
        long startTime = System.nanoTime();
        for (final Range r : ranges) {
            //System.out.println("range: " + r.initalK + " " + r.finalK);

            futures.add(executor.submit(new Callable<List<Apfloat>>() {
                @Override
                public List<Apfloat> call() throws Exception {
                    return ChudnovskyAlgorithm.calculateTermSums(r, precision);
                }
            }));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(3l, TimeUnit.MINUTES);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        Apfloat a_sum = new Apfloat(0l);
        Apfloat b_sum = new Apfloat(0l);

        for (Future<List<Apfloat>> f : futures) {
            try {
                List<Apfloat> termSums = f.get();

                a_sum = a_sum.add(termSums.get(0));
                b_sum = b_sum.add(termSums.get(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long duration = (System.nanoTime() - startTime);
        if (duration > 0) {
            duration /= 1000000;
        }

        System.out.println("execution time: " + duration + "ms");

        System.out.println("PI: " + ChudnovskyAlgorithm.merge(a_sum, b_sum, precision) + "\n");
    }
}
