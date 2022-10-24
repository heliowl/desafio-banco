package service;

import model.Cliente;
import model.Conta;
import repository.Repository;

import java.util.Scanner;
import java.util.TreeMap;

public class ClienteService {
    Scanner sc = new Scanner(System.in);
    Repository<Integer, Cliente> repository = new Repository<>();

    public ClienteService(Scanner sc) {
        this.sc = sc;
        Cliente cli1 = new Cliente("Breuníssio", "1234");
        repository.salvar(cli1.getId(), cli1);
    }

    public void salvarDados(int id, Cliente cliente){
        repository.salvar(id, cliente);
    }

    public Cliente cadastrarCliente(){
        System.out.println("Preencha seus dados pessoais:");
        System.out.println("Nome: ");
        String nome = sc.nextLine();
        //System.out.println();
        System.out.println("Senha: ");
        String senha = sc.nextLine();

        Cliente cliente = new Cliente(nome, senha);
        salvarDados(cliente.getId(), cliente);

        return cliente;
    }

    public Cliente buscarClientePorId(int id){
        Cliente cliente = repository.buscarPorId(id);
        return cliente;
    }
}
