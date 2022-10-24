package service;
import exception.SistemException;
import menu.Menu;
import model.Cliente;
import model.Conta;
import model.ContaCorrente;
import model.ContaPoupanca;
import repository.Repository;
import java.util.List;
import java.util.Scanner;
import static model.Conta.TIPO.CC;
import static model.Conta.TIPO.CP;
import static util.ImprimeValores.valorFinanceiro;

public class ContaService {

    private Repository<Integer, Conta> repository = new Repository<>();

    Scanner sc = new Scanner(System.in);

    public ContaService(Scanner sc) {
        this.sc = sc;
    }

    //Verifica se existe a conta no repositório
    public Conta confereConta(int numConta){
        List<Conta> contas = repository.buscarTodos();
        Conta conta = contas.stream().filter(c -> c.getNumConta().equals(numConta)).findFirst().orElse(null);
        return conta;
    }

    //Verifica se a senha digitada confere com a do proprietário da conta
    public boolean validaSenha(int numConta, String senha){
        Conta conta = repository.buscarPorId(numConta);
        return conta.getCliente().getSenha().equals(senha);

    }


    //Mostra os dados da conta
    public void dadosConta(int numConta) {
        Conta conta = repository.buscarPorId(numConta);
        Cliente cliente = conta.getCliente();
        System.out.println("=======================================");
        String saldo = valorFinanceiro(conta.getSaldo());
        System.out.println( conta.getNomeBanco() + " - " +
                "Agencia: " + conta.getAgencia() + "   Conta: " + conta.getNumConta() + "   " + conta.getTipo() + "\n" +
                "Cliente: " + cliente.getNome() + "\n" +
                "Saldo: R$ " + saldo);
        System.out.println("=======================================");
    }

    public Conta abrirConta(Cliente cliente){
        Menu.tipoConta();

        int opcao = sc.nextInt();

        if(opcao == 1){
            Conta conta = new ContaCorrente(cliente);
            conta.setTipo(CC);
            deposito(conta);
            salvarDados(conta.getNumConta(), conta );
            confirmacaoAberturaConta(conta);
            return conta;

        }else{
            Conta conta = new ContaPoupanca(cliente);
            conta.setTipo(CP);
            deposito(conta);
            salvarDados(conta.getNumConta(), conta );
            confirmacaoAberturaConta(conta);
            return conta;
        }

    }

    public void confirmacaoAberturaConta(Conta conta){
        Cliente cliente = conta.getCliente();
        System.out.println("Sua Conta Corrente foi aberta com sucesso!");
        System.out.println();
        System.out.println("Guarde seus dados para poder acessá-la!");
        System.out.println("Conta: " + conta.getNumConta() + "   Senha: " + cliente.getSenha());
        System.out.println();
    }

    public void deposito(Conta conta){
        System.out.println("Valor do Depósito: R$ ");
        double valor = sc.nextDouble();
        conta.depositar(valor);
    }

    public void saque(Conta conta){
        System.out.println("Valor do saque: R$ ");
        double valor = sc.nextDouble();
        conta.sacar(valor);
    }

    public void transferencia(Conta conta, int contaBeneficiario) throws SistemException {

        Conta contaDeposito = confereConta(contaBeneficiario);
        if (contaDeposito == null) {
            throw new SistemException("Conta não encontrada!");
        }else {
            System.out.println("Valor da transferência: ");
            double valor = sc.nextDouble();

                conta.sacar(valor);
                contaDeposito.depositar(valor);
                salvarDados(contaDeposito.getNumConta(), contaDeposito);
            }
    }




    public void extrato(Conta conta) {
        Cliente cliente = conta.getCliente();
        System.out.println("=======================================");
        String saldo = valorFinanceiro(conta.getSaldo());
        System.out.println( conta.getNomeBanco() + "\n" +
                "Agencia: " + conta.getAgencia() + "   Conta: " + conta.getNumConta() + "   " + conta.getTipo() + "\n" +
                "Cliente: " + cliente.getNome() + "\n Saldo: R$ " + saldo);
        System.out.println("=======================================");

    }

    //Mostra os dados da conta que vai ser transferido o valor
    public void confirmaTransferencia(Conta contaDeposito, double valor) {
        Cliente cliente = contaDeposito.getCliente();
        String valorDeposito = valorFinanceiro(valor);
        System.out.println("Confirme os dados para transferência!");
        System.out.println("=======================================");
        System.out.println( contaDeposito.getNomeBanco() + " - " +
                "Agencia: " + contaDeposito.getAgencia() + "   Conta: " + contaDeposito.getNumConta() + "   " + contaDeposito.getTipo() + "\n" +
                "Cliente: " + cliente.getNome() + "\n" + "Valor do depósito: R$ "+valorDeposito);
        System.out.println("=======================================");

    }


    //Cadastra usuário e salva novos dados
    public void salvarDados(int numConta, Conta conta){
        repository.salvar(numConta, conta);
    }


}
