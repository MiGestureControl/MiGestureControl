package kinector;

import junit.framework.TestCase;

/** JUnit-Test-Klasse für die Methoden der Klasse StraightLine.
 *
 * Diese Test-Klasse enthält Tests für alle von der Klasse StraightLine bereitgestellten Methoden zur Vektorrechnung.
 *
 */
public class StraightLineTest extends TestCase {

    /** Test für die Methode calcStraightLineIntersection.
     *
     * Überprüft ob die Objektposition aus der Berechnung des Mittelpunktes der kürzesten Strecke zwischen zwei
     * Geraden an der tatsächlichen Position liegt. Die Geraden verlaufen weder parallel noch schneiden sie sich.
     *
     */
    public void testCalcStraightLineIntersection01() {
        StraightLine firstLine = new StraightLine(new double[]{-1,3,0}, new double[]{1,3,0});
        StraightLine secondLine = new StraightLine(new double[]{0,0,6}, new double[]{0,3,6});

        double[] resultPoint = StraightLine.calcStraightLineIntersection(firstLine, secondLine);

        assertEquals(resultPoint[0], 0.0);
        assertEquals(resultPoint[1], 3.0);
        assertEquals(resultPoint[2], 3.0);
    }

    /** Test für die Methode calcStraightLineIntersection.
     *
     * Überprüft ob die Objektposition aus der Berechnung des Mittelpunktes der kürzesten Strecke zweier Geraden
     * gefunden werden kann. Da die geraden parallel zueinander verlaufen, soll calcStraightLineIntersection
     * die jeweils maximalen Werte des Typs Double zurückgeben, um eine Parallelität zu kennzeichnen.
     *
     */
    public void testCalcStraightLineIntersection02() {
        StraightLine firstLine = new StraightLine(new double[]{1,1,1}, new double[]{5,5,5});
        StraightLine secondLine = new StraightLine(new double[]{1,1,1}, new double[]{7,7,7});

        double[] resultPoint = StraightLine.calcStraightLineIntersection(firstLine, secondLine);

        assertEquals(resultPoint[0], Double.MAX_VALUE);
        assertEquals(resultPoint[1], Double.MAX_VALUE);
        assertEquals(resultPoint[2], Double.MAX_VALUE);
    }


    /** Test für die Methode calcStraightLineIntersection.
     *
     * Überprüft ob die Objektposition aus der Berechnung des Mittelpunktes der kürzesten Strecke zwischen zwei
     * Geraden an der tatsächlichen Position liegt. Die Geraden schneiden sich tatsächlich, weshalb calcStraightLineIntersection
     * den Schnittpunkt zurückgegeben sollte.
     *
     */
    public void testCalcStraightLineIntersection03() {
        StraightLine firstLine = new StraightLine(new double[]{-3,2,0}, new double[]{3,-2,0});
        StraightLine secondLine = new StraightLine(new double[]{3,-2,1}, new double[]{-3,2,0});

        double[] resultPoint = StraightLine.calcStraightLineIntersection(firstLine, secondLine);

        assertEquals(resultPoint[0], -3.0);
        assertEquals(resultPoint[1], 2.0);
        assertEquals(resultPoint[2], 0.0);
    }

    /** Test für die Methode calcAngleToGivenPoint.
     *
     * Überprüft den berechneten Winkel zwischen einer Geraden und einem Vektor. Der Winkel entspricht hierbei
     * tatsächlich, ohne Berücksichtigung der Nachkommastellen, 115 Grad.
     *
     */
    public void testCalcAngleToGivenPoint01() {
        StraightLine firstLine = new StraightLine(new double[]{2,2,0}, new double[]{-2,-2,0});
        double[] testPoint = new double[]{3,3,3};

        int resultAngle = (int)StraightLine.calcAngleToGivenPoint(firstLine, testPoint);

        assertEquals(resultAngle, 115);
    }

    /** Test für die Methode calcAngleToGivenPoint.
     *
     * Überprüft den berechneten Winkel zwischen einer Geraden und einem Vektor. Der Winkel entspricht hierbei
     * tatsächlich, ohne Berücksichtigung der Nachkommastellen, 90 Grad.
     *
     */
    public void testCalcAngleToGivenPoint02() {
        StraightLine firstLine = new StraightLine(new double[]{4,4,4}, new double[]{-4,-4,-4});
        double[] testPoint = new double[]{3,2,7};

        int resultAngle = (int)StraightLine.calcAngleToGivenPoint(firstLine, testPoint);

        assertEquals(resultAngle, 90);
    }
}
