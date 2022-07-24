package cursojava;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Banco dev = new Banco("Dev");
        Cliente joao = new Cliente("João");

	    Conta cc = new ContaCorrente(joao);
        Conta cp = new ContaPoupanca(joao);

        dev.salvarContas(cc);
        dev.salvarContas(cp);

        System.out.println("=====DEPÓSITO=====");
        cc.depositar(1800);
        cp.depositar(4700);

        cc.extrato();
        cp.extrato();

        System.out.println("=====TRANSFERÊNCIA=====");
        cp.transferir(1000, cc);

        cc.extrato();
        cp.extrato();

        System.out.println("=====LISTA=====");
        dev.mostrarTodas();


    }
}
