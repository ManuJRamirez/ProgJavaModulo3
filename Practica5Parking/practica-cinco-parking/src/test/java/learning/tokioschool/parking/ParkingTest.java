package learning.tokioschool.parking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import learning.tokioschool.practicaCincoParking.Coche;
import learning.tokioschool.practicaCincoParking.Parking;

class ParkingTest {

	@Test
	void testParking() {
		Parking parking = new Parking();
		
		assertNotNull(parking);		
	}

	@Test
	void testPutCocheGetCocheExisteCoche() {
		LocalDateTime entrada = LocalDateTime.of(2026,1,1,10,0);
		LocalDateTime salida = LocalDateTime.of(2026, 1,1,10,30);
		Parking parking = new Parking();
		Coche coche = new Coche("Toyota", "Yaris",entrada, salida);
		
		parking.putCoche("7777KTM", coche);
		
		assertNotNull(parking);
		assertTrue(parking.existeCoche("7777KTM"));
		assertEquals(coche, parking.getCoche("7777KTM"));
	}

	@Test
	void testGetCocheExisteCocheInexistente() {
		Parking parking = new Parking();
		Coche testResultado = parking.getCoche("NoExiste");
		
		assertEquals(null, testResultado);
		assertFalse(parking.existeCoche("NoExiste"));
		
	}
	
	
	@Test
	void testImprimirCochesSistema() {
		LocalDateTime entrada = LocalDateTime.of(2026,1,1,10,0);
		LocalDateTime salida = LocalDateTime.of(2026,1,1,11,0);
		
		Parking parking = new Parking();
		Coche cocheFueraParking = new Coche("Toyota", "Yaris", entrada, salida);
		Coche cocheEnParking = new Coche("Seat", "Ibiza", entrada, null);
		parking.putCoche("7777KTM", cocheFueraParking);
		parking.putCoche("0000KTM", cocheEnParking);
		
		ByteArrayOutputStream bufferSalidaConsola = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bufferSalidaConsola));
		
		parking.imprimirCochesSistema();
		
		String salidaConsola = bufferSalidaConsola.toString();
		
		assertNotNull(salidaConsola);
		assertTrue(salidaConsola.contains("7777KTM"));
		assertTrue(salidaConsola.contains("Toyota"));
		assertTrue(salidaConsola.contains("Yaris"));
		assertTrue(salidaConsola.contains("0000KTM"));
		assertTrue(salidaConsola.contains("Seat"));
		assertTrue(salidaConsola.contains("Ibiza"));
		
		
	}

	@Test
	void testImprimirCochesParking() {
		LocalDateTime entrada = LocalDateTime.of(2026,1,1,10,0);
		LocalDateTime salida = LocalDateTime.of(2026,1,1,11,0);
		
		Parking parking = new Parking();
		Coche cocheEnParking = new Coche("Seat", "Ibiza", entrada, null);
		Coche cocheFueraParking = new Coche("Toyota","Yaris",entrada,salida);
		parking.putCoche("0000KTM", cocheEnParking);
		parking.putCoche("7777KTM", cocheFueraParking);
		
		ByteArrayOutputStream bufferSalidaConsola = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bufferSalidaConsola));
		
		parking.imprimirCochesParking();
		
		String salidaConsola = bufferSalidaConsola.toString();
		
		assertNotNull(salidaConsola);
		assertTrue(salidaConsola.contains("0000KTM"));
		assertFalse(salidaConsola.contains("7777KTM"));
		assertTrue(salidaConsola.contains("Seat"));
		assertFalse(salidaConsola.contains("Toyota"));
		
	}

	
	@Test
	void testCantidadAPagar() {
		LocalDateTime entrada = LocalDateTime.of(2026, 1,1,10,0);
		LocalDateTime salida = LocalDateTime.of(2026, 1,1,11,0);
		
		Parking parking = new Parking();
		Coche coche = new Coche("Toyota", "Yaris", entrada, salida);
		parking.putCoche("7777KTM", coche);

		ByteArrayOutputStream bufferSalidaConsola = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bufferSalidaConsola));
		
		parking.cantidadAPagar("7777KTM");
		
		String salidaConsola = bufferSalidaConsola.toString();
		String precioEsperado = String.valueOf(60 * 0.15f);

		
		assertTrue(salidaConsola.contains("Cantidad a pagar"));
		assertTrue(salidaConsola.contains(precioEsperado));
		
	}

	@Test
	void testCantidadAPagarMatriculaNull() {
		Parking parking = new Parking();
		
		ByteArrayOutputStream bufferSalidaConsola = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bufferSalidaConsola));
		
		parking.cantidadAPagar(null);
		
		String salidaConsola = bufferSalidaConsola.toString();
		
		assertTrue(salidaConsola.isEmpty());
		
	}
}
