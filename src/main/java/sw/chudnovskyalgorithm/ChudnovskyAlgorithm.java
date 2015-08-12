package sw.chudnovskyalgorithm;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

/*
 * @author scott
 */
public class ChudnovskyAlgorithm {

    /**
     * Needs to take powers of 10 to work correctly.
     *
     * @param precision
     * @return
     */
    public static Apfloat calculatePi(long precision) {
        Apfloat negitiveOne = new Apfloat(-1l);

        Apfloat oneWithPrecision = new Apfloat(1l, precision);

        Apfloat two = new Apfloat(2l);
        Apfloat five = new Apfloat(5l);
        Apfloat six = new Apfloat(6l);
        Apfloat k = new Apfloat(1l);
        Apfloat a_k = new Apfloat(precision, precision);
        Apfloat a_sum = new Apfloat(precision);
        Apfloat b_sum = new Apfloat(0l);
        Apfloat C = new Apfloat(640320l);
        Apfloat C3_OVER_24 = (C.multiply(C).multiply(C)).divide(new Apfloat(24, precision));
        while (true) {
            a_k = a_k.multiply(negitiveOne.multiply((six.multiply(k).subtract(five)).multiply(two.multiply(k).subtract(Apfloat.ONE)).multiply(six.multiply(k).subtract(Apfloat.ONE))));
            a_k = a_k.divide(k.multiply(k).multiply(k).multiply(C3_OVER_24));
            a_sum = a_sum.add(a_k);
            b_sum = b_sum.add(k.multiply(a_k));
            k = k.add(Apfloat.ONE);
            if (a_k.add(oneWithPrecision).equals(oneWithPrecision)) {
                break;
            }
        }
        Apfloat total = new Apfloat(13591409l).multiply(a_sum).add(new Apfloat(545140134l).multiply(b_sum));

        Apfloat squrtTenThousandAndFive = ApfloatMath.sqrt(new Apfloat(10005l, precision));
        Apfloat pi = new Apfloat(426880l).multiply(squrtTenThousandAndFive).divide(total).multiply(new Apfloat(precision));
        return pi;
    }
}
