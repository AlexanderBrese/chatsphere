import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Centroid {
	private static int centroidCount = 0;

	private double centerX;
	private double centerY;
	private int id;
	private boolean finished;

	public Centroid(double x, double y) {
		this.centerX = x;
		this.centerY = y;
		this.id = centroidCount++;
		System.out.println("Erstellter Centroid: " + this);
	}

	public double getCenterPointX() {
		return this.centerX;
	}

	public double getCenterPointY() {
		return this.centerY;
	}

	public int getId() { return this.id; }

	/**
	 *  A centroid is frozen when its coordinates do not change after re-calculation
	 */
	public boolean isFinished() { return this.finished; };

	public void SetCenterPointXY(double newX, double newY) {
		System.out.println("Ändere Koordinaten zu: " + newX + ", " + newY);
		this.centerX = newX;
		this.centerY = newY;
	}

	/**
	 * Diese Methode verschiebt einen Centroiden in dem Mittelpunkt dem ihm
	 * zugewiesenen DataPoints
	 *
	 */
	public void calculateCoords(List<DataPoint> points) {

		// Filter all points that belong to this centroid currently
		List<DataPoint> assignedPoints = new ArrayList<>();
		for (DataPoint point : points) {
			if(point.getAssignedCentroid() == id) {
				assignedPoints.add(point);
			}
		}

		double allX = 0;
		for (DataPoint point : assignedPoints) {
			allX += point.getX();
		}
		double newX = allX / assignedPoints.size();

		double allY = 0;
		for (DataPoint point : assignedPoints) {
			allY += point.getY();
		}
		double newY = allY / assignedPoints.size();

		if (newX == this.getCenterPointX() && newY == this.getCenterPointY())
			finished = true;
		else
			SetCenterPointXY(newX, newY); // sind die koordinaten nicht gleich, sollen die centroiden neue Koordinaten

	}

	@Override
	public String toString() {
		return String.format("Centroid%d (%f/%f))", id, centerX, centerY);
	}
}