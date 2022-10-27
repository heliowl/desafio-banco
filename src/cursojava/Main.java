package cursojava;

import exception.SistemException;
import menu.Menu;
import model.Cliente;
import model.Conta;
import model.ContaCorrente;
import service.ClienteService;
import service.ContaService;


import java.util.*;

import static util.ImprimeValores.valorFinanceiro;

public class Main {

    public static void main(String[] args) throws SistemException {

        Scanner sc = new Scanner(System.in);
        ClienteService clienteService = new ClienteService(sc);
        ContaService contaService = new ContaService(sc);

        Cliente cli1 = new Cliente("Breuníssio Dos Anjos", "1234");
        clienteService.salvarDados(cli1.getId(), cli1);
        Conta c1 = new ContaCorrente(cli1);
        c1.setTipo(Conta.TIPO.CC);
        c1.depositar(1200);
        contaService.salvarDados(c1.getNumConta(), c1);
        boolean continuar = true;

        do {
            try {
                Menu.bemVindo();
                Menu.menuCliente1();
                int opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {

                    //Entrada na conta do cliente
                    case 1:
                        System.out.println("Digite o número da sua conta: ");
                        int numConta = sc.nextInt();
                        Conta conta = contaService.confereConta(numConta);
                        if (conta == null) {
                            throw new SistemException("Conta não encontrada!");
                        } else {
                            boolean valida = contaService.validaSenha(conta);

                            if(valida){
                                boolean logado = true;
                                Cliente cliente = conta.getCliente();


                                //Menu do cliente com acesso autorizado
                                do {
                                    System.out.println("Bem vindo(a), "+cliente.getNome());
                                    contaService.dadosConta(numConta);
                                    System.out.println();
                                    Menu.menuCliente2();
                                    int opcao2 = sc.nextInt();

                                    switch (opcao2) {

                                        case 1:
                                            double valor = contaService.valorDeposito();
                                            contaService.deposito(conta, valor);
                                            System.out.println("Depósito de R$ "+ valorFinanceiro(valor) + " realizado!");
                                            System.out.println();
                                            break;

                                        case 2:
                                            contaService.saque(conta);
                                            break;

                                        case 3:
                                            System.out.println("Digite o N° da conta para qual irá realizar a transferência: ");
                                            int contaBeneficiario = sc.nextInt();
                                            if(contaBeneficiario == conta.getNumConta()) {
                                                System.out.println("Você não pode transferir dinheiro desta conta para esta conta!");
                                            }else {
                                                contaService.transferencia(conta, contaBeneficiario);
                                            }
                                            break;

                                        case 4:
                                            contaService.extrato(conta);
                                            break;

                                        case 0:
                                            logado = false;
                                            break;
                                    }

                                }while(logado);

                            }
                        }

                        break;

                    case 2:
                        Cliente cliente = clienteService.cadastrarCliente();
                        conta = contaService.abrirConta(cliente);
                        cliente.setConta(conta);
                        clienteService.salvarDados(cliente.getId(), cliente);

                        //Testando se a abertura de conta está funcionando
                        //System.out.println(cliente);
                        //System.out.println(conta);
                        break;

                    case 3:

                        break;

                    case 0:
                        continuar = false;
                        break;

                    default:
                        System.out.println("Digite uma opção válida!");
                        break;

                }


            } catch (SistemException e) {
                System.out.println(e.getMessage());
            }catch (InputMismatchException e){
                System.out.println("Digite uma opção válida!");
                sc.nextLine();
            }

        } while (continuar);
    }
}
