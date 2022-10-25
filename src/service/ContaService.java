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
    public boolean confereSenha(int numConta, String senha){

        Conta conta = repository.buscarPorId(numConta);
        return conta.getCliente().getSenha().equals(senha);

    }

    //Possibilita que o usuário tenha apenas três tentativas para inserir a senha correta
    public boolean validaSenha(Conta conta){
        boolean valida = false;
        for(int i = 0; i < 3; i++) {
            System.out.println("Senha: ");
            String senha = sc.next();
            sc.nextLine();
            valida = confereSenha(conta.getNumConta(), senha);
            if (!valida) {
                System.out.println("Senha Inválida!");
            } else {
                break;
            }
        }
        if(!valida){
            System.out.println("O número de tentativas expirou, tente novamente mais tarde!");
        }
        return valida;
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
            double valor = valorDeposito();
            deposito(conta, valor);
            salvarDados(conta.getNumConta(), conta );
            confirmacaoAberturaConta(conta);
            return conta;

        }else{
            Conta conta = new ContaPoupanca(cliente);
            conta.setTipo(CP);
            double valor = valorDeposito();
            deposito(conta, valor);
            salvarDados(conta.getNumConta(), conta );
            confirmacaoAberturaConta(conta);
            return conta;
        }

    }

    public double valorDeposito(){
        System.out.println("Valor do Depósito: R$ ");
        double valor = sc.nextDouble();
        return Math.abs(valor);
    }

    public void confirmacaoAberturaConta(Conta conta){
        Cliente cliente = conta.getCliente();
        System.out.println("Sua Conta Corrente foi aberta com sucesso!");
        System.out.println();
        System.out.println("Guarde seus dados para poder acessá-la!");
        System.out.println("Conta: " + conta.getNumConta() + "   Senha: " + cliente.getSenha());
        System.out.println();
    }

    public void deposito(Conta conta, double valor){
        valor = calculaJuros(conta, valor);
        conta.depositar(valor);
    }

    //Verifica se a Conta Corrente está no cheque especial e calcula o juros cobrados no depósito
    public Double calculaJuros(Conta conta, double valor){
        if(conta.getSaldo() < 0){
            valor -= (conta.getSaldo() * (- ContaCorrente.getJuros()));
            return valor;
        }else{
            return valor;
        }

    }

    public void saque(Conta conta){
        System.out.println("Valor do saque: R$ ");
        double valor = sc.nextDouble();
        valor = Math.abs(valor);
        boolean aprovado = limiteSaque(conta, valor);
        if(aprovado) {
            conta.sacar(valor);
            validaSenha(conta);
        }
    }

    public boolean limiteSaque(Conta conta, double valor){
        boolean aprovado = false;
        if(conta.getTipo().equals(CC)) {
            if ((conta.getSaldo() - valor) < -500) {
                System.out.println("Saldo insuficiente! Valor disponível: Saldo + R$ 500 do cheque especial!");
                System.out.println();
            }else{
                aprovado = true;
            }
        }else if(conta.getTipo().equals(CP)){
            if ((conta.getSaldo() - valor) < 0) {
                System.out.println("Saldo insuficiente!");
                System.out.println();
            }else{
                aprovado = true;
            }
        }
        return aprovado;
    }

    public void transferencia(Conta conta, int contaBeneficiario) throws SistemException {

        Conta contaDeposito = confereConta(contaBeneficiario);
        if (contaDeposito == null) {
            throw new SistemException("Conta não encontrada!");
        }else {
            System.out.println("Valor da transferência: ");
            double valor = sc.nextDouble();
            valor = Math.abs(valor);
            boolean aprovado = limiteSaque(conta, valor);
            if(aprovado) {
                confirmaTransferencia(contaDeposito, valor);
                System.out.println("Confirmar transferência? [S/N] ");
                String confirmar = sc.next();
                sc.nextLine();
                while (!confirmar.equalsIgnoreCase("s") && !confirmar.equalsIgnoreCase("n")) {
                    System.out.println("Opção inválida!");
                    confirmaTransferencia(contaDeposito, valor);
                    System.out.println("Confirmar transferência? [S/N] ");
                    confirmar = sc.next();
                    sc.nextLine();

                }
                if (confirmar.equalsIgnoreCase("s")) {
                    boolean valida = validaSenha(conta);
                    if (valida) {
                        conta.sacar(valor);
                        deposito(contaDeposito, valor);
                        salvarDados(contaDeposito.getNumConta(), contaDeposito);
                        String valorImp = valorFinanceiro(valor);
                        System.out.println("Transferência de R$ " + valorImp + " realizada com sucesso!");
                        System.out.println();
                    }
                }
            }
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
