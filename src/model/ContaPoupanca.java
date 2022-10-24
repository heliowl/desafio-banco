package model;

public class ContaPoupanca extends Conta{

    private final double taxaManutencao = 10;

    public ContaPoupanca(Cliente cliente) {

        super(cliente);
    }

    public double getTaxaManutencao() {
        return taxaManutencao;
    }



}
