package cursojava;

public class ContaCorrente extends Conta{


    public ContaCorrente(Cliente cliente) {
        super(cliente);
    }

    @Override
    public void extrato() {

        System.out.println("====Extrato CC====");
        dadosConta();
        System.out.println("==================");
        System.out.println();
    }
}
