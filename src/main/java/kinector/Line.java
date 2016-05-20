package kinector;

import java.util.Arrays;

/**
 * Created by johan on 16.03.2016.
 */
public class Line {

    private double[] point = new double[3];
    private double[] vector = new double[3];

    public Line(double[] firstPoint, double[] secondPoint){
        this.point = firstPoint;

        this.vector[0] = secondPoint[0] - firstPoint[0];
        this.vector[1] = secondPoint[1] - firstPoint[1];
        this.vector[2] = secondPoint[2] - firstPoint[2];
    }

    /*public double distanceToGivenPoint(double[] givenPoint)
    {
        double distanceToGivenPoint;

        double[] tempVector = new double[] {
                this.point[0] - givenPoint[0],
                this.point[1] - givenPoint[1],
                this.point[2] - givenPoint[2]
        };

        boolean vectorPointingOppositeDirection = (VectorHelper.calcScalarProduct(this.vector, tempVector) >= 0);

        if (vectorPointingOppositeDirection)
        {
            distanceToGivenPoint = Double.MAX_VALUE;
        }

        else
        {
            double[] crossProduct = VectorHelper.calcCrossProduct(this.vector, tempVector);
            distanceToGivenPoint = VectorHelper.calcLength(crossProduct) / VectorHelper.calcLength(this.vector);
        }

        return distanceToGivenPoint;
    }*/

    public double angleToGivenPoint(double[] givenPoint)
    {
        double[] tempVector = new double[] {
                givenPoint[0] - this.point[0],
                givenPoint[1] - this.point[1],
                givenPoint[2] - this.point[2]
        };

        double cosineOfAngle = VectorHelper.calcScalarProduct(tempVector, this.vector) /
                (VectorHelper.calcLength(this.vector) * VectorHelper.calcLength(tempVector));

        //double inDegrees = Math.acos(cosineOfAngle) * 180 / Math.PI;
        //return inDegrees;

        return Math.toDegrees(Math.acos(cosineOfAngle));


        }

    public double[] calcLineIntersection(Line givenLine)
    {
        double[] crossProduct_givenLine = VectorHelper.calcCrossProduct(this.vector, givenLine.vector);
        double[] perpendicularVectorToGivenLine = VectorHelper.calcCrossProduct(givenLine.vector, crossProduct_givenLine);

        if (Arrays.equals(perpendicularVectorToGivenLine, new double[]{0.0,0.0,0.0})) {
            return new double[]{Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
        }

        double[] qpVector = new double[]{givenLine.point[0] - this.point[0],
                givenLine.point[1] - this.point[1],
                givenLine.point[2] - this.point[2]};

        double firstScalarProduct = VectorHelper.calcScalarProduct(perpendicularVectorToGivenLine, qpVector) /
                VectorHelper.calcScalarProduct(perpendicularVectorToGivenLine, this.vector);

        double[] tempVector = VectorHelper.multiplyWithScalar(this.vector, firstScalarProduct);

        double[] firstPoint = {this.point[0] + tempVector[0],
                this.point[1] + tempVector[1],
                this.point[2] + tempVector[2]};

        double[] perpendicularVectorFromGivenLine = VectorHelper.calcCrossProduct(this.vector, crossProduct_givenLine);

        if (Arrays.equals(perpendicularVectorFromGivenLine, new double[]{0.0,0.0,0.0})) {
            return new double[]{Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
        }

        double[] pqVector = new double[]{this.point[0] - givenLine.point[0],
                this.point[1] - givenLine.point[1],
                this.point[2] - givenLine.point[2]};

        double secondScalarProduct = VectorHelper.calcScalarProduct(perpendicularVectorFromGivenLine, pqVector) /
                VectorHelper.calcScalarProduct(perpendicularVectorFromGivenLine, givenLine.vector);

        tempVector = VectorHelper.multiplyWithScalar(givenLine.vector, secondScalarProduct);

        double[] secondPoint = {givenLine.point[0] + tempVector[0],
                givenLine.point[1] + tempVector[1],
                givenLine.point[2] + tempVector[2]};


        System.out.println("First point: " + firstPoint[0] + ", " + firstPoint[1] + ", " +  firstPoint[2]);
        System.out.println("Second point: " + secondPoint[0] + ", " + secondPoint[1] + ", " +  secondPoint[2]);

        double[] middlePoint = {((firstPoint[0] + secondPoint[0]) / 2),
                ((firstPoint[1] + secondPoint[1]) / 2),
                ((firstPoint[2] + secondPoint[2]) / 2)};
        return middlePoint;
    }


}
