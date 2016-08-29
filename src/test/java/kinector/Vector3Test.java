package kinector;

import junit.framework.TestCase;

/** JUnit-Test-Klasse für die Methoden der Klasse Vector3.
 *
 * Diese Test-Klasse enthält Tests für alle von der Klasse Vector3 bereitgestellten Methoden zur Vektorrechnung.
 *
 */
public class Vector3Test extends TestCase {

    /** Test für die Methode calcScalarProduct.
     *
     * Überprüft ob das berechnete Skalarprodukt der Vektoren (4,1,1) und (8,2,2) dem tatsächlichen Skalarprodukt 36.0 entspricht.
     *
     */
    public void testCalcScalarProduct01() {
        Vector3 firstTestVector3 = new Vector3(4,1,1);
        Vector3 secondTestVector3 = new Vector3(8,2,2);

        double scalarProduct = Vector3.calcScalarProduct(firstTestVector3, secondTestVector3);

        assertEquals(scalarProduct, 36.0);
    }

    /** Test für die Methode calcScalarProduct.
     *
     * Überprüft ob das berechnete Skalarprodukt der Vektoren (-2,-3,-4) und (-5,-6,-7) dem tatsächlichen Skalarprodukt 56.0 entspricht.
     *
     */
    public void testCalcScalarProduct02() {
        Vector3 firstTestVector3 = new Vector3(-2,-3,-4);
        Vector3 secondTestVector3 = new Vector3(-5,-6,-7);

        double scalarProduct = Vector3.calcScalarProduct(firstTestVector3, secondTestVector3);

        assertEquals(scalarProduct, 56.0);
    }

    /** Test für die Methode calcScalarProduct.
     *
     * Überprüft ob das berechnete Skalarprodukt der Vektoren (-1,-3,5) und (1,4,-7) dem tatsächlichen Skalarprodukt -48.0 entspricht.
     *
     */
    public void testCalcScalarProduct03() {
        Vector3 firstTestVector3 = new Vector3(-1,-3,5);
        Vector3 secondTestVector3 = new Vector3(1,4,-7);

        double scalarProduct = Vector3.calcScalarProduct(firstTestVector3, secondTestVector3);

        assertEquals(scalarProduct, -48.0);
    }

    /** Test für die Methode calcCrossProduct.
     *
     * Überprüft ob das berechnete Kreuzprodukt der Vektoren (4,1,1) und (8,2,2) dem tatsächlichen Kreuzprodukt (0,0,0) entspricht.
     *
     */
    public void testCalcCrossProduct01() {
        Vector3 firstTestVector3 = new Vector3(4,1,1);
        Vector3 secondTestVector3 = new Vector3(8,2,2);

        Vector3 crossProduct = Vector3.calcCrossProduct(firstTestVector3, secondTestVector3);

        assertEquals(crossProduct.getX(), 0.0);
        assertEquals(crossProduct.getY(), 0.0);
        assertEquals(crossProduct.getZ(), 0.0);
    }

    /** Test für die Methode calcCrossProduct.
     *
     * Überprüft ob das berechnete Kreuzprodukt der Vektoren (-2,-3,-4) und (-5,-6,-7) dem tatsächlichen Kreuzprodukt (-3,6,-3) entspricht.
     *
     */
    public void testCalcCrossProduct02() {
        Vector3 firstTestVector3 = new Vector3(-2,-3,-4);
        Vector3 secondTestVector3 = new Vector3(-5,-6,-7);

        Vector3 crossProduct = Vector3.calcCrossProduct(firstTestVector3, secondTestVector3);

        assertEquals(crossProduct.getX(), -3.0);
        assertEquals(crossProduct.getY(), 6.0);
        assertEquals(crossProduct.getZ(), -3.0);
    }

    /** Test für die Methode calcCrossProduct.
     *
     * Überprüft ob das berechnete Kreuzprodukt der Vektoren (-1,-3,5) und (1,4,-7) dem tatsächlichen Kreuzprodukt (1,-2,-1) entspricht.
     *
     */
    public void testCalcCrossProduct03() {
        Vector3 firstTestVector3 = new Vector3(-1,-3,5);
        Vector3 secondTestVector3 = new Vector3(1,4,-7);

        Vector3 crossProduct = Vector3.calcCrossProduct(firstTestVector3, secondTestVector3);

        assertEquals(crossProduct.getX(), 1.0);
        assertEquals(crossProduct.getY(), -2.0);
        assertEquals(crossProduct.getZ(), -1.0);
    }

