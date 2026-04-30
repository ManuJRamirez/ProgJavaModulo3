package learning.tokioschool.parking.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import learning.tokioschool.practicaSeisParkingDataBase.Coche;

// TODO: Auto-generated Javadoc
/**
 * The Class ManagerDbParking.
 */
public class ManagerDbParking extends ManagerDbAbstract {

	/**
	 * Update.
	 *
	 * @param matricula  the matricula
	 * @param horaSalida the hora salida
	 * @return the int
	 */
	@Override
	public int update(String matricula, LocalDateTime horaSalida) {
		int filasAfectadas = 0;

		try (Connection conexionDB = iniConexion(); PreparedStatement query = conexionDB.prepareStatement(UPDATE)) {
			query.setObject(1, horaSalida);
			query.setObject(2, matricula);
			filasAfectadas = query.executeUpdate();

		} catch (Exception e) {
			System.out.println("No se puedo realizar la actualización en Base de Datos.\n" + e.getMessage());
		}

		return filasAfectadas;
	}
	
	
	/**
	 * Update reentrada.
	 *
	 * @param matricula the matricula
	 * @param horaEntrada the hora entrada
	 * @return the int
	 */
	@Override
	public int updateReentrada(String matricula, LocalDateTime horaEntrada) {
		int filasAfectadas = 0;
		
		try(Connection conexionDB = iniConexion(); PreparedStatement query = conexionDB.prepareStatement(UPDATE_ENTRADA)){
			query.setObject(1, horaEntrada);
			query.setObject(2, matricula);
			
			filasAfectadas = query.executeUpdate();
				
		}catch (Exception e) {
			System.out.println("Error en en el registro de entrada al parking.");
		}
		
		return filasAfectadas;
	}
	

	/**
	 * Insert.
	 *
	 * @param matricula the matricula
	 * @param coche     the coche
	 * @return the int
	 */
	@Override
	public int insert(String matricula, Coche coche) {
		int filasAfectadas = 0;

		try (Connection conexcionDB = iniConexion(); PreparedStatement query = conexcionDB.prepareStatement(INSERT)) {
			query.setObject(1, matricula);
			query.setObject(2, coche.getMarca());
			query.setObject(3, coche.getModelo());
			query.setObject(4, coche.getHoraEntrada());
			query.setObject(5, coche.getHoraSalida());

			filasAfectadas = query.executeUpdate();

		} catch (Exception e) {
			System.out.println("No se puede insertar el dato.\n" + e.getMessage());
		}
		return filasAfectadas;
	}

	/**
	 * Search.
	 *
	 * @param matricula the matricula
	 * @return the coche
	 */
	@Override
	public Coche search(String matricula) {
		Coche cocheBuscado = null;

		try (Connection conexionDB = iniConexion();
				PreparedStatement query = conexionDB.prepareStatement(SELECT_BY_MATRICULA)) {
			System.out.println("Buscando coche por matrícula.");
			query.setObject(1, matricula);

			ResultSet resultado = query.executeQuery();
			if (resultado.next()) {
				String marca = resultado.getString("Marca");
				String modelo = resultado.getString("Modelo");
				LocalDateTime entrada = resultado.getObject("HoraEntrada", LocalDateTime.class);
				LocalDateTime salida = resultado.getObject("HoraSalida", LocalDateTime.class);

				cocheBuscado = new Coche(marca, modelo, entrada, salida);
				System.out.println("Coche encontrado.");
			}

		} catch (Exception e) {
			System.out.println("No se ha podido realizar la búsqueda del coche por matrícula.\n" + e.getMessage());
		}

		return cocheBuscado;
	}

	/**
	 * Search all.
	 *
	 * @return the map
	 */
	@Override
	public Map<String, Coche> searchAll() {
		Map<String, Coche> listadoDeTodosLosCoches = new HashMap<>();

		try (Connection conexionDB = iniConexion(); PreparedStatement query = conexionDB.prepareStatement(SELECT_ALL)) {

			ResultSet resultado = query.executeQuery();

			while (resultado.next()) {
				String matricula = resultado.getString("Matricula");
				String marca = resultado.getString("Marca");
				String modelo = resultado.getString("Modelo");
				LocalDateTime entrada = resultado.getObject("HoraEntrada", LocalDateTime.class);
				LocalDateTime salida = resultado.getObject("HoraSalida", LocalDateTime.class);

				Coche coche = new Coche(marca, modelo, entrada, salida);

				listadoDeTodosLosCoches.put(matricula, coche);
			}

		} catch (Exception e) {
			System.out.println("No se ha podido realizar la busqueda de todos los coches.\n" + e.getMessage());
		}

		if (listadoDeTodosLosCoches.isEmpty()) {
			System.out.println("No hay registro de ningún coche en la base de datos.");
		}

		return listadoDeTodosLosCoches;
	}

	/**
	 * Search all filter hora salida.
	 *
	 * @return the map
	 */
	@Override
	public Map<String, Coche> searchAllFilterHoraSalida() {
		Map<String, Coche> listadoCochesEnParking = new HashMap<>();

		try (Connection conexionDB = iniConexion();
				PreparedStatement query = conexionDB.prepareStatement(SELECT_ALL_WITHOUT_HORA_SALIDA)) {

			ResultSet resultado = query.executeQuery();

			while (resultado.next()) {
				String matricula = resultado.getString("Matricula");
				String marca = resultado.getString("Marca");
				String modelo = resultado.getString("Modelo");
				LocalDateTime entrada = resultado.getObject("HoraEntrada", LocalDateTime.class);
				LocalDateTime salida = resultado.getObject("HoraSalida", LocalDateTime.class);

				Coche coche = new Coche(marca, modelo, entrada, salida);

				listadoCochesEnParking.put(matricula, coche);
			}

		} catch (Exception e) {
			System.out
					.println("No se ha podido realizar la busqueda de los coches que estan en el Parking actualmente.\n"
							+ e.getMessage());
		}

		if (listadoCochesEnParking.isEmpty()) {
			System.out.println("No hay registro de ningún coche en la base de datos.");
		}

		return listadoCochesEnParking;
	}

}
