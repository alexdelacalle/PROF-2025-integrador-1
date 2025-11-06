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

    @Test
    public void testSaldoPositivo() {
        CuentaBancaria cuentaPositiva = new CuentaBancaria("ES789", 200.0);
    
        Operacion ingreso = mock(Operacion.class);
        when(ingreso.getId()).thenReturn("OP4");
        when(ingreso.getImporte()).thenReturn(100.0);
    
        cuentaPositiva.addOperacion(ingreso);
    
        double saldo = cuentaPositiva.getSaldoActual();
        assertTrue("El saldo debe ser positivo", saldo > 0);
        assertEquals(300.0, saldo, 0.001);
    }

    @Test
    public void testAceptarDescubierto() {
        CuentaBancaria cuentaConDescubierto = new CuentaBancaria("ES999", 50.0);
        // Permitimos descubierto manualmente
        cuentaConDescubierto.admiteDescubierto = true;
    
        Operacion retiro = mock(Operacion.class);
        when(retiro.getId()).thenReturn("OP5");
        when(retiro.getImporte()).thenReturn(-100.0); 
    
        cuentaConDescubierto.addOperacion(retiro);
    
        try {
            double saldo = cuentaConDescubierto.getSaldoActual();
            assertEquals(-50.0, saldo, 0.001);
        } catch (SaldoNegativoException e) {
            fail("No debería lanzarse SaldoNegativoException cuando se permite descubierto");
        }
    }


}
