package model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.DataFormatException;

public class Transacoes {

    private Conta conta;
    private TIPO tipo;
    private double valor;

    private LocalDateTime dataTransacao;

    public Transacoes(Conta conta, TIPO tipo, double valor) {
        this.conta = conta;
        this.tipo = tipo;
        this.valor = valor;
        this.dataTransacao = LocalDateTime.now();
    }


    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public TIPO getTipo() {
        return tipo;
    }

    public void setTipo(TIPO tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataTransacao() {
        return dataTransacao;
    }

    public static enum TIPO{
        DEP,
        SAQ,
        TRA
    }
}
