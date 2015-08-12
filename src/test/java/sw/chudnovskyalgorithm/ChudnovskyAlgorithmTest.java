package sw.chudnovskyalgorithm;

import org.junit.Test;

import static org.junit.Assert.*;

/*
 * @author scott
 */
public class ChudnovskyAlgorithmTest {

    @Test
    public void testPrecision10() {
        System.out.println(ChudnovskyAlgorithm.calculatePi(10));
    }

    @Test
    public void testPrecision100() {
        System.out.println(ChudnovskyAlgorithm.calculatePi(100));
    }

    @Test
    public void testPrecision1000() {
        System.out.println(ChudnovskyAlgorithm.calculatePi(1000));
    }

    @Test
    public void testPrecision100000() {
        System.out.println(ChudnovskyAlgorithm.calculatePi(100000));
    }
}
