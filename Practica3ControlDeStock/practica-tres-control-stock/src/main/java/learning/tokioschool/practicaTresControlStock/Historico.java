package learning.tokioschool.practicaTresControlStock;

/**
 * Clase Historico.
 * 
 * Representa un registro del historial de operaciones realizadas con el stock de un Producto.
 * 
 * Cada objeto Historico va a almacenar la fecha, el tipo de movimiento realizado con el stock (Aumento, Reduccion o Nuevo Producto) y el stock.
 */
public class Historico {
	
	/** Atributo donde guardamos la fecha. */
	private String fecha;
	
	/** Atributo donde tenemos el stock. */
	private int stock;
	
	/** Atributo tipo de operacion con el stock */
	private String tipo;
	
	/**
	 * Constructor de clase.
	 *
	 * @param fecha  fecha
	 * @param stock  stock
	 * @param tipo  tipo
	 */
	public Historico(String fecha, int stock, String tipo) {
		super();
		this.fecha = fecha;
		this.stock = stock;
		this.tipo = tipo;
	}

	/**
	 * Método Get de fecha.
	 *
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * Método Sets de la fecha.
	 *
	 * @param fecha the new fecha
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * Método Get del stock.
	 *
	 * @return the stock
	 */
	public int getStock() {
		return stock;
	}

	/**
	 * Método Sets del stock.
	 *
	 * @param stock the new stock
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}

	/**
	 * Método Get del tipo.
	 *
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Método Sets del tipo.
	 *
	 * @param tipo the new tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
