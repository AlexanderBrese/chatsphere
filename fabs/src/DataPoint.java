
public class DataPoint {
	private double x;
	private double y;
	private int centroidId;

	public DataPoint(double x, double y) {
		this.x = x;
		this.y = y;
		this.centroidId = -1; // -1 = nicht assigned
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public int getAssignedCentroid() {
		return centroidId;
	}

	public void assignCentroid(Centroid centr) {
		this.centroidId = centr.getId();
	}

	@Override
	public String toString() {
		return String.format("Point (%f/%f, ID: %d)", x, y, centroidId);
	}
}