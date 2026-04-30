package learning.tokioschool.practicaSeisParkingDataBase;

import java.time.LocalDateTime;
import java.util.Scanner;

public class App {

	private static Parking parking = new Parking();

	/**
	 * The main method.
	 * 
	 * HE MODIFICADO ESTE METODO, AÑADIENDO LA CREACION DE LAS TABLAS Y ELIMINANDO EL METODO cantidadAPagar() DEL CASO (2).
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		parking.crearTabla();

		boolean salir = false;
		int opcionMenu;
		System.out.println("SE HA INICIALIZADO EL SOFTWARE PARA EL CONTROL DE ACCESO DEL PARKING");
		try (Scanner sc = new Scanner(System.in)) {
			do {
				pintarMenu();

				try {
					opcionMenu = Integer.parseInt(sc.nextLine());
				} catch (NumberFormatException ex) {
					opcionMenu = 0;
				}

				switch (opcionMenu) {
				case (0):
					System.out.println("Por favor, introduzca opcion del menu en formato numerico");
					break;
				case (1):
					registrarEntradaCoche(sc);
					break;
				case (2):
					registrarSalidaCoche(sc);
					break;
				case (3):
					parking.imprimirCochesParking();
					break;
				case (4):
					parking.imprimirCochesSistema();
					break;
				case (5):
					salir = true;
					break;
				default:
					System.out.println("Opcion no contemplada");
				}
			} while (!salir);
		} catch (Exception ex) {
			System.out.println("Error interno en el sistema");
		}
		System.out.println("SE HA PARADO EL SOFTWARE PARA EL CONTROL DE ACCESO DEL PARKING");

	}

	/**
	 * Metodo que pinta el menu principal
	 */
	public static void pintarMenu() {
		System.out.println();
		System.out.println("¿Que desea hacer?");
		System.out.println("1. Registrar entrada coche");
		System.out.println("2. Registrar salida coche");
		System.out.println("3. Imprimir coches en el parking");
		System.out.println("4. Imprimir coches en el sistema");
		System.out.println("5. Salir");
		System.out.println();
	}

	/**
	 * Metodo que registra la entrada de coches en el parking siempre y cuando no
	 * existe un coche dentro de este (horaSalida = null)
	 * 
	 * HE MODIFICADO ESTE METODO PARA QUE HAYA MAS CONTROL EN EL REGISTRO DEL VEHICULO:
	 * <ul>
	 * <li>NUEVO FORMATO DE TEXTO EN EL REGISTRO DE LAS MATRICULAS. </li>
	 * <li>NUEVO FORMATO DE TEXTO A LA HORA DE REGISTRAR LOS COCHES(MARCA Y MODELO)</li>
	 * <li>HE AÑADIDO UNA OPCION QUE DA LA POSIBILIDAD A QUE UN COCHE PUEDA VOLVER DE NUEVO AL PARKING.</li>
	 * <li>CONTROL PARA QUE UNA MATRICULA CORRESPONDA AL MISMO COCHE AUNQUE SE HAYA MARCHADO Y LUEGO VUELVA.</li>
	 * </ul>
	 * 
	 * @param sc
	 */
	public static void registrarEntradaCoche(final Scanner sc) {

		try {
			System.out.println("REGISTRAR COCHE ENTRADA");
			System.out.println("Introduzca matricula");
			String matricula = sc.nextLine().toUpperCase();
			System.out.println("Introduzca marca");
			String marca = primeraLetraMayusculas(sc.nextLine());
			System.out.println("Introduzca modelo");
			String modelo = primeraLetraMayusculas(sc.nextLine());

			Coche comprobarCoche = parking.getCoche(matricula);
			Coche addCoche = null;

			if (comprobarCoche != null && comprobarCoche.getHoraSalida() == null) {
				System.out.println(
						"Coche actualmente dentro del parking, no se puede registrar su entrada hasta que no se registre su salida.");
			} else if (comprobarCoche != null && comprobarCoche.getHoraSalida() != null) {
				if (!comprobarCoche.getMarca().equalsIgnoreCase(marca)
						|| !comprobarCoche.getModelo().equalsIgnoreCase(modelo)) {
					System.out.println(
							"Error: ya existe un coche con esa matrícula, pero no corresponde a esa marca y modelo.");
				} else {
					parking.updateHoraReentrada(matricula, LocalDateTime.now());
					System.out.println("COCHE REGISTRADO EN EL SISTEMA. MATRICULA " + matricula + " DATOS "
							+ comprobarCoche.toString() + "\nBIENVENIDO DE NUEVO.");
				}

			} else {
				addCoche = new Coche(marca, modelo, LocalDateTime.now(), null);
				parking.putCoche(matricula, addCoche);
				System.out.println("COCHE REGISTRADO EN EL SISTEMA. MATRICULA " + matricula + " DATOS "
						+ addCoche.toString() + "\nBIENVENID@.");
			}

		} catch (Exception ex) {
			System.out.println("Error al registrar coche.");
		}
	}

	/**
	 * Metodo que registra la salida de un coche dando valor a la hora de salida.
	 * HEMOS CORREGIDO ESTE METODO PARA QUE NO REALIZARA TANTAS CONSULTAS A LA BASE
	 * DE DATOS, MODIFICANDO EL METODO "cantidadAPagar". YA NO SE LE PASA UNA
	 * MATRICULA PARA CONSULTAR EL COCHE Y REALIZAD EL CALCULO. AHORA SE LE PASA DIRECTAMENTE UN COCHE.
	 * TAMBIEN SE HA MODIFICADO PARA QUE SEA DIRECTAMENTE ESTE METODO EL QUE SE ENCARGUE DE INVOCAR LA cantidadAPagar() CUANDO CORRESPONDA.
	 * 
	 * @param sc
	 * @return
	 */
	public static String registrarSalidaCoche(final Scanner sc) {
		String matricula = null;
		try {
			System.out.println("REGISTRAR COCHE SALIDA");
			System.out.println("Introduzca matricula");
			matricula = sc.nextLine().toUpperCase();

			Coche coche = parking.getCoche(matricula);

			if (coche != null) {
				if (coche.getHoraSalida() == null) {
					LocalDateTime fechaSalida = LocalDateTime.now();
					parking.updateHoraSalida(matricula, fechaSalida);
					coche.setHoraSalida(fechaSalida);

					System.out.println("REGISTRADO SALIDA DE COCHE EN EL SISTEMA. MATRICULA " + matricula + " DATOS "
							+ coche.toString() + "\nHASTA PRONTO.");

					parking.cantidadAPagar(coche);
				} else {
					System.out.println("Este coche ya no está en el parking.");
				}
			} else {
				System.out.println("No hay ningún coche registrado con la matricula introducida");
				matricula = null;
			}

		} catch (Exception ex) {
			System.out.println("Error al registrar salida coche");
		}
		return matricula;
	}

	/**
	 * Primera letra mayusculas.
	 * 
	 * Método para formatear la marca y el modelo del coche, en el que se guardara
	 * en la base de datos con la primera letra en mayúsculas.
	 *
	 * @param texto String texto obtenido del scanner.
	 * @return texto String formateado.
	 */
	public static String primeraLetraMayusculas(String texto) {
		if (texto == null || texto.isBlank()) {
			return texto;
		}
		texto = texto.trim().toLowerCase();
		return texto.substring(0, 1).toUpperCase() + texto.substring(1);
	}
}
