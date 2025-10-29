package es.upm.grise.prof.curso2025.integrador1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class CuentaBancariaTest {

    private CuentaBancaria cuenta;
    private Operacion operacionMock;

    @Before
    public void setUp() {
        cuenta = new CuentaBancaria("ES123", 100.0);

        // Creamos un mock de Operacion
        operacionMock = mock(Operacion.class);
        when(operacionMock.getId()).thenReturn("OP1");
        when(operacionMock.getImporte()).thenReturn(50.0);
    }

    @Test
    public void testAddOperacion_valida() {
        cuenta.addOperacion(operacionMock);
        assertEquals(150.0, cuenta.getSaldoActual(), 0.001);
    }

    @Test(expected = OperacionNulaException.class)
    public void testAddOperacion_nula() {
        cuenta.addOperacion(null);
    }

    @Test(expected = OperacionDuplicadaException.class)
    public void testAddOperacion_duplicada() {
        cuenta.addOperacion(operacionMock);
        // Intento de añadir la misma operación de nuevo
        cuenta.addOperacion(operacionMock);
    }

    @Test
    public void testGetSaldoActual_redondeo() {
        // Operación con decimales
        Operacion opDecimal = mock(Operacion.class);
        when(opDecimal.getId()).thenReturn("OP2");
        when(opDecimal.getImporte()).thenReturn(33.456);
        cuenta.addOperacion(opDecimal);

        double saldo = cuenta.getSaldoActual();
        assertEquals(133.46, saldo, 0.0); // Redondeado a 2 decimales
    }

    @Test(expected = SaldoNegativoException.class)
    public void testSaldoNegativo_noAdmiteDescubierto() {
        CuentaBancaria cuentaSinDescubierto = new CuentaBancaria("ES456", 10.0);

        Operacion opGrande = mock(Operacion.class);
        when(opGrande.getId()).thenReturn("OP3");
        when(opGrande.getImporte()).thenReturn(-20.0);

        cuentaSinDescubierto.addOperacion(opGrande);

        // Esto debe lanzar SaldoNegativoException
        cuentaSinDescubierto.getSaldoActual();
    }
}
