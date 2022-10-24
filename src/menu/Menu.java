package menu;

import model.Banco;

public class Menu {

    public static void bemVindo(){
        System.out.println("============================================");
        System.out.println("Bem vindo ao sistema do "+Banco.nomeBanco());
        System.out.println("============================================");
    }

    public static void menuCliente1() {
        System.out.println();
        System.out.println("1 - Acessar Conta");
        System.out.println("2 - Abrir Conta");
        System.out.println("3 - Encerrar Conta");
        System.out.println("0 - Sair do Sistema");
    }

    public static void tipoConta() {
        System.out.println();
        System.out.println("Escolha o tipo da Conta: ");
        System.out.println("[1] - Conta corrente");
        System.out.println("[2] - Conta Poupança");
    }

    public static void menuCliente2() {
        System.out.println();
        System.out.println("1 - Depósito ");
        System.out.println("2 - Saque");
        System.out.println("3 - Transferência");
        System.out.println("4 - Extrato");
        System.out.println("5 - Abrir uma nova conta");
        System.out.println("0 - Encerrar sessão");
    }
}
