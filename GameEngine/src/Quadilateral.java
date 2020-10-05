import org.joml.Vector2d;

public class Quadilateral {

	
	public static boolean isPointWithinConvexQuad(Vector2d intervalP11, Vector2d intervalP12, Vector2d intervalP21, Vector2d intervalP22, Vector2d p) {
		
		return false;
	}
	
	
	public double[] lineIntersection(double x1, double y1, double x2, double y2, double a1, double b1, double a2, double b2) {
		// returns {intersectionPointX, intersectionPointY} or null if lines do not intersect

		double sTime = System.nanoTime();
		
		double boundsP = 0.000001;
		double A1 = y2 - y1;
		double B1 = x1 - x2;
		double C1 = A1 * x1 + B1 * y1;

		double A2 = b2 - b1;
		double B2 = a1 - a2;
		double C2 = A2 * a1 + B2 * b1;

		double d = A1 * B2 - A2 * B1;
		
		double intersectionPointX, intersectionPointY;
		
		if (Math.abs(d) <= 0.000000000000000000000001) {
			//System.out.println("Lines do not intersect at all");
			return null;
		}
		else {
			double pointX = (B2 * C1 - B1 * C2) / d;
			double pointY = (A1 * C2 - A2 * C1) / d;
			if ((pointX > Math.min(x1, x2) || Math.abs(pointX - Math.min(x1, x2)) <= boundsP)
					&& (pointX < Math.max(x1, x2) || Math.abs(pointX - Math.max(x1, x2)) <= boundsP)
					&& (pointY > Math.min(y1, y2) || Math.abs(pointY - Math.min(y1, y2)) <= boundsP)
					&& (pointY < Math.max(y1, y2) || Math.abs(pointY - Math.max(y1, y2)) <= boundsP)
					&& (pointX > Math.min(a1, a2) || Math.abs(pointX - Math.min(a1, a2)) <= boundsP)
					&& (pointX < Math.max(a1, a2) || Math.abs(pointX - Math.max(a1, a2)) <= boundsP)
					&& (pointY > Math.min(b1, b2) || Math.abs(pointY - Math.min(b1, b2)) <= boundsP)
					&& (pointY < Math.max(b1, b2) || Math.abs(pointY - Math.max(b1, b2)) <= boundsP)) {
			
				intersectionPointX = pointX;
				intersectionPointY = pointY;
				//System.out.println("LINES INTERSECT with intersectionPointX and intersectionPointY being "+intersectionPointX+ ", "+intersectionPointY);
				double fTime = System.nanoTime();
			//System.out.println("LINE INTERSECTION FOUND IN TIME: "+ (fTime-sTime));

				return new double[] {intersectionPointX, intersectionPointY};
			} else {
				//System.out.println("Lines Intersect but Solution is oob");
				intersectionPointX = pointX;
				intersectionPointY = pointY;
			//	System.out.println("LINES DO NOT INTERSECT with intersectionPointX and intersectionPointY being "+intersectionPointX+ ", "+intersectionPointY);
				return null;
			}
		}

	}
}
