package sw.chudnovskyalgorithm;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.ApintMath;

import java.util.ArrayList;
import java.util.List;

/*
 * @author scott
 */
public class ChudnovskyAlgorithm {

    /**
     * Finds the mathematical constant pi to <code>precision</code> number of digits
     *
     * @param precision desired for return value
     * @return mathematical constant pi
     * @see <a href="http://www.craig-wood.com/nick/articles/pi-chudnovsky/">http://www.craig-wood.com/nick/articles/pi-chudnovsky/</a> for details
     */
    public static Apfloat calculatePi(long precision) {
        long startTime = System.nanoTime();
        // need one extra place for the 3, and one extra place for some rounding issues
        precision = precision + 2;
        Apfloat negativeOne = new Apfloat(-1l);

        Apfloat two = new Apfloat(2l);
        Apfloat five = new Apfloat(5l);
        Apfloat six = new Apfloat(6l);
        Apfloat C = new Apfloat(640320l);
        Apfloat C3_OVER_24 = (C.multiply(C).multiply(C)).divide(new Apfloat(24, precision));
        Apfloat DIGITS_PER_TERM = ApfloatMath.log(C3_OVER_24.divide(new Apfloat(72, precision)), new Apfloat(10l));

        // find the first term in the series
        Apfloat k = new Apfloat(0l);
        Apfloat a_k = new Apfloat(1l, precision);

        Apfloat a_sum = new Apfloat(1l);
        Apfloat b_sum = new Apfloat(0l);
        k = k.add(Apfloat.ONE);

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
        if (duration > 0) {
            duration /= 1000000;
        }
        System.out.println("execution time: " + duration + "ms");

        return pi;
    }

    /**
     * Figures out which ranges {@link ChudnovskyAlgorithm#calculateTermSums(Range, long)} should act on.
     * <p/>
     * NOTE: the size of the list of ranges returned may be slightly higher than the numberOfRanges specified.  This has to do with the precision of the algorithm.
     *
     * @param numberOfRanges About how many ranges you want returned.
     * @param precision
     * @return mutually exclusive ranges that can run executed in parallel.
     */
    public static List<Range> calculateTermRanges(long numberOfRanges, long precision) {
        List<Range> ranges = new ArrayList<Range>();

        Apfloat C = new Apfloat(640320l);
        Apfloat C3_OVER_24 = (C.multiply(C).multiply(C)).divide(new Apfloat(24, precision));
        Apfloat DIGITS_PER_TERM = ApfloatMath.log(C3_OVER_24.divide(new Apfloat(72, precision)), new Apfloat(10l));

        long numberOfLoopsToRun = (new Apfloat(precision, precision)).divide(DIGITS_PER_TERM).add(Apfloat.ONE).longValue();

        long rangeSize = numberOfLoopsToRun / numberOfRanges;

        for (int i = 0; i < numberOfLoopsToRun; i += rangeSize) {
//            if(i+rangeSize+1 == numberOfLoopsToRun) {
//                i++;
//            }

            long f = Math.min(i + rangeSize, numberOfLoopsToRun);
            ranges.add(new Range(i, f));
        }
        return ranges;
    }

    /**
     * Method to be run in parallel.
     *
     * @param range     from {@link ChudnovskyAlgorithm#calculateTermRanges(long, long)}
     * @param precision desired for return value
     * @return List of size 2.  Contains a_sum and a_sum.  To be feed into {@link ChudnovskyAlgorithm#merge(Apfloat, Apfloat, long)}.
     */
    public static List<Apfloat> calculateTermSums(Range range, long precision) {
        long startTime = System.nanoTime();
        // need one extra place for the 3, and one extra place for some rounding issues
        precision = precision + 2;
        Apfloat negativeOne = new Apfloat(-1l);

        Apfloat two = new Apfloat(2l);
        Apfloat three = new Apfloat(3l);
        Apfloat five = new Apfloat(5l);
        Apfloat six = new Apfloat(6l);
        Apfloat C = new Apfloat(640320l);
        Apfloat C3_OVER_24 = (C.multiply(C).multiply(C)).divide(new Apfloat(24, precision));
        Apfloat DIGITS_PER_TERM = ApfloatMath.log(C3_OVER_24.divide(new Apfloat(72, precision)), new Apfloat(10l));

        // find the first term in the series
        Apfloat k = new Apfloat(range.initalK);
        Apfloat a_k = ((k.longValue() % 2 == 0) ? Apfloat.ONE : negativeOne).multiply(ApintMath.factorial(6 * k.longValue())).precision(precision);
        Apfloat kFactorial = ApintMath.factorial(k.longValue());
        a_k = a_k.divide(ApintMath.factorial(three.multiply(k).longValue()).multiply(kFactorial.multiply(kFactorial).multiply(kFactorial)).multiply(ApfloatMath.pow(C, k.longValue() * 3)));

        Apfloat a_sum = new Apfloat(0l).add(a_k);
        Apfloat b_sum = new Apfloat(0l).add(k.multiply(a_k));
        k = k.add(Apfloat.ONE);

        for (long i = range.initalK + 1; i < range.finalK; i++) {
            a_k = a_k.multiply(negativeOne.multiply((six.multiply(k).subtract(five)).multiply(two.multiply(k).subtract(Apfloat.ONE)).multiply(six.multiply(k).subtract(Apfloat.ONE))));
            a_k = a_k.divide(k.multiply(k).multiply(k).multiply(C3_OVER_24));
            a_sum = a_sum.add(a_k);
            b_sum = b_sum.add(k.multiply(a_k));
            k = k.add(Apfloat.ONE);
        }

        List<Apfloat> termSums = new ArrayList<Apfloat>();
        termSums.add(a_sum);
        termSums.add(b_sum);

        long duration = (System.nanoTime() - startTime);
        if (duration > 0) {
            duration /= 1000000;
        }
        //System.out.println("execution time: " + duration + "ms");

        return termSums;
    }

    /**
     * Merges the results from {@link ChudnovskyAlgorithm#calculateTermSums(Range, long)}.
     *
     * @param a_sum     cumulative sum of all the a terms
     * @param b_sum     cumulative sum of all the b terms
     * @param precision desired for return value
     * @return mathematical constant pi
     */
    public static Apfloat merge(Apfloat a_sum, Apfloat b_sum, long precision) {
        precision++;
        Apfloat total = new Apfloat(13591409l).multiply(a_sum).add(new Apfloat(545140134l).multiply(b_sum));

        Apfloat sqrtTenThousandAndFive = ApfloatMath.sqrt(new Apfloat(10005l, precision));
        Apfloat pi = (new Apfloat(426880l).multiply(sqrtTenThousandAndFive).divide(total)).precision(precision);

        return pi;
    }
}
