package cursojava;

public class ContaPoupanca extends Conta{


    public ContaPoupanca(Cliente cliente) {
        super(cliente);
    }

    @Override
    public void extrato() {

        System.out.println("====Extrato CP====");
        dadosConta();
        System.out.println("==================");
        System.out.println();
    }
}
