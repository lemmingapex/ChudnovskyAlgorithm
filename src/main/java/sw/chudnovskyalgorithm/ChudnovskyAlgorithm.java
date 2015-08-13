package sw.chudnovskyalgorithm;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

/*
 * @author scott
 */
public class ChudnovskyAlgorithm {

    /**
     *
     * @see <a href="http://www.craig-wood.com/nick/articles/pi-chudnovsky/">http://www.craig-wood.com/nick/articles/pi-chudnovsky/</a> for details
     *
     * @param precision desired for return value
     * @return pi
     */
    public static Apfloat calculatePi(long precision) {
        long startTime = System.nanoTime();
        // need one extra place for the 3, and one extra place for some rounding issues
        precision = precision + 2;
        Apfloat negativeOne = new Apfloat(-1l);

        Apfloat two = new Apfloat(2l);
        Apfloat five = new Apfloat(5l);
        Apfloat six = new Apfloat(6l);
        Apfloat k = new Apfloat(1l);
        Apfloat a_k = new Apfloat(1l, precision);
        Apfloat a_sum = new Apfloat(1l);
        Apfloat b_sum = new Apfloat(0l);
        Apfloat C = new Apfloat(640320l);
        Apfloat C3_OVER_24 = (C.multiply(C).multiply(C)).divide(new Apfloat(24, precision));
        Apfloat DIGITS_PER_TERM = ApfloatMath.log(C3_OVER_24.divide(new Apfloat(72, precision)), new Apfloat(10l));
        long numberOfLoopsToRun = new Apfloat(precision, precision).divide(DIGITS_PER_TERM).add(Apfloat.ONE).longValue();

        while (k.longValue() < numberOfLoopsToRun) {
            a_k = a_k.multiply(negativeOne.multiply((six.multiply(k).subtract(five)).multiply(two.multiply(k).subtract(Apfloat.ONE)).multiply(six.multiply(k).subtract(Apfloat.ONE))));
            a_k = a_k.divide(k.multiply(k).multiply(k).multiply(C3_OVER_24));
            a_sum = a_sum.add(a_k);
            b_sum = b_sum.add(k.multiply(a_k));
            k = k.add(Apfloat.ONE);
        }
        Apfloat total = new Apfloat(13591409l).multiply(a_sum).add(new Apfloat(545140134l).multiply(b_sum));

        Apfloat sqrtTenThousandAndFive = ApfloatMath.sqrt(new Apfloat(10005l, precision));
        Apfloat pi = (new Apfloat(426880l).multiply(sqrtTenThousandAndFive).divide(total)).precision(precision - 1);

        long duration = (System.nanoTime() - startTime);
        if(duration > 0) {
            duration/=1000000;
        }
        System.out.println("# of loops executed: " + k.longValue());
        System.out.println("execution time: " + duration + "ms");

        return pi;
    }
}
