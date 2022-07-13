import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * @author Fabian
 *
 */
public class TestMain {

	public static void main(String[] args) {
		int j = 0; // Index des Punktes der Liste der mit den anderen Punkten verglichen wird

		// Centerpoint Koordinate liegt zwischen (0,0) und (9,9)
		// double randomX = (Math.random() * 5); // Lege maximale X-Koordinate als 5
		// fest
		// double randomY = (Math.random() * 5); // Lege maximale Y-Koordinate als 5
		// fest

		// Initialize some points
		List<DataPoint> points = new LinkedList<>();
		points.add(new DataPoint(2.0, 2.0));
		points.add(new DataPoint(2.0, 1.0));
		points.add(new DataPoint(5.0, 5.0));
		points.add(new DataPoint(4.0, 4.0));

		System.out.println();
		// Initialize some centroids
		List<Centroid> centroids = new LinkedList<>();
		centroids.add(new Centroid(2.0, 2.0));
		centroids.add(new Centroid(4.0, 4.0));

		// Start a clustering algorithm.
		new Clustering(centroids, points);

	}
}
