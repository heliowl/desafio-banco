package cursojava;


import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public abstract class Conta implements IConta{
    private static final int AGENCIA_PADRAO = 1;

    protected Banco banco;
    protected int agencia;
    protected int numero;
    protected double saldo;
    protected Cliente cliente;

    private static int SEQUENCIAL = 1;

    public Conta(Cliente cliente) {
        this.banco = banco;
        this.agencia = AGENCIA_PADRAO;
        this.numero = SEQUENCIAL++;
        this.cliente = cliente;

    }

    public int getAgencia() {
        return agencia;
    }

    public int getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }


    @Override
    public void sacar(double valor) {
        this.saldo -= valor;
    }

    @Override
    public void depositar(double valor) {
        saldo += valor;
    }


    @Override
    public void transferir(double valor, Conta contaDestino) {
        this.sacar(valor);

        contaDestino.depositar(valor);
    }

    public String toString() {
        return "Conta: "+this.numero +" Agência: "+ this.agencia+ " Cliente: "+ this.cliente.getNome() +" Saldo: R$ "+ this.saldo;
    }

    protected void dadosConta(){

        System.out.println("Cliente: "+this.cliente.getNome());
        System.out.println("Agencia: "+this.agencia);
        System.out.println("Conta: "+this.numero);
        System.out.println(String.format("Saldo: R$ %.2f", this.saldo));
    }
}
