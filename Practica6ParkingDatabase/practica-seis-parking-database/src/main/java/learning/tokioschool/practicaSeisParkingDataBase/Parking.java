package learning.tokioschool.practicaSeisParkingDataBase;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

import learning.tokioschool.parking.db.ManagerDbParking;

public class Parking {

	/** Atributo en el que guardaremos el objeto de la base de datos. */
	final ManagerDbParking parkingDB;

	public Parking() {
		parkingDB = new ManagerDbParking();
	}

	/**
	 * Update hora salida.
	 *
	 * @param Matricula the matricula
	 * @param horaSalida the hora salida
	 */
	public void updateHoraSalida(String Matricula, LocalDateTime horaSalida) {
		parkingDB.update(Matricula, horaSalida);

	}
	
	/**
	 * Update hora reentrada.
	 *
	 * @param matricula the matricula
	 * @param horaReentrada the hora reentrada
	 */
	public void updateHoraReentrada(String matricula, LocalDateTime horaReentrada) {
		parkingDB.updateReentrada(matricula, horaReentrada);
	}

	/**
	 * Metodo que comprueba si existe un coche en el sistema
	 * 
	 * @param matricula
	 * @return
	 */
	public boolean existeCoche(final String matricula) {

		Coche coche = parkingDB.search(matricula);

		return coche != null;
	}

	/**
	 * Metodo que obtiene un coche del sistema
	 * 
	 * @param matricula
	 * @return
	 */
	public Coche getCoche(final String matricula) {
		return parkingDB.search(matricula);
	}

	/**
	 * Metodo que añade un coche al sistema
	 * 
	 * @param matricula
	 * @param coche
	 */
	public void putCoche(final String matricula, final Coche coche) {
		parkingDB.insert(matricula, coche);
	}

	/**
	 * Metodo que imprime todos los coches del sistema, tanto los que estan dentro
	 * del parking como los que no.
	 */
	public void imprimirCochesSistema() {

		Map<String, Coche> listadoTodosLosCoches = parkingDB.searchAll();

		try {
			listadoTodosLosCoches.forEach((k, v) -> {
				System.out.println("Matricula: " + k + " Datos del " + v);
			});

		} catch (Exception ex) {
			System.out.println("Error al imprimir coches en el sistema");
		}
	}

	/**
	 * Metodo que imprime los coches que estan dentro del parking (horaSalida =
	 * null)
	 */
	public void imprimirCochesParking() {

		Map<String, Coche> listadoCochesEnParking = parkingDB.searchAllFilterHoraSalida();

		try {
			listadoCochesEnParking.forEach((k, v) -> {
				System.out.println("Matricula: " + k + " Datos del " + v);
			});

		} catch (Exception ex) {
			System.out.println("Error al imprimir coches en el parking");
		}
	}

	/**
	 * Método que calcula la cantidad a pagar por un coche según el tiempo que ha
	 * estado dentro del parking
	 * 
	 * @param matricula
	 */

	public void cantidadAPagar(Coche coche) {

		if (coche.getHoraSalida() != null) {
			System.out.println("Cantidad a pagar " + String.format("%.2f", coche.cantidadAPagar()) + " €.");
		} else {
			System.out.println("El coche aún no tiene hora de salida.");
		}

	}

	/**
	 * Crear tabla.
	 */
	public void crearTabla() {
		try {
			parkingDB.crearTabla();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
