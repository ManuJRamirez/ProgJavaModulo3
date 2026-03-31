package learning.tokioschool.practicaTresControlStock;

/**
 * Clase Producto.
 * 
 * Esta clase define el objeto producto de nuestro almacén. Un producto tiene tres atributos: Identificador único, un nombre de producto y un stock disponible.
 */
public class Producto {
	
	/** Atributo identificador. Se generará de forma automática y debe ser único. */
	private String identificador;
	
	/** Nombre del producto */
	private String nombreProducto;
	
	/** Atributo que declara la cantidad disponible en el stock. */
	private int stock;
	
	
	/**
	 * Constructor de clase.
	 *
	 * @param identificador 
	 * @param nombreProducto 
	 * @param stock 
	 */
	public Producto(String identificador, String nombreProducto, int stock) {
		super();
		this.identificador = identificador;
		this.nombreProducto = nombreProducto;
		this.stock = stock;
	}
	
	/**
	 * Método Get del identificador.
	 *
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}
	
	/**
	 * Método Sets del identificador.
	 *
	 * @param identificador the new identificador
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	/**
	 * Método Get del the nombre producto.
	 *
	 * @return the nombre producto
	 */
	public String getNombreProducto() {
		return nombreProducto;
	}
	
	/**
	 * Método Sets del nombre producto.
	 *
	 * @param nombreProducto the new nombre producto
	 */
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
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
	
	

}