    /** Test für die Methode calcLength.
     *
     * Überprüft ob die berechnete Länge des Vektors (4,1,1) der tatsächlichen Länge 4.24264 (nach 5 Nachkommastellen abgeschnitten) entspricht.
     *
     */
    public void testCalcLength01() {
        Vector3 testVector3 = new Vector3(4,1,1);

        double vectorLength = Vector3.calcLength(testVector3);
        vectorLength =  (long) (vectorLength * 1e5) / 1e5;

        assertEquals(vectorLength, 4.24264);
    }

    /** Test für die Methode calcLength.
     *
     * Überprüft ob die berechnete Länge des Vektors (-2,-3,-4) der tatsächlichen Länge 5.38516 (nach 5 Nachkommastellen abgeschnitten) entspricht.
     *
     */
    public void testCalcLength02() {
        Vector3 testVector3 = new Vector3(-2,-3,-4);

        double vectorLength = Vector3.calcLength(testVector3);
        vectorLength =  (long) (vectorLength * 1e5) / 1e5;

        assertEquals(vectorLength, 5.38516);
    }

    /** Test für die Methode calcLength.
     *
     * Überprüft ob die berechnete Länge des Vektors (-1,-3,5) der tatsächlichen Länge 5.91607 (nach 5 Nachkommastellen abgeschnitten) entspricht.
     *
     */
    public void testCalcLength03() {
        Vector3 testVector3 = new Vector3(-1,-3,5);

        double vectorLength = Vector3.calcLength(testVector3);
        vectorLength =  (long) (vectorLength * 1e5) / 1e5;

        assertEquals(vectorLength, 5.91607);
    }

    /** Test für die Methode multiplyWithScalar.
     *
     * Überprüft ob der berechnete Vektor aus der Multiplikation des Vektors (4,1,1) mit dem Skalar 4 dem tatsäclichen Vektor (16,4,4) entspricht
     *
     */
    public void testMultiplyWithScalar01() {
        Vector3 testVector3 = new Vector3(4,1,1);
        double testScalar = 4;

        Vector3 resultVector = Vector3.multiplyWithScalar(testVector3, testScalar);

        assertEquals(resultVector.getX(), 16.0);
        assertEquals(resultVector.getY(), 4.0);
        assertEquals(resultVector.getZ(), 4.0);
    }

    /** Test für die Methode multiplyWithScalar.
     *
     * Überprüft ob der berechnete Vektor aus der Multiplikation des Vektors (-2,-3,-4) mit dem Skalar 6 dem tatsäclichen Vektor (-12,-18,-24) entspricht
     *
     */
    public void testMultiplyWithScalar02() {
        Vector3 testVector3 = new Vector3(-2,-3,-4);
        double testScalar = 6;

        Vector3 resultVector = Vector3.multiplyWithScalar(testVector3, testScalar);

        assertEquals(resultVector.getX(), -12.0);
        assertEquals(resultVector.getY(), -18.0);
        assertEquals(resultVector.getZ(), -24.0);
    }

    /** Test für die Methode multiplyWithScalar.
     *
     * Überprüft ob der berechnete Vektor aus der Multiplikation des Vektors (-1,-3,5) mit dem Skalar 3 dem tatsäclichen Vektor (-3,-9,15) entspricht
     *
     */
    public void testMultiplyWithScalar03() {
        Vector3 testVector3 = new Vector3(-1,-3,5);

        double testScalar = 3;

        Vector3 resultVector = Vector3.multiplyWithScalar(testVector3, testScalar);

        assertEquals(resultVector.getX(), -3.0);
        assertEquals(resultVector.getY(), -9.0);
        assertEquals(resultVector.getZ(), 15.0);
    }

    /** Test für die Methode defineVectorFromPointToPoint.
     *
     * Überprüft ob der, aus den Punkten [6,2,5] und [1-3,2] bestimmte Vektor dem tatsäclichen Vektor (-5,-5,-3) entspricht
     *
     */
    public void testDefineVector() {
        double[] firstPoint = new double[]{6,2,5};
        double[] secondPoint = new double[]{1,-3,2};

        Vector3 definedVector = Vector3.defineVectorFromPointToPoint(firstPoint, secondPoint);

        assertEquals(definedVector.getX(), -5.0);
        assertEquals(definedVector.getY(), -5.0);
        assertEquals(definedVector.getZ(), -3.0);
    }
}
