package kinector;

import java.util.Vector;

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

    public static double calcScalarProduct(Vector3 firstVector, Vector3 secondVector){
        return firstVector.getX()*secondVector.getX()
                + firstVector.getY()*secondVector.getY()
                + firstVector.getZ()*secondVector.getZ();
    }

    public static Vector3 calcCrossProduct(Vector3 firstVector, Vector3 secondVector){

        double crossProductX = (firstVector.getY() * secondVector.getZ() - secondVector.getY() * firstVector.getZ());
        double crossProductY = (firstVector.getZ() * secondVector.getX() - secondVector.getZ() * firstVector.getX());
        double crossProductZ = (firstVector.getX() * secondVector.getY() - secondVector.getX() * firstVector.getY());


        return new Vector3(crossProductX, crossProductY, crossProductZ);
    }

    public static double calcLength(Vector3 vector){
        return (float) Math.sqrt(vector.getX()*vector.getX()
                + vector.getY()*vector.getY()
                + vector.getZ()*vector.getZ());
    }

    public static double[] multiplyWithScalar(Vector3 vector, double scalar){
        return new double[]{
                (vector.getX() * scalar),
                (vector.getY() * scalar),
                (vector.getZ() * scalar)
        };
    }

    public static Vector3 defineVectorFromPointToPoint(double[] fromPoint, double[] toPoint){
        double fromPointToPointVectorX = toPoint[0] - fromPoint[0];
        double fromPointToPointVectorY = toPoint[1] - fromPoint[1];
        double fromPointToPointVectorZ = toPoint[2] - fromPoint[2];

        return new Vector3(fromPointToPointVectorX, fromPointToPointVectorY, fromPointToPointVectorZ);

    }
}
