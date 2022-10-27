package model;

public class ContaCorrente extends Conta {

    private static double juros = 0.025;
    private static String jurosImp = "2,5%";

    private static double chequeEspecial = 500;

    public ContaCorrente(Cliente cliente) {

        super(cliente);

    }

    public static double getJuros() {
        return juros;
    }

    public static String getJurosImp() {
        return jurosImp;
    }

    public static double getChequeEspecial() {
        return chequeEspecial;
    }
}
