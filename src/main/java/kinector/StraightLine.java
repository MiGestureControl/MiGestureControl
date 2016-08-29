package kinector;

/** Klasse zur Beschreibung einer Geraden im dreidimensionalen Raum.
 *
 * Diese Klasse dient zur Beschreibung einer Geraden im dreidimensionalen Raum. Entsprechend der Punktrichtungsgleichung
 * wird die Gerade anhand eines Punktes im Koordinatensystem, durch den sie verläuft, sowie eines Richtungsvektors beschrieben.
 * Die Klasse verfügt zudem über eine statische Methode zur Winkelberechnung zwischen einer Geraden und einem Punkt im Raum.
 * Zudem stellt sie eine weitere statische Methode bereit, welche auf Basis der Abstandsberechnung zweier windschiefer Geraden
 * die kürzeste Strecke zwischen beiden Geraden ermittelt und ihren Mittelpunkt als Position zurückgibt. Diese Methode wird verwendet,
 * um die Position eines Gerätes im Konfigurationsmodus bei ausführen zweier deiktischer Gesten zu ermitteln.
 */
public class StraightLine {

    /**
     * Punkt im Koordinatensystem durch welchen die Gerade verläuft.
     */
    private double[] point = new double[3];

    /**
     * Richtungsvektor, welcher die Richtung die Ausrichtung der Geraden im Koordinatensystem beschreibt.
     */
    private Vector3 vector = new Vector3();

    /** Konstruktor der Klasse StraightLine.
     *
     *  Der Konstruktor der Klasse StraightLine erhält zwei Punkte im Koordinatensystem. Der erste Punkt wird hierbei als Punkt
     *  abgelegt, durch welchen die Gerade gemäß der Punktrichtungsgleichung verläuft. Mit Hilfe der beiden Punkte wird
     *  zudem ein Richtungsvektor definiert, welcher die Ausrichtung der Geraden im Raum beschreibt.
     *
     * @param firstPoint Erster Punkt im Raum, durch welchen die Gerade verläuft.
     * @param secondPoint Zweiter Punkt im Raum, durch welchen die Gerade verläuft.
     */
    public StraightLine(double[] firstPoint, double[] secondPoint){
        this.point = firstPoint;

        this.vector = Vector3.defineVectorFromPointToPoint(firstPoint, secondPoint);
    }

    /**
     * Methode zum Berechnen des Winkels zwischen der Gerade und einem Punkt im Koordinatensystem.
     * <p/>
     * Diese Methode berechnet den Winkel zwischen der Geraden sowie einem Punkt im Koordinatensystem.
     * Hierfür wird ein temporärer Vektor definiert. Zwischen dem Richtungsvektor der Geraden sowie dem
     * temporären Vektor wird der Winkel zwischen beiden Vektoren berechnet, welcher zugleich den Winkel
     * zwischen Geraden und Punkt im Raum kennzeichnet.
     *
     * @param givenPoint Punkt im Koordinatensystem.
     */
    public static double calcAngleToGivenPoint(StraightLine givenStraightLine, double[] givenPoint)
    {
        Vector3 tempVector = new Vector3(
                givenPoint[0] - givenStraightLine.point[0],
                givenPoint[1] - givenStraightLine.point[1],
                givenPoint[2] - givenStraightLine.point[2]
        );

        double cosineOfAngle = Vector3.calcScalarProduct(tempVector, givenStraightLine.vector) /
                (Vector3.calcLength(givenStraightLine.vector) * Vector3.calcLength(tempVector));

        return Math.toDegrees(Math.acos(cosineOfAngle));
        }

