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

    @Test
    public void testSingleThreaded() {
        long precision = 200l;
        System.out.println("single-threaded Pi with precision of " + precision + ": " + ChudnovskyAlgorithm.calculatePi(precision) + "\n");
    }

    @Test
    public void testMultiThreaded() {
        long precision = 200l;
        int numberOfThreads = 4;
        System.out.println("multi-threaded (" + numberOfThreads + " threads) Pi with precision of " + precision + ": " + ChudnovskyAlgorithm.calculatePi(precision, numberOfThreads) + "\n");
    }

    @Test
    public void testCompareSingleToMultiThreaded() {
        for(long precision = 1; precision <= Math.pow(2, 17); precision*=2l) {

            long startTime = System.nanoTime();
            Apfloat singleThreadedPi = ChudnovskyAlgorithm.calculatePi(precision);
            long duration = (System.nanoTime() - startTime);
            if (duration > 0) {
                duration /= 1000000;
            }
            System.out.println("single-threaded Pi with precision of " + precision + ": " + singleThreadedPi);
            System.out.println("execution time: " + duration + "\n");

            for (int numberOfThreads = 1; numberOfThreads <= 8; numberOfThreads++) {

                startTime = System.nanoTime();
                Apfloat multiThreadedPi = ChudnovskyAlgorithm.calculatePi(precision, numberOfThreads);
                duration = (System.nanoTime() - startTime);
                if (duration > 0) {
                    duration /= 1000000;
                }
                System.out.println("multi-threaded (" + numberOfThreads + " threads) Pi with precision of " + precision + ": " + multiThreadedPi);
                System.out.println("execution time: " + duration + "\n");

                assertEquals(singleThreadedPi, multiThreadedPi);
            }
        }
    }

}
