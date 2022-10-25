package model;

public class ContaCorrente extends Conta {

    private static double juros = 0.025;
    private static String jurosImp = "2,5%";

    public ContaCorrente(Cliente cliente) {

        super(cliente);

    }

    public static double getJuros() {
        return juros;
    }

    public static String getJurosImp() {
        return jurosImp;
    }

}
