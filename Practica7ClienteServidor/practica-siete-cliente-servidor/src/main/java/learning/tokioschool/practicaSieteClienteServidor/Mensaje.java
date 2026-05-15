package learning.tokioschool.practicaSieteClienteServidor;

import java.io.Serializable;


/**
 * The Class Mensaje.
 * 
 * Esta clase va a representar al objeto de datos que vamos a usar para la comunicacion entre Servidor/Cliente. Implementa Serializable para permitir su envio a traves de los ObjectInputStream y los ObjectOutputStream.
 */
public class Mensaje implements Serializable{
	
	
	/** Identificador unico necesario para la serializacion. */
	private static final long serialVersionUID = 1L;
	
	/** Atributo donde se guarda el contenido real del mensaje. */
	private String texto;
	
	/**
	 * Constructor de clase.
	 *
	 * @param texto Se le pasa el texto que queramos enviar a traves de los stream.
	 */
	public Mensaje(String texto) {
		this.texto = texto; 
	}

	/**
	 * Gets the texto.
	 *
	 * @return the texto
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * Sets the texto.
	 *
	 * @param texto the new texto
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Mensaje [texto=" + texto + "]";
	}
	
	

}
