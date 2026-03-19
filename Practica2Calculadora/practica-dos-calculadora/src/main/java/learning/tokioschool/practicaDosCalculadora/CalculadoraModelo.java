package learning.tokioschool.practicaDosCalculadora;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase CalculadoraModelo.
 * 
 * Modelo de la calculadora. Aquí gestionamos la lógica de las operaciones, la memoria y el historial.
 * 
 */
public class CalculadoraModelo {

	/** Atributo donde almacenamos el número en memoria. */
	private double memoria = 0;
	
	/** Primer número que se introduce en la calculadora antes de seleccionar el tipo de operación. */
	private double primerValor = 0;
	
	/** Atributo donde guardamos el tipo de operación. */
	private String tipoOperacion = "";
	
	/** Atributo donde guardamos una lista con el historico de las operaciones. */
	private final List<String> historico = new ArrayList<>();

	/**
	 * Método que establece el tipo de operación y guarda el primer número introducido. 
	 *
	 * @param operacion tipo de operacion (+ - * /).
	 * @param primerValor primer número introducido.
	 */
	public void setOperacion(String operacion, double primerValor) {
		this.tipoOperacion = operacion;
		this.primerValor = primerValor;
	}

	/**
	 * Método en el que realizamos el cálculo entre el primer número (primerValor) y el segundo número (valorActual) introducido segun el tipo de operación seleccionada.
	 * 
	 * También deja el registro de todas las operaciones realizadas en el historial.
	 *
	 * @param valorActual Segundo número pasado para realizar la operación.
	 * @return resultado. El resultado de la operación.
	 */
	public double calcular(double valorActual) {

		if (tipoOperacion.equals("/") && valorActual == 0) {
			throw new ArithmeticException("No se puede dividir entre 0");
		}

		double resultado = 0;

		switch (tipoOperacion) {
		case "+":
			resultado = primerValor + valorActual;
			break;
		case "-":
			resultado = primerValor - valorActual;
			break;
		case "*":
			resultado = primerValor * valorActual;
			break;
		case "/":
			resultado = primerValor / valorActual;
		}

		DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String fechaHora = LocalDateTime.now().format(formatoFecha);

		historico.add(
				primerValor + " " + tipoOperacion + " " + valorActual + " = " + resultado + "   [" + fechaHora + "]");

		return resultado;
	}
	
	/**
	 * Método para en caso de error, cambiar el tipo de operación sin tener que borrar el primer número introducido.
	 *
	 * @param nuevaOperacion Se le pasa el nuevo tipo de operación.
	 */
	public void cambiarOperacion(String nuevaOperacion) {
		this.tipoOperacion = nuevaOperacion;
	}

	/**
	 * Método en con el que guardaremos el número actual que se muestra en el display en la memoria.
	 *
	 * @param valor Se le pasa el valor que se encuentra actualmente en el display de la calculadora.
	 */
	public void guardar(double valor) {
		memoria = valor;
	}

	/**
	 * Método que usamos para mostrar en el display de la calculadora el valor guardado en memoria.
	 *
	 * @return memoria Devuelve el valor almacenado en el atributo memoria.
	 */
	public double recuperar() {
		return memoria;
	}

	/**
	 * Devuelve la lista completa del historial de operaciones realizadas.
	 *
	 * @return Lista de operaciones guardadas.
	 */
	public List<String> getHistorico() {
		return historico;
	}
}
