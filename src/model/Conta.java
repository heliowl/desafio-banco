package model;


public abstract class Conta{
    private static final int AGENCIA_PADRAO = 16;
    private String nomeBanco = Banco.nomeBanco();
    private int agencia;
    private Integer NumConta;
    private double saldo;
    private Cliente cliente;
    private TIPO tipo;
    private static int SEQUENCIAL = 1;

    public Conta(Cliente cliente) {

        this.agencia = AGENCIA_PADRAO;
        this.NumConta = SEQUENCIAL++;
        this.cliente = cliente;
        this.saldo = saldo;
        this.tipo = tipo;
    }



    public int getAgencia() {
        return agencia;
    }

    public Integer getNumConta() {
        return NumConta;
    }

    public double getSaldo() {
        return saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public TIPO getTipo() {
        return tipo;
    }

    public void setTipo(TIPO tipo) {
        this.tipo = tipo;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public static enum TIPO{
        CC,
        CP
    }

    public void sacar(double valor){
        this.saldo -= valor;
    }

    public void depositar(double valor){
        this.saldo += valor;
    }

    @Override
    public String toString() {
        return   "Agencia: "+this.agencia + "   Conta: "+this.NumConta+ "   "+this.tipo+
                "   Cliente: "+this.cliente.getNome()+"   "+
                String.format("Saldo: R$ %.2f", this.saldo);

    }
}
