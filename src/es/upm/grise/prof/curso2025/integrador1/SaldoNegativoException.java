package es.upm.grise.prof.curso2025.integrador1;

public class SaldoNegativoException extends RuntimeException {
    public SaldoNegativoException(String mensaje) {
        super(mensaje);
    }
}
