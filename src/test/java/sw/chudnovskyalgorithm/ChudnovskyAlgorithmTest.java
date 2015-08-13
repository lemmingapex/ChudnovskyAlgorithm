package sw.chudnovskyalgorithm;

import org.junit.Test;

import static org.junit.Assert.*;

/*
 * @author scott
 */
public class ChudnovskyAlgorithmTest {

    @Test
    public void testPrecision1() {
        System.out.println(ChudnovskyAlgorithm.calculatePi(1l));
    }

    @Test
    public void testPrecision2() {
        System.out.println(ChudnovskyAlgorithm.calculatePi(2l));
    }

    @Test
    public void testPrecision10() {
        System.out.println(ChudnovskyAlgorithm.calculatePi(10l));
    }

    @Test
    public void testPrecision1000() {
        System.out.println(ChudnovskyAlgorithm.calculatePi(1000l));
    }

    @Test
    public void testPrecision10000() {
        System.out.println(ChudnovskyAlgorithm.calculatePi(10000l));
    }
}
