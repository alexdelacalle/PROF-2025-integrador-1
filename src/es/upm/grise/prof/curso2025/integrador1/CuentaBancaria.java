package es.upm.grise.prof.curso2025.integrador1;

import java.util.ArrayList;
import java.util.List;

public class CuentaBancaria {
	
	String numeroCuenta;
	double saldoInicial;
	boolean admiteDescubierto;
	List<Operacion> operaciones;
	
	//
	// CUERPO DEL EXAMEN
	//
	
	public CuentaBancaria(String numeroCuenta, double saldoInicial) {
		
		this.numeroCuenta = numeroCuenta;
		this.saldoInicial = saldoInicial;
		this.admiteDescubierto = false;
		this.operaciones = new ArrayList<Operacion>();		
	}
		
	public void addOperacion(Operacion operacion) {
		if (operacion == null) {
			throw new OperacionNulaException("La operacion no puede ser nula");
		}
		boolean existe = this.operaciones.stream().anyMatch(op -> op.getId().equals(operacion.getId()));
		if (existe) {
			throw new OperacionDuplicadaException("La operacion ya existe en la cuenta");
		}
		this.operaciones.add(operacion);
		
	}
	
	public double getSaldoActual() {
		double saldoActual = this.saldoInicial;
		for (Operacion operacion : this.operaciones) {
			saldoActual += operacion.getImporte();
		}

		BigDecimal bd = BigDecimal.valueOf(saldoActual);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		saldoActual = bd.doubleValue();

		if (!this.admiteDescubierto) {
			if (saldoActual < 0) {
				throw new SaldoNegativoException("La cuenta no admite descubierto y el saldo es negativo");
			}
		}

		return saldoActual;
	}

}
