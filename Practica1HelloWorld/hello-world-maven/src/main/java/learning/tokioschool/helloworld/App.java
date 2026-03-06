package learning.tokioschool.helloworld;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Clase App.
 * 
 * En esta clase vamos a prácticar con Maven. Para ello mostraremos por consola
 * "Hello world" y usaremos una dependencia "commons-math3" para realizar una
 * operación y sacarla por consola también.
 */
public class App {

	/**
	 * Método main.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		System.out.println("Hello World!");

		double notas[] = { 9.5, 10, 1.5, 6.3, 4.5, 5, 9, 8.5 };
		DescriptiveStatistics estadisticas = new DescriptiveStatistics();

		for (double item : notas) {
			estadisticas.addValue(item);
		}

		System.out.println("El percentil 50 de las notas es: " + estadisticas.getPercentile(50));
	}
}
