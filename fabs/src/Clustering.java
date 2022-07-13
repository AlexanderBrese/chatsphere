import java.sql.Array;
import java.util.*;

/**
 * 
 * @author Fabian
 *
 *
 *         Diese Klasse enth�lt die Methoden die LP-Distance zu berechnen und
 *         eine Methode in der die Punkte einem Centroiden zugewiesen werden.
 */

public class Clustering {

// --------------- Konstruktoren ----------------------------
	public Clustering(List<Centroid> centroids, List<DataPoint> points) {
		while (!isFinished(centroids)) {

			// For each datapoint find the smallest Centroid
			for (DataPoint point : points) {
				Centroid smallestDistanceCentr = shortestDistance(centroids, point);
				point.assignCentroid(smallestDistanceCentr);
			}

			// For each centroid, with the newly assigned datapoints above: recalculate coordinates
			for (Centroid centroid : centroids) {
				centroid.calculateCoords(points);
			}
		}

		System.out.println("Clustering DONE");
		System.out.println("-- Punkte --");
		for (DataPoint point: points) {
			System.out.println(point);
		}
		System.out.println("-- Cluster Koordinaten --");
		for (Centroid centroid: centroids) {
			System.out.println(centroid);
		}
	}

	/**
	 * Die Methode ermittelt, zu welchen Centroiden die Distanz am kleinsten ist.
	 */
	public Centroid shortestDistance(List<Centroid> centroids, DataPoint point) {
		Map<Centroid, Double> list = new HashMap<>();

		// Calculate distance of each centroid to the given point
		for (Centroid centroid : centroids) {
			list.put(centroid, getLpDistance(centroid, point));
		}

		// Find the centroid with the smallest distance
		// getKey() gives the centroid, getValue() the distance
		double shortest = Double.MAX_VALUE;
		Centroid resultCentroid = null;
		for (Map.Entry<Centroid, Double> entry : list.entrySet()) {

			if (entry.getValue() < shortest) {
				shortest = entry.getValue();
				resultCentroid = entry.getKey();
			}
		}

		return resultCentroid;
	}

	/**
	 * Berechnet die Distanz zwischen 2 Punkten mithilfe des Satzes des Pythagoras
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public double getLpDistance(Centroid a, DataPoint b) {
		int p = 2; // p is the exponent and root for the LpDistance
		double root = (double) 1 / p; // Wird spaeter fuer die Berechnung der Wurzel benoetigt
		double pointaX = a.getCenterPointX(); // Weist die X Koordinate der Variable von Punkt a zu.
		double pointaY = a.getCenterPointY(); // Weist die Y Koordinate der Variable von Punkt a zu
		double pointbX = b.getX(); // Weist die X Koordinate der Variable von Punkt b zu.
		double pointbY = b.getY(); // Weist die y Koordinate der Variable von Punkt b zu.

		double differenceX = Math.abs(pointaX - pointbX); // Berechnet die Distanz zwischen der X Koordinate
		double differenceY = Math.abs(pointaY - pointbY); // Berechnet die Distanz zwischen der y Koordinate

		double distance = Math.pow(Math.pow(differenceX, p) + Math.pow(differenceY, p), root); // Verwendet den Satz des
																								// Pythagoras fuer die
																								// beiden Distanzen
		return distance; // Gibt den Wert der Variablen distance an die Main-Methode aus
	}

	/**
	 * The whole clustering process is finished
	 * if each centroid's coordinates is finished (i.e. their coordinates do
	 * not change after re-calculation)
	 *
	 */
	public boolean isFinished(List<Centroid> centroids) {
		for(Centroid centroid : centroids) {
			if(!centroid.isFinished()) {
				return false;
			}
		}
		return true;
	}
}
