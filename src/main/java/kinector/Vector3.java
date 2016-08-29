package kinector;

import java.util.Vector;

/** Klasse zur Beschreibung eines Vektors im dreidimensionalen Raum.
 *
 * Diese Klasse dient zur Beschreibung eines Vektors im dreidimensionalen Raum. Sie verfügt über drei Attribute
 * für die Achsen X, Y und Z sowie entsprechende Getter- und Setter-Methoden. Des Weiteren werden statische Methoden
 * zum Berechnen es Skalar- und Kreuzproduktes, zum Bestimmen der Länge eines Vektors, zum Multiplizieren eines Vektors
 * mit einem Skalar als auch eine Methode zum Erzeugen eines Vektors auf Basis zweier Punkte bereitgestellt.
 */
public class Vector3 {
    /** X-Wert des Vektors. */
    private double x;

    /** Y-Wert des Vektors. */
    private double y;

    /** Z-Wert des Vektors. */
    private double z;

    /** Konstruktor der Klasse Vector3.
     *
     *  Der Default-Konstruktor der Klasse Vector3 erzeugt ein Vector3-Objekt ohne gesetzte Achsen-Werte.
     */
    public Vector3(){

    }

    /** Konstruktor der Klasse Vector3.
     *
     *  Der Konstruktor der Klasse Vector3 erhält bei Initialisierung drei Werte für die jeweiligen Achsen.
     *
     * @param x X-Wert des Vektors.
     * @param y Y-Wert des Vektors.
     * @param z Z-Wert des Vektors.
     */
    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** Getter-Methode für den X-Wert des Vektors.
     *
     * @return X-Wert des Vektors.
     */
    public double getX(){
        return x;
    }

    /** Getter-Methode für den Y-Wert des Vektors.
     *
     * @return Y-Wert des Vektors.
     */
    public double getY() {
        return y;
    }

    /** Getter-Methode für den Z-Wert des Vektors.
     *
     * @return Z-Wert des Vektors.
     */
    public double getZ() {
        return z;
    }

    /** Setter-Methode für den X-Wert des Vektors.
     *
     * @param x neuer X-Wert des Vektors.
     */
    public void setX(double x) {
        this.x = x;
    }

    /** Getter-Methode für den Y-Wert des Vektors.
     *
     * @param y neuer Y-Wert des Vektors.
     */
    public void setY(double y) {
        this.y = y;
    }

    /** Getter-Methode für den Z-Wert des Vektors.
     *
     * @param z neuer Z-Wert des Vektors.
     */
    public void setZ(double z) {
        this.z = z;
    }

    /** Methode zum Berechnen des Skalarproduktes zweier Vektoren.
     *
     * Diese Methode berechnet auf Basis zweier gegebener Vektoren das Skalarprodukt.
     *
     * @param firstVector erster gegebener Vektor.
     * @param secondVector zweiter gegebener Vektor.
     * @return Skalarprodukt beider Vektoren.
     */
    public static double calcScalarProduct(Vector3 firstVector, Vector3 secondVector){
        return firstVector.getX()*secondVector.getX()
                + firstVector.getY()*secondVector.getY()
                + firstVector.getZ()*secondVector.getZ();
    }

    /** Methode zum Berechnen des Kreuzproduktes zweier Vektoren.
     *
     * Diese Methode berechnet auf Basis zweier gegebener Vektoren das Kreuzprodukt.
     *
     * @param firstVector erster gegebener Vektor.
     * @param secondVector zweiter gegebener Vektor.
     * @return Kreuzprodukt beider Vektoren.
     */
    public static Vector3 calcCrossProduct(Vector3 firstVector, Vector3 secondVector){

        double crossProductX = (firstVector.getY() * secondVector.getZ() - secondVector.getY() * firstVector.getZ());
        double crossProductY = (firstVector.getZ() * secondVector.getX() - secondVector.getZ() * firstVector.getX());
        double crossProductZ = (firstVector.getX() * secondVector.getY() - secondVector.getX() * firstVector.getY());


        return new Vector3(crossProductX, crossProductY, crossProductZ);
    }

    /** Methode zum Berechnen der Länge eines Vektors.
     *
     * Diese Methode berechnet für einen gegebenen Vektor die Länge.
     *
     * @param vector gegebener Vektor.
     * @return Länge des Vektoren.
     */
    public static double calcLength(Vector3 vector){
        return (float) Math.sqrt(vector.getX()*vector.getX()
                + vector.getY()*vector.getY()
                + vector.getZ()*vector.getZ());
    }

    /** Methode zum Multiplizieren eines Vektors mit einem Skalar.
     *
     * Diese Methode multipliziert einen gegebenen Vektor mit einem gegebenen Skalar.
     *
     * @param vector gegebener Vektor.
     * @return neuer Vektor.
     */
    public static Vector3 multiplyWithScalar(Vector3 vector, double scalar){
        return new Vector3((vector.getX() * scalar),
                (vector.getY() * scalar),
                (vector.getZ() * scalar));
    }

    /** Methode zum Bestimmen eines Vektors mit zwei gegebenen Punkten.
     *
     * Diese Methode erhält zwei Punkte im dreidimensionalen Raum. Auf Basis der Punkte
     * wird ein Richtungsvektor vom ersten Punkt in Richtung des zweiten Punktes definiert.
     *
     * @param fromPoint erster gegebener Punkt im Raum.
     * @param toPoint zweiter gegebener Punkt im Raum.
     * @return Vektor von fromPoint zu toPoint.
     */
    public static Vector3 defineVectorFromPointToPoint(double[] fromPoint, double[] toPoint){
        double fromPointToPointVectorX = toPoint[0] - fromPoint[0];
        double fromPointToPointVectorY = toPoint[1] - fromPoint[1];
        double fromPointToPointVectorZ = toPoint[2] - fromPoint[2];

        return new Vector3(fromPointToPointVectorX, fromPointToPointVectorY, fromPointToPointVectorZ);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3 vector3 = (Vector3) o;

        if (Double.compare(vector3.x, x) != 0) return false;
        if (Double.compare(vector3.y, y) != 0) return false;
        return Double.compare(vector3.z, z) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
