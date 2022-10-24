package model;

public class ContaCorrente extends Conta {

    private final double taxaManutencao = 15;


    public ContaCorrente(Cliente cliente) {

        super(cliente);

    }

    public double getTaxaManutencao() {
        return taxaManutencao;
    }



}