    /**
     * Methode zum Berechnen des Mittelpunktes der kürzesten Strecke zwischen zwei Geraden im dreidimensionalen Raum.
     * <p/>
     * Diese Methode berechnet den Mittelpunkt der kürzesten Strecke zwischen zwei Geraden im dreidimensionalen Raum unter der Berücksichtigung, dass diese
     * gegebenenfalls windschief verlaufen, also nicht parallel zueinander stehen und sich nicht schneiden.
     *
     * In Folge der Berechnung wird die kürzeste Strecke zwischen den zwei Geraden ermittelt. Ihr Mittelpunkt definiert die
     * gesuchte Position. Sofern die Geraden sich schneiden, wird der tatsächliche Schnittpunkt zurückgegeben.
     *
     * @param firstStraightLine Erste Gerade im dreidimensionalen Raum.
     * @param secondStraightLine Zweite Gerade im dreidimensionalen Raum.
     */
    public static double[] calcStraightLineIntersection(StraightLine firstStraightLine, StraightLine secondStraightLine)
    {
        // Kreuzprodukt beider Geraden berechnen
        Vector3 crossProduct_firstLineSecondLine = Vector3.calcCrossProduct(firstStraightLine.vector, secondStraightLine.vector);

        // Vektor bestimmten der Senkrecht auf beiden Geraden steht und in Richtung der zweiten Geraden zeigt
        Vector3 perpendicularVectorToSecondStraightLine = Vector3.calcCrossProduct(secondStraightLine.vector, crossProduct_firstLineSecondLine);

        // Wenn Vektor (0,0,0) entspricht, verlaufen die Geraden parallel
        if (perpendicularVectorToSecondStraightLine.equals(new Vector3(0,0,0))) {

            // Punkt mit Werten MAX_VALUE wird zurückgegeben, um ihn bei späterer Prüfung abzufangen
            return new double[]{Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
        }

        // Vektor von erster Geraden zur zweiten Geraden bestimmen
        Vector3 firstToSecondLineVector = Vector3.defineVectorFromPointToPoint(firstStraightLine.point, secondStraightLine.point);

        // Skalarprodukt bestimmen
        double firstScalarProduct = Vector3.calcScalarProduct(perpendicularVectorToSecondStraightLine, firstToSecondLineVector) /
                Vector3.calcScalarProduct(perpendicularVectorToSecondStraightLine, firstStraightLine.vector);

        // Richtungsvektor der ersten Geraden mit Skalarprodukt multiplizieren
        Vector3 tempVector = Vector3.multiplyWithScalar(firstStraightLine.vector, firstScalarProduct);

        // Bestimmen des ersten Punktes der Strecke
        double[] firstPoint = {firstStraightLine.point[0] + tempVector.getX(),
                firstStraightLine.point[1] + tempVector.getY(),
                firstStraightLine.point[2] + tempVector.getZ()};

        // Vektor bestimmten der Senkrecht auf beiden Geraden steht und in Richtung der zweiten Geraden zeigt
        Vector3 perpendicularVectorToFirstStraightLine = Vector3.calcCrossProduct(firstStraightLine.vector, crossProduct_firstLineSecondLine);

        // Vektor von zweiter Geraden zur ersten Geraden bestimmen
        Vector3 secondToFirstLineVector = Vector3.defineVectorFromPointToPoint(secondStraightLine.point, firstStraightLine.point);

        // Skalarprodukt bestimmen
        double secondScalarProduct = Vector3.calcScalarProduct(perpendicularVectorToFirstStraightLine, secondToFirstLineVector) /
                Vector3.calcScalarProduct(perpendicularVectorToFirstStraightLine, secondStraightLine.vector);

        // Richtungsvektor der zweiten Geraden mit Skalarprodukt multiplizieren
        tempVector = Vector3.multiplyWithScalar(secondStraightLine.vector, secondScalarProduct);

        // Bestimmen des ersten Punktes der Strecke
        double[] secondPoint = {secondStraightLine.point[0] + tempVector.getX(),
                secondStraightLine.point[1] + tempVector.getY(),
                secondStraightLine.point[2] + tempVector.getZ()};


        //System.out.println("First point: " + firstPoint[0] + ", " + firstPoint[1] + ", " +  firstPoint[2]);
        //System.out.println("Second point: " + secondPoint[0] + ", " + secondPoint[1] + ", " +  secondPoint[2]);

        // Mittelpunkt der Strecke bestimmen
        double[] middlePointOfStraightLineSegment = {((firstPoint[0] + secondPoint[0]) / 2),
                ((firstPoint[1] + secondPoint[1]) / 2),
                ((firstPoint[2] + secondPoint[2]) / 2)};
        return middlePointOfStraightLineSegment;
    }

}
