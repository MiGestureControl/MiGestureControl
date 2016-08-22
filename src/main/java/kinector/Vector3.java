package kinector;

/**
 * Created by johan on 20.03.2016.
 */
public class Vector3 {
    private double x;
    private double y;
    private double z;

    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX(){
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

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
