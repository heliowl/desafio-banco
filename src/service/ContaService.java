package service;
import exception.SistemException;
import menu.Menu;
import model.*;
import repository.Repository;
import util.Confirmar;

import java.util.List;
import java.util.Scanner;

import static model.Conta.TIPO.*;
import static model.Transacoes.TIPO.*;
import static util.Confirmar.confirmarSimNao;
import static util.FormataData.dataHora;
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
        System.out.println();
        System.out.println("Sua Conta Corrente foi aberta com sucesso!");
        System.out.println();
        System.out.println("Guarde seus dados para poder acessá-la!");
        System.out.println("Conta: " + conta.getNumConta() + "   Senha: " + cliente.getSenha());
        System.out.println();
    }

    public void deposito(Conta conta, double valor){
        valor = calculaJuros(conta, valor);
        conta.depositar(valor);

        //Cria a transação depósito e adiciona a lista transações
        Transacoes transacao = new Transacoes(conta, DEP, valor);
        conta.adicionaTransacao(transacao);
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

            //Cria a transação saque e adiciona a lista transações
            Transacoes transacao = new Transacoes(conta, SAQ, - valor);
            conta.adicionaTransacao(transacao);
            System.out.println("Transação aprovada!");
            System.out.println();
        }
    }
    //Verifica se o cliente tem saldo ou saldo + cheque especial para fazer o saque
    public boolean limiteSaque(Conta conta, double valor){
        boolean aprovado = false;
        if(conta.getTipo().equals(CC)) {
            if ((conta.getSaldo() - valor) < - ContaCorrente.getChequeEspecial()) {
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

                String pergunta = "Confirmar transferência? [S/N] ";
                String confirmar = confirmarSimNao(pergunta);


                if (confirmar.equalsIgnoreCase("s")) {
                    boolean valida = validaSenha(conta);
                    if (valida) {
                        conta.sacar(valor);

                        //Cria a transação transferência e inclui a na lista transações do agente da transferência
                        Transacoes transacao = new Transacoes(conta, TRA, - valor);
                        conta.adicionaTransacao(transacao);

                        deposito(contaDeposito, valor);

                        //exclui a transação depósito criada pela função "deposito" para inserção da transação transferência
                        int num = contaDeposito.getTransacoes().size()-1;
                        contaDeposito.getTransacoes().remove(num);

                        //Cria a transação transferência e inclui a na lista transações de quem recebe a transferência
                        Transacoes transacaoTraDep = new Transacoes(contaDeposito, TRA, valor);
                        contaDeposito.adicionaTransacao(transacaoTraDep);
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
        System.out.println();
        System.out.println("=======================================");
        String saldo = valorFinanceiro(conta.getSaldo());
        System.out.println( "                EXTRATO"+
                "\n"+conta.getNomeBanco() + "   " +
                "Agencia: " + conta.getAgencia() + "   Conta: " + conta.getNumConta() + "   " + conta.getTipo() + "\n" +
                "Cliente: " + cliente.getNome() + "\nSaldo: R$ " + saldo);
        System.out.println(1);
        System.out.println("Últimas movimentações: ");
        mostrarTransacoes(conta);
        System.out.println("=======================================");
        System.out.println();

    }
    //Função para imprimir as transações no extrato
    public void mostrarTransacoes(Conta conta){
        List<Transacoes> transacoes = conta.getTransacoes();

        transacoes.forEach(t -> System.out.println(t.getTipo()+"   R$ "
                +valorFinanceiro(t.getValor())+"   "+ dataHora(t.getDataTransacao())));

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
    //Verifica se o cliente tem débito ou crédito e impõe condições para o encerramento da conta
    public boolean encerrarConta(Conta conta){
        boolean logado = true;
        if(conta.getSaldo() > 0){
            System.out.println("Para encerrar a conta você precisa sacar a totalidade de seu saldo R$ "+valorFinanceiro(conta.getSaldo()));
            String pergunta = "Confirmar saque? [S/N]";
            String confirmar = confirmarSimNao(pergunta);
            if(confirmar.equalsIgnoreCase("s")){
                boolean valida = validaSenha(conta);
                if (valida) {
                    double valor = conta.getSaldo();
                    conta.sacar(valor);
                    System.out.println("Saque de R$ " + valorFinanceiro(valor) + " realizado!");
                    excluirConta(conta.getNumConta());
                    System.out.println();
                    System.out.println("Sua conta foi encerrada, esperamos vê-lo novamente em breve!");
                    System.out.println();
                    logado = false;
                }
            }else{
                System.out.println("Sua conta continua aberta! Saldo R$ "+valorFinanceiro(conta.getSaldo()));
                logado = true;
            }

        }else if(conta.getSaldo() < 0){
            String pergunta = "Deseja fazer o depósito? [S/N]";
            double saldo = Math.abs(conta.getSaldo());
            double valorDep = saldo + (saldo * ContaCorrente.getJuros());
            System.out.println("Você está com saldo negativo de R$ "+valorFinanceiro(conta.getSaldo())+
                    ", para encerrar a conta deposite o débito + os juros no valor de R$ "+valorFinanceiro(valorDep));
            String confirmar = confirmarSimNao(pergunta);
            if(confirmar.equalsIgnoreCase("s")){
                boolean valida = validaSenha(conta);
                if (valida) {
                    deposito(conta, valorDep);
                    System.out.println();
                    System.out.println("O valor de R$ " + valorFinanceiro(valorDep) + " foi depositado!");
                    System.out.println("Sua conta foi encerrada, esperamos vê-lo novamente em breve!");
                    System.out.println();
                    excluirConta(conta.getNumConta());
                    logado = false;
                }
            }else{
                System.out.println();
                System.out.println("Sua conta continua aberta! Saldo R$ "+valorFinanceiro(conta.getSaldo()));
                System.out.println();
                logado = true;
            }
        }else{
            String pergunta = "Tem certeza que deseja excluir essa conta? [S/N]";
            String confirmar = confirmarSimNao(pergunta);
            if(confirmar.equalsIgnoreCase("s")){
                boolean valida = validaSenha(conta);
                if (valida) {
                    excluirConta(conta.getNumConta());
                    System.out.println();
                    System.out.println("Sua conta foi encerrada, esperamos vê-lo novamente em breve!");
                    System.out.println();
                    logado = false;
                }else {
                    System.out.println();
                    System.out.println("Sua conta continua aberta! Saldo R$ "+valorFinanceiro(conta.getSaldo()));
                    System.out.println();
                    logado = true;
                }
            }
        }

        return logado;
    }


    //Cadastra conta de usuário e salva novos dados
    public void salvarDados(int numConta, Conta conta){
        repository.salvar(numConta, conta);
    }
    //exclui a conta do repositório
    public void excluirConta(int numConta){
        repository.excluir(numConta);
    }

}
