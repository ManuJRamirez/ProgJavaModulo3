package learning.tokioschool.practicaCuatroCumpleanios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

/**
 * Clase App (main).
 * 
 * En esta clase hemos creado un programa que interactuando con la consola,
 * realiza el cálculo en días hasta el proximo cumpleaños. Las funciones son:
 * 
 * <ul>
 * <li>Solicitar una fecha de nacimiento al usuario.</li>
 * <li>Vamos a validar que esa fecha introducida cumpla con ciertos parámetros.
 * Si no lo cumple, se le volverá a pedir.</li>
 * <li>Calcula la edad actual del usuario.</li>
 * <li>Detectar si hoy es el cumpleaños del usuario y felicitarlo.</li>
 * <li>Calcula los días que faltan hasta el próximo cumpleaños ymuestra la fecha
 * de cuando será día.
 * <li>Preguntar si desea seguir volver a introducir una fecha nueva o cerrar el
 * programa</li>
 * </ul>
 */
public class App {

	/**
	 * Método main donde vamos a implementar las funciones descritas arriba.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Scanner pedirFechaNacimiento = new Scanner(System.in);

		boolean buclePrincipal = true;

		while (buclePrincipal) {
			DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalDate fechaNacimiento = null;
			LocalDate fechaHoy = LocalDate.now();

			int intentosDisponibles = 5;
			do {

				String mostrarFechaHoraActual = LocalDateTime.now().format(formatoFecha) + " "
						+ LocalDateTime.now().format(formatoHora);

				System.out.println(
						"================================= Fecha y Hora actual =================================\n");
				System.out.println("Fecha y hora actual: " + mostrarFechaHoraActual + "\n");
				System.out.println(
						"=======================================================================================\n");
				System.out.println("Introducir su fecha de nacimiento (aaaa-mm-dd): ");

				String fechaNacimientoIntroducida = pedirFechaNacimiento.nextLine();
				System.out.println("\n");

				if (fechaNacimientoIntroducida.matches("\\d{4}-\\d{2}-\\d{2}")) {
					try {
						fechaNacimiento = LocalDate.parse(fechaNacimientoIntroducida);
						if (fechaNacimiento.isAfter(fechaHoy)) {
							fechaNacimiento = null;
							intentosDisponibles--;
							System.out.println(
									"==>> La fecha de nacimiento no puede ser posterior al día de hoy. <<== \nATENCION: Número de intentos disponibles "
											+ intentosDisponibles + ".\n");

						}
					} catch (DateTimeParseException error) {
						intentosDisponibles--;
						System.out.println(
								"==>> Error en la fecha introducida. Vuelva a intentarlo. <<== \nATENCION: Número de intentos disponibles "
										+ intentosDisponibles + ".\n");
					}
				} else {
					intentosDisponibles--;
					System.out.println(
							"==>> Formato de fecha introducida no válido. Siga el patrón \"aaaa-mm-dd\" <<== \nATENCION: Número de intentos disponibles "
									+ intentosDisponibles + ".\n");
				}
			} while (fechaNacimiento == null && intentosDisponibles > 0);

			if (fechaNacimiento == null) {
				buclePrincipal = false;
				System.out.println("Intentos restante 0. Finalizando el programa.");

			} else {

				LocalDate cumpleEsteAnio = fechaNacimiento.withYear(fechaHoy.getYear());
				int edadActual = Period.between(fechaNacimiento, fechaHoy).getYears();

				if (cumpleEsteAnio.isEqual(fechaHoy)) {
					System.out.println("¡¡Felicidades!! ¡¡Hoy es tu cumpleaños!!");
					System.out.println("   ,   ,   ,  ");
					System.out.println("  /////|\\\\\\\\\\");
					System.out.println(" |~~~~~|~~~~~|");
					System.out.println(" |  " + edadActual + " años  |");
					System.out.println(" |_____|_____| \n");

					LocalDate proximoCumpleanio = cumpleEsteAnio.plusYears(1);
					long diasHastaProximoCumpleanio = ChronoUnit.DAYS.between(fechaHoy, proximoCumpleanio);

					System.out.println("Te faltan " + diasHastaProximoCumpleanio
							+ " días hasta tu próximo cumpleaños en " + proximoCumpleanio + ".");
				} else {
					LocalDate proximoCumpleanio;
					if (!cumpleEsteAnio.isAfter(fechaHoy)) {
						proximoCumpleanio = cumpleEsteAnio.plusYears(1);
					} else {
						proximoCumpleanio = cumpleEsteAnio;
					}

					long diasHastaProximoCumpleanio = ChronoUnit.DAYS.between(fechaHoy, proximoCumpleanio);

					System.out.println("Tu próximo cumpleaños será el " + proximoCumpleanio + ", y quedan "
							+ diasHastaProximoCumpleanio + " días.");
				}

				String respuestaRepetir;
				boolean bucleCalcularOtraFecha = true;
				int intentosDisponiblesBucleInterno = 5;

				do {
					System.out.println("\n¿Quieres calcular otra fecha? (Si/No): ");
					respuestaRepetir = pedirFechaNacimiento.nextLine().trim().toLowerCase();

					if (respuestaRepetir.equals("si")) {
						System.out.println("\nDe acuerdo... comencemos de nuevo... \n");
						bucleCalcularOtraFecha = false;
					}

					if (respuestaRepetir.equals("no")) {
						System.out.println("\n¡Hasta pronto!");
						pedirFechaNacimiento.close();
						buclePrincipal = false;
						bucleCalcularOtraFecha = false;
					} else {
						intentosDisponiblesBucleInterno--;
						System.out.println(
								"=>> Respuesta no válida. Debe responder con \"Si\" o \"No\".<<=\nATENCION: Número de intentos disponibles "
										+ intentosDisponiblesBucleInterno + ".\n");
					}
				} while (bucleCalcularOtraFecha && intentosDisponiblesBucleInterno > 0);

				if (intentosDisponiblesBucleInterno == 0) {
					buclePrincipal = false;
					System.out.println("Intentos restante 0. Finalizando el programa.");
				}

			}
		}
	}
}
