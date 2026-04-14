package learning.tokioschool.parking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import learning.tokioschool.practicaCincoParking.Coche;

class CocheTest {

	@Test
	void testCoche() {
		LocalDateTime entrada = LocalDateTime.of(2026,1,1,10,0);
		LocalDateTime salida = LocalDateTime.of(2026,1,1,11,0);
		
		Coche coche = new Coche("Toyota", "Yaris", entrada, salida);
		
		assertEquals("Toyota", coche.getMarca());
		assertEquals("Yaris", coche.getModelo());
		assertEquals(entrada, coche.getHoraEntrada());
		assertEquals(salida, coche.getHoraSalida());
	}

	@Test
	void testCantidadAPagar() {
		LocalDateTime entrada = LocalDateTime.of(2026,1,1,10,0);
		LocalDateTime salida = LocalDateTime.of(2026,1,1,11,0);
		
		Coche coche = new Coche("Toyota", "Yaris", entrada, salida);
		
		float resultado = coche.cantidadAPagar();
		float resultadoEsperado = 60 * 0.15f;
		
		assertEquals(resultadoEsperado, resultado, 0.0001f);
	}
	
	@Test
	void testCantidadAPagarUsandoMinutos() {
		LocalDateTime entrada = LocalDateTime.of(2026,1,1,10,15);
		LocalDateTime salida = LocalDateTime.of(2026,1,1,10,45);
		
		Coche coche = new Coche("Toyota", "Yaris", entrada, salida);
		
		float resultado = coche.cantidadAPagar();
		float resultadoEsperado = 30 * 0.15f;
		
		assertEquals(resultadoEsperado, resultado, 0.0001f);
	}

	@Test
	void testToString() {
		LocalDateTime entrada = LocalDateTime.of(2026,1,1,10,0);
		LocalDateTime salida = LocalDateTime.of(2026,1,1,12,30);
		
		Coche coche = new Coche("Toyota", "Yaris", entrada, salida);
		
		String texto = coche.toString();
		String textoEsperado = "Coche [marca=Toyota, modelo=Yaris, horaEntrada=2026-01-01T10:00, horaSalida=2026-01-01T12:30]";

		
		assertNotNull(texto);
		assertTrue(texto.contains("Toyota"));
		assertTrue(texto.contains("Yaris"));
		assertTrue(texto.contains(entrada.toString()));
		assertTrue(texto.contains(salida.toString()));
		assertEquals(textoEsperado, texto);
	}

}
