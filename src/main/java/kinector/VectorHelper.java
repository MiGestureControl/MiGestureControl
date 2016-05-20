package kinector;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by johan on 16.03.2016.
 */
public class VectorHelper {

    public static double calcScalarProduct(double[] firstVector, double[] secondVector){
        return firstVector[0]*secondVector[0]
                + firstVector[1]*secondVector[1]
                + firstVector[2]*secondVector[2];
    }

    public static double[] calcCrossProduct(double[] firstVector, double[] secondVector){

        return new double[]{
                (firstVector[1] * secondVector[2] - secondVector[1] * firstVector[2]),
                (firstVector[2] * secondVector[0] - secondVector[2] * firstVector[0]),
                (firstVector[0] * secondVector[1] - secondVector[0] * firstVector[1])
        };
    }

    public static double calcLength(double[] vector){
        return (float) Math.sqrt(vector[0]*vector[0]
                + vector[1]*vector[1]
                + vector[2]*vector[2]);
    }

    public static double[] multiplyWithScalar(double[] vector, double scalar){
        return new double[]{
                (vector[0] * scalar),
                (vector[1] * scalar),
                (vector[2] * scalar)
        };
    }
}
